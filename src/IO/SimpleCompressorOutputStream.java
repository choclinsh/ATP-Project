package IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Custom output stream that compresses data using a simple run-length encoding algorithm.
 *
 * This class implements a specialized compression scheme designed for binary maze data.
 * It uses run-length encoding for sequences of 0s and 1s, with a special marker (0xFF)
 * to separate the compressed portion from any literal data that follows.
 *
 * The compression is optimized for maze structures containing mostly 0s and 1s.
 *
 * @author [Your Name]
 * @version 1.0
 */
public class SimpleCompressorOutputStream extends OutputStream {
    /** The underlying output stream to write compressed data to */
    OutputStream out;

    /**
     * Creates a new simple compressor output stream that writes compressed data to the specified output stream.
     *
     * @param outputStream The underlying output stream to write compressed data to (cannot be null)
     */
    public SimpleCompressorOutputStream(OutputStream outputStream){
        this.out = outputStream;
    }

    /**
     * Writes a single byte to the underlying output stream without compression.
     *
     * Note: This method bypasses compression and writes the byte directly.
     * For consistent compression behavior, use write(byte[]) instead.
     *
     * @param b The byte to write (0-255)
     * @throws IOException If an I/O error occurs
     */
    public void write(int b) throws IOException {
        out.write(b);
    }

    /**
     * Compresses the byte array using run-length encoding and writes the compressed data.
     *
     * The compression process:
     * 1. First compresses using custom run-length encoding for 0s and 1s
     * 2. Then applies additional run-length encoding to consecutive identical bytes
     * 3. Writes the final compressed data to the underlying stream
     *
     * Each run is represented as: [value][count] where count is limited to 255.
     *
     * @param b The byte array to compress and write (cannot be null)
     * @throws IOException If an I/O error occurs during compression or writing
     */
    public void write(byte[] b) throws IOException{
        byte[] compressed = compress(b);
        int counter = 1;

        if (compressed.length > 0) {
            int i;
            // Apply additional run-length encoding to the pre-compressed data
            for (i = 1; i < compressed.length; i++) {
                if (compressed[i] == compressed[i-1] && counter < 255) {
                    counter++;
                } else {
                    // Write the value and its count
                    out.write(compressed[i-1]);
                    out.write(counter);
                    counter = 1;
                }
            }
            // Write the final value and count
            out.write(compressed[i-1]);
            out.write(counter);
        }
    }

    /**
     * Compresses a byte array using a specialized run-length encoding algorithm.
     *
     * This algorithm is designed specifically for maze data and works as follows:
     * 1. Encodes runs of 0s and 1s using run-length encoding
     * 2. When a 0xFF marker is encountered, stops run-length encoding
     * 3. Appends any remaining literal data after the marker
     * 4. Ends with a 0xFF terminator
     *
     * The algorithm alternates between encoding 0s and 1s, with counters limited to 255.
     * When a counter reaches 255, it's written and reset, switching to the opposite value.
     *
     * @param b The byte array to compress (cannot be null)
     * @return A new byte array containing the compressed data
     */
    public byte[] compress(byte[] b){
        ByteArrayOutputStream dynamicArr = new ByteArrayOutputStream();
        int counter = 0;
        int index = 0;
        int lastCell = 0; // Start with 0s

        // Process bytes until we hit the separator marker
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 0xFF) == 0xFF){
                // Found separator - write final counter and break
                dynamicArr.write(counter);
                index = i;
                break;
            }

            if ((b[i] & 0xFF) == lastCell) {
                // Same value as expected - increment counter
                counter++;
                if (counter == 255){
                    // Counter overflow - write it and switch to opposite value
                    dynamicArr.write(counter);
                    counter = 0;
                    lastCell = (b[i] & 0xFF) == 0 ? 1 : 0; // Switch between 0 and 1
                }
            } else {
                // Different value - write current counter and switch
                dynamicArr.write(counter);
                lastCell = b[i] & 0xFF;
                counter = 1;
            }
        }

        // Append any remaining literal data after the separator
        while (index < b.length){
            dynamicArr.write(b[index] & 0xFF);
            index++;
        }

        // Add terminator marker
        dynamicArr.write(0xFF);
        return dynamicArr.toByteArray();
    }
}