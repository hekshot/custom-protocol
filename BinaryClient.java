import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BinaryClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8888);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            // Send binary data to the server
            int intValue = 42;
            double doubleValue = 3.14;

            outputStream.writeInt(intValue);
            outputStream.writeDouble(doubleValue);

            // Receive the processed data from the server
            int result = inputStream.readInt();
            double resultDouble = inputStream.readDouble();

            System.out.println("Received from server: " + result + ", " + resultDouble);

            // Close the connection
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
