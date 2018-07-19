package model;

/**
 *
 * @author Prayogo Cendra
 */
public class Angle {

    private static final double PI = Math.PI;
    private final double radians;

    // coordinate system (flipped y)
    //     -y
    //      ^
    //      |
    //-x <- + -> +x
    //      |
    //      v
    //     +y
    public static Angle rotationAngle(Point point, Point center) {
        //if normal y
//        double radians = Math.atan2( 
//                point.getY() - center.getY(),
//                point.getX() - center.getX()
//        );
        //if flipped y
        double radians = Math.atan2(
                center.getY() - point.getY(),
                point.getX() - center.getX()
        );
        Angle angle = new Angle(radians, true);
        return angle;
    }

    private static double normalizeAngle(double radians) {
        radians %= 2 * PI;
        if (radians < 0) {
            radians += 2 * PI;
        }
        return radians;
    }

    private static double radToDeg(double radians) {
        int decimalPrecision = 3;
        return (double) Math.round(
                radians * 180 / PI * Math.pow(10, decimalPrecision)
        ) / Math.pow(10, decimalPrecision);
    }

    private static double degToRad(double degress) {
        return degress / 180 * PI;
    }

    private static Angle newAngle(double radians) {
        return new Angle(radians, true);
    }

    public Angle(double degrees) {
        this(degrees, false);
    }

    public Angle(double angle, boolean isRadian) {
        if (!isRadian) {
            angle = degToRad(angle);
        }
        this.radians = normalizeAngle(angle);
    }

    public double deg() {
        return radToDeg(radians);
    }

    public double rad() {
        return radians;
    }

    public Angle add(Angle angle) {
        return newAngle(radians + angle.radians);
    }

    public Angle subtract(Angle angle) {
        return newAngle(radians - angle.radians);
    }

    public Angle divide(int divisor) {
        return newAngle(radians / divisor);
    }

    public Angle multiply(int multiplicator) {
        return newAngle(radians * multiplicator);
    }

    public boolean isBetween(Angle start, Angle end) {
        if (start.rad() < end.rad()) {
            return start.rad() < radians && radians < end.rad();
        } else {
            return start.rad() < radians || radians < end.rad();
        }
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
        final Angle other = (Angle) obj;
        if (Math.abs(this.radians - other.radians) > 0.000001) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(deg());
        sb.append("°");
        return sb.toString();
    }
}
