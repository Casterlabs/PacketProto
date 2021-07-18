package co.casterlabs.packetproto.test;

import java.io.IOException;
import java.util.UUID;

import co.casterlabs.packetproto.Packet;
import co.casterlabs.packetproto.io.NetCalc;
import co.casterlabs.packetproto.io.NetIn;
import co.casterlabs.packetproto.io.NetOut;
import co.casterlabs.packetproto.io.buffer.ByteBufferNetOut;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

public class CreateJSTestDeser {

    public static void main(String[] args) throws Exception {
        // Setup the test class
        JSTestPacket test1 = new JSTestPacket();

        test1.testLong = 123456;
        test1.testString = UUID.randomUUID().toString();
        test1.testFloat = 123.456f;

        // Calculate how much we need to allocate to the buffer
        NetCalc calc = new NetCalc();
        test1.calc(calc);

        int byteLength = calc.getRequiredBytes();

        // Serialize to bytes
        ByteBufferNetOut out = new ByteBufferNetOut(byteLength);
        test1.serialize(out);

        // Rewind the buffer.
        out.getBuf().rewind();

        byte[] bytes = new byte[byteLength];
        out.getBuf().get(bytes);

        // Create javascript paste
        String[] bytesAsString = new String[byteLength];

        for (int idx = 0; idx < byteLength; idx++) {
            bytesAsString[idx] = Byte.toString(bytes[idx]);
        }

        System.out.println(test1);

        System.out.println("\nPaste this into the JS interpreter:");
        System.out.printf("testDeser([%s])\n", String.join(", ", bytesAsString));
    }

    @ToString
    @EqualsAndHashCode
    public static class JSTestPacket implements Packet {
        private long testLong;
        private String testString;
        private float testFloat;

        // Calculate how much we need to allocate
        @Override
        public void calc(@NonNull NetCalc calc) {
            calc.allocateLong();
            calc.allocateString(this.testString);
            calc.allocateFloat();
        }

        // Write out the contents
        @Override
        public void serialize(@NonNull NetOut out) throws IOException {
            out.writeLong(this.testLong);
            out.writeString(this.testString);
            out.writeFloat(this.testFloat);
        }

        @Override
        public void deserialize(@NonNull NetIn in) throws IOException {}

    }

}
