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

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody);

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
        przeszkody.add(new Point(3, 1));
        przeszkody.add(new Point(3, 5));

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody);

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
        przeszkody.add(new Point(5, 0));
        przeszkody.add(new Point(3, 5));

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody);

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
        przeszkody.add(new Point(2,2));
        przeszkody.add(new Point(2,4));
        przeszkody.add(new Point(3,1));
        przeszkody.add(new Point(3,5));
        przeszkody.add(new Point(5,1));
        przeszkody.add(new Point(6,2));
        przeszkody.add(new Point(5,5));
        przeszkody.add(new Point(6,4));

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody);

        assertThat(result, hasSize(0)); // nie ma ruchow?
    }

    // skoczek pozcyja (7,7), wykonuje ruch {1,2}
    @Test
    void knightBounceTest1() {
        int knightX = 7;
        int knightY = 7;
        Set<Point> przeszkody = new HashSet<>();
        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody);

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
    void knightTest2(){
        int knightX = 1;
        int knightY = 1;
        Set<Point> przeszkody = new HashSet<>();

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody);

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
        przeszkody.add(new Point(6, 5)); // przeszkoda w miejscu odbicia
        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody);

        //  pole (6,5) NIE powinno byc w wynikach
        assertThat(result, not(hasItem(new Point(6, 5))));
        assertThat(result, hasItem(new Point(5, 6)));

        // wypisuje wsyzstkie ruchy
        System.out.println("Ruchy skoczka z (7,7) po odbiciach (z przeszkoda):");
        for (Point p : result) {
            System.out.println("  -> (" + p.x + ", " + p.y + ")");
        }
    }



}
