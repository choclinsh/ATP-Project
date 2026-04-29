package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.io.*;

/**
 * Represents a state in a maze search problem.
 * Encapsulates a position in the maze along with cost and parent information.
 *
 * This class extends AState and provides maze-specific functionality for
 * search algorithms. The state is represented as a string in the format "{row,col}"
 * and can be serialized to/from byte arrays for network transmission.
 */
public class MazeState extends AState{

    /**
     * Creates a new maze state for the given position.
     * Initializes with no parent and zero cost.
     *
     * @param pos The position in the maze (cannot be null)
     */
    public MazeState(Position pos){
        super(pos.toString());
        this.cameFrom = null; // check if new is better
        this.cost = 0;
    }

    /**
     * Creates a new maze state with the specified position, parent, and cost.
     *
     * @param pos The position in the maze (cannot be null)
     * @param father The parent state (can be null for start state)
     * @param cost The cost to reach this state (should be non-negative)
     */
    public MazeState(Position pos, AState father, int cost){
        super(pos.toString());
        this.cameFrom = father;
        this.cost = cost;
    }

    /**
     * Creates a MazeState from a byte array representation.
     * Deserializes the state from the byte representation using DataInputStream.
     * The byte array should contain a UTF string representing the position.
     *
     * Expected format: UTF string in the format "{row,col}"
     * The cost and parent are initialized to default values (0 and null respectively).
     *
     * @param arr The byte array containing the serialized MazeState data
     * @throws RuntimeException If deserialization fails due to invalid byte array format
     */
    public MazeState(byte[] arr){
        super(""); // Initialize with empty string, will be set from byte array

        try (ByteArrayInputStream bais = new ByteArrayInputStream(arr);
             DataInputStream dis = new DataInputStream(bais)) {

            // Reconstruct the state string in Position format: "{row,col}"
            this.state = dis.readUTF();
            this.cost = 0;
            this.cameFrom = null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize MazeState from byte array", e);
        }
    }

    /**
     * Extracts the row index from the state string representation.
     * Parses the state string (format: "{row,col}") to extract the row value.
     *
     * Example: For state "{3,5}", this method returns 3.
     *
     * @return The row index as an integer
     * @throws NumberFormatException If the state string format is invalid
     * @throws StringIndexOutOfBoundsException If the state string doesn't contain expected delimiters
     */
    public int getRowIndex(){
        int startIndex = state.indexOf('{') + 1;
        int endIndex = state.indexOf(',');

        String rowValue = state.substring(startIndex, endIndex);
        return Integer.parseInt(rowValue);
    }

    /**
     * Extracts the column index from the state string representation.
     * Parses the state string (format: "{row,col}") to extract the column value.
     *
     * Example: For state "{3,5}", this method returns 5.
     *
     * @return The column index as an integer
     * @throws NumberFormatException If the state string format is invalid
     * @throws StringIndexOutOfBoundsException If the state string doesn't contain expected delimiters
     */
    public int getColIndex(){
        int startIndex = state.indexOf(',') + 1;
        int endIndex = state.indexOf('}');

        String colValue = state.substring(startIndex, endIndex);
        return Integer.parseInt(colValue);
    }

    /**
     * Converts the MazeState to a byte array representation for storage or transmission.
     * Serializes only the state string using DataOutputStream.writeUTF().
     * The cost and parent information are not included in the serialization.
     *
     * The resulting byte array format:
     * - First 2 bytes: UTF string length (big-endian)
     * - Remaining bytes: UTF-8 encoded state string
     *
     * This method is used for network transmission and caching purposes.
     *
     * @return Byte array representation of the MazeState
     * @throws RuntimeException If serialization fails due to I/O errors
     */
    public byte[] toByteArray() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();  // when created from byte array dont include the
             // recursive cameFrom field and the cost is not relevant.
             DataOutputStream dos = new DataOutputStream(baos)) {

            // Write the state string using UTF format
            dos.writeUTF(this.state);

            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize MazeState to byte array", e);
        }
    }
}