import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(4445);

        while(true) {
            byte[] buf = new byte[256];

            // receive request
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            VideoRequestHandler videoRequestHandler = new VideoRequestHandler(socket, packet);
            videoRequestHandler.start();

        }
    }
}
