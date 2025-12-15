# Assignment 1 - Software Architecture

A Java project demonstrating clean OOP principles and design patterns through a **Document Editor** and **Car Configuration** system.

## Project Overview

This project implements:

1. **Part 1 - Document Editor**: An extensible document editor using Factory and Registry patterns
2. **Part 2 - Car Configuration**: A car builder with step-by-step validation using Builder and Director patterns
3. **Bonus - Integration**: A Car Management System that generates car documents in multiple formats

## Requirements

- **Java 17+**
- **Gradle 7+** (or use the Gradle wrapper)

## How to Build

```bash
# Using Gradle wrapper (recommended)
./gradlew build        # macOS/Linux
./gradlew.bat build    # Windows PowerShell / Command Prompt

# Or if you have Gradle installed globally
gradle build
```

## How to Run Tests

```bash
# Run all tests
./gradlew test        # macOS/Linux
./gradlew.bat test    # Windows PowerShell / Command Prompt

# Run tests with verbose output
./gradlew test --info
./gradlew.bat test --info
```

## How to Measure Code Coverage

Coverage is measured using **JaCoCo** and is enforced at **≥85%**.

```bash
# Run tests and generate coverage report
./gradlew test jacocoTestReport        # macOS/Linux
./gradlew.bat test jacocoTestReport    # Windows

# Check coverage threshold (fails if <85%)
./gradlew jacocoTestCoverageVerification
./gradlew.bat jacocoTestCoverageVerification
```

**Coverage reports are generated in:**
- HTML: `build/reports/jacoco/test/html/index.html`
- XML: `build/reports/jacoco/test/jacocoTestReport.xml`

## Project Structure

```
src/
├── main/java/
│   ├── editor/                    # Part 1 - Document Editor
│   │   ├── core/                  # Core abstractions
│   │   │   ├── Document.java      # Document interface
│   │   │   ├── AbstractDocument.java
│   │   │   ├── Editor.java        # Main editor class
│   │   │   └── UnknownDocumentFormatException.java
│   │   ├── factory/               # Factory & Registry
│   │   │   ├── DocumentFactory.java
│   │   │   ├── DocumentProvider.java
│   │   │   ├── PdfDocumentProvider.java
│   │   │   ├── WordDocumentProvider.java
│   │   │   └── HtmlDocumentProvider.java
│   │   └── formats/               # Concrete implementations
│   │       ├── PdfDocument.java
│   │       ├── WordDocument.java
│   │       └── HtmlDocument.java
│   ├── car/                       # Part 2 - Car Configuration
│   │   ├── domain/                # Domain model
│   │   │   ├── Car.java           # Immutable car class
│   │   │   ├── CarModel.java      # Enum with allowed options
│   │   │   ├── EngineType.java
│   │   │   ├── TransmissionType.java
│   │   │   ├── Color.java
│   │   │   ├── InteriorFeature.java
│   │   │   ├── ExteriorFeature.java
│   │   │   └── SafetyFeature.java
│   │   └── builder/               # Builder pattern
│   │       ├── CarBuilder.java
│   │       ├── CarConfigurationWizard.java
│   │       └── InvalidCarConfigurationException.java
│   └── integration/               # Bonus - Integration
│       ├── CarManagementSystem.java
│       └── CarReportGenerator.java
├── test/java/                     # Unit tests (mirror structure)
│   ├── editor/
│   ├── car/
│   └── integration/
uml/                               # PlantUML diagrams
├── document_editor_class.puml
├── car_builder_class.puml
├── document_creation_sequence.puml
├── car_configuration_sequence.puml
└── car_document_generation_sequence.puml
```

## How to Add a New Document Format

Adding a new format requires **no changes to the Editor or DocumentFactory**:

### Step 1: Create the Document Implementation

