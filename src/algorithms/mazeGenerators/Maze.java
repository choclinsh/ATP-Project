package algorithms.mazeGenerators;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;

import java.io.*;
import java.util.Random;

/**
 * Represents a 2D maze with walls, paths, start position, and goal position.
 * The maze consists of a 2D array where 0 represents traversable paths and 1 represents walls.
 *
 * This class implements Serializable with custom compression for efficient network transmission.
 * Start and goal positions are automatically generated on maze borders if not explicitly set.
 */
public class Maze implements Serializable {
    /** 2D array representing the maze structure (0 = path, 1 = wall) */
    private int[][] maze;

    /** Number of rows in the maze */
    private int rows;

    /** Number of columns in the maze */
    private int cols;

    /** Starting position in the maze */
    private Position start_pos;

    /** Goal/end position in the maze */
    private Position goal_pos;

    /**
     * Creates a maze with the specified dimensions.
     * Initializes the 2D maze array but does not populate it with walls/paths.
     *
     * @param rows The number of rows in the maze (must be positive)
     * @param cols The number of columns in the maze (must be positive)
     */
    public Maze(int rows, int cols){
        this.maze = new int[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Reconstructs a maze from a byte array representation.
     * Used for deserialization and network transmission.
     *
     * Expected byte array format:
     * - First (rows × cols) bytes: maze cell values (0 or 1)
     * - Last 12 bytes: metadata in big-endian format:
     *   - rows (2 bytes), cols (2 bytes)
     *   - start_row (2 bytes), start_col (2 bytes)
     *   - goal_row (2 bytes), goal_col (2 bytes)
     *
     * @param b Byte array containing maze data in the expected format
     */
    public Maze(byte[] b){
        int metadataStart = b.length - 12;
        int idx = 0;

        // Read metadata fields from the end of the byte array
        rows = readBytes(b, metadataStart);
        cols = readBytes(b, metadataStart + 2);
        int startRow = readBytes(b, metadataStart + 4);
        int startCol = readBytes(b, metadataStart + 6);
        int goalRow = readBytes(b, metadataStart + 8);
        int goalCol = readBytes(b, metadataStart + 10);

        start_pos = new Position(startRow, startCol);
        goal_pos = new Position(goalRow, goalCol);

        // Read the maze structure from the array
        maze = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = b[idx++];
            }
        }
    }

    /**
     * Reads a 2-byte integer from a byte array at the specified offset.
     * Uses big-endian byte order (most significant byte first).
     *
     * @param b The byte array to read from
     * @param offset The starting position in the array
     * @return The integer value represented by the 2 bytes
     */
    private int readBytes(byte[] b, int offset) {
        return ((b[offset] & 0xFF) << 8) | (b[offset + 1] & 0xFF);
    }

