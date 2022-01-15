package byog.Core;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;


public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 90;
    public static final int HEIGHT = 46;

    long seed;
    int[][] playerdoor = new int[2][2];
    int saveFlage = 0;
    int isLSavedGame = 0;


    TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];


    public Game() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
    }


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        Menu menu = new Menu(WIDTH, HEIGHT);
        menu.controlMenus();
        switch (menu.lisentOnMenu()) {
            case 2 :
                run("L");
                break;

            case 3:
                System.exit(0);
                break;

            case 1:
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, String.valueOf(""));
                StdDraw.show();
                StringBuilder str = new StringBuilder();
                str.append(" ");
                while (true) {
                    if (StdDraw.hasNextKeyTyped()) {
                        str.append(StdDraw.nextKeyTyped());
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.text(WIDTH / 2, HEIGHT / 2, String.valueOf(str));
                        StdDraw.show();
                    }
                    char c = str.charAt(str.length() - 1);
                    if (c == 'S' || c == 's') {
                        break;
                    }
                }
                run(String.valueOf(str));
                break;
            default:
                break;
        }


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
        input = initialize(input);
        World world = new World(seed, WIDTH, HEIGHT);
        world.world(finalWorldFrame);

        if (isLSavedGame == 0) {
            playerdoor = world.playDoor(finalWorldFrame);
        } else {
            finalWorldFrame[playerdoor[0][0]][playerdoor[0][1]] = Tileset.PLAYER;
            finalWorldFrame[playerdoor[1][0]][playerdoor[1][1]] = Tileset.LOCKED_DOOR;
        }


        int[] altered = Movement.move(finalWorldFrame, playerdoor[0][0], playerdoor[0][1], input);

        playerdoor[0][0] = altered[0];
        playerdoor[0][1] = altered[1];

        if (saveFlage == 1) {
            save(playerdoor);
        }




        return finalWorldFrame;
    }


    // 序列化
    public static void writeObj(SavedGame p) {
        try {
            FileOutputStream file = new FileOutputStream("../saved.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
            objectOutputStream.writeObject(p);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 反序列化
    public static SavedGame readObj() {
        SavedGame p = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    new FileInputStream("../saved.txt"));
            try {
                p = (SavedGame) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }


    public String initialize(String input) {
        if (input.contains("Q") || input.contains("q")) {
            input = input.substring(0, input.length() - 2);
            saveFlage = 1;
        }
        if (input.contains("l") || input.contains("L")) {
            SavedGame saved = readObj();
            seed = saved.savedseed;
            playerdoor[0][0] = saved.x0;
            playerdoor[0][1] = saved.y0;
            playerdoor[1][0] = saved.xDoor;
            playerdoor[1][1] = saved.yDoor;
            isLSavedGame = 1;
            input = input.substring(1); // 获得步骤指令
        } else {
            int index = input.indexOf("S");
            String temp = input;
            seed = Long.parseLong(temp.replaceAll("[^0-9]", ""));
            input = input.substring(index + 1); //获得步骤指令
        }
        return input;
    }

    public char listen() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                return StdDraw.nextKeyTyped();
            }
        }
    }


    public void save(int[][] player) {
        SavedGame save = new SavedGame();
        save.x0 = player[0][0];
        save.y0 = player[0][1];
        save.xDoor = player[1][0];
        save.yDoor = player[1][1];
        save.savedseed = seed;
        writeObj(save);
    }

    public void run(String s) {
        playWithInputString(s);
        while (true) {
            char received = listen();
            if (received == ':') {
                char received2 = listen();
                if (received2 == 'Q' || received2 == 'q') {
                    save(playerdoor);
                    System.exit(0);
                    break;
                }
            } else {
                int x = playerdoor[0][0];
                int y = playerdoor[0][1];
                int[] altered = Movement.move(finalWorldFrame, x, y, String.valueOf(received));
                playerdoor[0][0] = altered[0];
                playerdoor[0][1] = altered[1];
                ter.renderFrame(finalWorldFrame);
            }
        }

    }
}

