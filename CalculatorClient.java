import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CalculatorClient {

    public static void main(String[] args) {
        try {
            continuousRequests();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void continuousRequests() throws IOException {
        while (true) {
            Socket socket = new Socket("127.0.0.1", 8888);

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a request (e.g., /HELP): ");
            String request = userInput.readLine();

            // Send the request to the server
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeBytes(request + "\n");

            // Receive the response from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = reader.readLine();
            System.out.println("Received from server: " + response);

            // /user:<aman>:[23]:{9102507571}

            //socket.close();
        }
    }
}
