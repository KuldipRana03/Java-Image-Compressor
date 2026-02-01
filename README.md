Java-Based Image Compressor App – README
📌 Overview
This project is a Java-based Image Compressor Application designed to reduce image file sizes while maintaining acceptable visual quality. It leverages Java’s built-in libraries and optional third-party APIs to compress images into formats like JPEG, PNG, and WebP. The app is lightweight, cross-platform, and suitable for both command-line and GUI usage.

🚀 Features
Compress images to reduce file size.

Support for multiple formats: JPEG, PNG, WebP.

Adjustable compression levels (quality vs. size trade-off).

Batch processing for multiple images.

Simple CLI interface and optional Swing-based GUI.

Preserves metadata (optional toggle).

Cross-platform (runs on any system with Java installed).

🛠️ Requirements
Java JDK 11+

Maven/Gradle (for dependency management)

Optional libraries:

javax.imageio (default image handling)

com.luciad.imageio.webp (for WebP support)

📂 Project Structure
Code
ImageCompressorApp/
│── src/
│   ├── main/java/com/compressor/
│   │   ├── App.java          # Entry point
│   │   ├── Compressor.java   # Core compression logic
│   │   ├── Utils.java        # Helper functions
│── resources/
│── README.md
│── pom.xml / build.gradle
⚙️ Usage
Command-Line
bash
java -jar ImageCompressorApp.jar input.jpg output.jpg 0.7
input.jpg → source image

output.jpg → compressed image

0.7 → compression quality (scale: 0.0–1.0)

GUI Mode
Run the app without arguments:

bash
java -jar ImageCompressorApp.jar
A simple Swing-based interface will open, allowing you to select files and compression levels.

📖 Example Code Snippet
java
BufferedImage image = ImageIO.read(new File("input.jpg"));
File compressedFile = new File("output.jpg");

Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
ImageWriter writer = writers.next();

ImageWriteParam param = writer.getDefaultWriteParam();
param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
param.setCompressionQuality(0.7f); // 70% quality

FileImageOutputStream output = new FileImageOutputStream(compressedFile);
writer.setOutput(output);
writer.write(null, new IIOImage(image, null, null), param);
writer.dispose();
🧪 Testing
Run unit tests with:

bash
mvn test
or

bash
gradle test
📌 Future Enhancements
Add support for GIF compression.

Implement drag-and-drop GUI.

Cloud integration for bulk compression.

Advanced algorithms (e.g., lossless compression with PNGQuant).
