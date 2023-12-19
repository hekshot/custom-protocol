import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BinaryServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Server listening on port 8888...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());

                Thread clientHandler = new Thread(() -> handleBinaryClient(clientSocket));
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleBinaryClient(Socket clientSocket) {
        try {
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            // Receive binary data from the client
            int intValue = inputStream.readInt();
            double doubleValue = inputStream.readDouble();

            System.out.println("Received from client: " + intValue + ", " + doubleValue);

            // Perform some processing (e.g., multiply the values)
            int result = intValue * 2;
            double resultDouble = doubleValue * 2;

            // Send the processed data back to the client
            outputStream.writeInt(result);
            outputStream.writeDouble(resultDouble);

            // Close the connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
