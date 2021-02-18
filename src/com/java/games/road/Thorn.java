package com.java.games.road;

public class Thorn extends RoadObject{

    public Thorn(int x, int y){
        super(RoadObjectType.THORN, x , y);
        super.speed = 0;
    }
}
