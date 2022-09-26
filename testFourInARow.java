
/**
 * Write a description of class testFourInARow here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class testFourInARow
{
    public static Board makeBoard(int[][] ints){
        Board newBoard = new Board(ints.length,ints[0].length);
        for (int y = 0; y < ints.length; y++){
            for(int x = 0; x < ints[0].length; x++){
                newBoard.selectCell(ints[y][x],y,x);
            }
        }
        return newBoard;
    }

    public static void main(String[] params){
        int[][] testarray1 = {{-1, -1, -1, -1},
                {-1, -1, -1, -1},
                { 1,  1,  1,  1},
                {-1, -1, -1, -1}};
        int[][] testarray2 = {{-1, -1, -1,  1},
                {-1, -1,  1, -1},
                {-1,  1, -1, -1},
                { 1, -1, -1, -1}};
        int[][] testarray3 = {{ 1, -1, -1, -1},
                {-1,  1, -1, -1},
                {-1, -1,  1, -1},
                {-1, -1, -1,  1}};
        int[][] testarray4 = {{-1,  1, -1, -1},
                {-1,  1, -1, -1},
                {-1,  1, -1, -1},
                {-1,  1, -1, -1}};
        int[][] testarray5 = {{-1,  1, -1, -1, -1, -1},
                {-1, -1,  1, -1, -1, -1},
                {-1, -1, -1,  1, -1, -1},
                {-1, -1, -1, -1,  1, -1},
                {-1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1}};
        int[][] testarray6 = {{-1,  1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1,  1},
                {-1, -1, -1, -1,  1, -1},
                {-1, -1, -1,  1, -1, -1},
                {-1, -1,  1, -1, -1, -1}};
        System.out.println("All of the following results should be 1:");
        System.out.println(makeBoard(testarray1).fourInARow());
        System.out.println(makeBoard(testarray2).fourInARow());
        System.out.println(makeBoard(testarray3).fourInARow());
        System.out.println(makeBoard(testarray4).fourInARow());
        System.out.println(makeBoard(testarray5).fourInARow());
        System.out.println(makeBoard(testarray6).fourInARow());


        int[][] badarray5 = {{-1, 1, -1, -1},
                {-1, 1, -1, -1},
                {-1, 2, -1, -1},
                {-1, -1, -1, -1},
                {-1,1,-1,-1}};
        System.out.println("All of the following results should be -1:");
        System.out.println(makeBoard(badarray5).fourInARow());
    }
}
