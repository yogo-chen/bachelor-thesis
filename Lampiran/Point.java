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
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (Math.abs(this.x - other.x) > 0.000001) {
            return false;
        }
        if (Math.abs(this.y - other.y) > 0.000001) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "("
                + twoDecimalPointFloatNumber(x)
                + ", "
                + twoDecimalPointFloatNumber(y)
                + ")";
    }
}
