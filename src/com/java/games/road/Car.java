package com.java.games.road;

public class Car extends RoadObject {
    public Car(RoadObjectType roadObjectType, int x, int y){
        super(roadObjectType, x, y);
        speed = 1;
    }
}
