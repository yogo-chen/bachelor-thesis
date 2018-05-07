package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;

/**
 *
 * @author Prayogo Cendra
 */
public class MinimumCameraPlacementSolverLPSolve extends MinimumCameraPlacementSolver {

    public MinimumCameraPlacementSolverLPSolve(Room room) {
        super(room);
    }

    @Override
    public void solve() {
        Cell[] shouldBeCoveredCells = room.getShouldBeCoveredCell();
        Point[] preferedPlacingPoints = room.getPreferedPlacingPoints();
        Angle[] directionAngles = generateDirectionAngles();

        int preferedPlacingPointsCount = preferedPlacingPoints.length;
        int shouldBeCoveredCellsCount = shouldBeCoveredCells.length;
        int directionAnglesCount = directionAngles.length;

        HashMap<Cell, Integer> shouldBeCoveredCellIndexMap = new HashMap<>(shouldBeCoveredCellsCount);
        for (int i = 0; i < shouldBeCoveredCellsCount; i++) {
            shouldBeCoveredCellIndexMap.put(shouldBeCoveredCells[i], i);
        }

        HashMap<CameraPlacement, Cell[]> possiblePlacementsCoverageMap = new HashMap<>();
        ArrayList<CameraPlacement> possiblePlacementList = new ArrayList<>();

        System.out.println("Finding coverage...");
        for (int i = 0; i < preferedPlacingPointsCount; i++) {
            for (int j = 0; j < directionAnglesCount; j++) {
                Point placingPoint = preferedPlacingPoints[i];
                Angle directionAngle = directionAngles[j];
                CameraPlacement placement
                        = new CameraPlacement(placingPoint, directionAngle);
                Cell[] coveredCells = room.findCoverage(placement);
                ArrayList<Cell> correlatedCellList = new ArrayList<>();
                for (Cell cell : coveredCells) {
                    if (shouldBeCoveredCellIndexMap.containsKey(cell)) {
                        correlatedCellList.add(cell);
                    }
                }
                if (correlatedCellList.size() > 0) {
                    Cell[] correlatedCells = new Cell[correlatedCellList.size()];
                    correlatedCellList.toArray(correlatedCells);
                    possiblePlacementsCoverageMap.put(placement, correlatedCells);
                    possiblePlacementList.add(placement);
                }
            }
        }

        int possiblePlacementsCount = possiblePlacementList.size();
        System.out.println(possiblePlacementsCount + " possible placements");

        /**
         * *******************************************************************
         */
        double[][] lpConstraints = new double[shouldBeCoveredCellsCount][possiblePlacementsCount];
        double[] lpConstraintsRhs = new double[shouldBeCoveredCellsCount];
        double[] lpObjective = new double[possiblePlacementsCount];

        for (int i = 0; i < possiblePlacementsCount; i++) {
            lpObjective[i] = 1;
            CameraPlacement placement = possiblePlacementList.get(i);
            for (Cell cell : possiblePlacementsCoverageMap.get(placement)) {
                lpConstraints[shouldBeCoveredCellIndexMap.get(cell)][i] = 1;
            }
        }

        for (int i = 0; i < shouldBeCoveredCellsCount; i++) {
            lpConstraintsRhs[i] = 1;
        }

        try {
            LpSolve lp = LpSolve.makeLp(shouldBeCoveredCellsCount, possiblePlacementsCount);

            System.out.println("Building linear programming...");

            for (int i = 0; i < possiblePlacementsCount; i++) {
                lp.setObj(i + 1, lpObjective[i]);
                lp.setBinary(i + 1, true);
            }
            for (int i = 0; i < shouldBeCoveredCellsCount; i++) {
                lp.setRowName(i + 1, shouldBeCoveredCells[i].toString());
                for (int j = 0; j < possiblePlacementsCount; j++) {
                    lp.setMat(i + 1, j + 1, lpConstraints[i][j]);
                }
                lp.setConstrType(i + 1, LpSolve.GE);
                lp.setRh(i + 1, lpConstraintsRhs[i]);
            }

//            lp.printLp();
//            lp.setTimeout(120); //seconds
            System.out.println("Solving linear programming...");
            lp.solve();
            System.out.println("Done!");

            double[] results = lp.getPtrVariables();
            for (int i = 0; i < results.length; i++) {
                if ((int) results[i] == 1) {
                    room.addCameraPlacement(possiblePlacementList.get(i));
                }
            }

            lp.deleteLp();
        } catch (LpSolveException ex) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
