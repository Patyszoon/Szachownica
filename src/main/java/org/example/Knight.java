package org.example;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Knight {

    // plansza rozmiarów n x n
    public Set<Point> calculateAttack(int n, int knightX, int knightY, Set<Point> przeszkody){
        Set<Point> possibleMoves = new HashSet<>();
        //poziom to x, pion to y
        int[][] ruchy={{-2,-1}, {-2,1}, {-1, -2}, {-1, 2}, {1,-2,}, {1,2}, {2,-1}, {2,1}};


        return possibleMoves;
    }
}