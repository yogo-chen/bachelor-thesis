package model;

/**
 *
 * @author Prayogo Cendra
 */
public class Dimension {

    private final double width, length;

    public Dimension(double width, double length) {
        this.width = width;
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }

    @Override
    public String toString() {
        return width + "x" + length;
    }
}
