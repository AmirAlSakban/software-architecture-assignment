package integration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

final class DocumentStorage {

    private DocumentStorage() {
        // utility class
    }

    static Path save(Path outputDir, String formatKey, String title, byte[] payload) throws IOException {
        Files.createDirectories(outputDir);

        String extension = switch (formatKey) {
            case "pdf" -> "pdf";
            case "html" -> "html";
            case "word" -> "xml";
            default -> "bin";
        };

        String baseName = sanitizeFileName(title);
        if (baseName.isBlank()) {
            baseName = "document";
        }

        Path outputFile = outputDir.resolve(baseName + "." + extension);
        return Files.write(outputFile, payload, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    static String sanitizeFileName(String input) {
        if (input == null) {
            return "";
        }

        String normalized = input.trim();
        normalized = normalized.replaceAll("[\\\\/:*?\"<>|]", "_");
        normalized = normalized.replaceAll("\\s+", " ");
        normalized = normalized.replace(' ', '_');
        return normalized;
    }
}
