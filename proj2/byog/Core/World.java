package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    Random random = new Random();
    int WIDTH;
    int HEIGHT;
    int x0 = 0;
    int y0 = 0;
    int xDoor = 0;
    int yDoor = 0;


    public World(long seed, int width, int height) {
        random.setSeed(seed);
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    public void world(TETile[][] finalWorldFrame) {
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
    }

    public int[][] playDoor(TETile[][] finalWorldFrame) {
        int[][] results = new int[2][2];
        while (!DoorPeopleCheck.peoplecheck(finalWorldFrame, x0, y0)) {
            x0 = random.nextInt(WIDTH - 1);
            y0 = random.nextInt(HEIGHT - 1);
        }
        while (!DoorPeopleCheck.doorcheck(finalWorldFrame, xDoor, yDoor)) {
            xDoor = random.nextInt(WIDTH - 1);
            yDoor = random.nextInt(HEIGHT - 1);
        }

        finalWorldFrame[x0][y0] = Tileset.PLAYER;
        finalWorldFrame[xDoor][yDoor] = Tileset.LOCKED_DOOR;

        results[0][0] = x0;
        results[0][1] = y0;
        results[1][0] = xDoor;
        results[1][1] = yDoor;
        return results;
    }
}
