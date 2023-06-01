public class DataUtils {

    public static byte[] convertIntIn2Byte(int value) {
        if(value >= Math.pow(2, 64)) {
            return new byte[0];
        }

        byte[] numInByte = new byte[2];

        numInByte[0] = (byte)((0x00_00_FF_00 & value) >> 8);
        numInByte[1] = (byte)(0x00_00_00_FF & value);
        return numInByte;
    }

    public static int convert2ByteInInt(byte[] value) {

        if(value == null || value.length==0)
            return -1;

        int valueConverted = ((int)value[0]) << 8;
        valueConverted = valueConverted |
                (0b0000_0000_0000_0000_0000_0000_1111_1111 & value[1]);

        return valueConverted;
    }

    public static byte[] doPDU(int numPacket, byte[] data) {
        byte[] payload = new byte[data.length + 2];
        byte[] numPacketInByte = convertIntIn2Byte(numPacket);
        payload[0] = numPacketInByte[0];
        payload[1] = numPacketInByte[1];
        if(data.length != 0)
            System.arraycopy(data, 0, payload, 2, data.length);

        return payload;
    }

    public static Packet parsePDU(byte[] payload) {

        byte[] numPart = new byte[2];
        byte[] dataPacket = new byte[payload.length -2];

        // fill
        numPart[0] = payload[0];
        numPart[1] = payload[1];

        int numPacket = DataUtils.convert2ByteInInt(numPart);
        System.arraycopy(payload, 2, dataPacket, 0,payload.length -2);
        return new Packet(numPacket, dataPacket);
    }


}
