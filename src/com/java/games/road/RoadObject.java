package com.java.games.road;

import com.java.games.GameObject;
import com.java.games.ShapeMatrix;

import java.util.List;

public class RoadObject extends GameObject {
    public RoadObjectType type;
    public int speed;

    public RoadObject(RoadObjectType type, int x, int y){
        super(x,y);
        this.type = type;
        this.matrix = getMatrix(type);
        this.width = matrix[0].length;
        this.height = matrix.length;
    }

    /**
     * The method that is responsible for the movement of obstacles.
     * The obstacle can have its own speed and an additional one, which depends on the speed of the player's movement.
     */
    public void move(int boost, List<RoadObject> roadObjectList){
        this.y +=  boost;
    }

    /**
     *  Checks the current object and the object that came as a parameter for the intersection of their images,
     *  taking into account the distance.
     *  For example, if the number 12 is passed as distance,
     *  and 2 objects are located at a distance less than 12 cells of the playing field,
     *  the method returns true. Otherwise, it returns false.
     */
    public boolean isCollisionWithDistance(RoadObject roadObject, int distance) {
        if ((x - distance > roadObject.x + roadObject.width) || (x + width + distance < roadObject.x)) {
            return false;
        }

        if ((y - distance > roadObject.y + roadObject.height) || (y + height + distance < roadObject.y)) {
            return false;
        }

        return true;
    }

    private static int[][] getMatrix(RoadObjectType type) {
        switch (type) {
            case CAR:
                return ShapeMatrix.PASSENGER_CAR;
            case BUS:
                return ShapeMatrix.BUS;
            case SPORT_CAR:
                return ShapeMatrix.SPORT_CAR;
            case THORN:
                return ShapeMatrix.THORN;
            case DRUNK_CAR:
                return ShapeMatrix.DRUNK_CAR;
            default:
                return ShapeMatrix.TRUCK;
        }
    }
    // returns object height
    public static int getHeight(RoadObjectType type) {
        return getMatrix(type).length;
    }
}
