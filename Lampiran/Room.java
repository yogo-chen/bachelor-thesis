package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Prayogo Cendra
 */
public class Room extends GridPoint {

    private final Dimension roomDimension;
    private final CameraSpecification cameraSpecification;
    private final Point[] preferedPlacingPoints;
    private final ArrayList<CameraPlacement> cameraPlacementList;
    private final HashMap<Cell, ArrayList<CameraPlacement>> coveringCameraMap;

    public static Room build(
            Dimension roomDimension,
            CameraSpecification cameraSpecification,
            double maximumCellSize
    ) {
        double roomWidth = roomDimension.getWidth();
        double roomLength = roomDimension.getLength();
        double minimumMargin = cameraSpecification.getRange();

        int innerRows = (int) Math.ceil(roomLength / maximumCellSize);
        int innerColumns = (int) Math.ceil(roomWidth / maximumCellSize);
        double cellWidth = roomWidth / innerColumns;
        double cellLength = roomLength / innerRows;
        int verticalMargins = (int) Math.ceil(minimumMargin / cellLength);
        int horizontalMargins = (int) Math.ceil(minimumMargin / cellWidth);
        Dimension cellDimension = new Dimension(cellWidth, cellLength);

        return new Room(
                roomDimension,
                cameraSpecification,
                innerRows,
                innerColumns,
                verticalMargins,
                horizontalMargins,
                cellDimension
        );
    }

    public Room(
            Dimension roomDimension,
            CameraSpecification cameraSpecification,
            int innerRows,
            int innerColumns,
            int verticalMargins,
            int horizontalMargins,
            Dimension cellDimension
    ) {
        super(
                innerRows,
                innerColumns,
                verticalMargins,
                horizontalMargins,
                cellDimension
        );

        this.roomDimension = roomDimension;
        this.cameraSpecification = cameraSpecification;
        this.cameraPlacementList = new ArrayList<>();

        double cellWidth = cellDimension.getWidth();
        double cellLength = cellDimension.getLength();

        this.preferedPlacingPoints
                = new Point[(innerRows + 1) * (innerColumns + 1)];
        for (int i = 0; i < innerRows + 1; i++) {
            for (int j = 0; j < innerColumns + 1; j++) {
                preferedPlacingPoints[i * (innerColumns + 1) + j]
                        = new Point(
                                j * cellWidth,
                                i * cellLength
                        );
            }
        }

        this.coveringCameraMap = new HashMap<>();
        for (Cell[] cells : this.cellsMatrix) {
            for (Cell cell : cells) {
                this.coveringCameraMap.put(cell, new ArrayList<>());
            }
        }
    }

    public Dimension getRoomDimension() {
        return roomDimension;
    }

    public CameraSpecification getCameraSpecification() {
        return cameraSpecification;
    }

    public Point[] getPreferedPlacingPoints() {
        return preferedPlacingPoints;
    }

    public CameraPlacement[] getCameraPlacements() {
        CameraPlacement[] cameraPlacements
                = new CameraPlacement[cameraPlacementList.size()];
        cameraPlacementList.toArray(cameraPlacements);
        return cameraPlacements;
    }

    public void addCameraPlacement(CameraPlacement placement) {
        cameraPlacementList.add(placement);
        for (Cell coveredCell : findCoverage(placement)) {
            coveringCameraMap.get(coveredCell).add(placement);
        }
    }

    public void removeCameraPlacement(CameraPlacement placement) {
        cameraPlacementList.remove(placement);
        for (Cell coveredCell : findCoverage(placement)) {
            coveringCameraMap.get(coveredCell).remove(placement);
        }
    }

    public Cell[] findCoverage(CameraPlacement placement) {
        ArrayList<Cell> coverageCellList = new ArrayList();

        Angle directionAngle = placement.getDirectionAngle();
        Angle halfAngle = cameraSpecification.getWideAngle().divide(2);
        Angle startAngle = directionAngle.subtract(halfAngle);
        Angle endAngle = directionAngle.add(halfAngle);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = cellsMatrix[i][j];
                Point centerPoint = cell.getCenterPoint();
                Angle rotationAngle = Angle.rotationAngle(centerPoint,
                        placement.getPlacingPoint());

                boolean isInsideRange
                        = placement.getPlacingPoint()
                        .distanceTo(centerPoint)
                        < cameraSpecification.getRange();
                boolean isInsideViewAngle = rotationAngle
                        .isBetween(startAngle, endAngle);

                if (isInsideRange && isInsideViewAngle) {
                    coverageCellList.add(cell);
                }
            }
        }

        Cell[] coverageCells = new Cell[coverageCellList.size()];
        coverageCellList.toArray(coverageCells);
        return coverageCells;
    }

    public boolean isCoveredCell(Cell cell) {
        return !coveringCameraMap.get(cell).isEmpty();
    }

    public Cell[] getCoveredCell() {
        ArrayList<Cell> coveredCellList = new ArrayList<>();
        for (Cell[] cells : cellsMatrix) {
            for (Cell cell : cells) {
                if (isCoveredCell(cell)) {
                    coveredCellList.add(cell);
                }
            }
        }
        Cell[] coveredCells = new Cell[coveredCellList.size()];
        coveredCellList.toArray(coveredCells);
        return coveredCells;
    }

    public Cell[] getInnerCoveredCell() {
        ArrayList<Cell> innerCoveredCellList = new ArrayList<>();
        for (Cell[] cells : cellsMatrix) {
            for (Cell cell : cells) {
                if (isInnerCell(cell) && isCoveredCell(cell)) {
                    innerCoveredCellList.add(cell);
                }
            }
        }
        Cell[] coveredCells = new Cell[innerCoveredCellList.size()];
        innerCoveredCellList.toArray(coveredCells);
        return coveredCells;
    }

    public boolean isShouldBeCoveredCell(Cell cell) {
        return isInnerCell(cell) && !isCoveredCell(cell);
    }

    public Cell[] getShouldBeCoveredCell() {
        ArrayList<Cell> shouldBeCoveredCellList = new ArrayList<>();
        for (Cell[] cells : cellsMatrix) {
            for (Cell cell : cells) {
                if (isShouldBeCoveredCell(cell)) {
                    shouldBeCoveredCellList.add(cell);
                }
            }
        }
        Cell[] shouldBeCoveredCells = new Cell[shouldBeCoveredCellList.size()];
        shouldBeCoveredCellList.toArray(shouldBeCoveredCells);
        return shouldBeCoveredCells;
    }

    public boolean isOverlapCell(Cell cell) {
        return coveringCameraMap.get(cell).size() > 1;
    }

    public double getOverlapOutOfBoundPercentage() {
        for (Cell innerCell : innerCellList) {
            if (!isCoveredCell(innerCell)) {
                return Double.NaN;
            }
        }
        int totalCoverageCell = 0;
        for (Cell[] cells : cellsMatrix) {
            for (Cell cell : cells) {
                totalCoverageCell += coveringCameraMap.get(cell).size();
            }
        }
        int totalInnerCell = innerCellList.size();
        return (1.0 * totalCoverageCell / totalInnerCell) - 1.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\n\nCAMERA LIST\n");
        Iterator cameraListIterator = cameraPlacementList.iterator();
        while (cameraListIterator.hasNext()) {
            sb.append(cameraListIterator.next());
            if (cameraListIterator.hasNext()) {
                sb.append("\n\n");
            }
        }
        return sb.toString();
    }
}
