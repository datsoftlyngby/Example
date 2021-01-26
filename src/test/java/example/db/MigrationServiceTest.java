package example.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integration")
class MigrationServiceTest {
  @Test
  @DisplayName("A clean DB should have version minus 1")
  void aCleanDbShouldHaveVersionMinus1() {
    assertEquals(
        -1,
        new MigrationService(Utils.cleanDatabase()).getCurrentVersion()
    );
  }

  @Test
  @DisplayName("A migrated DB should have version Migration.VERSION")
  void aMigratedDbShouldHaveVersionMigrationVersion() {
    assertEquals(
        MigrationService.VERSION,
        new MigrationService(Utils.migratedDatabase()).getCurrentVersion()
    );
  }
}
