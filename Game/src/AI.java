import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
public class AI {
    ArrayList<int[][]> a = new ArrayList<>();
    ArrayList<Integer> b = new ArrayList<>();
    public String name;
    public AI() {
        name = "AdityaBot";
    }
    public int ai_move(int[][] board) {
        int returned = 0;
        int direction = (int) (Math.random() * 2);
        a.add(board);
        if (a.size() > 2)
            if (Arrays.deepEquals(a.get(a.size() - 1), a.get(a.size() - 2))) {
                direction = (int) (Math.random() * 4);
                if (direction == 0) returned =  KeyEvent.VK_LEFT;
                if (direction == 1) returned =  KeyEvent.VK_UP;
                if (direction == 2) returned =  KeyEvent.VK_RIGHT;
                if (direction == 3) returned =  KeyEvent.VK_DOWN;
            }
        if (direction == 0) returned =  KeyEvent.VK_LEFT;
        if (direction == 1) returned =  KeyEvent.VK_UP;
        b.add(returned);
        return returned;
    }
}