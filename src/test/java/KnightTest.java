import org.example.Knight;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

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
    private int knightX, knightY;
    private Set<Point> result;


    @AfterEach
    void printPoints(TestInfo testInfo) {
        System.out.println("\n[" + testInfo.getDisplayName() + "]");

        if (result == null) {
            System.out.println("Brak wyników jako Set (result = null)");
            return;
        }
        System.out.println("Ruchy skoczka z (" + knightX + "," + knightY + "):");
        result.forEach(p ->
                System.out.println("  -> (" + p.x + ", " + p.y + ")")
        );
    }
//    // executes before each test method in this class
//    @BeforeEach
//    void init() {
//
//    }

    // brak przeszkod, knight znajduje sie w centrum planszy, mozliwych attakow = 8
    @Test
    void knightHas8PossibleMoves(){
        knightX = 4; knightY = 3;
        Set<Point> przeszkody = new HashSet<>();// pusty set
        Set<Point> lustra = new HashSet<>();// pusty set

        result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

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
        knightX = 4; knightY = 3;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();// pusty set
        przeszkody.add(new Point(3, 1));
        przeszkody.add(new Point(3, 5));

        result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

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
        knightX = 7; knightY = 0;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();// pusty set
        przeszkody.add(new Point(5, 0));
        przeszkody.add(new Point(3, 5));

        result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        assertThat(result, hasSize(2)); // czy sa mozliwe 2 ruchy
        assertThat(result, hasItem(new Point(5,1)));
        assertThat(result, hasItem(new Point(6,2)));
    }

    // skoczek nie ma mozliwych ruchow, wszedzie przeszkody
    @Test
    void knightHasNoMoves(){
        knightX = 4; knightY = 3;
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

        result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        assertThat(result, hasSize(0)); // nie ma ruchow?
    }

    // skoczek przeskakuje przeszkody, pole pomiedzy nim a celem nie blokuje ruchu
    @Test
    void jumpOverObstacles() {
        knightX = 4; knightY = 4;
        Set<Point> przeszkody = new HashSet<>();
        przeszkody.add(new Point(3, 4));
        przeszkody.add(new Point(4, 3));

        result = knight.calculateAttack(8, knightX, knightY, przeszkody, null);

        assertThat(result, hasSize(8));
    }

    // TESTY ODBIC

    // skoczek pozcyja (7,7), wykonuje ruch {1,2}
    @Test
    void knightBounceTest1() {
        knightX = 7; knightY = 7;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();// pusty set
        result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        //  pole (6,5) powinno byc w wynikach
        assertThat(result, hasItem(new Point(6, 5)));
        assertThat(result, hasItem(new Point(5, 6)));
    }

    @Test
    void knightBounceTest2(){
        knightX = 1; knightY = 1;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();// pusty set

        result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        // wsord wynikow powinien byc (0,3)
        assertThat(result, hasItem(new Point(0, 3)));
    }

    //testuje konkretna funkcje calculateBouncePosition
    @Test
    void knightBounceTest1_calculateBouncePositionTest(){
        knightX = 7; knightY = 7;
        result = null;
        Point resultPoint = knight.calculateBouncePosition(n, knightX, knightY, 1, 2);
        assertThat(resultPoint, equalTo(new Point(6, 5)));
    }

    // z (0,1) ruch {-2,-1} - wiele odbic
    @Test
    void knightBounceTest3() {
        knightX = 0; knightY = 1;

        result = null;

        Point resultPoint = knight.calculateBouncePosition(n, knightX, knightY, -2, -1);

        // oczekiwany wynik
        // newX = -2 -> odbicie: dx=2, newX=2
        // newY = 0
        assertThat(resultPoint, equalTo(new Point(2,0)));
    }

    @Test
    void knightBounceTest1WithObstacle() {
        knightX = 7; knightY = 7;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();// pusty set
        przeszkody.add(new Point(6, 5)); // przeszkoda w miejscu odbicia
        result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        //  pole (6,5) NIE powinno byc w wynikach
        assertThat(result, not(hasItem(new Point(6, 5))));
        assertThat(result, hasItem(new Point(5, 6)));
    }

    // TESTY LUSTER

    // sprawdzenie obliczenia pozycji skoczka po przeskoku przez lustro
    @Test
    void oneMirrorTestNoObstacles(){
        // skoczek (2,2), lustro na (4,3), ruch {2,1}:
        // (2,2)+(2,1)=(4,3) lustro -> (4,3)+(2,1)=(6,4)
        knightX = 2; knightY = 2;
        Set<Point> lustra = new HashSet<>();
        lustra.add(new Point(4, 3));

        result = knight.calculateAttack(n, knightX, knightY, new HashSet<>(), lustra);

        assertThat(result, hasItem(new Point(6, 4)));       // wynik po lustrze
        assertThat(result, not(hasItem(new Point(4, 3)))); // lustro nie jest polem ataku
        assertThat(result, hasItem(new Point(3, 4)));       // ruch {1,2}
        assertThat(result, hasItem(new Point(4, 1)));       // ruch {2,-1}
    }

    // pozycja skoczka po przeskokou przez lustro nachodzi na przeszkode
    @Test
    void oneMirrorTestWithObstacles(){
        knightX = 2; knightY = 2;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> lustra = new HashSet<>();
        // lustro na polu (4,3), na drodze ruchu {2,1}
        lustra.add(new Point(4, 3));
        przeszkody.add(new Point(6,4));

        result = knight.calculateAttack(n, knightX, knightY, przeszkody, lustra);

        // (2,2) + (2,1) = (4,3) -> lustro -> odbicie (4,3)+(2,1) = (6,4)
        assertThat(result, not(hasItem(new Point(6, 4))));

        // pole z lustrem NIE powinno byc atakowane
        assertThat(result, not(hasItem(new Point(4, 3))));

        // inne ruchy normalnie
        assertThat(result, hasItem(new Point(3, 4))); // ruch {1,2}
        assertThat(result, hasItem(new Point(4, 1))); // ruch {2,-1}
    }

    // pozycja po przeskoku przez lustro jeset poza plansza
    @Test
    void oneMirrorTestAndKnightOutOfBounds(){
        // skoczek (4,4), lustro na (6,5), ruch {2,1}
        // (4,4)+(2,1)=(6,5) lustro -> (6,5)+(2,1)=(8,6) poza plansze -> odbicie -> (6,6)
        knightX = 4; knightY = 4;
        Set<Point> lustra = new HashSet<>();
        lustra.add(new Point(6, 5));

        result = knight.calculateAttack(n, knightX, knightY, new HashSet<>(), lustra);

        assertThat(result, hasItem(new Point(4, 6)));       // wynik po lustrze i odbiciu
        assertThat(result, not(hasItem(new Point(6, 5)))); // lustro nie jest polem ataku
    }

    // test z dwoma lustrami - po przeskoku przez 1. lustro skoczek trafia na lustro nr. 2
    @Test
    void twoMirrorsTest(){
        knightX = 0; knightY = 0;
        Set<Point> lustra = new HashSet<>();

        lustra.add(new Point(2, 1));
        lustra.add(new Point(4, 2));
        result = knight.calculateAttack(8, knightX, knightY, new HashSet<>(), lustra);
        assertThat(result, hasItem(new Point(6, 3)));
        assertThat(result, not(hasItem(new Point(2, 1))));
        assertThat(result, not(hasItem(new Point(4, 2))));
    }

    // test z petla z luster, 5 luster, skoczek zayczna na (0,0) i wykonuje ruch {1,2}
    // (1,2) → (2,4) → odbicie → (3,2) → (4,4) → odbicie → (5,2) → odbicie → (4,4) PETLA
    @Test
    void infiniteLoopMirrorsTestBoardIs6x6(){
        // n=6, skoczek (0,0), ruch {1,2}:
        // (1,2) lustro -> (2,4) lustro -> odbicie -> (3,2) lustro
        //   -> (4,4) lustro -> odbicie -> (5,2) lustro -> odbicie -> (4,4) *** PETLA ***
        // ruch {1,2} powinien zostac pominiery (zwraca null)
        knightX = 0; knightY = 0;

        int boardSize = 6;
        Set<Point> lustra = new HashSet<>();
        lustra.add(new Point(1, 2)); // L1
        lustra.add(new Point(2, 4)); // L2
        lustra.add(new Point(3, 2)); // L3
        lustra.add(new Point(4, 4)); // L4 - tu powstaje petla
        lustra.add(new Point(5, 2)); // L5

        result = knight.calculateAttack(boardSize, knightX, knightY, new HashSet<>(), lustra);
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
        knightX = 0; knightY = 0;
        int boardSize = 6;
        Set<Point> lustra = new HashSet<>();
        lustra.add(new Point(2, 1));
        lustra.add(new Point(4, 2));
        lustra.add(new Point(2, 3));
        result = knight.calculateAttack(boardSize, knightX, knightY, new HashSet<>(), lustra);
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
    }

    // plansza 4x4, skoczek w rogu (0,0)
    @Test
    void boardIs4x4KnightInCorner(){
        knightX = 0; knightY = 0;
        int boardSize = 4;

        result = knight.calculateAttack(boardSize, knightX, knightY, new HashSet<>(), new HashSet<>());
        assertThat(result, hasSize(2));
        assertThat(result, hasItem(new Point(2, 1)));
        assertThat(result, hasItem(new Point(1, 2)));
    }

    // plansza 11x11, skoczek w centrum (5,5), jedna przeszkoda w miejscu skoku
    @Test
    void boardIs11x11KnightInCenterWithObstacle(){
        knightX = 5; knightY = 5;
        int boardSize = 11;
        Set<Point> przeszkody = new HashSet<>();
        przeszkody.add(new Point(3, 6));
        result = knight.calculateAttack(boardSize, knightX, knightY, przeszkody, new HashSet<>());

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
        knightX = 0; knightY = 0;
        int boardSize = 11;
        result = knight.calculateAttack(boardSize, knightX, knightY, new HashSet<>(), new HashSet<>());

        assertThat(result, hasSize(2));
        assertThat(result, hasItem(new Point(2, 1)));
        assertThat(result, hasItem(new Point(1, 2)));
    }

    // plansza 1x1
    @Test
    void boardIs1x1KnightHasNoMoves(){
        knightX = 0; knightY = 0;
        result = knight.calculateAttack(1, knightX, knightY, new HashSet<>(), new HashSet<>());
        assertThat(result, hasSize(0));
    }

    // plansza 2x2
    @Test
    void boardIs2x2KnightHasNoMoves(){
        knightX = 0; knightY = 0;
        result = knight.calculateAttack(2, knightX, knightY, new HashSet<>(), new HashSet<>());
        assertThat(result, hasSize(0));
    }


    // TESTY WYJATKOW

    @Test
    void shouldThrowWhenBoardSizeIsZero() {
        result = null;
        assertThrows(IllegalArgumentException.class, () ->
                knight.calculateAttack(0, 0, 0, new HashSet<>(), null)
        );
    }

    @Test
    void shouldThrowWhenBoardSizeIsNegative() {
        result = null;
        assertThrows(IllegalArgumentException.class, () ->
                knight.calculateAttack(-3, 0, 0, new HashSet<>(), null)
        );
    }

    @Test
    void shouldThrowWhenKnightYIsOutOfBoard() {
        result = null;
        assertThrows(IllegalArgumentException.class, () ->
                knight.calculateAttack(8, 0, 8, new HashSet<>(), null)
        );
    }

    @Test
    void shouldThrowWhenKnightOutOfBounds(){
        assertThrows(IllegalArgumentException.class, () ->
                knight.calculateAttack(8, 8, 8, new HashSet<>(), null)
        );
    }

}
