package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator{




    public AMazeGenerator(int rows, int cols){}

    public abstract Maze generate(int rows, int cols);

    @Override
    public long measureAlgorithmTimeMillis(int rows, int cols){
        long start = System.currentTimeMillis();
        Maze created_maze = generate(rows, cols);
        long finish = System.currentTimeMillis();
        return (finish - start);
    }
}
