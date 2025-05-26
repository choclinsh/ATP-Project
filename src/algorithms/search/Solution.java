package algorithms.search;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import java.io.*;
import java.util.ArrayList;

/**
 * Represents a solution to a search problem.
 * Contains the path from the start state to the goal state.
 *
 * This class implements Serializable with custom compression for efficient network transmission.
 * Solutions can be constructed from ArrayList paths or reconstructed from byte arrays.
 */
public class Solution implements Serializable {
    /** List of states representing the path from start to goal */
    private ArrayList<AState> solutionPath;

    /**
     * Creates a new solution with the specified path.
     *
     * @param path The path from start to goal (can be null for empty solution)
     */
    public Solution(ArrayList<AState> path)
    {
        this.solutionPath = path;
    }

    /**
     * Reconstructs a Solution from a byte array representation.
     * Parses the byte array to reconstruct individual MazeState objects that make up the solution path.
     *
     * Expected byte array format:
     * - Consecutive UTF-encoded state strings, each with:
     *   - 2 bytes: UTF string length (big-endian)
     *   - N bytes: UTF-8 encoded state string
     *
     * This constructor is used for deserializing solutions received from network transmission.
     *
     * @param arr Byte array containing serialized solution data
     * @throws IOException If the byte array format is invalid or reconstruction fails
     */
    public Solution(byte[] arr) throws IOException {
        try {
            solutionPath = new ArrayList<>();
            int length = arr.length;

            // Parse the byte array to reconstruct MazeState objects
            int offset = 0;
            while (offset < length) {
                // Read UTF string length (first 2 bytes)
                if (offset + 2 > length) break;

                int utfLength = ((arr[offset] & 0xFF) << 8) | (arr[offset + 1] & 0xFF);
                offset += 2;

                if (offset + utfLength > length) break;

                // Extract the state string bytes including length prefix
                byte[] stateBytes = new byte[utfLength + 2]; // Include length prefix
                stateBytes[0] = (byte)((utfLength >> 8) & 0xFF);
                stateBytes[1] = (byte)(utfLength & 0xFF);
                System.arraycopy(arr, offset, stateBytes, 2, utfLength);

                // Create MazeState from bytes and add to solution path
                MazeState state = new MazeState(stateBytes);
                solutionPath.add(state);

                offset += utfLength;
            }

        } catch (Exception e) {
            throw new IOException("Failed to reconstruct solution path", e);
        }
    }

    /**
     * Gets the solution path.
     * Returns the list of states from start to goal.
     *
     * @return The path from start to goal as a list of AState objects
     */
    public ArrayList<AState> getSolutionPath(){
        return solutionPath;
    }

    /**
     * Returns a string representation of the solution.
     * Shows the number of steps in the solution path.
     *
     * @return "0" if no solution path exists, otherwise the number of steps as a string
     */
    @Override
    public String toString() {
        if (solutionPath == null){
            return "0";
        }
        else {
            return String.valueOf(solutionPath.size());
        }
    }

    /**
     * Converts the solution to a byte array representation for storage or transmission.
     * Serializes each state in the solution path to bytes and concatenates them.
     *
     * The resulting byte array format consists of consecutive UTF-encoded state strings,
     * where each state includes its length prefix for proper parsing.
     *
     * @return Byte array representation of the entire solution path
     * @throws IOException If serialization of any state fails
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] stateBytes;

        // Serialize each state in the solution path
        for (AState state : solutionPath) {
            stateBytes = state.toByteArray();
            out.write(stateBytes);
        }

        return out.toByteArray();
    }

    /**
     * Custom serialization method that compresses solution data before transmission.
     * Converts the solution to byte array, compresses it using MyCompressorOutputStream,
     * and writes the compressed data with length information.
     *
     * @param out The ObjectOutputStream to write to
     * @throws IOException If an I/O error occurs during serialization
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        // For large fields, compress before writing
        if (solutionPath != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MyCompressorOutputStream compressor = new MyCompressorOutputStream(byteArrayOutputStream);
            byte[] solutionByteArr = this.toByteArray();
            compressor.write(solutionByteArr);
            compressor.flush();
            byte[] compressed = byteArrayOutputStream.toByteArray();

            out.writeInt(compressed.length);    // Write compressed data length
            out.writeInt(solutionByteArr.length); // Write original data length
            out.write(compressed);              // Write compressed data
        } else {
            out.writeInt(0); // No data to serialize
        }
    }

    /**
     * Custom deserialization method that decompresses solution data after transmission.
     * Reads compressed data, decompresses it using MyDecompressorInputStream,
     * and reconstructs the solution path from the decompressed byte array.
     *
     * @param in The ObjectInputStream to read from
     * @throws IOException If an I/O error occurs during deserialization
     * @throws ClassNotFoundException If a required class cannot be found
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int compressedLength = in.readInt();
        int decompressedLength = in.readInt();

        if (compressedLength > 0) {
            byte[] compressedData = new byte[compressedLength];
            in.readFully(compressedData);

            // Decompress the data
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
            MyDecompressorInputStream decompressor = new MyDecompressorInputStream(byteArrayInputStream);

            byte[] decompressedData = new byte[decompressedLength];
            decompressor.read(decompressedData);

            // Reconstruct solutionPath from decompressed bytes
            reconstructSolutionPath(decompressedData, decompressedLength);
        } else {
            // Initialize empty solution path for empty solutions
            solutionPath = new ArrayList<>();
        }
    }

    /**
     * Reconstructs the solution path from decompressed byte array data.
     * Helper method used by readObject() to rebuild the ArrayList of states.
     *
     * Parses the byte array in the same format as the byte array constructor,
     * creating MazeState objects and adding them to the solution path.
     *
     * @param data Decompressed byte array containing solution data
     * @param length Length of the data array to process
     * @throws IOException If reconstruction fails due to invalid data format
     */
    private void reconstructSolutionPath(byte[] data, int length) throws IOException {
        try {
            solutionPath = new ArrayList<>();

            // Parse the byte array to reconstruct MazeState objects
            int offset = 0;
            while (offset < length) {
                // Read UTF string length (first 2 bytes)
                if (offset + 2 > length) break;

                int utfLength = ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
                offset += 2;

                if (offset + utfLength > length) break;

                // Extract the state string bytes including length prefix
                byte[] stateBytes = new byte[utfLength + 2]; // Include length prefix
                stateBytes[0] = (byte)((utfLength >> 8) & 0xFF);
                stateBytes[1] = (byte)(utfLength & 0xFF);
                System.arraycopy(data, offset, stateBytes, 2, utfLength);

                // Create MazeState from bytes and add to solution path
                MazeState state = new MazeState(stateBytes);
                solutionPath.add(state);

                offset += utfLength;
            }

        } catch (Exception e) {
            throw new IOException("Failed to reconstruct solution path", e);
        }
    }
}