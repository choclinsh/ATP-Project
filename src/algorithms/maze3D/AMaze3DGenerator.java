package algorithms.maze3D;

import algorithms.mazeGenerators.Maze;
/**
 * An abstract base class for 3D maze generators.
 * Provides a method to measure the time it takes to generate a maze.
 */
public abstract class AMaze3DGenerator implements IMazeGenerator3D {

    /**
     * Default constructor for AMaze3DGenerator.
     */
    public AMaze3DGenerator(){}

    /**
     * Generates a 3D maze with the specified dimensions.
     *
     * @param depth the number of layers in the maze.
     * @param rows  the number of rows in each layer.
     * @param cols  the number of columns in each layer.
     * @return a generated Maze3D object.
     */
    public abstract Maze3D generate(int depth, int rows, int cols);


    /**
     * Measures the time it takes to generate a 3D maze.
     *
     * @param depth the number of layers in the maze.
     * @param rows  the number of rows in each layer.
     * @param cols  the number of columns in each layer.
     * @return the time in milliseconds taken to generate the maze.
     */
    @Override
    public long measureAlgorithmTimeMillis(int depth, int rows, int cols){
        long start = System.currentTimeMillis();
        Maze3D created_maze = generate(depth, rows, cols);
        long finish = System.currentTimeMillis();
        return (finish - start);
    }
}