    /**
     * Custom serialization method that compresses maze data before transmission.
     * Converts the maze to byte array, compresses it using MyCompressorOutputStream,
     * and writes the compressed data with length information.
     *
     * @param out The ObjectOutputStream to write to
     * @throws IOException If an I/O error occurs during serialization
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        if (maze != null && goal_pos != null && start_pos != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MyCompressorOutputStream compressor = new MyCompressorOutputStream(byteArrayOutputStream);
            byte[] mazeByteArr = this.toByteArray();
            compressor.write(mazeByteArr); // compress the array
            compressor.flush();
            byte[] compressedMaze = byteArrayOutputStream.toByteArray();

            out.writeInt(compressedMaze.length); // Write compressed data length first
            out.writeInt(mazeByteArr.length);    // Write original data length
            out.write(compressedMaze);           // Write compressed data
        } else {
            out.writeInt(0); // No data to serialize
        }
    }

    /**
     * Custom deserialization method that decompresses maze data after transmission.
     * Reads compressed data, decompresses it using MyDecompressorInputStream,
     * and reconstructs the maze from the decompressed byte array.
     *
     * @param in The ObjectInputStream to read from
     * @throws IOException If an I/O error occurs during deserialization
     * @throws ClassNotFoundException If a required class cannot be found
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int compressedLength = in.readInt();

        if (compressedLength > 0) {
            int decompressedLength = in.readInt();  // read first the size to know in what size to create the array
            byte[] compressedData = new byte[compressedLength];
            in.readFully(compressedData);

            // Decompress the data
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
            MyDecompressorInputStream decompressor = new MyDecompressorInputStream(byteArrayInputStream);
            byte[] decompressedData = new byte[decompressedLength];
            decompressor.read(decompressedData);

            // Use existing constructor logic to populate fields
            initializeFromByteArray(decompressedData);
        }
    }

    /**
     * Initializes all maze fields from a byte array.
     * Used by both the byte array constructor and the readObject method.
     * Separates metadata reading from maze structure reading for code reuse.
     *
     * @param b Byte array containing maze data in the expected format
     */
    private void initializeFromByteArray(byte[] b) {
        int metadataStart = b.length - 12;
        int idx = 0;

        // Read metadata fields from the end of the byte array. used to updating the fields after the object has been
        // already created, unlike in the constructor.
        this.rows = readBytes(b, metadataStart);
        this.cols = readBytes(b, metadataStart + 2);
        int startRow = readBytes(b, metadataStart + 4);
        int startCol = readBytes(b, metadataStart + 6);
        int goalRow = readBytes(b, metadataStart + 8);
        int goalCol = readBytes(b, metadataStart + 10);

        this.start_pos = new Position(startRow, startCol);
        this.goal_pos = new Position(goalRow, goalCol);

        maze = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = b[idx++];
            }
        }
    }

    /**
     * Gets the 2D array representing the maze structure.
     * Returns a reference to the internal array - modifications will affect the maze.
     *
     * @return The 2D maze array where 0 = path, 1 = wall
     */
    public int[][] getMaze(){
        return this.maze;
    }

    /**
     * Gets the value of a specific cell in the maze.
     * No bounds checking is performed - ensure valid coordinates.
     *
     * @param row The row index (0-based)
     * @param col The column index (0-based)
     * @return The cell value (0 for path, 1 for wall)
     */
    public int getCell(int row, int col){
        return maze[row][col];
    }

    /**
     * Gets the number of rows in the maze.
     *
     * @return The number of rows
     */
    public int getRows(){
        return this.rows;
    }

    /**
     * Gets the number of columns in the maze.
     *
     * @return The number of columns
     */
    public int getCols(){
        return this.cols;
    }

    /**
     * Prints the maze to the console with special markers for start and goal positions.
     * Uses 'S' to mark the start position, 'E' to mark the goal position,
     * and numeric values (0/1) for other cells.
     * Each cell is formatted with 4-character width for alignment.
     */
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == start_pos.getRowIndex() && j == start_pos.getColumnIndex()) {
                    System.out.printf("%4c", 'S');
                } else if (i == goal_pos.getRowIndex() && j == goal_pos.getColumnIndex()) {
                    System.out.printf("%4c", 'E');
                } else {
                    System.out.printf("%4d", maze[i][j]);
                }
            }
            System.out.println();
        }
    }

    /**
     * Gets the start position of the maze.
     * If the start position is not already set, generates a random position
     * on the maze border using getPosition() method.
     *
     * @return A copy of the start position (modifications won't affect the original)
     */
    public Position getStartPosition() {
        if (start_pos == null)
        {
            start_pos = getPosition();
            return new Position(start_pos);
        }
        else {
            return new Position(start_pos);
        }
    }

    /**
     * Gets the goal position of the maze.
     * If the goal position is not already set, generates a random position
     * on the maze border using getPosition() method.
     *
     * @return A copy of the goal position (modifications won't affect the original)
     */
    public Position getGoalPosition() {
        if (goal_pos == null)
        {
            goal_pos = getPosition();
            return new Position(goal_pos);
        }
        else {
            return new Position(goal_pos);
        }
    }

    /**
     * Generates a random position on the border of the maze that is traversable (value 0).
     * The algorithm randomly selects between horizontal and vertical borders,
     * then randomly picks a coordinate on that border.
     * Continues until a traversable position is found.
     *
     * Used internally for automatic start/goal position generation.
     *
     * @return A randomly generated traversable position on the maze border
     */
    public Position getPosition() {
        int row_index;
        int col_index;
        Random rd = new Random();
        boolean choice;

        while (true)
        {
            choice = rd.nextBoolean();

            if (choice) // Try horizontal borders (left/right edges)
            {
                row_index = (int)(Math.random() * rows);
                choice = rd.nextBoolean();

                if (choice){ // Try left border
                    if (maze[row_index][0] == 0){
                        return new Position(row_index, 0);
                    }
                } else{ // Try right border
                    if (maze[row_index][cols-1] == 0){
                        return new Position(row_index, cols-1);
                    }
                }
            }
            else { // Try vertical borders (top/bottom edges)
                col_index = (int)(Math.random() * cols);
                choice = rd.nextBoolean();

                if (choice) { // Try top border
                    if (maze[0][col_index] == 0){
                        return new Position(0, col_index);
                    }
                }
                else { // Try bottom border
                    if (maze[rows-1][col_index] == 0){
                        return new Position(rows-1, col_index);
                    }
                }
            }
        }
    }

    /**
     * Converts the maze to a byte array representation for storage or transmission.
     *
     * Format:
     * - First (rows × cols) bytes: maze cell values (0 or 1)
     * - One separator byte (0xFF)
     * - Last 12 bytes: metadata in big-endian format:
     *   - rows (2 bytes), cols (2 bytes)
     *   - start_row (2 bytes), start_col (2 bytes)
     *   - goal_row (2 bytes), goal_col (2 bytes)
     *
     * @return Byte array representation of the maze
     */
    public byte[] toByteArray(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Write maze content (0s and 1s)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                out.write(maze[i][j]);
            }
        }

        out.write(0xFF); // Separator byte

        // Write metadata fields as 2-byte integers each
        addBytesToArr(out, rows);
        addBytesToArr(out, cols);
        addBytesToArr(out, start_pos.getRowIndex());
        addBytesToArr(out, start_pos.getColumnIndex());
        addBytesToArr(out, goal_pos.getRowIndex());
        addBytesToArr(out, goal_pos.getColumnIndex());

        return out.toByteArray();
    }

    /**
     * Converts an integer to 2 bytes and adds them to the output stream.
     * Uses big-endian byte order (most significant byte first).
     *
     * Helper method for toByteArray() to maintain consistent byte ordering.
     *
     * @param byteArr The ByteArrayOutputStream to write to
     * @param num The integer value to convert (should fit in 2 bytes: 0-65535)
     */
    public void addBytesToArr(ByteArrayOutputStream byteArr, int num){
        byteArr.write((num >> 8) & 0xFF); // high byte (most significant)
        byteArr.write(num & 0xFF);        // low byte (least significant)
    }
}