import java.awt.*;
import javax.swing.JFrame;
import java.util.*;

public class Board extends JFrame {
    private Cell[][] grid;
    private int winner = -1;
    private ArrayList<CellPoint> forbidden = new ArrayList<>();

    public final int squareSizePix = 50;

    public Board(int row, int col) {
        grid = new Cell[row][col];
        for (int i = 0; i < grid.length; i++) {
            for (int k = 0; k < grid[0].length; k++) {
                grid[i][k] = new Cell();
            }
        }
    }

    //Returns true if the board has no empty spaces available for a player to go;
    // false otherwise
    public boolean isFull() {
        for (int i = 0; i < grid.length; i++) {
            for (int k = 0; k < grid[0].length; k++) {
                if (grid[i][k].isEmpty) {
                    return false;
                }
            }
        }
        return true;
    }

    //sets the winner variable to winningPlayer
    public void setWinner(int winningPlayer) {
        winner = winningPlayer;
    }
    public int getWinner() {
        return winner;
    }

    //Return true if the game is done (someone has won or the board is full)
    //Return false if not.
    public boolean over() {
        return isFull() || fourInARow() != -1;
    }

    //Return the number of columns in the board
    public int getWidth() {
        return grid[0].length;
    }

    //Return the number of rows in the board
    public int getHeight() {
        return grid.length;
    }

    //The user picks a cell at row,col to be filled by player.
    //If the cell is empty, update the appropriate variables
    //(which will include making the cell non-empty)
    // and return true. If it already filled with a player,
    // do nothing except return false

