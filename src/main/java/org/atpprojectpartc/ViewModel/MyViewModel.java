package org.atpprojectpartc.ViewModel;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.stage.Stage;
import org.atpprojectpartc.Model.IModel;
import org.atpprojectpartc.Model.Move;
import org.atpprojectpartc.Model.MyModel;
import org.atpprojectpartc.View.Direction;

import java.io.IOException;
import java.util.ArrayList;

public class MyViewModel {
    private IModel model = new MyModel();
    public Maze generateMaze(String rows, String cols) throws IOException, ClassNotFoundException {
        return model.generateMaze(rows, cols);
    }

    public int[] getPlayerLocation(){
        return model.getPlayerLocation();
    }

    public Move setPlayerLocation(Direction direction) {
        return model.setPlayerLocation(direction);
    }

    public void saveMazeWithFileChooser(Stage stage){
        model.saveMazeWithFileChooser(stage);
    }

    public Maze loadMazeWithFileChooser(Stage stage){
        return model.loadMazeWithFileChooser(stage);
    }

    public ArrayList<AState> solveMaze() throws IOException, ClassNotFoundException {
        return model.solveMaze();
    }

    public int[][] getCurrMaze(){
        return model.getCurrMaze();
    }
}
