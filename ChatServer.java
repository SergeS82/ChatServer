import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1732);
        ChartList chartList = new ChartList();
        chartList.newChat("Chat1");
        chartList.newChat("Chat2");
        while (true) {
            System.out.println("Waiting ...");
            Socket socket = server.accept();
            System.out.println("Client connected");
            new Client(socket, chartList).start();
        }
    }
}
