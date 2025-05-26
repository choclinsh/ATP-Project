package IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Custom input stream that decompresses data compressed by SimpleCompressorOutputStream.
 *
 * This class reverses the compression algorithm used by SimpleCompressorOutputStream,
 * reconstructing the original data from the run-length encoded format.
 * It handles both the run-length encoded portion and any literal data that follows.
 *
 * @author [Your Name]
 * @version 1.0
 */
public class SimpleDecompressorInputStream extends InputStream {
    /** The underlying input stream to read compressed data from */
    private InputStream in;

    /**
     * Creates a new simple decompressor input stream that reads compressed data from the specified input stream.
     *
     * @param input The underlying input stream to read compressed data from (cannot be null)
     */
    public SimpleDecompressorInputStream(InputStream input){
        this.in = input;
    }

    /**
     * Reads a single byte from the underlying input stream without decompression.
     *
     * Note: This method bypasses decompression and reads the byte directly.
     * For consistent decompression behavior, use read(byte[]) instead.
     *
     * @return The next byte of data, or -1 if the end of the stream is reached
     * @throws IOException If an I/O error occurs
     */
    public int read() throws IOException {
        return in.read();
    }

    /**
     * Reads and decompresses data from the underlying input stream into the provided byte array.
     *
     * This method reads the compressed data in the format produced by SimpleCompressorOutputStream:
     * 1. Reads [value][count] pairs until the first 0xFF marker
     * 2. Expands each pair into the corresponding number of bytes
     * 3. Continues until the second 0xFF marker (terminator)
     * 4. Decompresses the expanded data using the decompress method
     *
     * The amount of data copied is limited by the smaller of the buffer size
     * and the decompressed data size.
     *
     * @param b The buffer into which the decompressed data is read
     * @return The number of bytes actually read into the buffer
     * @throws IOException If an I/O error occurs or the data format is invalid
     */
    @Override
    public int read(byte[] b) throws IOException {
        ByteArrayOutputStream encoded = new ByteArrayOutputStream();
        int value;
        int numMarkers = 0;

        // Read and expand run-length encoded data
        while (true) {
            if ((value = in.read()) == 0xFF){
                numMarkers++;
                if (numMarkers == 2){
                    // Found terminator - end of compressed data
                    break;
                }
            }

            // Read the count for this value
            int counter = in.read() & 0xFF;
            if (counter == 0) {
                throw new IOException("Missing counter, invalid format");
            }

            // Expand the run-length encoded pair
            for (int i = 0; i < counter; i++) {
                encoded.write(value);
            }
        }

        // Decompress the expanded data
        byte[] decompressed = decompress(encoded.toByteArray());
        int len = Math.min(b.length, decompressed.length);
        System.arraycopy(decompressed, 0, b, 0, len);
        return len;
    }

    /**
     * Decompresses data that was compressed using the custom run-length encoding algorithm.
     *
     * This method reverses the compression process:
     * 1. Reads run-length counts and alternates between 0s and 1s
     * 2. When 0xFF is encountered, switches to literal data mode
     * 3. Appends any remaining literal bytes to the result
     *
     * The algorithm reconstructs the original binary data by expanding
     * the run-length encoded sequences back into their original form.
     *
     * @param compressed The compressed byte array to decompress
     * @return A new byte array containing the decompressed data
     */
    public byte[] decompress(byte[] compressed) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        int counter;
        int i = 0;
        int currentValue = 0; // Start with 0s

        // Process run-length encoded data
        while (i < compressed.length) {
            counter = compressed[i++] & 0xFF;

            if (counter == 0xFF) {
                // Separator found - switch to literal data mode
                result.write(counter);
                break;
            }

            // Expand the run of current value
            for (int j = 0; j < counter; j++) {
                result.write(currentValue);
            }

            // Alternate between 0 and 1
            currentValue = currentValue == 0 ? 1 : 0;
        }

        // Append any remaining literal data
        if (i < compressed.length) {
            while (i < compressed.length) {
                result.write(compressed[i++] & 0xFF);
            }
        }

        return result.toByteArray();
    }
}