package algorithms.mazeGenerators;
/**
 * Represents a position in a 2D maze with row and column coordinates.
 */
public class Position {
    private int row;
    private int col;
    /**
     * Creates a position with the specified coordinates.
     *
     * @param row The row index
     * @param col The column index
     */
    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }
    /**
     * Creates a deep copy of another position.
     *
     * @param other The position to copy
     */
    public Position(Position other) {
        this.row = other.row;
        this.col = other.col; // deep copy of address
    }
    /**
     * Gets the row index.
     *
     * @return The row index
     */
    public int getRowIndex(){
        return this.row;
    }
    /**
     * Gets the column index.
     *
     * @return The column index
     */
    public int getColumnIndex(){
        return this.col;
    }
    /**
     * Returns a string representation of the position in the format {row,col}.
     *
     * @return String representation of the position
     */
    @Override
    public String toString() {
        return "{" + row  + "," + col + "}";
    }
}
