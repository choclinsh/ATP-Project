package IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleDecompressorInputStream extends InputStream {
    private InputStream in;
    public SimpleDecompressorInputStream(InputStream input){
        this.in = input;
    }

    public int read() throws IOException {
        return in.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        ByteArrayOutputStream encoded = new ByteArrayOutputStream();
        int value;
        int numMarkers = 0;
        while (true) {
            if ((value = in.read()) == 0xFF){
                numMarkers++;
                if (numMarkers ==2){
                    break;
                }
            }
            int counter = in.read() & 0xFF;
            if (counter == 0) {
                throw new IOException("Missing counter, invalid format");
            }

            for (int i = 0; i < counter; i++) {
                encoded.write(value);
            }
        }
        byte[] decompressed = decompress(encoded.toByteArray());
        int len = Math.min(b.length, decompressed.length);
        System.arraycopy(decompressed, 0, b, 0, len);
        return len;
    }

    public byte[] decompress(byte[] compressed) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        int counter ;
        int i = 0;
        int currentValue = 0;

        while (i < compressed.length) {
            counter = compressed[i++] & 0xFF;
            if (counter == 0xFF) {
                result.write(counter);
                break;
            }

            for (int j = 0; j < counter; j++) {
                result.write(currentValue);
            }
            currentValue = currentValue == 0 ? 1 : 0;
        }
        // all remaining bytes are literal data
        if (i < compressed.length) {
            while (i < compressed.length) {
                result.write(compressed[i++]& 0xFF);
            }
        }
        return result.toByteArray();
    }
}
