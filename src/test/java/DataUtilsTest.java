import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataUtilsTest {

    @Test
    void convertIntIn2Byte() {

        byte[] value = DataUtils.convertIntIn2Byte(5);

        assertEquals((byte) 0b0000_0000, (byte) value[0]);
        assertEquals((byte) 0b0000_0101, (byte) value[1]);

        value = DataUtils.convertIntIn2Byte(4525);

        // 00010001 10101101
        assertEquals((byte) 0b0001_0001, (byte) value[0]);
        assertEquals((byte) 0b1010_1101, (byte) value[1]);
    }

    @Test
    void convert2ByteInInt() {
        byte[] numPacket = new byte[] {(byte) 0b0001_0001, (byte) 0b1010_1101};

        int numPkt = DataUtils.convert2ByteInInt(numPacket);

        assertEquals(4525, numPkt);

        numPacket = new byte[] {(byte) 0b0000_0000, (byte) 0b0000_0101};

        numPkt = DataUtils.convert2ByteInInt(numPacket);

        assertEquals(5, numPkt);
    }

    @Test
    void doPDU() {
        int numPacket = 5473;
        byte[] data = {(byte) 0b0101_1101, (byte) 0b1100_0011, (byte) 0b1011_1111};
        byte[] pdu = DataUtils.doPDU(numPacket, data);
        // 0001 0101  0110 0001
        byte[] expected = {0b0001_0101, 0b0110_0001, 0b0101_1101, (byte) 0b1100_0011, (byte) 0b1011_1111};
        assertArrayEquals(expected, pdu);
    }

    @Test
    void parsePDU() {
        byte[] pdu = {0b0001_0101, 0b0110_0001, 0b0101_1101, (byte) 0b1100_0011, (byte) 0b1011_1111};
        Packet packet = DataUtils.parsePDU(pdu);
        assertEquals(5473, packet.numPacket());
        byte[] expectedData = {0b0101_1101, (byte) 0b1100_0011, (byte) 0b1011_1111};
        assertArrayEquals(expectedData,
                packet.dataPacket());
    }


}