package example.db;

import com.mysql.cj.exceptions.AssertionFailedException;

import java.sql.SQLException;

public class Utils {

  public static final String DB_NAME = "exampletest";

  public static Database setupDatabase() {
    return new Database(DB_NAME);
  }

  /**
   * Delete the database and create a new one
   *
   * @return a clean Database
   */
  public static Database cleanDatabase() {
    Database db = setupDatabase();
    try (var stmt = db.connect()) {
      // NOTE: Never do this, use real prepared statements instead of String.format.
      // We need to do this here because we want to inject the database name.
      stmt.prepareStatement(String.format("DROP DATABASE IF EXISTS %s;", DB_NAME)).execute();
      stmt.prepareStatement(String.format("CREATE DATABASE %s;", DB_NAME)).execute();
      return db;
    } catch (SQLException e) {
      throw new AssertionFailedException(e);
    }
  }

  public static Database migratedDatabase() {
    Database db = cleanDatabase();
    new MigrationService(db).migrate();
    return db;
  }
}
