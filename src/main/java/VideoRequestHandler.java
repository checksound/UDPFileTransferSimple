import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class VideoRequestHandler extends Thread {

    protected DatagramSocket socket = null;
    protected DatagramPacket packetRequest = null;
    public VideoRequestHandler(DatagramSocket socket, DatagramPacket packetRequest)
            throws IOException {
        this("VideoRequestHandler", socket, packetRequest);
    }

    public VideoRequestHandler(String name, DatagramSocket socket, DatagramPacket packetRequest)
            throws IOException {
        super(name);
        this.socket = socket;
        this.packetRequest = packetRequest;

    }

    public void run() {
            try {

                byte[] byteRequest = packetRequest.getData();

                String requestCommend = new String(byteRequest, 0,
                        packetRequest.getLength());

                // send the response to the client at "address" and "port"
                InetAddress address = packetRequest.getAddress();
                int port = packetRequest.getPort();

                doSendFile(requestCommend, address, port);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private void doSendFile(String fileRequest, InetAddress address, int port) throws IOException {

        byte[] buffer = new byte[1022];

        System.out.println("REQUEST FILE:" + fileRequest + "-" + new File(fileRequest).getAbsolutePath());
        // read from file
        BufferedInputStream input =
                new BufferedInputStream(
                        new FileInputStream(fileRequest));
        int byteRead;
        int numPacket = 0;
        while((byteRead = input.read(buffer))!= -1){
            byte[] dataToSend = null;
            if(byteRead < buffer.length){
                dataToSend = new byte[byteRead];
                System.arraycopy(buffer, 0, dataToSend, 0, byteRead);
            } else {
                dataToSend = buffer;
            }

            byte[] pdu = DataUtils.doPDU(numPacket, dataToSend);

            //
            // send
            System.out.println("SENDING: " + numPacket);
            DatagramPacket packet = new DatagramPacket(pdu, pdu.length, address, port);
            socket.send(packet);

            numPacket++;

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        // invio packetto vuoto - chiusura invio
        System.out.println("Sending sentinel packet: " + numPacket);
        byte[] pdu = DataUtils.doPDU(numPacket, new byte[0]);
        DatagramPacket packet = new DatagramPacket(pdu, pdu.length, address, port);
        socket.send(packet);
    }

}
