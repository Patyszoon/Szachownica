package org.example;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Knight {

    // plansza rozmiarow n x n
    public Set<Point> calculateAttack(int n, int knightX, int knightY, Set<Point> przeszkody, Set<Point> lustra){
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

                // sprawdz czy na tej pozycji jest lustro
                if (lustra != null && lustra.contains(nowaPozycja)) {
                    // jest lustro - kontynuuj ruch
                    nowaPozycja = calculateMirrorPosition(n, nowaPozycja.x, nowaPozycja.y, ruch[0], ruch[1], lustra);
                }else if(!przeszkody.contains(nowaPozycja)){
                    possibleMoves.add(nowaPozycja);
                }
            }else{ // jesli sie nie miesci, to odbicie
                Point odbiciePozycja = calculateBouncePosition(n, knightX, knightY, ruch[0], ruch[1]);
                //TODO: sprawdz czy po odbiciu trafilismy na lustro

                if(!przeszkody.contains(odbiciePozycja)){
                    possibleMoves.add(odbiciePozycja);
                }
            }
        }

        return possibleMoves;
    }

    // odbicie od planszy
    public Point calculateBouncePosition(int n, int knightX, int knightY, int dx, int dy){
        int newX = knightX + dx;
        int newY = knightY + dy;

//        if(newX < 0){
//            newX = -newX - 1;
//        }else if(newX >= n){
//            newX = 2*n - newX - 1;
//        }
//
//        if(newY < 0){
//            newY = -newY - 1;
//        }else if(newY >= n){
//            newY = 2*n - newY - 1;
//        }

        if (newX < 0 || newX >= n) {
            dx = -dx;  // odwroc kierunek w x
            newX = knightX + dx;
            // poki jest poza plansza - odbijaj ponownie
            while (newX < 0 || newX >= n) {
                dx = -dx;
                newX = knightX + dx;
            }
        }

        // odbiciedla y
        if (newY < 0 || newY >= n) {
            dy = -dy;
            newY = knightY + dy;
            while (newY < 0 || newY >= n) {
                dy = -dy;
                newY = knightY + dy;
            }
        }

        return new Point(newX, newY);

    }

    public Point calculateMirrorPosition(int n, int lustroX, int lustroY, int dx, int dy, Set<Point> lustra) {
        // wykonaj ten sam ruch z pozycji lustra
        int newX = lustroX + dx;
        int newY = lustroY + dy;

        Point nowaPozycja = new Point(newX, newY);
        // TODO: sprawdz czy nowa pozycja poza plansza, oraz czy nowa pozycja to lustro. petle?
        return nowaPozycja;
    }

    }