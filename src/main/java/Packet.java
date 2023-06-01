public record Packet(int numPacket, byte[] dataPacket) implements Comparable<Packet>{

    @Override
    public int compareTo(Packet o) {
        if(numPacket > o.numPacket)
            return 1;
        else if(numPacket < o.numPacket)
            return -1;
        return 0;
    }
}
