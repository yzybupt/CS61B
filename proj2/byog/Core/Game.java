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
    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;



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
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        long seed;
        if (input.equals("l") || input.equals("L")) {
            seed = Saved_Game.saved_seed;
            Saved_Game.flag = 0;
        } else if ((input.length() >= 2) && ((input.substring(input.length() - 2).contains(":Q")) || (input.substring(input.length() - 2).contains(":q")))) {
            seed = Long.parseLong(input.replaceAll("[^0-9]", ""));
            Saved_Game.saved_seed = seed;
            Saved_Game.flag = 1;
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

        List<Room> existing_rooms = new ArrayList<>();

        int number = random.nextInt(11)%(11-8+1) + 8;
        while (existing_rooms.size() < number) {
            int x = random.nextInt(WIDTH - 1);
            int y = random.nextInt(HEIGHT - 1);
            int width = random.nextInt(12)%(12-6+1) + 6;
            int height = random.nextInt(12)%(12-6+1) + 6;
            if(Room.border_check(WIDTH,HEIGHT,x,y,width,height) && Room.existing_check(existing_rooms,x,y,width,height)) {

                existing_rooms.add(new Room(x,y,width,height));
            }
        }


        for (int i = 0; i < existing_rooms.size(); i++) {
            existing_rooms.get(i).drawRoom(finalWorldFrame);
        }


        for (int i = 0; i < existing_rooms.size(); i++) {
            for (int j = i + 1; j < existing_rooms.size(); j++) {
                Hall_Way.draw_hall_way(finalWorldFrame,Hall_Way.hall_way_generator(existing_rooms.get(i), existing_rooms.get(j)));
                Hall_Way.draw_hall_way2(finalWorldFrame,Hall_Way.hall_way_generator(existing_rooms.get(i), existing_rooms.get(j)));
                Hall_Way.draw_hall_way3(finalWorldFrame,Hall_Way.hall_way_generator(existing_rooms.get(i), existing_rooms.get(j)));
            }
        }





        return finalWorldFrame;
    }
}
