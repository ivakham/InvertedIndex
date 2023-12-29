package inverted.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InvertedIndex {

    private final ConcurrentHashMap<String, HashSet<String>> index = new ConcurrentHashMap<>();

    public void indexContent(String content, File file) {
        String[] tokens = content.split("\\s+");
        for (String token : tokens) {
            addToken(token, file.getAbsolutePath());
        }
    }

    private void addToken(String token, String filePath) {
        index.computeIfAbsent(token, k -> new HashSet<>()).add(filePath);
    }

    public void indexFiles(List<File> files, int numThreads) {
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (File file : files) {
            executorService.execute(() -> {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    indexContent(content.toString(), file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        File rootDirectory = new File("acllmdb");

        List<File> textFiles = fileHandler.getAllTextFiles(rootDirectory);

        InvertedIndex invertedIndex = new InvertedIndex();
        int numThreads = 5;
        invertedIndex.indexFiles(textFiles, numThreads);

    }

    public Set<String> getFilesContainingWord(String word) {
        return index.getOrDefault(word, new HashSet<>());
    }


    public void indexFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    addToken(word, file.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
