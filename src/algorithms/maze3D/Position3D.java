package algorithms.maze3D;

import algorithms.mazeGenerators.Position;

/**
 * Represents a position in a 3D space using depth, row, and column indices.
 * This class is used in the context of 3D mazes to identify specific locations.
 */
public class Position3D {
    private int depth;
    private int row;
    private int col;

    /**
     * Constructs a new Position3D with the specified depth, row, and column.
     * @param depth the depth index (z-axis).
     * @param row the row index (y-axis).
     * @param col the column index (x-axis).
     */
    public Position3D(int depth, int row, int col){
        this.depth = depth;
        this.row = row;
        this.col = col;
    }

    /**
     * Constructs a new Position3D by copying another Position3D.
     * Performs a deep copy of the position data.
     * @param other the Position3D to copy.
     */
    public Position3D(Position3D other) {
        this.depth = other.depth;
        this.row = other.row;
        this.col = other.col; // deep copy of address
    }

    /**
     * Gets the depth index (z-axis) of this position.
     * @return the depth index.
     */
    public int getDepthIndex(){
        return this.depth;
    }

    /**
     * Gets the row index (y-axis) of this position.
     * @return the row index.
     */
    public int getRowIndex(){
        return this.row;
    }

    /**
     * Gets the column index (x-axis) of this position.
     * @return the column index.
     */
    public int getColumnIndex(){
        return this.col;
    }

    /**
     * Returns a string representation of the 3D position.
     * Format: "{depth,row,col}"
     * @return the string representation of this position.
     */
    @Override
    public String toString() {
        return "{" + depth + "," + row  + "," + col + "}";
    }
}
