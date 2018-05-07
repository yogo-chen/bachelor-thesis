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
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.width) ^ (Double.doubleToLongBits(this.width) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.length) ^ (Double.doubleToLongBits(this.length) >>> 32));
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
        final Dimension other = (Dimension) obj;
        if (Math.abs(this.width - other.width) > 0.000001) {
            return false;
        }
        if (Math.abs(this.length - other.length) > 0.000001) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return width + "x" + length;
    }
}
