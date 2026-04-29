package algorithms.mazeGenerators;
/**
 * Abstract base class for maze generators.
 * Implements the time measurement functionality and defines the abstract generate method.
 */
public abstract class AMazeGenerator implements IMazeGenerator{
    /**
     * Constructor for the abstract maze generator.
     */
    public AMazeGenerator(){}
    /**
     * Abstract method to generate a maze with the specified dimensions.
     * To be implemented by concrete subclasses.
     *
     * @param rows The number of rows in the maze
     * @param cols The number of columns in the maze
     * @return A new Maze object
     */
    public abstract Maze generate(int rows, int cols);
    /**
     * Measures the time it takes to generate a maze with the specified dimensions.
     *
     * @param rows The number of rows in the maze
     * @param cols The number of columns in the maze
     * @return The time in milliseconds it took to generate the maze
     */
    @Override
    public long measureAlgorithmTimeMillis(int rows, int cols){
        long start = System.currentTimeMillis();
        if (rows == 0 || cols == 0)
        {
            return 0;
        }
        Maze created_maze = generate(rows, cols);
        long finish = System.currentTimeMillis();
        return (finish - start);
    }
}
