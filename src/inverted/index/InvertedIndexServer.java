// InvertedIndexServer.java
package inverted.index;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InvertedIndexServer {
    private static final int DEFAULT_NUM_THREADS = 10;
    private static InvertedIndex invertedIndex = new InvertedIndex();
    private static boolean isIndexed = false;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Сервер запущено. Очікування підключення...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String command;
                if (!isIndexed) {
                    writer.write("Enter the number of threads:\n");
                    writer.flush();
                    int numThreads = Integer.parseInt(reader.readLine());
                    FileHandler fileHandler = new FileHandler();
                    List<File> textFiles = fileHandler.getAllTextFiles(new File("acllmdb"));
                    ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
                    long startTime = System.currentTimeMillis();
                    for (File file : textFiles) {
                        executorService.submit(() -> invertedIndex.indexFile(file));
                    }
                    executorService.shutdown();
                    try {
                        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    isIndexed = true;

                    long endTime = System.currentTimeMillis();
                    long indexingTime = endTime - startTime;
                    writer.write("Indexing completed in " + indexingTime + " milliseconds.\n");
                    writer.flush();
                }

                do {
                    writer.println("Enter the word to search:");
                    String word = reader.readLine();
                    writer.println("Files containing the word:" + invertedIndex.getFilesContainingWord(word));

                    writer.println("Do you want to continue? (Type 'yes' or 'exit' to close or anything else to continue)");
                    command = reader.readLine();
                    System.out.println(command);

                } while (!"exit".equalsIgnoreCase(command));

                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


