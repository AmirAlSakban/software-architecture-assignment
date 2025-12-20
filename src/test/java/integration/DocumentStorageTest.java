package integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DocumentStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void saveShouldWritePdfToOutputDir() throws IOException {
        byte[] payload = "%PDF-1.4\nHello".getBytes(StandardCharsets.UTF_8);

        Path saved = DocumentStorage.save(tempDir, "pdf", "My Report", payload);

        assertTrue(Files.exists(saved));
        assertTrue(saved.getFileName().toString().endsWith(".pdf"));
        assertArrayEquals(payload, Files.readAllBytes(saved));
    }

    @Test
    void saveShouldUseBinExtensionForUnknownFormat() throws IOException {
        byte[] payload = "data".getBytes(StandardCharsets.UTF_8);

        Path saved = DocumentStorage.save(tempDir, "unknown", "Title", payload);

        assertTrue(saved.getFileName().toString().endsWith(".bin"));
    }
    @Test
    void saveShouldWriteDocxForWord() throws IOException {
        byte[] payload = "doc".getBytes(StandardCharsets.UTF_8);

        Path saved = DocumentStorage.save(tempDir, "word", "Report", payload);

        assertTrue(saved.getFileName().toString().endsWith(".docx"));
    }

    @Test
    void sanitizeFileNameShouldRemoveInvalidCharacters() {
        String sanitized = DocumentStorage.sanitizeFileName("A:/B\\C*D?E\"F<G>H|I");
        assertFalse(sanitized.contains(":"));
        assertFalse(sanitized.contains("/"));
        assertFalse(sanitized.contains("\\\\"));
        assertFalse(sanitized.contains("*"));
        assertFalse(sanitized.contains("?"));
        assertFalse(sanitized.contains("\""));
        assertFalse(sanitized.contains("<"));
        assertFalse(sanitized.contains(">"));
        assertFalse(sanitized.contains("|"));
    }
}
