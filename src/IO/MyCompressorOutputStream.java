package IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

public class MyCompressorOutputStream extends OutputStream {
    OutputStream out;

    public MyCompressorOutputStream(OutputStream outputStream){
        this.out = outputStream;
    }

    public void write(int b) throws IOException {
        out.write(b);
    }

    public void write(byte[] b) throws IOException{
        byte[] compressed = compress(b);
        for (byte value : compressed) {
            out.write(value);
        }
    }

    public byte[] compress(byte[] b){
        Deflater deflater = new Deflater();
        deflater.setInput(b);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int compressedSize = deflater.deflate(buffer);
            outputStream.write(buffer, 0, compressedSize);
        }

        return outputStream.toByteArray();
    }
}
