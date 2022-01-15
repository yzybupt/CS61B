package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

public class Menu {
    int width;
    int height;
    public Menu(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public void controlMenus() {
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.WHITE);

        Font font = new Font("Monaco", Font.BOLD, 35);
        StdDraw.setFont(font);
        StdDraw.text(width / 2, height / 2 + 15, "CS61B: THE GAME");

        Font font2 = new Font("Arial", Font.BOLD, 25);
        StdDraw.setFont(font2);
        StdDraw.text(width / 2, height / 2, "New Game (N)");
        StdDraw.text(width / 2, height / 2 - 2, "Load Game (L)");
        StdDraw.text(width / 2, height / 2 - 4, "Quit (Q)");
        StdDraw.show();

    }

    public int lisentOnMenu() {
        char received;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                received = StdDraw.nextKeyTyped();
                if (received == 'L' || received == 'l') {
                    return 2;
                } else if (received == 'N' || received == 'n') {
                    return 1;
                } else if (received == 'Q' || received == 'q') {
                    return 3;
                }
            }
        }
    }
}
