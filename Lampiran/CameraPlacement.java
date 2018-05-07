package model;

import java.util.Objects;

/**
 *
 * @author Prayogo Cendra
 */
public class CameraPlacement {

    private final Point placingPoint;
    private final Angle directionAngle;

    public CameraPlacement(Point placingPoint, Angle directionAngle) {
        this.placingPoint = placingPoint;
        this.directionAngle = directionAngle;
    }

    public Point getPlacingPoint() {
        return placingPoint;
    }

    public Angle getDirectionAngle() {
        return directionAngle;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.placingPoint);
        hash = 11 * hash + Objects.hashCode(this.directionAngle);
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
        final CameraPlacement other = (CameraPlacement) obj;
        if (!Objects.equals(this.placingPoint, other.placingPoint)) {
            return false;
        }
        if (!Objects.equals(this.directionAngle, other.directionAngle)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "("
                + placingPoint
                + " - "
                + directionAngle
                + ")";
    }
}
