package org.atpprojectpartc.View;

import algorithms.search.AState;
import java.util.ArrayList;

/**
 * Interface defining the contract for View components in the MVVM pattern.
 * This interface ensures that any View implementation provides the essential
 * methods for displaying maze-related information and handling user interactions.
 */
public interface IView {

    /**
     * Sets the player's position in the view and updates the display
     * @param row The row index of the player's position
     * @param col The column index of the player's position
     */
    void setPlayerPosition(int row, int col);

    /**
     * Sets the goal position in the view and updates the display
     * @param row The row index of the goal position
     * @param col The column index of the goal position
     */
    void setGoalPosition(int row, int col);

    /**
     * Enables or disables solution mode for displaying the solution path
     * @param mode true to enable solution mode, false to disable
     */
    void setSolutionMode(boolean mode);

    /**
     * Sets the solution path to be displayed when in solution mode
     * @param solPath ArrayList of AState objects representing the solution path
     */
    void setSolutionPath(ArrayList<AState> solPath);

    /**
     * Updates the player row display property
     * @param updatePlayerRow The new row value to display
     */
    void setUpdatePlayerRow(int updatePlayerRow);

    /**
     * Updates the player column display property
     * @param updatePlayerCol The new column value to display
     */
    void setUpdatePlayerCol(int updatePlayerCol);

    /**
     * Updates the goal row display property
     * @param updateGoalRow The new goal row value to display
     */
    void setUpdateGoalRow(int updateGoalRow);

    /**
     * Updates the goal column display property
     * @param updateGoalCol The new goal column value to display
     */
    void setUpdateGoalCol(int updateGoalCol);

    /**
     * Gets the current player row display value
     * @return String representation of the player's row
     */
    String getUpdatePlayerRow();

    /**
     * Gets the current player column display value
     * @return String representation of the player's column
     */
    String getUpdatePlayerCol();

    /**
     * Gets the current goal row display value
     * @return String representation of the goal's row
     */
    String getUpdateGoalRow();

    /**
     * Gets the current goal column display value
     * @return String representation of the goal's column
     */
    String getUpdateGoalCol();
}