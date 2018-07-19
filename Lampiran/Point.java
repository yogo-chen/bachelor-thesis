package model;

import java.text.DecimalFormat;

/**
 *
 * @author Prayogo Cendra
 */
public class Point {

    private final double x, y;

    private static String twoDecimalPointFloatNumber(double number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceTo(Point point) {
        double horizontalDifference = point.getX() - x;
        double verticalDifference = point.getY() - y;
        return Math.sqrt(
                Math.pow(horizontalDifference, 2)
                + Math.pow(verticalDifference, 2)
        );
    }

    @Override
    public String toString() {
        return twoDecimalPointFloatNumber(x)
                + ", "
                + twoDecimalPointFloatNumber(y);
    }
}
