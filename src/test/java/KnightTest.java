import org.example.Knight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KnightTest {
    private final Knight knight = new Knight();
    private final int n = 8;

//    // executes before each test method in this class
//    @BeforeEach
//    void init() {
//
//    }

    // brak przeszkod, knight znajduje sie w centrum planszy, mozliwych attakow = 8
    @Test
    void knightHas8PossibleMoves(){
        int knightX = 4;
        int knightY = 3;
        Set<Point> przeszkody = new HashSet<>();// pusty set
        Set<Point> lustra = new HashSet<>();// pusty set

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        assertThat(result, hasSize(8)); // czy sa mozliwe 8 ruchow
        assertThat(result, hasItem(new Point(2,2)));
        assertThat(result, hasItem(new Point(2,4)));
        assertThat(result, hasItem(new Point(3,1)));
        assertThat(result, hasItem(new Point(3,5)));
        assertThat(result, hasItem(new Point(5,1)));
        assertThat(result, hasItem(new Point(6,2)));
        assertThat(result, hasItem(new Point(5,5)));
        assertThat(result, hasItem(new Point(6,4)));
    }

    // skoczek w centrum, sa 2 przeszkody
    @Test
    void knightInCenterWith2Obstacles(){
        int knightX = 4;
        int knightY = 3;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();// pusty set
        przeszkody.add(new Point(3, 1));
        przeszkody.add(new Point(3, 5));

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        assertThat(result, hasSize(6)); // czy sa mozliwe 6 ruchow
        assertThat(result, hasItem(new Point(2,2)));
        assertThat(result, hasItem(new Point(2,4)));
        assertThat(result, hasItem(new Point(5,1)));
        assertThat(result, hasItem(new Point(6,2)));
        assertThat(result, hasItem(new Point(5,5)));
        assertThat(result, hasItem(new Point(6,4)));
    }

    // skoczek w prawym dolnym rogu planszy
    @Test
    void knightInCornerWith2Moves(){
        int knightX = 7;
        int knightY = 0;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();// pusty set
        przeszkody.add(new Point(5, 0));
        przeszkody.add(new Point(3, 5));

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        assertThat(result, hasSize(2)); // czy sa mozliwe 2 ruchy
        assertThat(result, hasItem(new Point(5,1)));
        assertThat(result, hasItem(new Point(6,2)));
    }

    // skoczek nie ma mozliwych ruchow, wszedzie przeszkody
    @Test
    void knightHasNoMoves(){
        int knightX = 4;
        int knightY = 3;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();// pusty set
        przeszkody.add(new Point(2,2));
        przeszkody.add(new Point(2,4));
        przeszkody.add(new Point(3,1));
        przeszkody.add(new Point(3,5));
        przeszkody.add(new Point(5,1));
        przeszkody.add(new Point(6,2));
        przeszkody.add(new Point(5,5));
        przeszkody.add(new Point(6,4));

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        assertThat(result, hasSize(0)); // nie ma ruchow?
    }

    // skoczek przeskakuje przeszkody, pole pomiedzy nim a celem nie blokuje ruchu
    @Test
    void jumpOverObstacles() {
        Set<Point> przeszkody = new HashSet<>();
        przeszkody.add(new Point(3, 4));
        przeszkody.add(new Point(4, 3));

        Set<Point> moves = knight.calculateAttack(8, 4, 4, przeszkody, null);

        assertThat(moves, hasSize(8));
    }

    // TESTY ODBIC

    // skoczek pozcyja (7,7), wykonuje ruch {1,2}
    @Test
    void knightBounceTest1() {
        int knightX = 7;
        int knightY = 7;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();// pusty set
        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        //  pole (6,5) powinno byc w wynikach
        assertThat(result, hasItem(new Point(6, 5)));
        assertThat(result, hasItem(new Point(5, 6)));

        // wypisuje wsyzstkie ruchy
        System.out.println("Ruchy skoczka z (7,7) po odbiciach:");
        for (Point p : result) {
            System.out.println("  -> (" + p.x + ", " + p.y + ")");
        }
    }

    @Test
    void knightBounceTest2(){
        int knightX = 1;
        int knightY = 1;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();// pusty set

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        // wsord wynikow powinien byc (0,3)
        assertThat(result, hasItem(new Point(0, 3)));

        System.out.println("Ruchy skoczka z (1,1) po odbiciach:");
        for (Point p : result) {
            System.out.println("  -> (" + p.x + ", " + p.y + ")");
        }
    }

    //testuje konkretna funkcje calculateBouncePosition
    @Test
    void knightBounceTest1_calculateBouncePositionTest(){
        Point result = knight.calculateBouncePosition(n, 7, 7, 1, 2);
        assertThat(result, equalTo(new Point(6, 5)));
    }

    // z (0,1) ruch {-2,-1} - wiele odbic
    @Test
    void knightBounceTest3() {
        Point result = knight.calculateBouncePosition(n, 0, 1, -2, -1);

        // oczekiwany wynik
        // newX = -2 -> odbicie: dx=2, newX=2
        // newY = 0
        assertThat(result, equalTo(new Point(2,0)));
    }

    @Test
    void knightBounceTest1WithObstacle() {
        int knightX = 7;
        int knightY = 7;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();// pusty set
        przeszkody.add(new Point(6, 5)); // przeszkoda w miejscu odbicia
        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        //  pole (6,5) NIE powinno byc w wynikach
        assertThat(result, not(hasItem(new Point(6, 5))));
        assertThat(result, hasItem(new Point(5, 6)));

        // wypisuje wsyzstkie ruchy
        System.out.println("Ruchy skoczka z (7,7) po odbiciach (z przeszkoda):");
        for (Point p : result) {
            System.out.println("  -> (" + p.x + ", " + p.y + ")");
        }
    }

    // TESTY LUSTER

    // sprawdzenie obliczenia pozycji skoczka po przeskoku przez lustro
    @Test
    void oneMirrorTestNoObstacles(){
        // skoczek (2,2), lustro na (4,3), ruch {2,1}:
        // (2,2)+(2,1)=(4,3) lustro -> (4,3)+(2,1)=(6,4)
        int knightX = 2;
        int knightY = 2;
        Set<Point> lustra = new HashSet<>();
        lustra.add(new Point(4, 3));

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, new HashSet<>(), lustra);

        assertThat(result, hasItem(new Point(6, 4)));       // wynik po lustrze
        assertThat(result, not(hasItem(new Point(4, 3)))); // lustro nie jest polem ataku
        assertThat(result, hasItem(new Point(3, 4)));       // ruch {1,2}
        assertThat(result, hasItem(new Point(4, 1)));       // ruch {2,-1}
    }

    // pozycja skoczka po przeskokou przez lustro nachodzi na przeszkode
    @Test
    void oneMirrorTestWithObstacles(){
        int knightX = 2;
        int knightY = 2;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();
        // lustro na polu (4,3), na drodze ruchu {2,1}
        lustra.add(new Point(4, 3));
        przeszkody.add(new Point(6,4));

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        // (2,2) + (2,1) = (4,3) -> lustro -> odbicie (4,3)+(2,1) = (6,4)
        assertThat(result, not(hasItem(new Point(6, 4))));

        // pole z lustrem NIE powinno byc atakowane
        assertThat(result, not(hasItem(new Point(4, 3))));

        // inne ruchy normalnie
        assertThat(result, hasItem(new Point(3, 4))); // ruch {1,2}
        assertThat(result, hasItem(new Point(4, 1))); // ruch {2,-1}

        System.out.println("Ruchy skoczka z (2,2) z lustrem na (4,3) i przeszkodą na (6,4):");
        for (Point p : result) {
            System.out.println("  -> (" + p.x + ", " + p.y + ")");
        }
    }

    // pozycja po przeskoku przez lustro jeset poza plansza
    @Test
    void oneMirrorTestAndKnightOutOfBounds(){
        // skoczek (4,4), lustro na (6,5), ruch {2,1}
        // (4,4)+(2,1)=(6,5) lustro -> (6,5)+(2,1)=(8,6) poza plansze -> odbicie -> (6,6)
        Set<Point> lustra = new HashSet<>();
        lustra.add(new Point(6, 5));

        Set<Point> result = knight.calculateAttack(n, 4, 4, new HashSet<>(), lustra);

        assertThat(result, hasItem(new Point(4, 6)));       // wynik po lustrze i odbiciu
        assertThat(result, not(hasItem(new Point(6, 5)))); // lustro nie jest polem ataku

        System.out.println("Ruchy skoczka z (4,4), lustro i odbicie:");
        for (Point p : result) {
            System.out.println("  -> (" + p.x + ", " + p.y + ")");
        }
    }

    // test z dwoma lustrami - po przeskoku przez 1. lustro skoczek trafia na lustro nr. 2
    @Test
    void twoMirrorsTest(){
        Set<Point> lustra = new HashSet<>();

        lustra.add(new Point(2, 1));
        lustra.add(new Point(4, 2));
        Set<Point> result = knight.calculateAttack(8, 0, 0, new HashSet<>(), lustra);
        assertThat(result, hasItem(new Point(6, 3)));
        assertThat(result, not(hasItem(new Point(2, 1))));
        assertThat(result, not(hasItem(new Point(4, 2))));

    }

    // test z petla z luster
    // (1,2) → (2,4) → odbicie → (3,2) → (4,4) → odbicie → (5,2) → odbicie → (4,4) *** PĘTLA ***
    @Test
    void infiniteLoopMirrorsTestBoardIs6x6(){
        // n=6, skoczek (0,0), ruch {1,2}:
        // (1,2) lustro -> (2,4) lustro -> odbicie -> (3,2) lustro
        //   -> (4,4) lustro -> odbicie -> (5,2) lustro -> odbicie -> (4,4) *** PETLA ***
        // ruch {1,2} powinien zostac pominiery (zwraca null)

        int boardSize = 6;
        Set<Point> lustra = new HashSet<>();
        lustra.add(new Point(1, 2)); // L1
        lustra.add(new Point(2, 4)); // L2
        lustra.add(new Point(3, 2)); // L3
        lustra.add(new Point(4, 4)); // L4 - tu powstaje petla
        lustra.add(new Point(5, 2)); // L5

        Set<Point> result = knight.calculateAttack(boardSize, 0, 0, new HashSet<>(), lustra);
        // zadne lustro nie jest polem ataku
        assertThat(result, not(hasItem(new Point(1, 2))));
        assertThat(result, not(hasItem(new Point(2, 4))));
        assertThat(result, not(hasItem(new Point(3, 2))));
        assertThat(result, not(hasItem(new Point(4, 4))));
        assertThat(result, not(hasItem(new Point(5, 2))));
        // pozostale ruchy sa normalne
        assertThat(result, hasItem(new Point(2, 1)));
        assertThat(result, hasItem(new Point(2, 0)));
    }

    // TEST ROZNE WYMIARY PLANSZY

    //test z trzema lustrami i plansza 6x6, skoczek przechodzi przez dwa lustra -> wychodzi poza plancze -> kolejne lustro -> (4,4)
    @Test
    void threeMirrorsTestBoardIs6x6(){
        // n=6, skoczek (0,0), ruch {2,1}:
        // (2,1) lustro -> (4,2) lustro -> poza plansze -> odbicie -> (2,3) lustro -> (0,2) lustro -> (4,4)
        int boardSize = 6;
        Set<Point> lustra = new HashSet<>();
        lustra.add(new Point(2, 1));
        lustra.add(new Point(4, 2));
        lustra.add(new Point(2, 3));
        Set<Point> result = knight.calculateAttack(boardSize, 0, 0, new HashSet<>(), lustra);
        assertThat(result, notNullValue());
        // zadne lustro nie jest polem ataku
        assertThat(result, not(hasItem(new Point(2, 1))));
        assertThat(result, not(hasItem(new Point(4, 2))));
        assertThat(result, not(hasItem(new Point(2, 3))));
        // pozostale ruchy
        assertThat(result, hasItem(new Point(1, 2)));
        assertThat(result, hasItem(new Point(4, 0)));
        assertThat(result, hasItem(new Point(4,4)));
        assertThat(result, hasItem(new Point(0, 0))); // moze wrocic?

        System.out.println("Ruchy skoczka z (0,0) z 3 lustrami i odbiciem, plansza 6x6:");
        for (Point p : result) {
            System.out.println("  -> (" + p.x + ", " + p.y + ")");
        }

    }

    // plansza 4x4, skoczek w rogu (0,0)
    @Test
    void boardIs4x4KnightInCorner(){
        int boardSize = 4;

        Set<Point> result = knight.calculateAttack(boardSize, 0, 0, new HashSet<>(), new HashSet<>());
        assertThat(result, hasSize(2));
        assertThat(result, hasItem(new Point(2, 1)));
        assertThat(result, hasItem(new Point(1, 2)));
    }

    // plansza 11x11, skoczek w centrum (5,5), jedna przeszkoda w miejscu skoku
    @Test
    void boardIs11x11KnightInCenterWithObstacle(){
        int boardSize = 11;
        Set<Point> przeszkody = new HashSet<>();
        przeszkody.add(new Point(3, 6));
        Set<Point> result = knight.calculateAttack(boardSize, 5, 5, przeszkody, new HashSet<>());

        assertThat(result, hasSize(7));
        assertThat(result, hasItem(new Point(3, 4)));
        assertThat(result, not(hasItem(new Point(3, 6)))); // tutaj przeszkoda
        assertThat(result, hasItem(new Point(4, 3)));
        assertThat(result, hasItem(new Point(4, 7)));
        assertThat(result, hasItem(new Point(6, 3)));
        assertThat(result, hasItem(new Point(6, 7)));
        assertThat(result, hasItem(new Point(7, 4)));
        assertThat(result, hasItem(new Point(7, 6)));
    }

    // plansza 11x11, skoczek w rogu (0,0)
    @Test
    void boardIs11x11KnightInCorner(){
        int boardSize = 11;
        Set<Point> result = knight.calculateAttack(boardSize, 0, 0, new HashSet<>(), new HashSet<>());

        assertThat(result, hasSize(2));
        assertThat(result, hasItem(new Point(2, 1)));
        assertThat(result, hasItem(new Point(1, 2)));
    }

    // plansza 1x1
    @Test
    void boardIs1x1KnightHasNoMoves(){
        Set<Point> result = knight.calculateAttack(1, 0, 0, new HashSet<>(), new HashSet<>());
        assertThat(result, hasSize(0));
    }

    // plansza 2x2
    @Test
    void boardIs2x2KnightHasNoMoves(){
        Set<Point> result = knight.calculateAttack(2, 0, 0, new HashSet<>(), new HashSet<>());
        assertThat(result, hasSize(0));
    }


    // TESTY WYJATKOW

    @Test
    void shouldThrowWhenBoardSizeIsZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                knight.calculateAttack(0, 0, 0, new HashSet<>(), null)
        );
    }

    @Test
    void shouldThrowWhenBoardSizeIsNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                knight.calculateAttack(-3, 0, 0, new HashSet<>(), null)
        );
    }

    @Test
    void shouldThrowWhenKnightYIsOutOfBoard() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                knight.calculateAttack(8, 0, 8, new HashSet<>(), null)
        );
    }

    @Test
    void shouldThrowWhenKnightOutOfBounds(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                knight.calculateAttack(8, 8, 8, new HashSet<>(), null)
        );
    }




}