```java
package editor.formats;

import editor.core.AbstractDocument;

public class MarkdownDocument extends AbstractDocument {
    public static final String FORMAT_KEY = "markdown";
    
    public MarkdownDocument(String title) {
        super(title);
    }
    
    @Override
    public byte[] save() {
        return ("# " + title + "\n\n" + content).getBytes();
    }
    
    @Override
    public String render() {
        return "[Markdown Preview]\n# " + title + "\n" + content;
    }
    
    @Override
    public String getFormatKey() {
        return FORMAT_KEY;
    }
}
```

### Step 2: Create the Provider

```java
package editor.factory;

import editor.formats.MarkdownDocument;
import editor.core.Document;

public class MarkdownDocumentProvider implements DocumentProvider {
    @Override
    public String formatKey() {
        return MarkdownDocument.FORMAT_KEY;
    }
    
    @Override
    public Document create(String title) {
        return new MarkdownDocument(title);
    }
}
```

### Step 3: Register the Provider

```java
DocumentFactory factory = DocumentFactory.createDefault()
    .register(new MarkdownDocumentProvider());

Editor editor = new Editor(factory);
editor.newDocument("markdown", "My Document");
```

**That's it!** The Editor class remains unchanged.

## Design Patterns Used

### Factory Pattern with Registry (Part 1)

The `DocumentFactory` uses a **registry-based approach** instead of switch statements:

- Providers are registered at startup via `register(DocumentProvider)`
- Lookup is done through a `Map<String, DocumentProvider>`
- New formats are added by creating a provider and registering it
- **Open/Closed Principle**: Open for extension, closed for modification

### Builder Pattern (Part 2)

The `CarBuilder` implements a **fluent builder**:

- Method chaining via `withModel()`, `withEngine()`, etc.
- Validation of required fields in `build()`
- Compatibility validation against `CarModel` constraints
- Immutable `Car` objects are created

### Director Pattern (Part 2)

The `CarConfigurationWizard` acts as a **Director**:

- Enforces step order through staged interfaces (`EngineStep → TransmissionStep → OptionsStep`)
- Provides pre-configured templates (`buildLuxurySUV()`, `buildSportsCar()`)
- Ensures valid-by-construction car configurations

### Strategy Pattern (Implicit)

Each document format implements the same `Document` interface but with different `save()` and `render()` strategies.

## Key Design Decisions

1. **Abstractions First**: `Editor` depends only on `Document` interface and `DocumentFactory`
2. **Registry over Switch**: No conditionals in factory - just map lookup
3. **Immutable Domain Objects**: `Car` is immutable; all fields set at construction
4. **Validation at Build Time**: `CarBuilder.build()` validates everything before creating `Car`
5. **Clear Exception Messages**: `InvalidCarConfigurationException` explains exactly what's wrong
6. **Separation of Concerns**: `CarReportGenerator` handles content; documents handle formatting

## Example Usage

### Document Editor

```java
DocumentFactory factory = DocumentFactory.createDefault();
Editor editor = new Editor(factory);

editor.newDocument("pdf", "Annual Report")
      .edit("This is the content of the report...")
      .save();

String preview = editor.preview();
```

### Car Configuration

```java
Car car = new CarBuilder()
    .withModel(CarModel.SUV)
    .withEngine(EngineType.V8)
    .withTransmission(TransmissionType.AUTOMATIC)
    .setColor(Color.BLACK)
    .addInteriorFeature(InteriorFeature.LEATHER)
    .addSafetyFeature(SafetyFeature.ABS)
    .build();

System.out.println(car.getSummary());
```

### Car Document Generation (Integration)

```java
CarManagementSystem system = new CarManagementSystem(DocumentFactory.createDefault());

Car car = new CarBuilder()
    .withModel(CarModel.SUV)
    .withEngine(EngineType.V8)
    .withTransmission(TransmissionType.AUTOMATIC)
    .build();

Document pdfDoc = system.generateCarDocument(car, "pdf");
Document wordDoc = system.generateCarDocument(car, "word");
Document htmlDoc = system.generateCarDocument(car, "html");
```

## License

This project is for educational purposes as part of Software Architecture coursework.
