package org.example;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Knight {

    // plansza rozmiarow n x n
    public Set<Point> calculateAttack(int n, int knightX, int knightY, Set<Point> przeszkody){
        Set<Point> possibleMoves = new HashSet<>();

        //wszystkie mozliwe ruchy skoczka
        int[][] ruchy={{-2,-1}, {-2,1}, {-1, -2}, {-1, 2}, {1,-2,}, {1,2}, {2,-1}, {2,1}};

        // sprawdzam kazdy mozliwy ruch
        for (int[] ruch : ruchy){
            int newX = knightX + ruch[0];
            int newY = knightY + ruch[1];

            // czy pole miesci sie na planszy
            if(newX >= 0 && newX < n && newY >= 0 && newY < n){
                Point nowaPozycja = new Point(newX, newY); // sprawdzana pozycja

                if(!przeszkody.contains(nowaPozycja)){
                    possibleMoves.add(nowaPozycja);
                }
            }else{ // jesli sie nie miesci, to odbicie
                calculateBouncePosition(n, knightX, knightY);
            }
        }

        return possibleMoves;
    }

    // odbicie od planszy
    public Point calculateBouncePosition(int n, int knightX, int knightY){
        Point bouncePosition = new Point();
        // obliczenie odbicia skoczka
        return bouncePosition;

    }
}