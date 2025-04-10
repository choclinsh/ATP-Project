package algorithms.mazeGenerators;

public class Position {
    private int row;
    private int col;

    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }
    public Position(Position other) {
        this.row = other.row;
        this.col = other.col; // deep copy of address
    }
    public int getRowIndex(){
        return this.row;
    }
    public int getColumnIndex(){
        return this.col;
    }

    @Override
    public String toString() {
        return "{" + row  + "," + col + "}";
    }
}
