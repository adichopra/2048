import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
public class AI {
    ArrayList<int[][]> a = new ArrayList<>();
    ArrayList<Integer> b = new ArrayList<>();
    public String name = "defaultBot";
    public boolean autoRestart = false;
    public int trials = 0;
    private int[][] board;
    private static double WHITESPACE = 1;
    private static double SMOOTHNESS = 2.7;
    public AI() {
        name = "AdityaBot";
        autoRestart = true;
        trials = 1000;
    }
    private void updateBoard(int n) {
        int[][] before = new int[4][4];
            System.arraycopy(board, 0, before, 0, board.length);
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
                updateBoard(KeyEvent.VK_LEFT);
                rotateCW();
            }
            else if (n == KeyEvent.VK_RIGHT) {
                rotateCCW();
                rotateCCW();
                updateBoard(KeyEvent.VK_LEFT);
                rotateCW();
                rotateCW();
            }
            else if (n == KeyEvent.VK_DOWN) {
                rotateCW();
                updateBoard(KeyEvent.VK_LEFT);
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
    private int eval_move(int[][] board) {
        
        int[][] before = new int[board.length][board.length];
        System.arraycopy(board, 0, before, 0, board.length);
        int[] directions = {KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN};
        double[] evals = new double[4];
        updateBoard(KeyEvent.VK_LEFT);
        evals[0] += WHITESPACE * Math.log(whiteSpace());
        evals[0] -= SMOOTHNESS * smoothness();
        board = before;
        updateBoard(KeyEvent.VK_UP);
        evals[1] += WHITESPACE * Math.log(whiteSpace());
        evals[1] -= SMOOTHNESS * smoothness();
        board = before;
        updateBoard(KeyEvent.VK_RIGHT);
        evals[2] += WHITESPACE * Math.log(whiteSpace());
        evals[2] -= SMOOTHNESS * smoothness();
        board = before;
        updateBoard(KeyEvent.VK_DOWN);
        evals[3] += WHITESPACE * Math.log(whiteSpace());
        evals[3] -= SMOOTHNESS * smoothness();
        board = before;
        ArrayList<Double> a = new ArrayList<>();
        for (double i: evals) {
            a.add(i);
        }
        return directions[a.indexOf(Math.max(evals[0], Math.max(evals[1], Math.max(evals[2], evals[3]))))];
    }
    private int whiteSpace() {
        int count = 0;
        for (int[] a: board) {
            for (int i: a) {
                if (i == 0) count++;
            }
        }
        return count;
    }
    private int smoothness() { //less is better
        int smoothness = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                smoothness += singleSmoothness(i, j);
            }
        }
        return smoothness;
    }
    private int singleSmoothness(int i, int j) {
        int sum = 0;
        if (i > 0 && i < board.length - 1) sum += (Math.abs(board[i][j] - board[i - 1][j]) + Math.abs(board[i][j] - board[i + 1][j]));
        else if (i == 0) sum += Math.abs(board[i][j] - board[i + 1][j]);
        else if (i == board.length - 1) sum += Math.abs(board[i][j] - board[i - 1][j]);
        if (j > 0 && j < board.length - 1) sum += (Math.abs(board[i][j] - board[i][j - 1]) + Math.abs(board[i][j] - board[i][j + 1]));
        else if (j == 0) sum += Math.abs(board[i][j] - board[i][j + 1]);
        else if (j == board.length - 1) sum += Math.abs(board[i][j] - board[i][j - 1]);
        return sum;
    }
    public int ai_move(int[][] board) {
        this.board = board;
        int returned = 0;
        int direction = (int) (Math.random() * 2);
        a.add(board);
        if (a.size() > 2)
            if (Arrays.deepEquals(a.get(a.size() - 1), a.get(a.size() - 2))) {
                direction = (int) (Math.random() * 4);
                if (direction == 0) returned = KeyEvent.VK_LEFT;
                if (direction == 1) returned = KeyEvent.VK_UP;
                if (direction == 2) returned = KeyEvent.VK_RIGHT;
                if (direction == 3) returned = KeyEvent.VK_DOWN;
            }
            else returned = eval_move(board);
        b.add(returned);
        return returned;
    }
}