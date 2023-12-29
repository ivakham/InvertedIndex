package inverted.index;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class InvertedIndexClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            Scanner scanner = new Scanner(System.in);
            String serverResponse;

            while (true) {
                serverResponse = reader.readLine();
                System.out.println(serverResponse);

                if (serverResponse.contains("Enter the number of threads:")) {
                    int numThreads = Integer.parseInt(scanner.nextLine());
                    writer.println(numThreads);
                } else if (serverResponse.contains("Enter the word to search:")) {
                    String word = scanner.nextLine();
                    writer.println(word);
                } else if (serverResponse.contains("Files containing the word:")) {
                    System.out.println(reader.readLine());
                    String nextAction = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(nextAction.trim())) {
                        writer.println("exit");
                        break;
                    } else {
                        writer.println(nextAction);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

