package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    private static final String defaultInputFileName = "./src/main/resources/image.jpg";
    private static final String defaultOutputFileName = "./src/main/resources/image2.jpg";

    public static void hideText() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Input File Name");
        String inputFileName = scanner.nextLine();
        if (inputFileName.isEmpty()) {
            inputFileName = defaultInputFileName;
        }
        System.out.printf("Read from %s%n", new File(inputFileName).getCanonicalPath());
        System.out.println("Enter Text");
        String text = scanner.nextLine();
        BufferedImage img = ImageIO.read(new File(inputFileName));
        BufferedImage imgWithText = ImageProcess.hideText(img, text.getBytes(StandardCharsets.UTF_8));
        System.out.println("Enter Output File Name");
        String outputFileName = scanner.nextLine();
        if (outputFileName.isEmpty()) {
            outputFileName = defaultOutputFileName;
        }
        ImageIO.write(imgWithText, "png", new File(outputFileName));
    }

    public static void findText() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Input File Name");
        String inputFileName = scanner.nextLine();
        if (inputFileName.isEmpty()) {
            inputFileName = defaultOutputFileName;
        }
        System.out.printf("Read from %s%n", new File(inputFileName).getCanonicalPath());
        BufferedImage img = ImageIO.read(new File(inputFileName));
        System.out.println("Enter Input Text Size");
        String textSize = scanner.nextLine();
        String foundText = ImageProcess.extractHiddenBytes(img, textSize.isEmpty()? null:Integer.getInteger(textSize));
        System.out.printf("Found Hidden Text: %s%n", foundText);
    }

    public static void fideWarAndPeace() throws IOException {
        String text = Files.readString(Path.of("./src/main/resources/War and Peace.txt"), StandardCharsets.UTF_8);
        BufferedImage img = ImageIO.read(new File(defaultInputFileName));
        BufferedImage imgWithText = ImageProcess.hideText(img, text.getBytes(StandardCharsets.UTF_8));
        String fText = ImageProcess.extractHiddenBytes(imgWithText, text.length());
        String substr = text.substring(0, fText.length());
        if (substr.equals(fText)) {
            System.out.printf("test text len %d\n", text.length());
            System.out.println("equals");
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What do you want to do? hide/find/test");
        String action = scanner.nextLine();
        switch (action) {
            case "hide" -> hideText();
            case "find" -> findText();
            case "test" -> fideWarAndPeace();
            default -> System.out.println("No such is the action");
        }
    }
}