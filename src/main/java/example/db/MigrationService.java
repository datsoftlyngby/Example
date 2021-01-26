package example.db;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Migration Service enables us to upgrade a database to the correct version.
 */
public class MigrationService {

  /**
   * The version that we aim to upgrade the system to. Change it so that it matches the
   * newest migration script in 'src/main/resources/migrations/xxxx.sql'
   */
  public static final int VERSION = 0;

  private final Database db;

  public MigrationService(Database db) {
    this.db = db;
  }

  /**
   * Get the current version of the database.
   * @return the version of the database
   */
  public int getCurrentVersion() {
    final String sql =
        "SELECT migrations.version " +
            "FROM migrations " +
            "ORDER BY migrations.migrated_on " +
            "LIMIT 1";
    try (
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement(sql)
    ) {
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      } else {
        return -1;
      }
    } catch (SQLException e) {
      if (e.getMessage().endsWith("migrations' doesn't exist")) {
        createMigrationsTable();
        return -1;
      } else {
        throw new DBRuntimeException(e);
      }
    }
  }

  /**
   * Migrates the database to the version provided.
   * @throws DBRuntimeException if something goes wrong
   */
  public void migrate() {
    try {
      int currentVersion;
      while ((currentVersion = getCurrentVersion()) < VERSION) {
        runMigration(currentVersion + 1);
      }
      checkVersion();
    } catch (SQLException | FileNotFoundException e) {
      throw new DBRuntimeException(e);
    }
  }

  /**
   * Check that the database has the correct version.
   * @throws DBRuntimeException if the database is in a wrong version
   */
  public void checkVersion() {
    int currentVersion = getCurrentVersion();
    if (currentVersion != VERSION) {
      throw new DBRuntimeException(
          String.format("Database in has version %d, expected %d", currentVersion, VERSION)
      );
    }
  }


  /**
   * An entry point for running migrating the database.
   */
  public static void main(String [] argv) {
    new MigrationService(new Database()).migrate();
  }

  /**
   * Run a specific migration
   * @param version
   * @return the version
   * @throws FileNotFoundException if the file was not found
   * @throws SQLException if something is wrong in the migration
   */
  private int runMigration(int version) throws FileNotFoundException, SQLException {
    runScript(getMigrationScriptStream(version));
    setDatabaseVersion(version);
    return getCurrentVersion();
  }

  /**
   * Create a migration table in the database.
   */
  private void createMigrationsTable() {
    final String sql
        = "CREATE TABLE migrations "
        + "( id INT PRIMARY KEY AUTO_INCREMENT, "
        + "  migrated_on TIMESTAMP NOT NULL DEFAULT NOW(), "
        + "  version INT NOT NULL "
        + ");";
    try (
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement(sql)
    ) {
      stmt.execute();
    } catch (SQLException e) {
      throw new DBRuntimeException(e);
    }
  }

  /**
   * Updates the database version
   * @param version
   */
  private void setDatabaseVersion(int version) {
    final String sql = "INSERT INTO migrations (version) VALUE (?);";
    try (
        Connection con = db.connect();
        PreparedStatement stmt = con.prepareStatement(sql)
    ) {
      stmt.setInt(1, version);
      stmt.execute();
    } catch (SQLException e) {
      throw new DBRuntimeException(e);
    }
  }

  /**
   * Run a migration script on the input stream
   * @param stream the script as an input stream
   * @throws SQLException if anything goes wrong
   */
  private void runScript(InputStream stream) throws SQLException {
    try (Connection conn = db.connect()) {
      // Turn off auto commit so that we can roll back the entier migration if it fails.
      conn.setAutoCommit(false);
      ScriptRunner runner = new ScriptRunner(conn);
      runner.setStopOnError(true);
      runner.runScript(new BufferedReader(new InputStreamReader(stream)));
      conn.commit();
    }
  }

  /**
   * Load a migration script as an input stream
   * @param version the version number of the migration script
   * @return an input stream
   * @throws FileNotFoundException if the version does not exist
   */
  private static InputStream getMigrationScriptStream(int version) throws FileNotFoundException {
    String migrationFile = String.format("migrations/%04d.sql", version);
    InputStream stream = MigrationService.class.getClassLoader().getResourceAsStream(migrationFile);
    if (stream == null) {
      throw new FileNotFoundException(migrationFile);
    }
    return stream;
  }

}
