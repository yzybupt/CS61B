package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class DoorPeopleCheck {
    public static boolean doorcheck(TETile[][] world, int x, int y) {
        return (world[x][y] == Tileset.WALL);
    }

    public static boolean peoplecheck(TETile[][] world, int x, int y) {
        return (world[x][y] == Tileset.FLOOR);
    }
}

