package Server;
import java.io.*;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

public class ServerStrategyGenerateMaze implements IServerStrategy{
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            OutputStream toClient = new MyCompressorOutputStream(outToClient);

            int[] sizeArr = (int[]) fromClient.readObject();
            AMazeGenerator mazeGenerator = new MyMazeGenerator();
            Maze maze = mazeGenerator.generate(sizeArr[0], sizeArr[1]);

            toClient.write(maze.toByteArray());
            toClient.flush();
            fromClient.close();
            toClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
