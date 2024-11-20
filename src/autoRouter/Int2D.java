package autoRouter;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Comparator;
import java.util.Objects;

import static java.lang.Math.abs;

/// Datatype for a 2D integer vector. Implements x and y axis-specific comparators. Is equal if its parameters x,y are equal.
public record Int2D(int x, int y) implements Comparable<Int2D>{
    public static final Int2D zero = new Int2D(0,0);
    public static int manhattan(Int2D from, Int2D to){
        return abs(to.x - from.x) + abs(to.y - from.y);
    }

    public Int2D plus(Int2D v){
        return new Int2D(x + v.x(), y + v.y());
    }
//    public Int2D xPlus (int x){
//        return new Int2D(this.x() + x, this.y());
//    }
//    public Int2D yPlus (int y){
//        return new Int2D(this.x(), this.y() + y);
//    }

    public Int2D minus(Int2D v){
        return new Int2D( x - v.x(), y - v.y());
    }


    /// Return the unit vector pointing to q
    Int2D to(Int2D q){
        return new Int2D(Integer.signum(q.minus(this).x()),
                Integer.signum(q.minus(this).y));
    }
//
//    boolean greater(Int2D v){
//        return this.compareTo(v) > 0;
//    }
//    boolean less(Int2D v){
//        return this.compareTo(v) < 0;
//    }

    public String toString(){
        return "("+ x + " " + y + ") ";
    }


    @Override
    public int compareTo(Int2D that) {
        if(x == that.x && y == that.y)
            return 0;
        else
            return Int2D.manhattan(this, that);
        /*this.manhattan() - that.manhattan()*/
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Int2D int2D)) return false;
        return x == int2D.x && y == int2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    static Comparator<Int2D> byX = (u, v) -> (u.x() - v.x());
    static Comparator<Int2D> byY = (u, v) -> (u.y() - v.y());

    // index for a flattened grid array
    int toIndex(Int2D v){
        return (v.x - SquareGrid.border) + SquareGrid.squares * v.y;
    }
    public static void main(String[] args) {

        Int2D a = new Int2D(1,1);
        Int2D b = new Int2D(3, 1);
        Int2D c = new Int2D(1,3);
        Int2D d = new Int2D(1, 3);
        Int2D e = new Int2D( 4, 4);
        assertEquals(Int2D.manhattan(a, b), Int2D.manhattan(a, c));
//        System.out.println(a.compareTo(b));
//        System.out.println(a.compareTo(c));
        assertEquals(c.compareTo(d) == 0, c.equals(d));
        assertEquals( 6, a.compareTo(e) );

    }



}
