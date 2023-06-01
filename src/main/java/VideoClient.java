import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import java.util.*;

public class VideoClient {
    public static void main(String[] args) throws IOException {

        List<Packet> list = new LinkedList<>();
        int numLastPacket = 0;
        int numInsertedPacket = 0;

        // get a datagram socket
        DatagramSocket socket = new DatagramSocket();

        // send request
        byte[] bufSend = new String("./files/video1.mp4").getBytes(StandardCharsets.UTF_8);
        InetAddress address = InetAddress.getByName("localhost");
        DatagramPacket packet = new DatagramPacket(bufSend, bufSend.length, address, 4445);
        socket.send(packet);

        while(true) {
            // get response
            byte[] bufReceive = new byte[1024];
            packet = new DatagramPacket(bufReceive, bufReceive.length);
            socket.receive(packet);

            // display response

            Packet receivedPacket = DataUtils.parsePDU(packet.getData());

            if(packet.getLength() == 2) {
                numLastPacket = receivedPacket.numPacket();
                System.out.println("Ricevuto LAST PACKET: " + receivedPacket.numPacket());
            } else {
                numInsertedPacket++;
                System.out.println("Ricevuto PACKET: " + receivedPacket.numPacket());
                list.add(receivedPacket);
            }

            if(numInsertedPacket == numLastPacket)
                break;
        }

        System.out.println("FINE LETTURA");
        socket.close();

        // ordinare i pacchetti
        Collections.sort(list);

        Iterator<Packet> iterator = list.iterator();

        var file = new File("./dump/video_" + System.currentTimeMillis()+".mp4");

        try(
                BufferedOutputStream out =
                        new BufferedOutputStream(new FileOutputStream(file));
        ) {

            while (iterator.hasNext()) {
                Packet pkt = iterator.next();
                out.write(pkt.dataPacket());

            }

            out.flush();

            System.out.println("FINE SCRITTURA FILE: " +
                    file.getAbsolutePath());
        } catch(IOException e) {
            System.out.println("IOException e: " + e.getMessage());
        }
    }
}

