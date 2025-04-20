package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

public class SearchableMaze implements ISearchable{
    private Maze maze;

    public SearchableMaze(Maze maze){
        this.maze = maze;
    }
    public AState getStartState(){
        return new MazeState(maze.getStartPosition());
    }
    public AState getGoalState(){
        return new MazeState(maze.getGoalPosition());
    }
    public void addSides(MazeState s, ArrayList<AState> successors_list) {
        if (s.getRowIndex() > 0) {
            if (maze.getCell((s.getRowIndex() - 1), s.getColIndex()) == 0) {
                successors_list.add(new MazeState(new Position((s.getRowIndex() - 1), s.getColIndex()),s, 10));
            }
        }
        if (s.getColIndex() < maze.getCols()-1) {
            if (maze.getCell((s.getRowIndex()), s.getColIndex() + 1) == 0) {
                successors_list.add(new MazeState(new Position((s.getRowIndex()), s.getColIndex() + 1),s ,10));  // right
            }
        }
        if (s.getRowIndex() < maze.getRows() - 1) {
            if (maze.getCell((s.getRowIndex() + 1), s.getColIndex()) == 0) {
                successors_list.add(new MazeState(new Position((s.getRowIndex() + 1), s.getColIndex()),s,10));  // down
            }
        }
        if (s.getColIndex() > 0) {
            if (maze.getCell((s.getRowIndex()), s.getColIndex() - 1) == 0) {
                successors_list.add(new MazeState(new Position((s.getRowIndex()), s.getColIndex() - 1),s,10));  // left
            }
        }
    }

    public void addDiagonals(MazeState s, ArrayList<AState> successors_list) {
        if (s.getRowIndex() > 0 && s.getColIndex() < maze.getCols() - 1) {
            if (maze.getCell((s.getRowIndex() - 1), s.getColIndex()) == 0 || maze.getCell((s.getRowIndex()),
                    s.getColIndex() + 1) == 0) {
                if (maze.getCell((s.getRowIndex() - 1), s.getColIndex() + 1) == 0) {  // right up
                    successors_list.add(new MazeState(new Position((s.getRowIndex() - 1), s.getColIndex() + 1), s,15));
                }
            }
        }
        if (s.getRowIndex() < maze.getRows()-1 && s.getColIndex() < maze.getCols()-1) {
            if (maze.getCell((s.getRowIndex() + 1), s.getColIndex()) == 0 || maze.getCell((s.getRowIndex()),
                    s.getColIndex() + 1) == 0) {
                if (maze.getCell((s.getRowIndex() + 1), s.getColIndex() + 1) == 0) {
                    successors_list.add(new MazeState(new Position((s.getRowIndex() + 1), s.getColIndex() + 1), s,15));  // right down
                }
            }
        }
        if (s.getRowIndex() < maze.getRows() - 1 && s.getColIndex() > 0) {
            if (maze.getCell((s.getRowIndex() + 1), s.getColIndex()) == 0 || maze.getCell((s.getRowIndex()),
                    s.getColIndex() - 1) == 0) {
                if (maze.getCell((s.getRowIndex() + 1), s.getColIndex() - 1) == 0) {
                    successors_list.add(new MazeState(new Position((s.getRowIndex() + 1), s.getColIndex() - 1), s,15));  // down left
                }
            }
        }
        if (s.getRowIndex() > 0 && s.getColIndex() > 0) {
            if (maze.getCell((s.getRowIndex() - 1), s.getColIndex()) == 0 || maze.getCell((s.getRowIndex()),
                    s.getColIndex() - 1) == 0) {
                if (maze.getCell((s.getRowIndex() - 1), s.getColIndex() - 1) == 0) {
                    successors_list.add(new MazeState(new Position((s.getRowIndex() - 1), s.getColIndex() - 1), s,15));  // left up
                }
            }
        }
    }

    public ArrayList<AState> getAllPossibleStates (AState state) {
        MazeState s = (MazeState)state;
        ArrayList<AState> successors_list = new ArrayList<>();
        if (s == null || s.getRowIndex() >= maze.getRows() || s.getColIndex() >= maze.getCols()) {
            return successors_list;
        }
        addSides(s, successors_list);
        addDiagonals(s, successors_list);
        return successors_list;
    }

}
