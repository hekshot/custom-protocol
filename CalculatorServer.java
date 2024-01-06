import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Server listening on port 8888...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());

                Thread clientHandler = new Thread(() -> handleClient(clientSocket));
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            // Receive the request from the client
            String request = reader.readLine();
            System.out.println("Received request from client: " + request);

            // Process the request
            String response = processRequest(request);
            System.out.println("Sending response to client: " + response);

            // Send the response back to the client
            outputStream.writeBytes(response + "\n");

            // Close the connection
            // clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processRequest(String request) {
        String[] tokens = request.split(" ");
        if (tokens.length >= 2) {
            String operation = tokens[0];
            String[] operands = tokens[1].split("\\|");

            System.out.println("Operation: " + operation);
            System.out.print("Operands: ");
            for (String operand : operands) {
                System.out.print(operand + " ");
            }
            System.out.println();

            int init = 0;

            if(operation=="/INIT"){
                for (String operand : operands) {
                        init += Integer.parseInt(operand);
                    }
                    return "Initialization completed. Initial value set to " + init;
            }

            switch (operation) {

                case "/ADD":
                    // Add the additional operands to the initialized value
                    int sum = init;
                    for (String operand : operands) {
                        sum += Integer.parseInt(operand);
                    }
                    init = sum; // Update the initialized value with the new sum
                    return "Result of addition: " + sum;

                case "/SUB":
                    int subtraction = Integer.parseInt(operands[0]);
                    for (int i = 1; i < operands.length; i++) {
                        subtraction -= Integer.parseInt(operands[i]);
                    }
                    return String.valueOf(subtraction);

                case "/MUL":
                    int product = 1;
                    for (String operand : operands) {
                        product *= Integer.parseInt(operand);
                    }
                    return String.valueOf(product);

                case "/DIV":
                    if (operands.length == 0) {
                        return "Invalid operation";
                    }
                    double divisionResult = Double.parseDouble(operands[0]);
                    for (int i = 1; i < operands.length; i++) {
                        double divisor = Double.parseDouble(operands[i]);
                        if (divisor != 0) {
                            divisionResult /= divisor;
                        } else {
                            return "Error: Division by zero";
                        }
                    }
                    return String.valueOf(divisionResult);

                case "/FACT":
                    if (operands.length != 1) {
                        return "Invalid operation";
                    }
                    int number = Integer.parseInt(operands[0]);
                    int factorialResult = 1;
                    for (int i = 1; i <= number; i++) {
                        factorialResult *= i;
                    }
                    return String.valueOf(factorialResult);

                case "/SQUARE":
                    if (operands.length != 1) {
                        return "Invalid operation";
                    }
                    int squareInput = Integer.parseInt(operands[0]);
                    int squareResult = squareInput * squareInput;
                    return String.valueOf(squareResult);

                // case "/USER":
                //     // Assuming the format is /user:<name>:[age]:{phoneNumber}
                //     if (operands.length == 1) {
                //         String userInfo = operands[0].substring(1, operands[0].length() - 1);
                //         String[] userInfoParts = userInfo.split(":");

                //         if (userInfoParts.length == 4) {
                //             String name = userInfoParts[0].substring(1); // Remove "<" from the beginning
                //             int age = Integer.parseInt(userInfoParts[1].substring(1)); // Remove "[" from the beginning
                //             String phoneNumber = userInfoParts[2].substring(1); // Remove "[" from the beginning
                //             phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1); // Remove "{" from the end

                //             return "User Information: Name = " + name + ", Age = " + age + ", Phone Number = "
                //                     + phoneNumber;
                //         } else {
                //             return "Invalid user information format";
                //         }
                //     } else {
                //         return "Invalid operation";
                //     }

                default:
                    return "Invalid operation";
            }
        } else if (tokens.length == 1 && tokens[0].equals("/HELP")) {
            // Special case: "/help" without additional arguments
            return "Available operations: /ADD, /SUB, /MUL, /DIV, /FACT, /SQUARE";
        } else {
            return "Invalid request format";
        }
    }
}