package integration;

import car.builder.CarBuilder;
import car.builder.InvalidCarConfigurationException;
import car.domain.Car;
import car.domain.CarModel;
import car.domain.Color;
import car.domain.EngineType;
import car.domain.InteriorFeature;
import car.domain.SafetyFeature;
import car.domain.TransmissionType;
import editor.core.Document;
import editor.core.UnknownDocumentFormatException;
import editor.factory.DocumentFactory;
import integration.order.Order;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Simple console launcher that wires the car builder with the document editor.
 */
public final class CarManagementApp {

    private CarManagementApp() {
        // utility class
    }

    public static void main(String[] args) {
        DocumentFactory documentFactory = DocumentFactory.createDefault();
        CarManagementSystem system = new CarManagementSystem(documentFactory);

        displayBanner();
        System.out.printf("Supported formats: %s%n", documentFactory.getSupportedFormats());

        try (Scanner scanner = new Scanner(System.in)) {
            String formatKey = resolveFormat(args, scanner, documentFactory);
            System.out.printf("Using format: %s%n", formatKey.toUpperCase());

            Car sampleCar = buildSampleCar();
            Order order = system.getOrderService().placeOrder(sampleCar);
            printCarSummary(sampleCar);

            Document document = system.generateCarDocument(sampleCar, formatKey, order);

            System.out.println("\n=== Document Preview ===");
            System.out.println(document.render());

            byte[] savedBytes = system.getEditor().save();
            Path outputPath = DocumentStorage.save(Path.of("output"), formatKey, document.getTitle(), savedBytes);
            System.out.printf("Document generated successfully (%d bytes).%n", savedBytes.length);
            System.out.printf("Saved to: %s%n", outputPath.toAbsolutePath().normalize());
        } catch (UnknownDocumentFormatException ex) {
            System.err.printf("Unknown document format '%s'. Supported formats: %s%n",
                    ex.getFormatKey(), ex.getSupportedFormats());
            System.exit(1);
        } catch (IOException ex) {
            System.err.printf("Failed to write document to disk: %s%n", ex.getMessage());
            System.exit(1);
        } catch (InvalidCarConfigurationException ex) {
            System.err.printf("Invalid car configuration: %s%n", ex.getMessage());
            System.exit(1);
        }
    }

    private static void displayBanner() {
        System.out.println("=========================================");
        System.out.println("      Car Management Console Demo");
        System.out.println("=========================================");
    }

    private static String resolveFormat(String[] args, Scanner scanner, DocumentFactory documentFactory) {
        if (args.length > 0) {
            return args[0].toLowerCase();
        }

        System.out.printf("Choose document format (%s) [default: pdf]: ",
                String.join(", ", documentFactory.getSupportedFormats()));
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? "pdf" : input.toLowerCase();
    }

    private static void printCarSummary(Car car) {
        System.out.println("\n=== Sample Configuration ===");
        System.out.printf("Model        : %s%n", car.getModel().getDisplayName());
        System.out.printf("Color        : %s%n", car.getColor().getDisplayName());
        System.out.printf("Engine       : %s%n", car.getEngine().getDisplayName());
        System.out.printf("Transmission : %s%n", car.getTransmission().getDisplayName());
        System.out.printf("Interior     : %s%n", joinFeatures(car.getInteriorFeatures()));
        System.out.printf("Exterior     : %s%n", joinFeatures(car.getExteriorFeatures()));
        System.out.printf("Safety       : %s%n", joinFeatures(car.getSafetyFeatures()));
    }

    private static String joinFeatures(Collection<?> features) {
        if (features.isEmpty()) {
            return "None";
        }
        return features.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    private static Car buildSampleCar() {
        return new CarBuilder()
                .withModel(CarModel.SUV)
                .withEngine(EngineType.V8)
                .withTransmission(TransmissionType.AUTOMATIC)
                .setColor(Color.SILVER)
                .addInteriorFeature(InteriorFeature.LEATHER)
                .addInteriorFeature(InteriorFeature.GPS)
                .addInteriorFeature(InteriorFeature.SOUND_SYSTEM)
                .withSunroof()
                .withRims(true)
                .addSafetyFeature(SafetyFeature.ABS)
                .addSafetyFeature(SafetyFeature.AIRBAGS)
                .addSafetyFeature(SafetyFeature.REAR_CAMERA)
                .build();
    }
}
