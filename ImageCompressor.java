import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import java.io.*;
import java.util.Iterator;

public class ImageCompressor extends JFrame {

    JButton chooseButton, saveButton;
    JLabel imageLabel, sliderLabel;
    JSlider qualitySlider;
    BufferedImage originalImage, compressedImage;

    public ImageCompressor() {
        setTitle("Simple Image Compressor");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Top Panel with Buttons and Slider ---
        JPanel topPanel = new JPanel();
        chooseButton = new JButton("Choose Image");
        saveButton = new JButton("Save Compressed");
        saveButton.setEnabled(false);

        // Slider for selecting target size in KB (50KB - 1000KB)
        sliderLabel = new JLabel("Target Size: 200 KB");
        qualitySlider = new JSlider(50, 1000, 200);
        qualitySlider.setMajorTickSpacing(250);
        qualitySlider.setMinorTickSpacing(50);
        qualitySlider.setPaintTicks(true);
        qualitySlider.setPaintLabels(true);

        qualitySlider.addChangeListener(e -> {
            sliderLabel.setText("Target Size: " + qualitySlider.getValue() + " KB");
        });

        topPanel.add(chooseButton);
        topPanel.add(saveButton);
        topPanel.add(sliderLabel);
        topPanel.add(qualitySlider);

        add(topPanel, BorderLayout.NORTH);

        // --- Label to show image ---
        imageLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        // --- Button Actions ---
        chooseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseImage();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCompressedImage();
            }
        });
    }

    // Method to choose an image
    // Method to choose an image
    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedImage tempImg = ImageIO.read(file);

                // ✅ FIX: Convert any colorspace to RGB
                originalImage = new BufferedImage(
                        tempImg.getWidth(),
                        tempImg.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                );
                Graphics2D g2d = originalImage.createGraphics();
                g2d.drawImage(tempImg, 0, 0, null);
                g2d.dispose();

                // Just show original (not resized)
                ImageIcon icon = new ImageIcon(originalImage.getScaledInstance(300, -1, Image.SCALE_SMOOTH));
                imageLabel.setIcon(icon);
                imageLabel.setText(null);

                saveButton.setEnabled(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }


    // Save compressed image with slider-based quality
    private void saveCompressedImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("compressed.jpg"));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File outFile = fileChooser.getSelectedFile();
                int targetKB = qualitySlider.getValue();

                compressAndSave(originalImage, outFile, targetKB);

                JOptionPane.showMessageDialog(this, "Compressed Image Saved! (" + targetKB + " KB target)");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    // Method to compress image to approx target size in KB
    private void compressAndSave(BufferedImage image, File file, int targetKB) throws IOException {
        float quality = 1.0f; // start with high quality
        byte[] imageBytes;

        do {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Get JPEG writer
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
            writer.dispose();
            ios.close();

            imageBytes = baos.toByteArray();
            quality -= 0.05f; // reduce quality gradually until under target
        } while (imageBytes.length / 1024 > targetKB && quality > 0.05f);

        // Write final image
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
        }
    }

    public static void main(String[] args) {
        new ImageCompressor().setVisible(true);
    }
}
