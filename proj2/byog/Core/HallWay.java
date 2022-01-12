package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;




public class HallWay {
    public static int[][] hallwaygenerator(Room r1, Room r2) {
        int[][] results = new int[3][2];
        int[] turning = new int[2];
        int[][] border = new int[4][2];
        int leftx = Math.min(r1.x, r2.x);
        int downy = Math.min(r1.y, r2.y);
        int rightx = Math.max(r1.x + r1.width - 1, r2.x + r2.width - 1);
        int upy = Math.max(r1.y + r1.height - 1, r2.y + r2.height - 1);
        border[0][0] = leftx;
        border[0][1] = downy;

        border[1][0] = leftx;
        border[1][1] = upy;

        border[2][0] = rightx;
        border[2][1] = downy;

        border[3][0] = rightx;
        border[3][1] = upy;


        int[][] vertices = new int[8][2];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0) {
                    vertices[j + 4 * i][0] = r1.x + (j % 2) * (r1.width - 1);
                    vertices[j + 4 * i][1] = r1.y + (j / 2) * (r1.height - 1);
                } else {
                    vertices[j + 4 * i][0] = r2.x + (j % 2) * (r2.width - 1);
                    vertices[j + 4 * i][1] = r2.y + (j / 2) * (r2.height - 1);
                }
            }
        }

        tag: for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                if ((border[i][0] == vertices[j][0]) && (border[i][1] == vertices[j][1])) {
                    continue tag;
                }
            }
            turning[0] = border[i][0];
            turning[1] = border[i][1];
        }

        int[] connect1 = new int[2];
        int[] connect2 = new int[2];
        int[] distances = new int[4];
        for (int j = 0; j < 4; j++) {
            int e = HallWay.distance(turning[0], turning[1], vertices[j][0], vertices[j][1]);
            distances[j] = e;
        }
        int temp = 0;
        for (int j = 0; j < 4; j++) {
            if (distances[j] < distances[temp]) {
                temp = j;
            }
        }
        connect1[0] = vertices[temp][0];
        connect1[1] = vertices[temp][1];

        for (int j = 4; j < 8; j++) {
            int e = HallWay.distance(turning[0], turning[1], vertices[j][0], vertices[j][1]);
            distances[j % 4] = e;
        }
        temp = 4;
        for (int j = 4; j < 8; j++) {
            if (distances[j % 4] < distances[temp % 4]) {
                temp = j;
            }
        }
        connect2[0] = vertices[temp][0];
        connect2[1] = vertices[temp][1];

        results[0] = turning;
        results[1] = connect1;
        results[2] = connect2;

        return results;
    }

    public static int distance(int x0, int y0, int x1, int y1) {
        return (x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1);
    }


    public static void drawhallway(TETile[][] world, int[][] points) {
        int flag = 4;
        int minx = Math.min(points[1][0], points[2][0]);
        int maxy = Math.max(points[1][1], points[2][1]);
        int maxx = Math.max(points[1][0], points[2][0]);
        int miny = Math.min(points[1][1], points[2][1]);
        if (points[0][0] == minx && points[0][1] == maxy) {
            flag = 1;
        } else if (points[0][0] == maxx && points[0][1] == miny) {
            flag = 0;
        } else if (points[0][0] == minx && points[0][1] == miny) {
            flag = 2;
        } else if (points[0][0] == maxx && points[0][1] == maxy) {
            flag = 3;
        }
        if (flag == 0) {
            for (int i = 0; i < (maxx - minx + 1); i++) {
                if (world[minx + i][miny] != Tileset.FLOOR) {
                    world[minx + i][miny] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                if (world[maxx][miny + i] != Tileset.FLOOR) {
                    world[maxx][miny + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 1) {
            for (int i = 0; i < (maxx - minx + 1); i++) {
                if (world[minx + i][maxy] != Tileset.FLOOR) {
                    world[minx + i][maxy] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                if (world[minx][miny + i] != Tileset.FLOOR) {
                    world[minx][miny + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 2) {
            for (int i = 0; i < (maxx - minx + 1); i++) {
                if (world[minx + i][miny] != Tileset.FLOOR) {
                    world[minx + i][miny] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                if (world[minx][miny + i] != Tileset.FLOOR) {
                    world[minx][miny + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 3) {
            for (int i = 0; i < (maxx - minx + 1); i++) {
                if (world[minx + i][maxy] != Tileset.FLOOR) {
                    world[minx + i][maxy] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                if (world[maxx][miny + i] != Tileset.FLOOR) {
                    world[maxx][miny + i]  = Tileset.WALL;
                }
            }
        }

    }

    public static void drawhallway2(TETile[][] world, int[][] points) {
        int flag = 4;
        int minx = Math.min(points[1][0], points[2][0]);
        int maxy = Math.max(points[1][1], points[2][1]);
        int maxx = Math.max(points[1][0], points[2][0]);
        int miny = Math.min(points[1][1], points[2][1]);
        if (points[0][0] == minx && points[0][1] == maxy) {
            flag = 1;
        } else if (points[0][0] == maxx && points[0][1] == miny) {
            flag = 0;
        } else if (points[0][0] == minx && points[0][1] == miny) {
            flag = 2;
        } else if (points[0][0] == maxx && points[0][1] == maxy) {
            flag = 3;
        }
        if (flag == 0) {
            minx = minx - 2;
            maxy = maxy + 2;
            maxx = maxx - 2;
            miny = miny + 2;
            for (int i = 0; i < (maxx - minx + 1); i++) {
                if (world[minx + i][miny] != Tileset.FLOOR) {
                    world[minx + i][miny] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                if (world[maxx][miny + i] != Tileset.FLOOR) {
                    world[maxx][miny + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 1) {
            minx = minx + 2;
            maxy = maxy - 2;
            maxx = maxx + 2;
            miny = miny - 2;
            for (int i = 0; i < (maxx - minx + 1); i++) {
                if (world[minx + i][maxy] != Tileset.FLOOR) {
                    world[minx + i][maxy] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                if (world[minx][miny + i] != Tileset.FLOOR) {
                    world[minx][miny + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 2) {
            minx = minx + 2;
            maxy = maxy + 2;
            maxx = maxx + 2;
            miny = miny + 2;
            for (int i = 0; i < (maxx - minx + 1); i++) {
                if (world[minx + i][miny] != Tileset.FLOOR) {
                    world[minx + i][miny] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                if (world[minx][miny + i] != Tileset.FLOOR) {
                    world[minx][miny + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 3) {
            minx = minx - 2;
            maxy = maxy - 2;
            maxx = maxx - 2;
            miny = miny - 2;
            for (int i = 0; i < (maxx - minx + 1); i++) {
                if (world[minx + i][maxy] != Tileset.FLOOR) {
                    world[minx + i][maxy] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                if (world[maxx][miny + i] != Tileset.FLOOR) {
                    world[maxx][miny + i]  = Tileset.WALL;
                }
            }
        }

    }

    public static void drawhallway3(TETile[][] world, int[][] points) {
        int flag = 4;
        int minx = Math.min(points[1][0], points[2][0]);
        int maxy = Math.max(points[1][1], points[2][1]);
        int maxx = Math.max(points[1][0], points[2][0]);
        int miny = Math.min(points[1][1], points[2][1]);
        if (points[0][0] == minx && points[0][1] == maxy) {
            flag = 1;
        } else if (points[0][0] == maxx && points[0][1] == miny) {
            flag = 0;
        } else if (points[0][0] == minx && points[0][1] == miny) {
            flag = 2;
        } else if (points[0][0] == maxx && points[0][1] == maxy) {
            flag = 3;
        }
        if (flag == 0) {
            minx = minx - 1;
            maxy = maxy + 1;
            maxx = maxx - 1;
            miny = miny + 1;
            for (int i = 0; i < (maxx - minx + 1); i++) {
                world[minx + i][miny] = Tileset.FLOOR;
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                world[maxx][miny + i]  = Tileset.FLOOR;
            }
        } else if (flag == 1) {
            minx = minx + 1;
            maxy = maxy - 1;
            maxx = maxx + 1;
            miny = miny - 1;
            for (int i = 0; i < (maxx - minx + 1); i++) {
                world[minx + i][maxy] = Tileset.FLOOR;
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                world[minx][miny + i]  = Tileset.FLOOR;
            }
        } else if (flag == 2) {
            minx = minx + 1;
            maxy = maxy + 1;
            maxx = maxx + 1;
            miny = miny + 1;
            for (int i = 0; i < (maxx - minx + 1); i++) {
                world[minx + i][miny] = Tileset.FLOOR;
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                world[minx][miny + i]  = Tileset.FLOOR;
            }
        } else if (flag == 3) {
            minx = minx - 1;
            maxy = maxy - 1;
            maxx = maxx - 1;
            miny = miny - 1;
            for (int i = 0; i < (maxx - minx + 1); i++) {
                world[minx + i][maxy] = Tileset.FLOOR;
            }
            for (int i = 0; i < (maxy - miny + 1); i++) {
                world[maxx][miny + i]  = Tileset.FLOOR;
            }
        }

    }


}
