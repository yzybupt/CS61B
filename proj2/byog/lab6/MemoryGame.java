package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};


    private class Random {
        java.util.Random random;
        Random(int seed) {
            random = new java.util.Random();
            random.setSeed(seed);
        }

        public int nextInt(int length) {
            return random.nextInt(length);
        }

    }



    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        rand = new Random(seed);
        //TODO: Initialize random number generator
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; ++i) {
            int number = rand.nextInt(CHARACTERS.length);
            sb.append(CHARACTERS[number]);
        }
        return sb.toString();
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        for(int i = 0; i < s.length(); i++) {
            StdDraw.clear(Color.BLACK);
            StdDraw.show(500);
            StdDraw.text(20, 20, String.valueOf(s.charAt(i)));
            StdDraw.show(1000);
        }
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        drawFrame(letters);
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        StdDraw.clear(Color.BLACK);
        StringBuilder typed = new StringBuilder();
        while(typed.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                typed.append(StdDraw.nextKeyTyped());
                StdDraw.clear(Color.BLACK);
                StdDraw.text(this.width / 2, this.height / 2, String.valueOf(typed));
                StdDraw.show();
            }
        }
        return String.valueOf(typed);
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        //TODO: Establish Game loop
        int i = 0;
        while (true) {
            i = i + 1;
            StdDraw.clear(Color.BLACK);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.clear(Color.BLACK);
            StdDraw.text(this.width / 2, this.height / 2, "Round: " + i);
            StdDraw.show(1000);
            String answer = generateRandomString(i);
            drawFrame(answer);
            StdDraw.clear(Color.BLACK);
            StdDraw.text(20, 20, "");
            StdDraw.show(1000);
            if (!answer.equals(solicitNCharsInput(i))) {
                break;
            }

        }
        System.out.println("Game Over! You made it to round:" + i);
        System.exit(0);
    }

}
