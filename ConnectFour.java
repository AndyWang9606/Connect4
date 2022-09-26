import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ConnectFour extends JPanel implements MouseListener{
    private static Board board;
    // Color array for random color selection
    static final Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.YELLOW,
            Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED};

    // Player Colors
    static Color[] playerColors;

    // Check for if it is traditional Connect4
    static boolean playTraditional = false;

    // The Current Player
    static int currentPlayer;

    // The amount of players
    static int playerNum = 1;

    // Check for if CPU mode is active
    static boolean playCPU = false;

    // Check if the game is finished
    static boolean gameOver = false;

    // The player with the first move
    static int firstPlayer;

    //Constructor for ConnectFour that calls the MouseListener
    public ConnectFour(){
        addMouseListener(this);
    }
    
    public static void main(String[] args){

        // Initializers
        Random rand = new Random();
        Scanner input = new Scanner(System.in);

        // Ask for traditional Connect 4
        System.out.print("Do you want to play traditional Connect4? (yes/no): ");
        String traditionalAnswer = input.nextLine().toLowerCase();
        if (traditionalAnswer.equals("yes") || traditionalAnswer.equals("y")) {
            playTraditional = true;
        }

        // Ask for CPU Mode
        System.out.print("Do you want to play against CPU? (yes/no): ");
        String cpuOpponent = input.nextLine().toLowerCase();
        if (cpuOpponent.equals("yes") || cpuOpponent.equals("y")) {
            playCPU = true;
        }

        // Asks for player count if CPU Mode is off
        boolean numSelect = false;
        while (!playCPU && !numSelect) {
            System.out.print("How many players?: ");
            try {
                playerNum = Integer.parseInt(input.nextLine());
            }
            catch (Exception e) {
                // If the input is not an integer
                System.out.println("Try Again!");
                continue;
            }
            if (playerNum <= 1) {
                System.out.println("Not enough players!");
                continue;
            }
            else if (playerNum > colors.length) {
                System.out.println("Too many players!");
                continue;
            }
            else {
                numSelect = true;
            }
        }

        // Player Color array intialization
        if (playCPU) {
            playerColors = new Color[2];
        }
        else {
            playerColors = new Color[playerNum];
        }

        // Color Selector
        System.out.print("What do you want your colors to be?: ");
        int counter = 0;

        // Chooses Colors and Checks if color has been chosen
        outerloop:
        while (counter < playerNum) {
            String colorInput = input.nextLine().toLowerCase();
            // Random Color Selection
            if (colorInput.equals("random")) {
                boolean colorIsDetermined = false;
                outerColorLoop:
                while (!colorIsDetermined) {
                    int colorIndex = rand.nextInt(colors.length);
                    if (counter != 0) {
                        for (int i = 0; i < counter; i++) {
                            if (colors[colorIndex].equals(playerColors[i])) {
                                continue outerColorLoop;
                            }
                        }
                        playerColors[counter] = colors[colorIndex];
                        colorIsDetermined = true;
                    }
                    else {
                        playerColors[counter] = colors[colorIndex];
                        colorIsDetermined = true;
                    }
                }
            }
            else {
                playerColors[counter] = convertStringToColor(colorInput);
                for (int i = 0; i < counter; i++) {
                    if (playerColors[i].equals(playerColors[counter])) {
                        System.out.println("Color has already been taken! Try again.");
                        continue outerloop;
                    }
                }
            }
            counter++;
        }

        // Randomly chooses CPU color
        if (playCPU) {
            boolean colorIsDetermined = false;
            while (!colorIsDetermined) {
                int colorIndex = rand.nextInt(colors.length);
                if (colors[colorIndex].equals(playerColors[0])) {
                    continue;
                }
                else {
                    playerColors[1] = colors[colorIndex];
                    colorIsDetermined = true;
                }
            }
        }

        // Asks for Board Size
        boolean isBoardGood = false;
        int width = 0;
        int height = 0;
        while (!isBoardGood) {
            System.out.print("What will the width of the board be?: ");
            try {
                width = Integer.parseInt(input.nextLine());
            }
            catch (Exception e) {
                System.out.println("Try Again!");
                continue;
            }
            System.out.print("What will the height of the board be?: ");
            try {
                height = Integer.parseInt(input.nextLine());
            }
            catch (Exception e) {
                System.out.println("Try Again!");
                continue;
            }
            if (width < 4 || height < 4) {
                System.out.println("Board too small!");
                continue;
            }
            isBoardGood = true;
        }

        // Randomly choose a starting player and tell the user who is starting.
        if (playCPU) {
            playerNum = 2;
        }
        firstPlayer = rand.nextInt(playerNum);
        currentPlayer = firstPlayer;
        if (playCPU) {
            if (currentPlayer == 1) {
                System.out.println("The CPU is going first!");
            }
            else {
                System.out.println("You are going first!");
            }
        }
        else {
            System.out.println("Player " + (currentPlayer + 1) + " is going first!");
        }

        // Window Initialization
        board = new Board(height, width);
        JFrame window = new JFrame("Connect4");
        window.setBounds(100, 100, board.getWidth() * board.squareSizePix + 20 + 20 * 2, board.getHeight() * board.squareSizePix + 55 + 20 * 2 + 20);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ConnectFour cf = new ConnectFour();
        cf.setBackground(Color.WHITE);
        Container c = window.getContentPane();
        c.add(cf);
        window.setVisible(true);

        // All player interactions are handled in MouseClicked
        if (firstPlayer == 1 && playCPU) {
            // First move of CPU
            virtualOpponent();
            currentPlayer = 0;
        }

    }
    
    public void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D)g;
            super.paintComponent(g);
            board.draw(g, playerColors);
    }
        
    //Required methods for MouseListener, though the only one you care about is click
    //You can ignore these
    public void mousePressed(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
        Point p = e.getPoint();

        if (!gameOver) {
            // Non-traditional Connect4
            if (!playTraditional) {
                // Not playing with CPU
                if (!playCPU) {
                    if (p.getY() > 20 && p.getX() > 20) {
                        // Math for checking if mouse is in bounds
                        if (board.selectCell(currentPlayer, (int) (p.getY() - 20) / (board.squareSizePix), (int) (p.getX() - 20) / (board.squareSizePix))) {
                            if (currentPlayer + 1 >= playerNum) {
                                currentPlayer = 0;
                            } else {
                                currentPlayer++;
                            }
                        }
                    }
                    if (board.over()) {
                        gameOver = true;
                    }
                }
                // CPU Mode
                else {
                    if (p.getY() > 20 && p.getX() > 20) {
                        // Math for checking if mouse is in bounds
                        if (board.selectCell(currentPlayer, (int) (p.getY() - 20) / (board.squareSizePix), (int) (p.getX() - 20) / (board.squareSizePix))) {
                            if (board.over()) {
                                gameOver = true;
                                this.repaint();
                                return;
                            }
                            currentPlayer = 1;
                            virtualOpponent();
                            // Checks if game is over
                            if (board.over()) {
                                gameOver = true;
                                this.repaint();
                                return;
                            }
                            currentPlayer = 0;
                        }
                    }
                    if (board.over()) {
                        gameOver = true;
                    }
                }
            }
            // Traditional Connect4
            else {
                if (!playCPU) {
                    // Math for checking if mouse is in bounds
                    if (p.getX() > 20 && (int) (p.getY() - 20) / (board.squareSizePix) < board.getHeight() && p.getY() > 20) {
                        if (board.selectCell(currentPlayer, (int) (p.getX() - 20) / (board.squareSizePix))) {
                            if (currentPlayer + 1 >= playerNum) {
                                currentPlayer = 0;
                            } else {
                                currentPlayer++;
                            }
                        }
                    }
                    if (board.over()) {
                        gameOver = true;
                    }
                }
                // CPU Mode
                else {
                    // Math for checking if mouse is in bounds
                    if (p.getX() > 20 && (int) (p.getY() - 20) / (board.squareSizePix) < board.getHeight() && p.getY() > 20 && currentPlayer == 0) {
                        if (board.selectCell(currentPlayer, (int) (p.getX() - 20) / (board.squareSizePix))) {
                            // Checks if game is over
                            if (board.over()) {
                                gameOver = true;
                                this.repaint();
                                return;
                            }
                            currentPlayer = 1;
                            virtualOpponent();
                            // Checks if game is over
                            if (board.over()) {
                                gameOver = true;
                                this.repaint();
                                return;
                            }
                            currentPlayer = 0;
                        }
                    }
                    if (board.over()) {
                        gameOver = true;
                    }
                }
            }
        }
        this.repaint();
    }
    // Beginning of CPU Code Section
    // (I wonder what they're thinking...)

    public static void virtualOpponent() {

        // Variable Initializer
        Random rand = new Random();
        int selectCol;
        int selectRow;
        boolean isGood;


        if (playTraditional) {
            // Traditional-Specific POI's
            ArrayList<CellPoint> winTradRows2 = board.checkTrad2WinRows();
            ArrayList<CellPoint> winTradMidRows2 = board.checkTrad2MidWinRows();
            ArrayList<CellPoint> tradRows3 = board.check3TradWinningRowEndpoints();
            ArrayList<CellPoint> tradMajDiag3 = board.checkTrad3MajDiags();
            ArrayList<CellPoint> tradMinDiag3 = board.checkTrad3MinDiags();
            ArrayList<CellPoint> tradMidRow = board.checkTradMiddleWinningRow();
            ArrayList<CellPoint> tradMidMajDiag = board.checkTradMidMajDiags();
            ArrayList<CellPoint> tradMidMinDiag = board.checkTradMidMinDiags();
            ArrayList<CellPoint> tradCols3 = board.checkTrad3ColEndpoints();
            ArrayList<CellPoint> forbidden = board.forbiddenMoves();

            // CPU Winning POI's (gains 1st priority)
            if (tradRows3.size() >= 1) {
                for (int i = 0; i < tradRows3.size(); i++) {
                    if (tradRows3.get(i).possibleWinner == 1) {
                        selectCol = tradRows3.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (tradCols3.size() >= 1) {
                for (int i = 0; i < tradCols3.size(); i++) {
                    if (tradCols3.get(i).possibleWinner == 1) {
                        selectCol = tradCols3.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (tradMajDiag3.size() >= 1) {
                for (int i = 0; i < tradMajDiag3.size(); i++) {
                    if (tradMajDiag3.get(i).possibleWinner == 1) {
                        selectCol = tradMajDiag3.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (tradMinDiag3.size() >= 1) {
                for (int i = 0; i < tradMinDiag3.size(); i++) {
                    if (tradMinDiag3.get(i).possibleWinner == 1) {
                        selectCol = tradMinDiag3.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (tradMidRow.size() >= 1) {
                for (int i = 0; i < tradMidRow.size(); i++) {
                    if (tradMidRow.get(i).possibleWinner == 1) {
                        selectCol = tradMidRow.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (tradMidMajDiag.size() >= 1) {
                for (int i = 0; i < tradMidMajDiag.size(); i++) {
                    if (tradMidMajDiag.get(i).possibleWinner == 1) {
                        selectCol = tradMidMajDiag.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (tradMidMinDiag.size() >= 1) {
                for (int i = 0; i < tradMidMinDiag.size(); i++) {
                    if (tradMidMinDiag.get(i).possibleWinner == 1) {
                        selectCol = tradMidMinDiag.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }

            // Blocking Opponent POI's (gains 2nd priority)
            if (tradRows3.size() >= 1) {
                for (int i = 0; i < tradRows3.size(); i++) {
                    if (tradRows3.get(i).possibleWinner == 0) {
                        selectCol = tradRows3.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (tradCols3.size() >= 1) {
                for (int i = 0; i < tradCols3.size(); i++) {
                    if (tradCols3.get(i).possibleWinner == 0) {
                        selectCol = tradCols3.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }

            }
            if (tradMajDiag3.size() >= 1) {
                for (int i = 0; i < tradMajDiag3.size(); i++) {
                    if (tradMajDiag3.get(i).possibleWinner == 0) {
                        selectCol = tradMajDiag3.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (tradMinDiag3.size() >= 1) {
                for (int i = 0; i < tradMinDiag3.size(); i++) {
                    if (tradMinDiag3.get(i).possibleWinner == 0) {
                        selectCol = tradMinDiag3.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (tradMidRow.size() >= 1) {
                for (int i = 0; i < tradMidRow.size(); i++) {
                    if (tradMidRow.get(i).possibleWinner == 0) {
                        selectCol = tradMidRow.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (tradMidMajDiag.size() >= 1) {
                for (int i = 0; i < tradMidMajDiag.size(); i++) {
                    if (tradMidMajDiag.get(i).possibleWinner == 0) {
                        selectCol = tradMidMajDiag.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (tradMidMinDiag.size() >= 1) {
                for (int i = 0; i < tradMidMinDiag.size(); i++) {
                    if (tradMidMinDiag.get(i).possibleWinner == 0) {
                        selectCol = tradMidMinDiag.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }

            // Guarantees own wins (gains 3rd priority)
            if (winTradRows2.size() >= 1) {
                for (int i = 0; i < winTradRows2.size(); i++) {
                    if (winTradRows2.get(i).possibleWinner == 1) {
                        selectCol = winTradRows2.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (winTradMidRows2.size() >= 1) {
                for (int i = 0; i < winTradMidRows2.size(); i++) {
                    if (winTradMidRows2.get(i).possibleWinner == 1) {
                        selectCol = winTradMidRows2.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }

            // Preventing opponent guaranteed wins (gains 4th priority)
            if (winTradRows2.size() >= 1) {
                for (int i = 0; i < winTradRows2.size(); i++) {
                    if (winTradRows2.get(i).possibleWinner == 0) {
                        selectCol = winTradRows2.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }
            if (winTradMidRows2.size() >= 1) {
                for (int i = 0; i < winTradMidRows2.size(); i++) {
                    if (winTradMidRows2.get(i).possibleWinner == 0) {
                        selectCol = winTradMidRows2.get(i).x;
                        board.selectCell(1, selectCol);
                        return;
                    }
                }
            }

            // Goes Randomly if the above have not been met (if there is no threat)
            isGood = false;
            int impossibleCounter = 0;
            outerloop:
            while (!isGood) {
                // Forces the computer to choose when all columns are "forbidden"
                if (impossibleCounter == 100 * board.getWidth()) {
                    selectCol = rand.nextInt(board.getWidth());
                    while (!board.selectCell(1, selectCol)) {
                        selectCol = rand.nextInt(board.getWidth());
                    }
                    return;
                }
                selectCol = rand.nextInt(board.getWidth());
                for (int i = 0; i < forbidden.size(); i++) {
                    if (selectCol == forbidden.get(i).x) {
                        impossibleCounter++;
                        continue outerloop;
                    }
                }
                while (!board.selectCell(1, selectCol)) {
                    selectCol = rand.nextInt(board.getWidth());
                }
                // Prevents from going into forbidden POIs
                for (int i = 0; i < forbidden.size(); i++) {
                    if (selectCol == forbidden.get(i).x) {
                        impossibleCounter++;
                        continue outerloop;
                    }
                }
                isGood = true;
            }
            return;

        }
        else {
            // Non-Traditional-Specific Initializers
            ArrayList<CellPoint> nonTradRows3 = board.check3NonTradWinningRowEndpoints();
            ArrayList<CellPoint> nonTradMajDiag3 = board.checkNonTrad3MajDiags();
            ArrayList<CellPoint> nonTradMinDiag3 = board.checkNonTrad3MinDiags();
            ArrayList<CellPoint> nonTradCols3 = board.checkNonTrad3ColEndpoints();
            ArrayList<CellPoint> nonTradMidRow = board.checkNonTradMiddleWinningRow();
            ArrayList<CellPoint> nonTradMidMajDiag = board.checkNonTradMidMajDiags();
            ArrayList<CellPoint> nonTradMidMinDiag = board.checkNonTradMidMinDiags();
            ArrayList<CellPoint> nonTradMidCol = board.checkNonTradMidCol();
            ArrayList<CellPoint> nonTrad2WinRows = board.checkNonTrad2WinRows();
            ArrayList<CellPoint> nonTrad2MidWinRows = board.checkNonTrad2MidWinRows();
            ArrayList<CellPoint> nonTrad2WinCols = board.checkNonTrad2WinCols();
            ArrayList<CellPoint> nonTrad2MidWinCols = board.checkNonTrad2MidWinCols();
            ArrayList<CellPoint> nonTrad2WinMajDiags = board.checkNonTrad2WinMajDiags();
            ArrayList<CellPoint> nonTrad2MidWinMajDiags = board.checkNonTrad2MidWinMajDiags();
            ArrayList<CellPoint> nonTrad2WinMinDiags = board.checkNonTrad2WinMinDiags();
            ArrayList<CellPoint> nonTrad2MidWinMinDiags = board.checkNonTrad2MidWinMinDiags();

            // CPU Winning POI's (gains 1st priority)
            if (nonTradRows3.size() >= 1) {
                for (int i = 0; i < nonTradRows3.size(); i++) {
                    if (nonTradRows3.get(i).possibleWinner == 1) {
                        selectCol = nonTradRows3.get(i).x;
                        selectRow = nonTradRows3.get(i).y;
                        board.selectCell(1, selectRow,selectCol);
                        return;
                    }
                }
            }
            if (nonTradCols3.size() >= 1) {
                for (int i = 0; i < nonTradCols3.size(); i++) {
                    if (nonTradCols3.get(i).possibleWinner == 1) {
                        selectCol = nonTradCols3.get(i).x;
                        selectRow = nonTradCols3.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMajDiag3.size() >= 1) {
                for (int i = 0; i < nonTradMajDiag3.size(); i++) {
                    if (nonTradMajDiag3.get(i).possibleWinner == 1) {
                        selectCol = nonTradMajDiag3.get(i).x;
                        selectRow = nonTradMajDiag3.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMinDiag3.size() >= 1) {
                for (int i = 0; i < nonTradMinDiag3.size(); i++) {
                    if (nonTradMinDiag3.get(i).possibleWinner == 1) {
                        selectCol = nonTradMinDiag3.get(i).x;
                        selectRow = nonTradMinDiag3.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMidRow.size() >= 1) {
                for (int i = 0; i < nonTradMidRow.size(); i++) {
                    if (nonTradMidRow.get(i).possibleWinner == 1) {
                        selectCol = nonTradMidRow.get(i).x;
                        selectRow = nonTradMidRow.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMidCol.size() >= 1) {
                for (int i = 0; i < nonTradMidCol.size(); i++) {
                    if (nonTradMidCol.get(i).possibleWinner == 1) {
                        selectCol = nonTradMidCol.get(i).x;
                        selectRow = nonTradMidCol.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMidMajDiag.size() >= 1) {
                for (int i = 0; i < nonTradMidMajDiag.size(); i++) {
                    if (nonTradMidMajDiag.get(i).possibleWinner == 1) {
                        selectCol = nonTradMidMajDiag.get(i).x;
                        selectRow = nonTradMidMajDiag.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMidMinDiag.size() >= 1) {
                for (int i = 0; i < nonTradMidMinDiag.size(); i++) {
                    if (nonTradMidMinDiag.get(i).possibleWinner == 1) {
                        selectCol = nonTradMidMinDiag.get(i).x;
                        selectRow = nonTradMidMinDiag.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }

            // Blocking Opponent POI's (gains 2nd priority)
            if (nonTradRows3.size() >= 1) {
                for (int i = 0; i < nonTradRows3.size(); i++) {
                    if (nonTradRows3.get(i).possibleWinner == 0) {
                        selectCol = nonTradRows3.get(i).x;
                        selectRow = nonTradRows3.get(i).y;
                        board.selectCell(1, selectRow,selectCol);
                        return;
                    }
                }
            }
            if (nonTradCols3.size() >= 1) {
                for (int i = 0; i < nonTradCols3.size(); i++) {
                    if (nonTradCols3.get(i).possibleWinner == 0) {
                        selectCol = nonTradCols3.get(i).x;
                        selectRow = nonTradCols3.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMajDiag3.size() >= 1) {
                for (int i = 0; i < nonTradMajDiag3.size(); i++) {
                    if (nonTradMajDiag3.get(i).possibleWinner == 0) {
                        selectCol = nonTradMajDiag3.get(i).x;
                        selectRow = nonTradMajDiag3.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMinDiag3.size() >= 1) {
                for (int i = 0; i < nonTradMinDiag3.size(); i++) {
                    if (nonTradMinDiag3.get(i).possibleWinner == 0) {
                        selectCol = nonTradMinDiag3.get(i).x;
                        selectRow = nonTradMinDiag3.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMidRow.size() >= 1) {
                for (int i = 0; i < nonTradMidRow.size(); i++) {
                    if (nonTradMidRow.get(i).possibleWinner == 0) {
                        selectCol = nonTradMidRow.get(i).x;
                        selectRow = nonTradMidRow.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMidCol.size() >= 1) {
                for (int i = 0; i < nonTradMidCol.size(); i++) {
                    if (nonTradMidCol.get(i).possibleWinner == 0) {
                        selectCol = nonTradMidCol.get(i).x;
                        selectRow = nonTradMidCol.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMidMajDiag.size() >= 1) {
                for (int i = 0; i < nonTradMidMajDiag.size(); i++) {
                    if (nonTradMidMajDiag.get(i).possibleWinner == 0) {
                        selectCol = nonTradMidMajDiag.get(i).x;
                        selectRow = nonTradMidMajDiag.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTradMidMinDiag.size() >= 1) {
                for (int i = 0; i < nonTradMidMinDiag.size(); i++) {
                    if (nonTradMidMinDiag.get(i).possibleWinner == 0) {
                        selectCol = nonTradMidMinDiag.get(i).x;
                        selectRow = nonTradMidMinDiag.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }

            // Guarantees own wins (gains 3rd priority)
            if (nonTrad2WinRows.size() >= 1) {
                for (int i = 0; i < nonTrad2WinRows.size(); i++) {
                    if (nonTrad2WinRows.get(i).possibleWinner == 1) {
                        selectCol = nonTrad2WinRows.get(i).x;
                        selectRow = nonTrad2WinRows.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2MidWinRows.size() >= 1) {
                for (int i = 0; i < nonTrad2MidWinRows.size(); i++) {
                    if (nonTrad2MidWinRows.get(i).possibleWinner == 1) {
                        selectCol = nonTrad2MidWinRows.get(i).x;
                        selectRow = nonTrad2MidWinRows.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2WinCols.size() >= 1) {
                for (int i = 0; i < nonTrad2WinCols.size(); i++) {
                    if (nonTrad2WinCols.get(i).possibleWinner == 1) {
                        selectCol = nonTrad2WinCols.get(i).x;
                        selectRow = nonTrad2WinCols.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2MidWinCols.size() >= 1) {
                for (int i = 0; i < nonTrad2MidWinCols.size(); i++) {
                    if (nonTrad2MidWinCols.get(i).possibleWinner == 1) {
                        selectCol = nonTrad2MidWinCols.get(i).x;
                        selectRow = nonTrad2MidWinCols.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2WinMajDiags.size() >= 1) {
                for (int i = 0; i < nonTrad2WinMajDiags.size(); i++) {
                    if (nonTrad2WinMajDiags.get(i).possibleWinner == 1) {
                        selectCol = nonTrad2WinMajDiags.get(i).x;
                        selectRow = nonTrad2WinMajDiags.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2MidWinMajDiags.size() >= 1) {
                for (int i = 0; i < nonTrad2MidWinMajDiags.size(); i++) {
                    if (nonTrad2MidWinMajDiags.get(i).possibleWinner == 1) {
                        selectCol = nonTrad2MidWinMajDiags.get(i).x;
                        selectRow = nonTrad2MidWinMajDiags.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2WinMinDiags.size() >= 1) {
                for (int i = 0; i < nonTrad2WinMinDiags.size(); i++) {
                    if (nonTrad2WinMinDiags.get(i).possibleWinner == 1) {
                        selectCol = nonTrad2WinMinDiags.get(i).x;
                        selectRow = nonTrad2WinMinDiags.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2MidWinMinDiags.size() >= 1) {
                for (int i = 0; i < nonTrad2MidWinMinDiags.size(); i++) {
                    if (nonTrad2MidWinMinDiags.get(i).possibleWinner == 1) {
                        selectCol = nonTrad2MidWinMinDiags.get(i).x;
                        selectRow = nonTrad2MidWinMinDiags.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }

            // Preventing opponent guaranteed wins (gains 4th priority)
            if (nonTrad2WinRows.size() >= 1) {
                for (int i = 0; i < nonTrad2WinRows.size(); i++) {
                    if (nonTrad2WinRows.get(i).possibleWinner == 0) {
                        selectCol = nonTrad2WinRows.get(i).x;
                        selectRow = nonTrad2WinRows.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2MidWinRows.size() >= 1) {
                for (int i = 0; i < nonTrad2MidWinRows.size(); i++) {
                    if (nonTrad2MidWinRows.get(i).possibleWinner == 0) {
                        selectCol = nonTrad2MidWinRows.get(i).x;
                        selectRow = nonTrad2MidWinRows.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2WinCols.size() >= 1) {
                for (int i = 0; i < nonTrad2WinCols.size(); i++) {
                    if (nonTrad2WinCols.get(i).possibleWinner == 0) {
                        selectCol = nonTrad2WinCols.get(i).x;
                        selectRow = nonTrad2WinCols.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2MidWinCols.size() >= 1) {
                for (int i = 0; i < nonTrad2MidWinCols.size(); i++) {
                    if (nonTrad2MidWinCols.get(i).possibleWinner == 0) {
                        selectCol = nonTrad2MidWinCols.get(i).x;
                        selectRow = nonTrad2MidWinCols.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2WinMajDiags.size() >= 1) {
                for (int i = 0; i < nonTrad2WinMajDiags.size(); i++) {
                    if (nonTrad2WinMajDiags.get(i).possibleWinner == 0) {
                        selectCol = nonTrad2WinMajDiags.get(i).x;
                        selectRow = nonTrad2WinMajDiags.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2MidWinMajDiags.size() >= 1) {
                for (int i = 0; i < nonTrad2MidWinMajDiags.size(); i++) {
                    if (nonTrad2MidWinMajDiags.get(i).possibleWinner == 0) {
                        selectCol = nonTrad2MidWinMajDiags.get(i).x;
                        selectRow = nonTrad2MidWinMajDiags.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2WinMinDiags.size() >= 1) {
                for (int i = 0; i < nonTrad2WinMinDiags.size(); i++) {
                    if (nonTrad2WinMinDiags.get(i).possibleWinner == 0) {
                        selectCol = nonTrad2WinMinDiags.get(i).x;
                        selectRow = nonTrad2WinMinDiags.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }
            if (nonTrad2MidWinMinDiags.size() >= 1) {
                for (int i = 0; i < nonTrad2MidWinMinDiags.size(); i++) {
                    if (nonTrad2MidWinMinDiags.get(i).possibleWinner == 0) {
                        selectCol = nonTrad2MidWinMinDiags.get(i).x;
                        selectRow = nonTrad2MidWinMinDiags.get(i).y;
                        board.selectCell(1, selectRow, selectCol);
                        return;
                    }
                }
            }

            // Goes Randomly if the above have not been met (if there is no threat)
            selectCol = rand.nextInt(board.getWidth());
            selectRow = rand.nextInt(board.getHeight());
            while (!board.selectCell(1, selectRow, selectCol)) {
                selectCol = rand.nextInt(board.getWidth());
                selectRow = rand.nextInt(board.getHeight());
            }
        }
    }

            
    //Helper function to convert a Color to a String, if needed
    public static String convertColorToString(Color col){
        String color;
        if (col.equals(Color.BLACK))
            color = "Black";
        else if(col.equals(Color.BLUE))
            color = "Blue";
        else if(col.equals(Color.CYAN))
            color = "Cyan";
        else if(col.equals(Color.DARK_GRAY))
            color = "Dark Gray";
        else if(col.equals(Color.GRAY))
            color = "Gray";
        else if(col.equals(Color.GREEN))
            color = "Green";
        else if(col.equals(Color.YELLOW))
            color = "Yellow";
        else if(col.equals(Color.LIGHT_GRAY))
            color = "Light Gray";
        else if(col.equals(Color.MAGENTA))
            color = "Magneta";
        else if(col.equals(Color.ORANGE))
            color = "Orange";
        else if(col.equals(Color.PINK))
            color = "Pink";
        else if(col.equals(Color.RED))
            color = "Red";
        else
            color = "Black";
        return color;
    }
    
    //Helper function to convert a String to a Color, if needed
    public static Color convertStringToColor(String col){
        Color color;
        switch (col.toLowerCase()) {
        case "black":
            color = Color.BLACK;
            break;
        case "blue":
            color = Color.BLUE;
            break;
        case "cyan":
            color = Color.CYAN;
            break;
        case "darkgray":
            color = Color.DARK_GRAY;
            break;
        case "gray":
            color = Color.GRAY;
            break;
        case "green":
            color = Color.GREEN;
            break;
        case "yellow":
            color = Color.YELLOW;
            break;
        case "lightgray":
            color = Color.LIGHT_GRAY;
            break;
        case "magenta":
            color = Color.MAGENTA;
            break;
        case "orange":
            color = Color.ORANGE;
            break;
        case "pink":
            color = Color.PINK;
            break;
        case "red":
            color = Color.RED;
            break;
        default:
            color = Color.BLACK;
            }
        return color;
    }
}