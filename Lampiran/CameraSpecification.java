package model;

/**
 *
 * @author Prayogo Cendra
 */
public class CameraSpecification {

    private final Angle wideAngle;
    private final double range;

    public CameraSpecification(Angle wideAngle, double range) {
        this.wideAngle = wideAngle;
        this.range = range;
    }

    public Angle getWideAngle() {
        return wideAngle;
    }

    public double getRange() {
        return range;
    }

    @Override
    public String toString() {
        return "("
                + range
                + ", "
                + wideAngle
                + ")";
    }
}
