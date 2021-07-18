package co.casterlabs.packetproto.test;

import java.io.IOException;

import co.casterlabs.packetproto.Packet;
import co.casterlabs.packetproto.io.NetCalc;
import co.casterlabs.packetproto.io.NetIn;
import co.casterlabs.packetproto.io.NetOut;
import co.casterlabs.packetproto.io.buffer.ByteBufferNetIn;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

public class TestJSSer {

    public static void main(String[] args) throws Exception {
        // Copy/paste the result from the JS environment
        byte[] bytes = {
                0, 0, 0, 0, 0, 0, 39, 8, 0, 0, 0, 22, 72, 101, 108, 108, 111, 32, 102, 114, 111, 109, 32, 74, 97, 118, 97, 83, 99, 114, 105, 112, 116, 33, 0, 62, -127, 117, -102
        };
        ByteBufferNetIn in = new ByteBufferNetIn(bytes);

        // Setup the test class
        JSTestPacket test1 = new JSTestPacket();

        // Deserialize
        test1.deserialize(in);

        System.out.println(test1);
    }

    @ToString
    @EqualsAndHashCode
    public static class JSTestPacket implements Packet {
        private long testLong;
        private String testString;
        private float testFloat;

        @Override
        public void calc(@NonNull NetCalc calc) {}

        @Override
        public void serialize(@NonNull NetOut out) throws IOException {}

        // Read in the contents
        @Override
        public void deserialize(@NonNull NetIn in) throws IOException {
            this.testLong = in.readLong();
            this.testString = in.readString();
            this.testFloat = in.readFloat();
        }

    }

}
