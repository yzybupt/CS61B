package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.lang.Math;



public class Hall_Way {
    public static int[][] hall_way_generator (Room r1, Room r2) {
        int[][] results = new int[3][2];

        int[] turning = new int[2];
        int[][] border = new int[4][2];
        int left_x = Math.min(r1.x,r2.x);
        int down_y = Math.min(r1.y,r2.y);
        int right_x = Math.max(r1.x + r1.width - 1, r2.x + r2.width - 1);
        int up_y = Math.max(r1.y + r1.height - 1, r2.y + r2.height - 1);
        border[0][0] = left_x;
        border[0][1] = down_y;

        border[1][0] = left_x;
        border[1][1] = up_y;

        border[2][0] = right_x;
        border[2][1] = down_y;

        border[3][0] = right_x;
        border[3][1] = up_y;


        int[][] vertices = new int[8][2];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if(i == 0) {
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
                if((border[i][0] == vertices[j][0]) && (border[i][1] == vertices[j][1])) {
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
            distances[j] = Hall_Way.distance(turning[0],turning[1],vertices[j][0],vertices[j][1]);
        }
        int temp = 0;
        for (int j = 0; j < 4; j++) {
            if(distances[j] < distances[temp]) {
                temp = j;
            }
        }
        connect1[0] = vertices[temp][0];
        connect1[1] = vertices[temp][1];

        for (int j = 4; j < 8; j++) {
            distances[j % 4] = Hall_Way.distance(turning[0],turning[1],vertices[j][0],vertices[j][1]);
        }
        temp = 4;
        for (int j = 4; j < 8; j++) {
            if(distances[j % 4] < distances[temp % 4]) {
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

    public static int distance (int x0, int y0, int x1,int y1) {
        return (x0 - x1) * (x0 -x1) + (y0 - y1) * (y0 - y1);
    }


    public static void draw_hall_way (TETile[][] world, int[][] points) {
        int flag = 4;//0 is low L
        int min_x = Math.min(points[1][0],points[2][0]);
        int max_y = Math.max(points[1][1],points[2][1]);
        int max_x = Math.max(points[1][0],points[2][0]);
        int min_y = Math.min(points[1][1],points[2][1]);
        if (points[0][0] == min_x && points[0][1] == max_y) {
            flag = 1;
        } else if (points[0][0] == max_x && points[0][1] == min_y) {
            flag = 0;
        } else if (points[0][0] == min_x && points[0][1] == min_y) {
            flag = 2;
        } else if (points[0][0] == max_x && points[0][1] == max_y) {
            flag = 3;
        }
        if (flag == 0) {
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                if(world[min_x + i][min_y] != Tileset.FLOOR) {
                    world[min_x + i][min_y] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                if(world[max_x][min_y + i] != Tileset.FLOOR) {
                    world[max_x][min_y + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 1){
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                if(world[min_x + i][max_y] != Tileset.FLOOR) {
                    world[min_x + i][max_y] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                if(world[min_x][min_y + i] != Tileset.FLOOR) {
                    world[min_x][min_y + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 2) {
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                if(world[min_x + i][min_y] != Tileset.FLOOR) {
                    world[min_x + i][min_y] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                if(world[min_x][min_y + i] != Tileset.FLOOR) {
                    world[min_x][min_y + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 3) {
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                if(world[min_x + i][max_y] != Tileset.FLOOR) {
                    world[min_x + i][max_y] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                if(world[max_x][min_y + i] != Tileset.FLOOR) {
                    world[max_x][min_y + i]  = Tileset.WALL;
                }
            }
        }

    }

    public static void draw_hall_way2 (TETile[][] world, int[][] points) {
        int flag = 4;//0 is low L
        int min_x = Math.min(points[1][0],points[2][0]);
        int max_y = Math.max(points[1][1],points[2][1]);
        int max_x = Math.max(points[1][0],points[2][0]);
        int min_y = Math.min(points[1][1],points[2][1]);
        if (points[0][0] == min_x && points[0][1] == max_y) {
            flag = 1;
        } else if (points[0][0] == max_x && points[0][1] == min_y) {
            flag = 0;
        } else if (points[0][0] == min_x && points[0][1] == min_y) {
            flag = 2;
        } else if (points[0][0] == max_x && points[0][1] == max_y) {
            flag = 3;
        }
        if (flag == 0) {
            min_x = min_x - 2;
            max_y = max_y + 2;
            max_x = max_x - 2;
            min_y = min_y + 2;
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                if(world[min_x + i][min_y] != Tileset.FLOOR) {
                    world[min_x + i][min_y] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                if(world[max_x][min_y + i] != Tileset.FLOOR) {
                    world[max_x][min_y + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 1){
            min_x = min_x + 2;
            max_y = max_y - 2;
            max_x = max_x + 2;
            min_y = min_y - 2;
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                if(world[min_x + i][max_y] != Tileset.FLOOR) {
                    world[min_x + i][max_y] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                if(world[min_x][min_y + i] != Tileset.FLOOR) {
                    world[min_x][min_y + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 2) {
            min_x = min_x + 2;
            max_y = max_y + 2;
            max_x = max_x + 2;
            min_y = min_y + 2;
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                if(world[min_x + i][min_y] != Tileset.FLOOR) {
                    world[min_x + i][min_y] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                if(world[min_x][min_y + i] != Tileset.FLOOR) {
                    world[min_x][min_y + i]  = Tileset.WALL;
                }
            }
        } else if (flag == 3) {
            min_x = min_x - 2;
            max_y = max_y - 2;
            max_x = max_x - 2;
            min_y = min_y - 2;
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                if(world[min_x + i][max_y] != Tileset.FLOOR) {
                    world[min_x + i][max_y] = Tileset.WALL;
                }
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                if(world[max_x][min_y + i] != Tileset.FLOOR) {
                    world[max_x][min_y + i]  = Tileset.WALL;
                }
            }
        }

    }

    public static void draw_hall_way3 (TETile[][] world, int[][] points) {
        int flag = 4;//0 is low L
        int min_x = Math.min(points[1][0],points[2][0]);
        int max_y = Math.max(points[1][1],points[2][1]);
        int max_x = Math.max(points[1][0],points[2][0]);
        int min_y = Math.min(points[1][1],points[2][1]);
        if (points[0][0] == min_x && points[0][1] == max_y) {
            flag = 1;
        } else if (points[0][0] == max_x && points[0][1] == min_y) {
            flag = 0;
        } else if (points[0][0] == min_x && points[0][1] == min_y) {
            flag = 2;
        } else if (points[0][0] == max_x && points[0][1] == max_y) {
            flag = 3;
        }
        if (flag == 0) {
            min_x = min_x - 1;
            max_y = max_y + 1;
            max_x = max_x - 1;
            min_y = min_y + 1;
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                world[min_x + i][min_y] = Tileset.FLOOR;
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                world[max_x][min_y + i]  = Tileset.FLOOR;
            }
        } else if (flag == 1){
            min_x = min_x + 1;
            max_y = max_y - 1;
            max_x = max_x + 1;
            min_y = min_y - 1;
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                world[min_x + i][max_y] = Tileset.FLOOR;
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                world[min_x][min_y + i]  = Tileset.FLOOR;
            }
        } else if (flag == 2) {
            min_x = min_x + 1;
            max_y = max_y + 1;
            max_x = max_x + 1;
            min_y = min_y + 1;
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                world[min_x + i][min_y] = Tileset.FLOOR;
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                world[min_x][min_y + i]  = Tileset.FLOOR;
            }
        } else if (flag == 3) {
            min_x = min_x - 1;
            max_y = max_y - 1;
            max_x = max_x - 1;
            min_y = min_y - 1;
            for (int i = 0; i < (max_x - min_x + 1); i++) {
                world[min_x + i][max_y] = Tileset.FLOOR;
            }
            for (int i = 0; i < (max_y - min_y + 1); i++) {
                world[max_x][min_y + i]  = Tileset.FLOOR;
            }
        }

    }


}
