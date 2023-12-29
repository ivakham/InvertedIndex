# Inverted Index Operation Guide

## Starting the Server

1. **Clone the repository**:
   git clone https://github.com/Da-nyaa/romanenko-course-project.git

2. Navigate to the `server` package and open the `InvertedIndexServer.java` file.

3. Optionally, modify the default port (by default, it's `12345`).

4. Execute the `main()` method within the `InvertedIndexServer.java` class.

## Client Connection

1. Go to the `client` package and open the `Client.java` file.

2. If the server is hosted remotely, replace `localhost` with the IP address of the remote machine.

3. Modify the port if required.

4. Run the `main()` method within the `Client.java` class.

## Working with the Inverted Index

- If you're the first to connect to the server after its deployment, you'll be prompted to input the number of threads for the inverted index creation.

- After providing the thread count, the server will index the files and respond with the processing time.

- Next, input a word to search within the indexed files. If the word exists in any file, the server will display the list of files; otherwise, it will return null.

- To shut down the server, send the command `exit`.
