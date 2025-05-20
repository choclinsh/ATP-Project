package IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SimpleCompressorOutputStream extends OutputStream {
    OutputStream out;

    public SimpleCompressorOutputStream(OutputStream outputStream){
        this.out = outputStream;
    }

    public void write(int b) throws IOException {
        out.write(b);
    }

    public void write(byte[] b) throws IOException{
        byte[] compressed = compress(b);
        int counter = 1;
        if (compressed.length > 0) {
            int i;
            for (i=1; i < compressed.length; i++) {
                if (compressed[i] == compressed[i-1] && counter < 255) {
                    counter++;

                } else {
                    out.write(compressed[i-1]);
                    out.write(counter);
                    counter = 1;
                }
            }
            out.write(compressed[i-1]);
            out.write(counter);
        }
    }

    public byte[] compress(byte[] b){
        ByteArrayOutputStream dynamicArr = new ByteArrayOutputStream();
        int counter = 0;
        int index = 0;
        int lastCell = 0;
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 0xFF) == 0xFF){
                dynamicArr.write(counter);
                index = i;
                break;
            }

            if ((b[i] & 0xFF) == lastCell) {
                counter++;
                if (counter == 255){
                    dynamicArr.write(counter);
                    counter = 0;
                    lastCell = (b[i]& 0xFF) == 0 ? 1 : 0;
                }
            } else {
                dynamicArr.write(counter);
                lastCell = b[i] & 0xFF;
                counter = 1;
            }
        }
        while (index < b.length){
            dynamicArr.write(b[index]& 0xFF);
            index++;
        }
        dynamicArr.write(0xFF);
        return dynamicArr.toByteArray();
    }
}
