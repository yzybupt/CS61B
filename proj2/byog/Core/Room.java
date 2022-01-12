package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Room {
    protected int width;
    protected int height;
    protected int x;
    protected int y;

    public Room(int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void drawRoom(TETile[][] world) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j == 0 || j == height - 1) {
                    world[x + i][y + j] = Tileset.WALL;
                } else if (i == 0 || i == width - 1) {
                    world[x + i][y + j] = Tileset.WALL;
                } else {
                    world[x + i][y + j] = Tileset.FLOOR;
                }
            }
        }
    }

    static boolean bordercheck(int bwidth, int bheight, int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (x + i < 0 || x + i > bwidth - 1) {
                    return false;
                } else if (y + j < 0 || y + j > bheight - 1) {
                    return false;
                }
            }
        }
        return true;

    }

    public static boolean existingcheck(List<Room> list, int x, int y, int width, int height) {
        if (list.isEmpty()) {
            return true;
        } else {
            for (int i = 0; i < list.size(); i++) {
                int x0 = list.get(i).x;
                int y0 = list.get(i).y;
                int width0 = list.get(i).width;
                int height0 = list.get(i).height;
                Set<Integer> sets = new HashSet<>();
                for (int k = 0; k < width0; k++) {
                    for (int j = 0; j < height0; j++) {
                        sets.add(x0 + k);
                        sets.add(y0 + j);
                    }
                }

                for (int k = 0; k < width; k++) {
                    for (int j = 0; j < height; j++) {
                        if ((sets.contains(x + k)) || (sets.contains(y + j))) {
                            return false;
                        }
                    }
                }

            }
            return true;
        }
    }

}
