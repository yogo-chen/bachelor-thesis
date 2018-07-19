package model;

/**
 *
 * @author Prayogo Cendra
 */
public abstract class MinimumCameraPlacementSolver {

    protected final Room room;
    private final int possibleOrientation;

    public MinimumCameraPlacementSolver(Room room, int possibleOrientation) {
        this.room = room;
        this.possibleOrientation = possibleOrientation;
    }

    public abstract void solve();

    protected Angle[] generateDirectionAngles() {
        Angle[] directionAngles = new Angle[possibleOrientation];
        for (int i = 0; i < possibleOrientation; i++) {
            directionAngles[i] = new Angle(i * 360.0 / possibleOrientation);
        }
        return directionAngles;
    }
}
