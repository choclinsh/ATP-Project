package IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

/**
 * Custom output stream that compresses data using the DEFLATE algorithm.
 *
 * This class extends OutputStream and provides transparent compression
 * of data written to it. The compressed data is then written to the
 * underlying output stream.
 *
 * Uses Java's built-in Deflater class for compression with default settings.
 * Suitable for reducing network bandwidth when transmitting large data structures.
 */
public class MyCompressorOutputStream extends OutputStream {
    /** The underlying output stream to write compressed data to */
    OutputStream out;

    /**
     * Creates a new compressor output stream that writes compressed data to the specified output stream.
     *
     * @param outputStream The underlying output stream to write compressed data to (cannot be null)
     */
    public MyCompressorOutputStream(OutputStream outputStream){
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
     * Compresses the byte array and writes the compressed data to the underlying output stream.
     *
     * The entire byte array is compressed as a single unit using the DEFLATE algorithm,
     * then the compressed bytes are written to the output stream.
     * This method provides the main compression functionality of the class.
     *
     * @param b The byte array to compress and write (cannot be null)
     * @throws IOException If an I/O error occurs during compression or writing
     */
    public void write(byte[] b) throws IOException{
        byte[] compressed = compress(b);

        // Write all compressed bytes to the output stream
        for (byte value : compressed) {
            out.write(value);
        }
    }

    /**
     * Compresses a byte array using the DEFLATE algorithm.
     *
     * Creates a new Deflater instance for each compression operation,
     * compresses the input data, and returns the compressed byte array.
     *
     * The compression uses default settings:
     * - Default compression level (balance between speed and compression ratio)
     * - DEFLATE algorithm (same as used by ZIP files)
     *
     * @param b The byte array to compress (cannot be null)
     * @return A new byte array containing the compressed data
     * @throws RuntimeException If compression fails (wraps any underlying exceptions)
     */
    public byte[] compress(byte[] b){
        Deflater deflater = new Deflater();
        deflater.setInput(b);
        deflater.finish(); // Signal that no more input will be provided

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // 1KB buffer for reading compressed chunks

        // Read compressed data in chunks until compression is complete
        while (!deflater.finished()) {
            int compressedSize = deflater.deflate(buffer);
            outputStream.write(buffer, 0, compressedSize);
        }

        deflater.end(); // Clean up deflater resources
        return outputStream.toByteArray();
    }
}