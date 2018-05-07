package model;

import java.util.Objects;

/**
 *
 * @author Prayogo Cendra
 */
public class Cell {

    private final Dimension dimension;
    private final Point topLeftCornerPoint, centerPoint;

    public Cell(
            Dimension dimension,
            Point topLeftCornerPoint,
            Point centerPoint
    ) {
        this.dimension = dimension;
        this.topLeftCornerPoint = topLeftCornerPoint;
        this.centerPoint = centerPoint;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Point getTopLeftCornerPoint() {
        return topLeftCornerPoint;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.dimension);
        hash = 47 * hash + Objects.hashCode(this.topLeftCornerPoint);
        hash = 47 * hash + Objects.hashCode(this.centerPoint);
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
        final Cell other = (Cell) obj;
        if (!Objects.equals(this.dimension, other.dimension)) {
            return false;
        }
        if (!Objects.equals(this.topLeftCornerPoint, other.topLeftCornerPoint)) {
            return false;
        }
        if (!Objects.equals(this.centerPoint, other.centerPoint)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Cell (" + centerPoint + ")";
    }
}
