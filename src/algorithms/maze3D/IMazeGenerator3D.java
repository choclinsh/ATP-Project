package algorithms.maze3D;

/**
 * Interface defining the operations for a 3D maze generator.
 */
public interface IMazeGenerator3D {
    /**
     * Generates a 3D maze with the given dimensions.
     *
     * @param depth  the number of depth layers.
     * @param row    the number of rows per layer.
     * @param column the number of columns per layer.
     * @return a Maze3D object representing the generated maze.
     */
    Maze3D generate(int depth, int row, int column);
    /**
     * Measures the time it takes to generate a 3D maze.
     *
     * @param depth  the number of depth layers.
     * @param row    the number of rows per layer.
     * @param column the number of columns per layer.
     * @return time taken in milliseconds.
     */
    long measureAlgorithmTimeMillis(int depth, int row, int column);
}
