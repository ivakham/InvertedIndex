package inverted.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public List<String> getAllTextFileContents(File directory) {
        List<String> fileContents = new ArrayList<>();

        if (!directory.exists()) {
            System.out.println("Directory not exist.");
            return fileContents;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    fileContents.addAll(getAllTextFileContents(file));
                } else if (file.isFile() && file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        StringBuilder content = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                        fileContents.add(content.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return fileContents;
    }

    public List<File> getAllTextFiles(File directory) {
        List<File> textFiles = new ArrayList<>();

        if (!directory.exists()) {
            System.out.println("Directory not exist.");
            return textFiles;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    textFiles.addAll(getAllTextFiles(file));
                } else if (file.isFile() && file.getName().endsWith(".txt")) {
                    textFiles.add(file);
                }
            }
        }

        return textFiles;
    }

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        File rootDirectory = new File("acllmdb");

        List<String> fileContents = fileHandler.getAllTextFileContents(rootDirectory);
        for (String content : fileContents) {
            System.out.println(content);
        }
    }
}
