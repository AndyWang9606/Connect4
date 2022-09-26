// CellPoint class used for easy tracking of cell positions and potential winners

public class CellPoint {
    public int x;
    public int y;
    public int possibleWinner;

    public CellPoint(int x, int y, int possibleWinner) {
        this.x = x;
        this.y = y;
        this.possibleWinner = possibleWinner;
    }
}
