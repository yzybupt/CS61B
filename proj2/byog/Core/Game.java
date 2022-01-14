package byog.Core;


import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 90;
    public static final int HEIGHT = 46;



    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        long seed;
        boolean e = input.substring(input.length() - 2).contains(":Q");
        boolean e2 = input.substring(input.length() - 2).contains(":q");
        if (input.equals("l") || input.equals("L")) {
            seed = SavedGame.savedseed;
            SavedGame.flag = 0;
        } else if ((input.length() >= 2) && (e || e2)) {
            seed = Long.parseLong(input.replaceAll("[^0-9]", ""));
            SavedGame.savedseed = seed;
            SavedGame.flag = 1;
        } else {
            seed = Long.parseLong(input.replaceAll("[^0-9]", ""));
        }

        Random random = new Random();
        random.setSeed(seed);

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }

        List<Room> existings = new ArrayList<>();

        int number = random.nextInt(15) % (15 - 9 + 1) + 9;
        int robust = 0;
        while (existings.size() < number) {
            robust++;
            int x = random.nextInt(WIDTH - 1);
            int y = random.nextInt(HEIGHT - 1);
            int width = random.nextInt(6) % (6 - 3 + 1) + 3;
            int height = random.nextInt(6) % (6 - 3 + 1) + 3;
            boolean l = Room.bordercheck(WIDTH, HEIGHT, x, y, width, height);
            if (l && Room.existingcheck(existings, x, y, width, height)) {
                existings.add(new Room(x, y, width, height));
            }
            if (robust > 400) {
                break;
            }
        }


        for (int i = 0; i < existings.size(); i++) {
            existings.get(i).drawRoom(finalWorldFrame);
        }


        for (int i = 0; i < existings.size(); i++) {
            for (int j = i + 1; j < existings.size(); j++) {
                Room r = existings.get(i);
                int[][] res = HallWay.hallwaygenerator(r, existings.get(j));
                HallWay.drawhallway(finalWorldFrame, res);
                HallWay.drawhallway2(finalWorldFrame, res);
                HallWay.drawhallway3(finalWorldFrame, res);
            }
        }



        return finalWorldFrame;
    }
}
