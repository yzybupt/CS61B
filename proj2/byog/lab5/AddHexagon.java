package byog.lab5;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class AddHexagon {
    public static void addHexagon(int x, int y, int length, TETile[][] world) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length + 2 * i; j++) {
                world[x - i + j][y + i] = Tileset.WALL;
            }
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length + 2 * i; j++) {
                world[x - i + j][y + length * 2 - 1 - i] = Tileset.WALL;
            }
        }
    }
}
