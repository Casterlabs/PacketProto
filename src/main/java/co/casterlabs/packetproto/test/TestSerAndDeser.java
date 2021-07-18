package co.casterlabs.packetproto.test;

import java.io.IOException;
import java.util.UUID;

import co.casterlabs.packetproto.Packet;
import co.casterlabs.packetproto.io.NetCalc;
import co.casterlabs.packetproto.io.NetIn;
import co.casterlabs.packetproto.io.NetOut;
import co.casterlabs.packetproto.io.buffer.ByteBufferNetIn;
import co.casterlabs.packetproto.io.buffer.ByteBufferNetOut;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

public class TestSerAndDeser {

    public static void main(String[] args) throws Exception {
        // Setup the test class
        TestPacket test1 = new TestPacket();

        test1.testLong = 123456;
        test1.testString = UUID.randomUUID().toString();
        test1.testInt = 123;

        // Calculate how much we need to allocate to the buffer
        NetCalc calc = new NetCalc();
        test1.calc(calc);

        // Serialize to bytes
        ByteBufferNetOut out = new ByteBufferNetOut(calc.getRequiredBytes());
        test1.serialize(out);

        // Rewind the buffer, initialize the input
        out.getBuf().rewind();
        ByteBufferNetIn in = new ByteBufferNetIn(out.getBuf());

        // Deserialize into a new test class
        TestPacket test2 = new TestPacket();
        test2.deserialize(in);

        // Test
        System.out.printf("test1 == test2 : %b\n", test1.equals(test2));
        System.out.println(test1);
        System.out.println(test2);
    }

    @ToString
    @EqualsAndHashCode
    public static class TestPacket implements Packet {
        private long testLong;
        private String testString;
        private int testInt;

        // Calculate how much we need to allocate
        @Override
        public void calc(@NonNull NetCalc calc) {
            calc.allocateLong();
            calc.allocateString(this.testString);
            calc.allocateInt();
        }

        // Write out the contents
        @Override
        public void serialize(@NonNull NetOut out) throws IOException {
            out.writeLong(this.testLong);
            out.writeString(this.testString);
            out.writeInt(this.testInt);
        }

        // Read in the contents
        @Override
        public void deserialize(@NonNull NetIn in) throws IOException {
            this.testLong = in.readLong();
            this.testString = in.readString();
            this.testInt = in.readInt();
        }

    }

}
