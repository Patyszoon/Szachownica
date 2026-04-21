package org.example;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Knight {

    // plansza rozmiarow n x n
    public Set<Point> calculateAttack(int n, int knightX, int knightY, Set<Point> przeszkody, Set<Point> lustra){
        Set<Point> possibleMoves = new HashSet<>();

        //wszystkie mozliwe ruchy skoczka
        int[][] ruchy={{-2,-1}, {-2,1}, {-1, -2}, {-1, 2}, {1,-2}, {1,2}, {2,-1}, {2,1}};

        // sprawdzam kazdy mozliwy ruch
        for (int[] ruch : ruchy){
            int newX = knightX + ruch[0];
            int newY = knightY + ruch[1];

            // czy pole miesci sie na planszy
            if(newX >= 0 && newX < n && newY >= 0 && newY < n){
                Point nowaPozycja = new Point(newX, newY); // sprawdzana pozycja

                // sprawdz czy na tej pozycji jest lustro
                if (lustra != null && lustra.contains(nowaPozycja)) {
                    Set<Point> odwiedzone = new HashSet<>(); // odwiedzone pozycje luster

                    // jest lustro - kontynuuj ruch
                    Point pozycjaPoLustrze = calculateMirrorPosition(n, nowaPozycja.x, nowaPozycja.y, ruch[0], ruch[1], lustra, odwiedzone);
                    if(pozycjaPoLustrze != null && !przeszkody.contains(pozycjaPoLustrze)){
                        possibleMoves.add(pozycjaPoLustrze); // dodaj nowa pozycje po przeskoczeniu przez lustro

                    }
                }else if(!przeszkody.contains(nowaPozycja)){
                    possibleMoves.add(nowaPozycja);
                }
            }else{ // jesli sie nie miesci, to odbicie
                Point odbiciePozycja = calculateBouncePosition(n, knightX, knightY, ruch[0], ruch[1]);
                //sprawdz czy po odbiciu trafilismy na lustro
                if (lustra != null && lustra.contains(odbiciePozycja)) {
                    Set<Point> odwiedzone = new HashSet<>(); // odwiedzone pozycje luster

                    Point pozycjaPoLustrze = calculateMirrorPosition(n, odbiciePozycja.x, odbiciePozycja.y, ruch[0], ruch[1], lustra, odwiedzone);

                    if(pozycjaPoLustrze != null && !przeszkody.contains(pozycjaPoLustrze)){
                        possibleMoves.add(pozycjaPoLustrze); // dodaj nowa pozycje po przeskoczeniu przez lustro
                    }

                }else if(!przeszkody.contains(odbiciePozycja)){
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

    public Point calculateMirrorPosition(int n, int lustroX, int lustroY, int dx, int dy, Set<Point> lustra, Set<Point> odwiedzone) {
        odwiedzone.add(new Point(lustroX, lustroY)); // dodaje lustro, na ktore trafilismy do odwiedoznych

        // wykonaj ten sam ruch z pozycji lustra
        int newX = lustroX + dx;
        int newY = lustroY + dy;

        // czy miesci sie w planszy
        if(newX >= 0 && newX < n && newY >= 0 && newY < n){
            Point nowaPozycja = new Point(newX, newY);

            // czy trafilismy na lustro
            if (lustra != null && lustra.contains(nowaPozycja)) {
                if(odwiedzone.contains(nowaPozycja)){
                    return null; // skonczenie petli, jesli juz to lustro odwiedzilismy
                }

                return calculateMirrorPosition(n, nowaPozycja.x, nowaPozycja.y, dx, dy, lustra, odwiedzone);
            }

            return nowaPozycja;
        }else {
            Point odbiciePozycja = calculateBouncePosition(n, lustroX, lustroY, dx, dy);

            // czy trafilismy na lustro po odbiciu
            if (lustra != null && lustra.contains(odbiciePozycja)) {
                if (odwiedzone.contains(odbiciePozycja)) {
                    return null;
                }

                return calculateMirrorPosition(n, odbiciePozycja.x, odbiciePozycja.y, dx, dy, lustra, odwiedzone);
            }
            return odbiciePozycja;
        }
    }

}