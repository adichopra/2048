import java.awt.event.KeyEvent;
import java.util.Arrays;
public class AI {
    public String name = "defaultBot";
    public boolean autoRestart = false;
    public int trials = 0;
    private int[][] board;
    public AI() {
        name = "AdityaBot";
        autoRestart = true;
        trials = 1000;
        GameGUI.sleep_time = 10;
    }
    private void updateBoard(int n, boolean real) {
        int[][] before = deepCopy(board);
        if (n == KeyEvent.VK_LEFT) {
            pushLeft();
            //check tiles with x > 0, from left to right to see if they collapse
            for (int i = 0; i < 4; i++) {
                for (int j = 1; j < 4; j++) {
                    if (board[i][j-1] == board[i][j]) {
                        board[i][j-1] *= 2;
                        board[i][j] = 0; //temporarily leave a 0 which will go away when we push again
                    }
                }
            }
            pushLeft();
        }
        else if (n == KeyEvent.VK_UP) {
            rotateCCW();
            updateBoard(KeyEvent.VK_LEFT, real);
            rotateCW();
        }
        else if (n == KeyEvent.VK_RIGHT) {
            rotateCCW();
            rotateCCW();
            updateBoard(KeyEvent.VK_LEFT, real);
            rotateCW();
            rotateCW();
        }
        else if (n == KeyEvent.VK_DOWN) {
            rotateCW();
            updateBoard(KeyEvent.VK_LEFT, real);
            rotateCCW();
        }
    }
    private void pushLeft() {
            //move tiles with values as far left as possible
            for (int i = 0; i < 4; i++) {
                    int[] row = new int[4];
                    for (int j = 0; j < 4; j++) {
                            if (board[i][j] != 0) {
                                    int curr = 0;
                                    while(row[curr] != 0) {
                                            curr++;
                                    }
                                    row[curr] = board[i][j];
                            }
                    }
                    board[i] = row;
            }
    }
    private void rotateCW() {
        int[][] rotated = new int[4][4];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                rotated[c][3-r] = board[r][c];
            }
        }
        board = rotated;
    }
    private void rotateCCW() {
            int[][] rotated = new int[4][4];
            for (int r = 0; r < 4; r++) {
                    for (int c = 0; c < 4; c++) {
                            rotated[3-c][r] = board[r][c];
                    }
            }
            board = rotated;
    }
    private static int[][] deepCopy(int[][] board) {
        int[][] copy = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }
    private boolean validMove(int direction) {
        int[][] before = deepCopy(board);
        updateBoard(direction, false);
        return !Arrays.deepEquals(before, board);
    }
    public int ai_move(int[][] board) {
        this.board = board;
        if (validMove(KeyEvent.VK_UP)) return KeyEvent.VK_UP;
        else if (validMove(KeyEvent.VK_LEFT)) return KeyEvent.VK_LEFT;
        else if (validMove(KeyEvent.VK_RIGHT)) return KeyEvent.VK_RIGHT;
        return KeyEvent.VK_DOWN;
    }
}
