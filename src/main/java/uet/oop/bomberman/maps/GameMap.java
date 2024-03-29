package uet.oop.bomberman.maps;

import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.entities.background.Brick;
import uet.oop.bomberman.entities.entities.background.Floor;
import uet.oop.bomberman.entities.entities.background.Wall;
import uet.oop.bomberman.entities.functional.Functional;
import uet.oop.bomberman.graphics.Sprite;

import java.util.HashMap;
import java.util.Map;

public class GameMap {

    //assign
    public static final int BURN = -1;
    public static final int WALL = 0;
    public static final int FLOOR = 1;
    public static final int BRICK = 2;


    public static Map<Character, Integer> map = new HashMap<Character, Integer>();

    public static char[] walls = {'2', '!', '#', '@', '(', '&', ';', '=', '|', '$', '%', '*', '~'};
    public static char[] floors = {'0', '9', '8', '7', 'v', 'b', 'm', 'n', 'a'
            , 'q', 'w', 's', 'z', 'c', 'e', 'i', 'o', 'h', 'j', 'k', 'l', '.'};

    public static char[] bricks = {'+', 'X', '1', '?', 'x'};

    public GameMap() {
        this.init();
    }

    private void init() {
        for (char x : walls) {
            map.put(x, WALL);
        }

        for (char x : floors) {
            map.put(x, FLOOR);
        }

        for (char x : bricks) {
            map.put(x, BRICK);
        }
    }

    /**
     * Trả về loại tile mà kí tự mô tả
     */
    public static int get(char x) {
        return map.get(x);
    }

    public static Entity getTile(char x, int i, int j) {
        switch (x) {

            /* ************ Middle Scene ************ */
            case '0' -> {
                return new Floor(j, i, Sprite.floor.getFxImage());
            }
            case '7' -> {
                return new Floor(j, i, Sprite.floor2.getFxImage());
            }
            case '8' -> {
                return new Floor(j, i, Sprite.floor3.getFxImage());
            }
            case '9' -> {
                return new Floor(j, i, Sprite.floor4.getFxImage());
            }
            case '2' -> {
                return new Wall(j, i, Sprite.wall.getFxImage());
            }
            case '!' -> {
                return new Wall(j, i, Sprite.wall2.getFxImage());
            }
            case '3' -> {
                return new Functional(j, i, Sprite.speed.getFxImage());
            }
            case '1' -> {
                return new Brick(j, i, Sprite.brick.getFxImage());
            }

            /* ************ Ice Scene ************ */
            case '#' -> {
                return new Wall(j, i, Sprite.iceWall.getFxImage());
            }
            case '@' -> {
                return new Wall(j, i, Sprite.iceWall2.getFxImage());
            }
            case '(' -> {
                return new Wall(j, i, Sprite.iceWall3.getFxImage());
            }
            case '^' -> {
                return new Wall(j, i, Sprite.iceTop.getFxImage());
            }
            case 'v' -> {
                return new Floor(j, i, Sprite.iceFloor.getFxImage());
            }
            case 'b' -> {
                return new Floor(j, i, Sprite.iceFloor2.getFxImage());
            }
            case 'm' -> {
                return new Floor(j, i, Sprite.iceFloor3.getFxImage());
            }
            case 'n' -> {
                return new Floor(j, i, Sprite.iceFloor4.getFxImage());
            }
            case '+' -> {
                return new Brick(j, i, Sprite.iceBrick.getFxImage());
            }

            /* ************ Tomb Scene ************ */
            case '&' -> {
                return new Wall(j, i, Sprite.tombWall.getFxImage());
            }
            case ';' -> {
                return new Wall(j, i, Sprite.tombWall2.getFxImage());
            }
            case '*' -> {
                return new Wall(j, i, Sprite.tombWall3.getFxImage());
            }
            case 'h' -> {
                return new Floor(j, i, Sprite.tombFloor.getFxImage());
            }
            case 'j' -> {
                return new Floor(j, i, Sprite.tombFloor2.getFxImage());
            }
            case 'k' -> {
                return new Floor(j, i, Sprite.tombFloor3.getFxImage());
            }
            case 'l' -> {
                return new Floor(j, i, Sprite.tombFloor4.getFxImage());
            }
            case '.' -> {
                return new Floor(j, i, Sprite.space.getFxImage());
            }
            case '~' -> {
                return new Wall(j, i, Sprite.space.getFxImage());
            }
            case '?' -> {
                return new Brick(j, i, Sprite.tombBrick.getFxImage());
            }

            /* ************ Spring Scene ************ */
            case '=' -> {
                return new Wall(j, i, Sprite.springWall.getFxImage());
            }
            case '|' -> {
                return new Wall(j, i, Sprite.springWall2.getFxImage());
            }
            case 'a' -> {
                return new Floor(j, i, Sprite.springFloor.getFxImage());
            }
            case 'q' -> {
                return new Floor(j, i, Sprite.springFloor2.getFxImage());
            }
            case 'w' -> {
                return new Floor(j, i, Sprite.springFloor3.getFxImage());
            }
            case 's' -> {
                return new Floor(j, i, Sprite.springFloor4.getFxImage());
            }
            case 'z' -> {
                return new Floor(j, i, Sprite.springFloor5.getFxImage());
            }
            case 'X' -> {
                return new Brick(j, i, Sprite.springBrick.getFxImage());
            }

            /* ************ Castle Scene ************ */
            case '$' -> {
                return new Wall(j, i, Sprite.castleWall.getFxImage());
            }
            case '%' -> {
                return new Wall(j, i, Sprite.castleWall2.getFxImage());
            }
            case 'c' -> {
                return new Floor(j, i, Sprite.castleFloor.getFxImage());
            }
            case 'e' -> {
                return new Floor(j, i, Sprite.castleFloor2.getFxImage());
            }
            case 'i' -> {
                return new Floor(j, i, Sprite.castleFloor3.getFxImage());
            }
            case 'o' -> {
                return new Floor(j, i, Sprite.castleFloor4.getFxImage());
            }
            case 'x' -> {
                return new Brick(j, i, Sprite.castleBrick.getFxImage());
            }
        }

        return new Floor(j, i, Sprite.space.getFxImage());
    }
}