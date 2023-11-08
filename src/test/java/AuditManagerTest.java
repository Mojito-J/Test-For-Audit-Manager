import org.example.AuditManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuditManagerTest {

    @TempDir
    static Path tempDirectory;

    private AuditManager auditManager;

    @BeforeEach
    void setup() {
        auditManager = new AuditManager(2, tempDirectory.toString());
    }

    @Test
    void addRecordCreatesNewFileWhenNoFilesExist() throws IOException {
        auditManager.addRecord("user1", LocalDateTime.now());
        assertTrue(Files.exists(tempDirectory.resolve("audit_0.csv")));
    }

    @Test
    void addRecordAppendsToExistingFileWhenSpaceAvailable() throws IOException {
        auditManager.addRecord("user1", LocalDateTime.now());
        auditManager.addRecord("user2", LocalDateTime.now());


        Path auditFilePath = tempDirectory.resolve("audit_0.csv");
        String fileContent = Files.readString(auditFilePath);
        assertTrue(fileContent.contains("user3"));
    }

    @Test
    void addRecordCreatesNewFileWhenMaxLinesReached() throws IOException {

        auditManager.addRecord("user1", LocalDateTime.now());
        auditManager.addRecord("user2", LocalDateTime.now());
        auditManager.addRecord("user3", LocalDateTime.now());
        auditManager.addRecord("user4", LocalDateTime.now());
        auditManager.addRecord("user5", LocalDateTime.now());

        assertTrue(Files.exists(tempDirectory.resolve("audit_1.csv")));
    }
}