package IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Custom input stream that decompresses data using the INFLATE algorithm.
 *
 * This class extends InputStream and provides transparent decompression
 * of data read from the underlying input stream. It serves as the counterpart
 * to MyCompressorOutputStream, using Java's built-in Inflater for decompression.
 *
 * @author [Your Name]
 * @version 1.0
 */
public class MyDecompressorInputStream extends InputStream{
    /** The underlying input stream to read compressed data from */
    private InputStream in;

    /**
     * Creates a new decompressor input stream that reads compressed data from the specified input stream.
     *
     * @param input The underlying input stream to read compressed data from (cannot be null)
     */
    public MyDecompressorInputStream(InputStream input){
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
     * This method reads ALL available data from the underlying stream until EOF,
     * decompresses the entire data block using the INFLATE algorithm,
     * and copies the result into the provided buffer.
     *
     * The amount of data copied is limited by the smaller of the buffer size
     * and the decompressed data size.
     *
     * @param b The buffer into which the decompressed data is read
     * @return The number of bytes actually read into the buffer
     * @throws IOException If an I/O error occurs during reading or decompression
     * @throws RuntimeException If decompression fails due to invalid compressed data
     */
    @Override
    public int read(byte[] b) throws IOException {
        ByteArrayOutputStream encoded = new ByteArrayOutputStream();
        int value;

        // Read all available data from the input stream
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
            throw new RuntimeException("Failed to decompress data", e);
        }

        // Copy decompressed data to the output buffer
        int len = Math.min(b.length, decompressed.length);
        System.arraycopy(decompressed, 0, b, 0, len);
        return len;
    }

    /**
     * Decompresses a byte array using the INFLATE algorithm.
     *
     * Creates a new Inflater instance to decompress the input data.
     * The INFLATE algorithm is the counterpart to DEFLATE used in compression.
     *
     * @param compressed The compressed byte array to decompress (cannot be null)
     * @return A new byte array containing the decompressed data
     * @throws DataFormatException If the compressed data format is invalid or corrupted
     */
    public byte[] decompress(byte[] compressed) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressed);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // 1KB buffer for reading decompressed chunks

        // Read decompressed data in chunks until decompression is complete
        while (!inflater.finished()) {
            int decompressedSize = inflater.inflate(buffer);
            outputStream.write(buffer, 0, decompressedSize);
        }

        inflater.end(); // Clean up inflater resources
        return outputStream.toByteArray();
    }
}