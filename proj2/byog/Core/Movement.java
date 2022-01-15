package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Movement {
    public static int[] move(TETile[][] world, int x0, int y0, String input) {
        int[] altered = new int[2];
        while (!input.equals("")) {
            char c = input.charAt(0);
            if ((c == 'W' || c == 'w') && world[x0][y0 + 1] != Tileset.WALL) {
                world[x0][y0] = Tileset.FLOOR;
                world[x0][y0 + 1] = Tileset.PLAYER;
                x0 = x0;
                y0 = y0 + 1;
            } else if ((c == 'S' || c == 's') && world[x0][y0 - 1] != Tileset.WALL) {
                world[x0][y0] = Tileset.FLOOR;
                world[x0][y0 - 1] = Tileset.PLAYER;
                x0 = x0;
                y0 = y0 - 1;
            } else if ((c == 'a' || c == 'A') && world[x0 - 1][y0] != Tileset.WALL) {
                world[x0][y0] = Tileset.FLOOR;
                world[x0 - 1][y0] = Tileset.PLAYER;
                x0 = x0 - 1;
                y0 = y0;
            } else if ((c == 'd' || c == 'D') && world[x0 + 1][y0] != Tileset.WALL) {
                world[x0][y0] = Tileset.FLOOR;
                world[x0 + 1][y0] = Tileset.PLAYER;
                x0 = x0 + 1;
                y0 = y0;
            }
            input = input.substring(1);

        }
        altered[0] = x0;
        altered[1] = y0;
        return altered;
    }

}