    public boolean selectCell(int player, int row, int col) {
        try {
            if (!grid[row][col].isEmpty) {
                return false;
            }
            grid[row][col].setPlayer(player);
            return true;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    //Overload the above method. Here, the user has only selected a
    // column, and you must fill in the lowest available
    //slot with the appropriate player ID. If the column is full,
    // return false; otherwise, update the appropriate varibales and
    // return true.
    public boolean selectCell(int player, int col) {
        try {
            for (int i = grid.length - 1; i >= 0; i--) {
                if (grid[i][col].isEmpty) {
                    grid[i][col].setPlayer(player);
                    repaint();
                    return true;
                }
            }
            return false;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    //Checks if there is any instance of 4-in-a-row on the board.
    //If it finds any, it returns the user ID of the player who has 4 in a row
    //It returns once it finds any instance of 4 in a row, so if there
    //are multiple (which there should never be in a real game), it will only
    // return the first one it finds
    public int fourInARow() {
        boolean isDone = false;
        outerloop:
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (checkRow(i, j) || checkCol(i, j) || checkMinorDiag(i, j) || checkMajorDiag(i, j)) {
                    isDone = true;
                    break outerloop;
                }
            }
        }
        if (isDone) {
            return winner;
        }
        else {
            return -1;
        }
    }

    // Checks if there is a winning row
    private boolean checkRow(int row, int col) {
        if (grid[row][col].isEmpty) {
            return false;
        }
        if (col < 3) {
            return false;
        }
        if (grid[row][col].getPlayer() == grid[row][col - 1].getPlayer() && grid[row][col].getPlayer() == grid[row][col - 2].getPlayer()
            && grid[row][col].getPlayer() == grid[row][col - 3].getPlayer()) {
            winner = grid[row][col].getPlayer();
            return true;
        }
        return false;
    }

    // Checks if there is a winning column
    private boolean checkCol(int row, int col) {
        if (grid[row][col].isEmpty) {
            return false;
        }
        if (row < 3) {
            return false;
        }
        if (grid[row][col].getPlayer() == grid[row - 1][col].getPlayer() && grid[row][col].getPlayer() == grid[row - 2][col].getPlayer()
                && grid[row][col].getPlayer() == grid[row - 3][col].getPlayer()) {
            winner = grid[row][col].getPlayer();
            return true;
        }
        return false;
    }

    // Checks if there is a winning major diagonal
    private boolean checkMajorDiag(int row, int col) {
        if (grid[row][col].isEmpty) {
            return false;
        }
        if (row < 3 || col < 3) {
            return false;
        }
        if (grid[row][col].getPlayer() == grid[row - 1][col - 1].getPlayer() && grid[row][col].getPlayer() == grid[row - 2][col - 2].getPlayer()
                && grid[row][col].getPlayer() == grid[row - 3][col - 3].getPlayer()) {
            winner = grid[row][col].getPlayer();
            return true;
        }
        return false;
    }

    // Checks if there is a winning minor diagonal
    private boolean checkMinorDiag(int row, int col) {
        if (grid[row][col].isEmpty) {
            return false;
        }
        if (row < 3 || col > grid[0].length - 1 - 3) {
            return false;
        }
        if (grid[row][col].getPlayer() == grid[row - 1][col + 1].getPlayer() && grid[row][col].getPlayer() == grid[row - 2][col + 2].getPlayer()
                && grid[row][col].getPlayer() == grid[row - 3][col + 3].getPlayer()) {
            winner = grid[row][col].getPlayer();
            return true;
        }
        return false;
    }

    /* Beginning of Traditional CPU Brain */
    // Checks for potential winning move with major diagonal
    public ArrayList<CellPoint> checkTrad3MajDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j - 2].getPlayer() && grid[i - 2][j - 3].isEmpty && grid[i - 3][j - 3].isEmpty && !grid[i - 1][j - 3].isEmpty && grid[i][j].getPlayer() == 0) {
                        forbidden.add(new CellPoint(j - 3, i - 2, -1));
                    }
                    if (grid[i][j].getPlayer() == grid[i - 1][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j - 2].getPlayer() && !grid[i - 2][j - 3].isEmpty && grid[i - 3][j - 3].isEmpty) {
                        Cell buffer = grid[i - 3][j - 3];
                        if (!points.contains(new CellPoint(j - 3, i - 3, grid[i][j].getPlayer())))
                            points.add(new CellPoint(j - 3, i - 3, grid[i][j].getPlayer()));
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j - 2].getPlayer() && (i == grid.length - 2 || !grid[i + 2][j + 1].isEmpty) && grid[i + 1][j + 1].isEmpty) {
                        Cell buffer = grid[i + 1][j + 1];
                        if (!points.contains(new CellPoint(j + 1, i + 1, grid[i][j].getPlayer())))
                            points.add(new CellPoint(j + 1, i + 1, grid[i][j].getPlayer()));
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j - 2].getPlayer() && i > grid.length - 2 && grid[i + 2][j + 1].isEmpty && grid[i + 1][j + 1].isEmpty && grid[i][j].getPlayer() == 0) {
                        forbidden.add(new CellPoint(j + 1, i + 2, -1));
                        if (grid[i + 3][j + 1].isEmpty) {
                            forbidden.remove(forbidden.size() - 1);
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkTradMidMajDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j - 3].getPlayer() && grid[i - 1][j - 2].isEmpty && grid[i - 2][j - 2].isEmpty && !grid[i][j - 2].isEmpty && grid[i][j].getPlayer() == 0) {
                        forbidden.add(new CellPoint(j - 3, i - 2, -1));

                    }
                    if (grid[i][j].getPlayer() == grid[i - 1][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j - 3].getPlayer() && !grid[i - 1][j - 2].isEmpty && grid[i - 2][j - 2].isEmpty) {
                        Cell buffer = grid[i - 2][j - 2];
                        if (!points.contains(new CellPoint(j - 2, i - 2, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j - 2, i - 2, grid[i][j].getPlayer()));
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 2][j - 2].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j - 3].getPlayer() && grid[i - 1][j - 1].isEmpty) {
                        Cell buffer = grid[i - 1][j - 1];
                        if (!points.contains(new CellPoint(j - 1, i - 1, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j - 1, i - 1, grid[i][j].getPlayer()));
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 2][j - 2].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j - 3].getPlayer() && grid[i][j - 1].isEmpty && grid[i - 1][j - 1].isEmpty && grid[i][j].getPlayer() == 0) {
                        forbidden.add(new CellPoint(j - 1, i, -1));
                        if (grid[i + 1][j - 1].isEmpty) {
                            forbidden.remove(forbidden.size() - 1);
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
            }
        }
        return points;
    }

    // Checks for potential winning move with minor diagonal
    public ArrayList<CellPoint> checkTrad3MinDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j + 2].getPlayer() && grid[i - 2][j + 3].isEmpty && grid[i - 3][j + 3].isEmpty && !grid[i - 1][j + 3].isEmpty && grid[i][j].getPlayer() == 0) {
                        forbidden.add(new CellPoint(j + 3, i - 2, -1));
                    }
                    if (grid[i][j].getPlayer() == grid[i - 1][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j + 2].getPlayer() && !grid[i - 2][j + 3].isEmpty && grid[i - 3][j + 3].isEmpty) {
                        Cell buffer = grid[i - 3][j + 3];
                        if (!points.contains(new CellPoint(j + 3, i - 3, grid[i][j].getPlayer())))
                            points.add(new CellPoint(j + 3, i - 3, grid[i][j].getPlayer()));
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j + 2].getPlayer() && (i == grid.length - 2 || !grid[i + 2][j - 1].isEmpty) && grid[i + 1][j - 1].isEmpty) {
                        Cell buffer = grid[i + 1][j - 1];
                        if (!points.contains(new CellPoint(j - 1, i + 1, grid[i][j].getPlayer())))
                            points.add(new CellPoint(j - 1, i + 1, grid[i][j].getPlayer()));
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j + 2].getPlayer() && i > grid.length - 2 && grid[i + 2][j - 1].isEmpty && grid[i + 1][j - 1].isEmpty && grid[i][j].getPlayer() == 0) {
                        forbidden.add(new CellPoint(j - 1, i + 2, -1));
                        if (grid[i + 3][j - 1].isEmpty) {
                            forbidden.remove(forbidden.size() - 1);
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkTradMidMinDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j + 3].getPlayer() && grid[i - 1][j + 2].isEmpty && grid[i - 2][j + 2].isEmpty && !grid[i][j + 2].isEmpty && grid[i][j].getPlayer() == 0) {
                        forbidden.add(new CellPoint(j + 2, i - 1, -1));
                    }
                    if (grid[i][j].getPlayer() == grid[i - 1][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j + 3].getPlayer() && !grid[i - 1][j + 2].isEmpty && grid[i - 2][j + 2].isEmpty) {
                        Cell buffer = grid[i - 3][j + 3];
                        if (!points.contains(new CellPoint(j + 2, i - 2, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j + 2, i - 2, grid[i][j].getPlayer()));
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 2][j + 2].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j + 3].getPlayer() && !grid[i][j + 1].isEmpty && grid[i - 1][j + 1].isEmpty) {
                        if (!points.contains(new CellPoint(j + 1, i - 1, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j + 1, i - 1, grid[i][j].getPlayer()));
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 2][j + 2].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j + 3].getPlayer() && grid[i][j + 1].isEmpty && grid[i][j].getPlayer() == 0) {
                        forbidden.add(new CellPoint(j + 1, i, -1));
                        if (grid[i + 1][j + 1].isEmpty) {
                            forbidden.remove(forbidden.size() - 1);
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
            }
        }
        return points;
    }

    // Checks for sequences that would lead to a guaranteed win (i.e. _XX__ -> _XXX_ -> OXXX_ -> OXXXX)
    public ArrayList<CellPoint> checkTrad2WinRows() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j > 1 && j < grid[0].length - 2) {
                    if (!grid[i][j - 2].isEmpty || !grid[i][j + 1].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j - 1].getPlayer() && grid[i][j - 2].isEmpty && grid[i][j + 1].isEmpty && (i == grid.length - 1 || (!grid[i + 1][j - 2].isEmpty && !grid[i + 1][j + 1].isEmpty))) {
                        boolean[] chosen = new boolean[2];
                        for (int k = 0; k < 2; k++) {
                            Random random = new Random();
                            randomLoop:
                            while (true) {
                                int index = random.nextInt(2);
                                if (!chosen[index] && index == 0) {
                                    points.add(new CellPoint(j - 2, i, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                } else if (!chosen[index] &&  index == 1) {
                                    points.add(new CellPoint(j + 1, i, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                int counter = 0;
                                for (int l = 0; l < 2; l++) {
                                    if (chosen[l]) {
                                        counter++;
                                    }
                                    if (counter == 2) {
                                        break randomLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkTrad2MidWinRows() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j > 0 && j < grid[0].length - 4) {
                    if (!grid[i][j - 1].isEmpty || !grid[i][j + 3].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j + 2].getPlayer() && grid[i][j + 3].isEmpty && grid[i][j - 1].isEmpty && (i == grid.length - 1 || (!grid[i + 1][j + 3].isEmpty && !grid[i + 1][j - 1].isEmpty))) {
                        boolean[] chosen = new boolean[3];
                        for (int k = 0; k < 3; k++) {
                            Random random = new Random();
                            randomLoop:
                            while (true) {
                                int index = random.nextInt(3);
                                if (!chosen[index] && index == 0) {
                                    if (!points.contains(new CellPoint(j + 3, i, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j + 3, i, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                else if (!chosen[index] && index == 1) {
                                    if (!points.contains(new CellPoint(j - 1, i, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j - 1, i, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                else if (!chosen[index] && index == 2) {
                                    if (!points.contains(new CellPoint(j + 1, i, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j + 1, i, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                int counter = 0;
                                for (int l = 0; l < 3; l++) {
                                    if (chosen[l]) {
                                        counter++;
                                    }
                                    if (counter == 3) {
                                        break randomLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return points;
    }

    // Checks for potential winning move with a row
    public ArrayList<CellPoint> check3TradWinningRowEndpoints() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j >= 3) {
                    if (!grid[i][j - 3].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j - 2].getPlayer() && (i == grid.length - 1 || !grid[i + 1][j - 3].isEmpty)) {
                        if (!points.contains(new CellPoint(j - 3, i, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j - 3, i, grid[i][j].getPlayer()));
                        }
                    }
                    try {
                        if (grid[i][j].getPlayer() == grid[i][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j - 2].getPlayer() && grid[i + 1][j - 3].isEmpty && grid[i][j - 3].isEmpty) {
                            forbidden.add(new CellPoint(j - 3, i + 1, -1));
                            if (grid[i + 2][j - 3].isEmpty) {
                                forbidden.remove(forbidden.size() - 1);
                            }
                        }
                    }
                    catch (Exception e) {

                    }
                }
                if (j < grid[0].length - 3) {
                    if (!grid[i][j + 3].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j + 2].getPlayer() && (i == grid.length - 1 || !grid[i + 1][j + 3].isEmpty)) {
                        if (!points.contains(new CellPoint(j + 3, i, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j + 3, i, grid[i][j].getPlayer()));
                        }
                        else {
                            if (!points.contains(new CellPoint(j + 3, i, grid[i][j].getPlayer()))) {
                                points.add(new CellPoint(j + 3, i, grid[i][j].getPlayer()));
                            }

                        }
                    }
                    try {
                        if (grid[i][j].getPlayer() == grid[i][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j + 2].getPlayer() && grid[i + 1][j + 3].isEmpty && grid[i][j + 3].isEmpty) {
                            forbidden.add(new CellPoint(j + 3, i + 1, -1));
                            if (grid[i + 2][j + 3].isEmpty) {
                                forbidden.remove(forbidden.size() - 1);
                            }
                        }
                    }
                    catch (Exception e) {

                    }
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkTradMiddleWinningRow() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j >= 3) {
                    if (!grid[i][j - 2].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j - 3].getPlayer() && (i == grid.length - 1 || !grid[i + 1][j - 2].isEmpty)) {
                        if (!points.contains(new CellPoint(j - 2, i, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j - 2, i, grid[i][j].getPlayer()));
                        }
                    }
                    try {
                        if (grid[i][j].getPlayer() == grid[i][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j - 3].getPlayer() && grid[i + 1][j - 2].isEmpty && grid[i][j - 2].isEmpty) {
                            forbidden.add(new CellPoint(j - 2, i + 1, -1));
                            if (grid[i + 2][j - 2].isEmpty) {
                                forbidden.remove(forbidden.size() - 1);
                            }
                        }
                    }
                    catch (Exception e) {

                    }
                }
                if (j < grid[0].length - 3) {
                    if (!grid[i][j + 2].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j + 3].getPlayer() && (i == grid.length - 1 || !grid[i + 1][j + 2].isEmpty)) {
                        if (!points.contains(new CellPoint(j + 2, i, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j + 2, i, grid[i][j].getPlayer()));
                        }
                        else {
                            if (!points.contains(new CellPoint(j + 2, i, grid[i][j].getPlayer()))){
                                points.add(new CellPoint(j + 2, i, grid[i][j].getPlayer()));
                            }
                        }
                    }
                    try {
                        if (grid[i][j].getPlayer() == grid[i][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j + 3].getPlayer() && grid[i + 1][j + 2].isEmpty && grid[i][j + 2].isEmpty) {
                            forbidden.add(new CellPoint(j + 2, i + 1, -1));
                            if (grid[i + 2][j + 2].isEmpty) {
                                forbidden.remove(forbidden.size() - 1);
                            }
                        }
                    }
                    catch (Exception e) {

                    }
                }
            }
        }
        return points;
    }

    // Checks for potential winning move with column
    public ArrayList<CellPoint> checkTrad3ColEndpoints() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (i >= 3) {
                    if (!grid[i - 3][j].isEmpty) {
                        continue;
                    }
                    else if (grid[i][j].getPlayer() == grid[i - 1][j].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j].getPlayer()) {
                        if (!points.contains(new CellPoint(j, i - 3, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j, i - 3, grid[i][j].getPlayer()));
                            continue;
                        }
                    }
                }


            }
        }
        return points;
    }

    /* Beginning of Non-Traditional CPU Brain */
    // Checks for potential winning move with major diagonal
    public ArrayList<CellPoint> checkNonTrad3MajDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j - 2].getPlayer() && grid[i - 3][j - 3].isEmpty) {
                        Cell buffer = grid[i - 3][j - 3];
                        if (!points.contains(new CellPoint(j - 3, i - 3, grid[i][j].getPlayer())))
                            points.add(new CellPoint(j - 3, i - 3, grid[i][j].getPlayer()));
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j - 2].getPlayer() && grid[i + 1][j + 1].isEmpty) {
                        Cell buffer = grid[i + 1][j + 1];
                        if (!points.contains(new CellPoint(j + 1, i + 1, grid[i][j].getPlayer())))
                            points.add(new CellPoint(j + 1, i + 1, grid[i][j].getPlayer()));
                    }
                }
                catch (Exception e) {
                    continue;
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkNonTradMidMajDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j - 3].getPlayer() && grid[i - 2][j - 2].isEmpty) {
                        Cell buffer = grid[i - 2][j - 2];
                        if (!points.contains(new CellPoint(j - 2, i - 2, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j - 2, i - 2, grid[i][j].getPlayer()));
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 2][j - 2].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j - 3].getPlayer() && grid[i - 1][j - 1].isEmpty) {
                        Cell buffer = grid[i - 1][j - 1];
                        if (!points.contains(new CellPoint(j - 1, i - 1, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j - 1, i - 1, grid[i][j].getPlayer()));
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
            }
        }
        return points;
    }

    // Checks for potential winning move with minor diagonal
    public ArrayList<CellPoint> checkNonTrad3MinDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j + 2].getPlayer() && grid[i - 3][j + 3].isEmpty) {
                        Cell buffer = grid[i - 3][j + 3];
                        if (!points.contains(new CellPoint(j + 3, i - 3, grid[i][j].getPlayer())))
                            points.add(new CellPoint(j + 3, i - 3, grid[i][j].getPlayer()));
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j + 2].getPlayer() && grid[i + 1][j - 1].isEmpty) {
                        Cell buffer = grid[i + 1][j - 1];
                        if (!points.contains(new CellPoint(j - 1, i + 1, grid[i][j].getPlayer())))
                            points.add(new CellPoint(j - 1, i + 1, grid[i][j].getPlayer()));
                    }
                }
                catch (Exception e) {
                    continue;
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkNonTradMidMinDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 1][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j + 3].getPlayer() && grid[i - 2][j + 2].isEmpty) {
                        Cell buffer = grid[i - 3][j + 3];
                        if (!points.contains(new CellPoint(j + 3, i - 3, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j + 3, i - 3, grid[i][j].getPlayer()));
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
                try {
                    if (grid[i][j].getPlayer() == grid[i - 2][j + 2].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j + 3].getPlayer() && grid[i - 1][j + 1].isEmpty) {
                        if (!points.contains(new CellPoint(j + 1, i - 1, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j + 1, i - 1, grid[i][j].getPlayer()));
                        }
                    }
                }
                catch (Exception e) {
                    continue;
                }
            }
        }
        return points;
    }

    // Checks for potential winning move with a row
    public ArrayList<CellPoint> check3NonTradWinningRowEndpoints() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j >= 3) {
                    if (!grid[i][j - 3].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j - 2].getPlayer()) {
                        if (!points.contains(new CellPoint(j - 3, i, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j - 3, i, grid[i][j].getPlayer()));
                        }
                    }
                }
                if (j < grid[0].length - 3) {
                    if (!grid[i][j + 3].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j + 2].getPlayer()) {
                        if (!points.contains(new CellPoint(j + 3, i, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j + 3, i, grid[i][j].getPlayer()));
                        }
                    }
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkNonTradMiddleWinningRow() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j >= 3) {
                    if (!grid[i][j - 2].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j - 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j - 3].getPlayer()) {
                        if (!points.contains(new CellPoint(j - 2, i, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j - 2, i, grid[i][j].getPlayer()));
                        }
                    }
                }
                if (j < grid[0].length - 3) {
                    if (!grid[i][j + 2].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j + 1].getPlayer() && grid[i][j].getPlayer() == grid[i][j + 3].getPlayer()) {
                        if (!points.contains(new CellPoint(j + 2, i, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j + 2, i, grid[i][j].getPlayer()));
                        }
                        else {
                            if (!points.contains(new CellPoint(j + 2, i, grid[i][j].getPlayer()))){
                                points.add(new CellPoint(j + 2, i, grid[i][j].getPlayer()));
                            }
                        }
                    }
                }
            }
        }
        return points;
    }

    // Checks for potential winning move with column
    public ArrayList<CellPoint> checkNonTrad3ColEndpoints() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (i >= 3) {
                    if (!grid[i - 3][j].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i - 1][j].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j].getPlayer()) {
                        if (!points.contains(new CellPoint(j, i - 3, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j, i - 3, grid[i][j].getPlayer()));
                        }
                    }
                }
                if (i < grid.length - 1 && i >= 2) {
                    if (!grid[i + 1][j].isEmpty) {
                        continue;
                    }
                    else if (grid[i][j].getPlayer() == grid[i - 1][j].getPlayer() && grid[i][j].getPlayer() == grid[i - 2][j].getPlayer()) {
                        if (!points.contains(new CellPoint(j, i + 1, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j, i + 1, grid[i][j].getPlayer()));
                        }
                    }
                }


            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkNonTradMidCol() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (i >= 3) {
                    if (!grid[i - 3][j].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i - 1][j].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j].getPlayer()) {
                        if (!points.contains(new CellPoint(j, i - 3, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j, i - 2, grid[i][j].getPlayer()));
                        }
                    }
                    if (!grid[i - 1][j].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i - 2][j].getPlayer() && grid[i][j].getPlayer() == grid[i - 3][j].getPlayer()) {
                        if (!points.contains(new CellPoint(j, i - 1, grid[i][j].getPlayer()))) {
                            points.add(new CellPoint(j, i - 1, grid[i][j].getPlayer()));
                        }
                    }
                }
            }
        }
        return points;
    }

    /* Checks for sequences that would lead to a guaranteed win (i.e. _XX__ -> _XXX_ -> OXXX_ -> OXXXX) */

    // Checks for sequences in rows
    public ArrayList<CellPoint> checkNonTrad2WinRows() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j > 1 && j < grid[0].length - 2) {
                    if (!grid[i][j - 2].isEmpty || !grid[i][j + 1].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j - 1].getPlayer() && grid[i][j - 2].isEmpty && grid[i][j + 1].isEmpty) {
                        boolean[] chosen = new boolean[2];
                        for (int k = 0; k < 2; k++) {
                            Random random = new Random();
                            randomLoop:
                            while (true) {
                                int index = random.nextInt(2);
                                if (!chosen[index] && index == 0) {
                                    points.add(new CellPoint(j - 2, i, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                } else if (!chosen[index] &&  index == 1) {
                                    points.add(new CellPoint(j + 1, i, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                int counter = 0;
                                for (int l = 0; l < 2; l++) {
                                    if (chosen[l]) {
                                        counter++;
                                    }
                                    if (counter == 2) {
                                        break randomLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkNonTrad2MidWinRows() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j > 0 && j < grid[0].length - 3) {
                    if (!grid[i][j - 1].isEmpty || !grid[i][j + 3].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i][j + 2].getPlayer() && grid[i][j + 3].isEmpty && grid[i][j - 1].isEmpty) {
                        boolean[] chosen = new boolean[3];
                        for (int k = 0; k < 3; k++) {
                            Random random = new Random();
                            randomLoop:
                            while (true) {
                                int index = random.nextInt(3);
                                if (!chosen[index] && index == 0) {
                                    if (!points.contains(new CellPoint(j + 3, i, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j + 3, i, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                else if (!chosen[index] && index == 1) {
                                    if (!points.contains(new CellPoint(j - 1, i, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j - 1, i, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                else if (!chosen[index] && index == 2) {
                                    if (!points.contains(new CellPoint(j + 1, i, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j + 1, i, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                int counter = 0;
                                for (int l = 0; l < 3; l++) {
                                    if (chosen[l]) {
                                        counter++;
                                    }
                                    if (counter == 3) {
                                        break randomLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return points;
    }

    // Checks for sequences in columns
    public ArrayList<CellPoint> checkNonTrad2WinCols() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (i >= 2 && i <= grid[0].length - 2) {
                    if (!grid[i - 2][j].isEmpty || !grid[i + 1][j].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i - 1][j].getPlayer() && grid[i - 2][j].isEmpty && grid[i + 1][j].isEmpty) {
                        boolean[] chosen = new boolean[2];
                        for (int k = 0; k < 2; k++) {
                            Random random = new Random();
                            randomLoop:
                            while (true) {
                                int index = random.nextInt(2);
                                if (!chosen[index] && index == 0) {
                                    points.add(new CellPoint(j, i - 2, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                } else if (!chosen[index] &&  index == 1) {
                                    points.add(new CellPoint(j, i + 1, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                int counter = 0;
                                for (int l = 0; l < 2; l++) {
                                    if (chosen[l]) {
                                        counter++;
                                    }
                                    if (counter == 2) {
                                        break randomLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkNonTrad2MidWinCols() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (i > 0 && i < grid[0].length - 3) {
                    if (!grid[i - 1][j].isEmpty || !grid[i + 3][j].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i + 2][j].getPlayer() && grid[i + 3][j].isEmpty && grid[i - 1][j].isEmpty) {
                        boolean[] chosen = new boolean[3];
                        for (int k = 0; k < 3; k++) {
                            Random random = new Random();
                            randomLoop:
                            while (true) {
                                int index = random.nextInt(3);
                                if (!chosen[index] && index == 0) {
                                    if (!points.contains(new CellPoint(j, i + 3, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j, i + 3, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                else if (!chosen[index] && index == 1) {
                                    if (!points.contains(new CellPoint(j, i - 1, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j, i - 1, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                else if (!chosen[index] && index == 2) {
                                    if (!points.contains(new CellPoint(j, i + 1, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j, i + 1, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                int counter = 0;
                                for (int l = 0; l < 3; l++) {
                                    if (chosen[l]) {
                                        counter++;
                                    }
                                    if (counter == 3) {
                                        break randomLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return points;
    }

    // Checks for sequences in diagonals
    public ArrayList<CellPoint> checkNonTrad2WinMajDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j > 2 && j < grid[0].length - 1 && i > 2 && i < grid.length - 1) {
                    if (!grid[i - 3][j - 3].isEmpty || !grid[i + 1][j + 1].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i - 1][j - 1].getPlayer() && grid[i - 2][j - 2].isEmpty && grid[i + 1][j + 1].isEmpty) {
                        boolean[] chosen = new boolean[2];
                        for (int k = 0; k < 2; k++) {
                            Random random = new Random();
                            randomLoop:
                            while (true) {
                                int index = random.nextInt(2);
                                if (!chosen[index] && index == 0) {
                                    points.add(new CellPoint(j - 2, i - 2, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                } else if (!chosen[index] &&  index == 1) {
                                    points.add(new CellPoint(j + 1, i + 1, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                int counter = 0;
                                for (int l = 0; l < 2; l++) {
                                    if (chosen[l]) {
                                        counter++;
                                    }
                                    if (counter == 2) {
                                        break randomLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkNonTrad2MidWinMajDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j >= 3  && j < grid[0].length - 1 && i >= 3 && i < grid.length - 1) {
                    if (!grid[i - 2][j - 2].isEmpty || !grid[i + 1][j + 1].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i - 2][j - 2].getPlayer() && grid[i - 3][j - 3].isEmpty && grid[i + 1][j + 1].isEmpty) {
                        boolean[] chosen = new boolean[3];
                        for (int k = 0; k < 3; k++) {
                            Random random = new Random();
                            randomLoop:
                            while (true) {
                                int index = random.nextInt(3);
                                if (!chosen[index] && index == 0) {
                                    if (!points.contains(new CellPoint(j - 3, i - 3, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j - 3, i - 3, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                else if (!chosen[index] && index == 1) {
                                    if (!points.contains(new CellPoint(j - 1, i - 1, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j - 1, i - 1, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                else if (!chosen[index] && index == 2) {
                                    if (!points.contains(new CellPoint(j + 1, i + 1, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j + 1, i + 1, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                int counter = 0;
                                for (int l = 0; l < 3; l++) {
                                    if (chosen[l]) {
                                        counter++;
                                    }
                                    if (counter == 3) {
                                        break randomLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkNonTrad2WinMinDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j > 0 && j < grid[0].length - 2 && i >= 2 && i < grid.length - 1) {
                    if (!grid[i - 2][j + 2].isEmpty || !grid[i + 1][j - 1].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i - 1][j + 1].getPlayer() && grid[i - 2][j + 2].isEmpty && grid[i + 1][j - 1].isEmpty) {
                        boolean[] chosen = new boolean[2];
                        for (int k = 0; k < 2; k++) {
                            Random random = new Random();
                            randomLoop:
                            while (true) {
                                int index = random.nextInt(2);
                                if (!chosen[index] && index == 0) {
                                    points.add(new CellPoint(j + 2, i - 2, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                } else if (!chosen[index] &&  index == 1) {
                                    points.add(new CellPoint(j - 1, i + 1, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                int counter = 0;
                                for (int l = 0; l < 2; l++) {
                                    if (chosen[l]) {
                                        counter++;
                                    }
                                    if (counter == 2) {
                                        break randomLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return points;
    }
    public ArrayList<CellPoint> checkNonTrad2MidWinMinDiags() {
        ArrayList<CellPoint> points = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (grid[i][j].isEmpty) {
                    continue;
                }
                if (j > 0  && j < grid[0].length - 3 && i >= 3 && i < grid.length - 1) {
                    if (!grid[i - 3][j + 3].isEmpty || !grid[i + 1][j - 1].isEmpty) {

                    }
                    else if (grid[i][j].getPlayer() == grid[i - 2][j + 2].getPlayer() && grid[i - 3][j + 3].isEmpty && grid[i + 1][j - 1].isEmpty) {
                        boolean[] chosen = new boolean[3];
                        for (int k = 0; k < 3; k++) {
                            Random random = new Random();
                            randomLoop:
                            while (true) {
                                int index = random.nextInt(3);
                                if (!chosen[index] && index == 0) {
                                    if (!points.contains(new CellPoint(j + 3, i - 3, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j + 3, i - 3, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                else if (!chosen[index] && index == 1) {
                                    if (!points.contains(new CellPoint(j - 1, i + 1, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j - 1, i + 1, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                else if (!chosen[index] && index == 2) {
                                    if (!points.contains(new CellPoint(j + 1, i - 1, grid[i][j].getPlayer())))
                                        points.add(new CellPoint(j + 1, i - 1, grid[i][j].getPlayer()));
                                    chosen[index] = true;
                                }
                                int counter = 0;
                                for (int l = 0; l < 3; l++) {
                                    if (chosen[l]) {
                                        counter++;
                                    }
                                    if (counter == 3) {
                                        break randomLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return points;
    }

    // Forbidden Moves are moves which, if played, would lead to the opponent winning (traditional-only)
    public ArrayList<CellPoint> forbiddenMoves() {
        return forbidden;
    }

    //called by paintComponent in the ConnectFour class. needs a
    //Graphics object as well as an array of colors
    //corresponding to the players. Player 1's color will be colors[0],
    //Player 2's color will be color[1], etc.
    //This should draw the grid, with each square squareSizePix x squareSizePix.
    //It should also draw all the placed pieces as circles of the appropriate color.
    public void draw(Graphics g, Color[] colors) {
        int offset = 20;
        // Drawing the board
        for (int i = 0; i < grid.length; i++) {
            for (int k = 0; k < grid[0].length; k++) {
                g.drawRect(offset + k * squareSizePix, offset + i * squareSizePix, squareSizePix, squareSizePix);
            }
        }
        // Drawing Text at the bottom of board
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        int stringXIndex = 0;
        if (over()) {
            if (isFull()) {
                if (getWidth() % 2 == 1) {
                    stringXIndex = getWidth() / 2 + 1;
                    g.drawString("No More Moves!", stringXIndex * 50 - 90, getHeight() * squareSizePix + offset * 2 + 10);
                } else {
                    stringXIndex = getWidth() / 2;
                    g.drawString("No More Moves!", stringXIndex * 50 - 65, getHeight() * squareSizePix + offset * 2 + 10);
                }
            }
            else {
                if (getWidth() % 2 == 1) {
                    stringXIndex = getWidth() / 2 + 1;
                    g.setColor(colors[winner]);
                    g.drawString("Player " + (winner + 1) + " Wins!", stringXIndex * 50 - 80, getHeight() * squareSizePix + offset * 2 + 10);
                } else {
                    stringXIndex = getWidth() / 2;
                    g.setColor(colors[winner]);
                    g.drawString("Player " + (winner + 1) + " Wins!", stringXIndex * 50 - 55, getHeight() * squareSizePix + offset * 2 + 10);
                }
            }
        }
        else {
            if (!ConnectFour.playCPU) {
                if (getWidth() % 2 == 1) {
                    stringXIndex = getWidth() / 2 + 1;
                    g.setColor(colors[ConnectFour.currentPlayer]);
                    g.drawString("Player " + (ConnectFour.currentPlayer + 1) + "'s Turn!", stringXIndex * 50 - 90, getHeight() * squareSizePix + offset * 2 + 10);
                } else {
                    stringXIndex = getWidth() / 2;
                    g.setColor(colors[ConnectFour.currentPlayer]);
                    g.drawString("Player " + (ConnectFour.currentPlayer + 1) + "'s Turn!", stringXIndex * 50 - 65, getHeight() * squareSizePix + offset * 2 + 10);
                }
            }
        }
        // Drawing pieces
        for (int i = 0; i < grid.length; i++) {
            for (int k = 0; k < grid[0].length; k++) {
                if (!grid[i][k].isEmpty) {
                    g.setColor(colors[grid[i][k].getPlayer()]);
                    g.fillOval(offset + k * squareSizePix, offset + i * squareSizePix, squareSizePix, squareSizePix);
                }
            }
        }

    }
}