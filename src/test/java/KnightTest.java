import org.example.Knight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

public class KnightTest {

//    // executes before each test method in this class
//    @BeforeEach
//    void init() {
//
//    }

    // brak przeszkod, knight znajduje sie w centrum planszy, mozliwych attakow = 8
    @Test
    void knightHas8PossibleMoves(){
        Knight knight = new Knight();
        int n = 8;
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
        Knight knight = new Knight();
        int n = 8;
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
        Knight knight = new Knight();
        int n = 8;
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
        Knight knight = new Knight();
        int n = 8;
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

    // skoczek jest w lewym dolnym rogu, robi ruch {-1,-2}
    @Test
    void knightBounceInLowerLeftCorner1(){
        Knight knight = new Knight();
        int n = 8;
        int knightX = 0;
        int knightY = 0;
        Set<Point> przeszkody = new HashSet<>();

        Set<Point> result = knight.calculateAttack(n, knightX, knightY, przeszkody);
//
//        assertThat(result, hasSize(2)); // ma mozliwe tylko dwa ruchy
//        assertThat(result, hasItem(new Point()));
//        assertThat(result, hasItem(new Point()));
    }

    // skoczek jest w lewym dolnym rogu, robi ruch {-2,1}
    @Test
    void knightBounceInLowerLeftCorner2(){
        Knight knight = new Knight();
        int n = 8;
        int knightX = 0;
        int knightY = 0;
        Set<Point> przeszkody = new HashSet<>();


    }

    //skoczek na pozycji (7,3)
    @Test
    void knightBounceOnRightMiddle(){

    }

}
