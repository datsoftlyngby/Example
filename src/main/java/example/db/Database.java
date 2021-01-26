package example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
  private final String url;
  private final String user;

  public Database(String url, String user) {
    this.url = url;
    this.user = user;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      throw new DBRuntimeException(e);
    }
  }

  public Database(String name) {
    this(
        "jdbc:mysql://localhost:3306/" + name + "?serverTimezone=CET",
        name
    );
  }

  public Database() {
    this("example");
  }

  public Connection connect() throws SQLException {
    return DriverManager.getConnection(url, user, null);
  }

}
