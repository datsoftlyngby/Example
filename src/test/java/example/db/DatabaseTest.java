package example.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.fail;

@Tag("integration")
class DatabaseTest {
  @Test
  @DisplayName("Should be able to connect")
  void shouldBeAbleToConnect() {
    try (Connection con = new Database("exampletest").connect()) {
      // Do nothing
    } catch (SQLException e) {
      fail(e);
    }
  }

}