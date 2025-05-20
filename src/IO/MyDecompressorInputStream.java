package IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class MyDecompressorInputStream extends InputStream{
    private InputStream in;
    public MyDecompressorInputStream(InputStream input){
        this.in = input;
    }
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        ByteArrayOutputStream encoded = new ByteArrayOutputStream();
        int value;
        while (true) {
            value = in.read();
            if (value == -1) {
                break;
            }
            encoded.write(value);
        }
        byte[] decompressed = null;
        try {
            decompressed = decompress(encoded.toByteArray());
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        }
        int len = Math.min(b.length, decompressed.length);
        System.arraycopy(decompressed, 0, b, 0, len);
        return len;
    }

    public byte[] decompress(byte[] compressed) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressed);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        while (!inflater.finished()) {
            int decompressedSize = inflater.inflate(buffer);
            outputStream.write(buffer, 0, decompressedSize);
        }

        return outputStream.toByteArray();
    }
}
