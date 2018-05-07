package model;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Prayogo Cendra
 */
public class MinimumCameraPlacementSolverGurobi extends MinimumCameraPlacementSolver {

    public MinimumCameraPlacementSolverGurobi(Room room) {
        super(room);
    }

    @Override
    public void solve() {
        boolean showTimeLapse = true;
        try {
            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);

            Cell[] shouldBeCoveredCells = room.getShouldBeCoveredCell();
            Point[] preferedPlacingPoints = room.getPreferedPlacingPoints();
            Angle[] directionAngles = generateDirectionAngles();

            int preferedPlacingPointsCount = preferedPlacingPoints.length;
            int shouldBeCoveredCellsCount = shouldBeCoveredCells.length;
            int directionAnglesCount = directionAngles.length;
            int posiblePlacementsCount = preferedPlacingPointsCount * directionAnglesCount;

            CameraPlacement[] posiblePlacements = new CameraPlacement[posiblePlacementsCount];
            Cell[][] coveredCellsOfPlacements = new Cell[posiblePlacementsCount][];

            long findingAllCoverageStartTime = System.currentTimeMillis();
            for (int i = 0; i < preferedPlacingPointsCount; i++) {
                for (int j = 0; j < directionAnglesCount; j++) {
                    int flattenIdx = (i * directionAnglesCount) + j;
                    Point placingPoint = preferedPlacingPoints[i];
                    Angle directionAngle = directionAngles[j];
                    CameraPlacement placement = new CameraPlacement(placingPoint, directionAngle);
                    posiblePlacements[flattenIdx] = placement;

                    long findingSingleCoverageStartTime = System.currentTimeMillis();

                    coveredCellsOfPlacements[flattenIdx] = room.findCoverage(placement);

                    long findingSingleCoverageEndTime = System.currentTimeMillis();
                    long currentSingleCoverageTime = findingSingleCoverageEndTime - findingSingleCoverageStartTime;
                    long sinceStartTime = findingSingleCoverageEndTime - findingAllCoverageStartTime;

                    double averageFindingSingleCoverageTime = 1.0 * sinceStartTime / (flattenIdx + 1);

                    if (showTimeLapse) {
                        System.out.println(
                                "Finding coverage "
                                + (flattenIdx + 1)
                                + "/"
                                + posiblePlacementsCount
                                + " - "
                                + currentSingleCoverageTime
                                + "ms - "
                                + sinceStartTime
                                + "ms avg. "
                                + averageFindingSingleCoverageTime + "ms");
                    }
                }
            }

            HashMap<Cell, Integer> shouldBeCoveredCellIndexesMap = new HashMap<>(shouldBeCoveredCellsCount);
            for (int i = 0; i < shouldBeCoveredCellsCount; i++) {
                shouldBeCoveredCellIndexesMap.put(shouldBeCoveredCells[i], i);
            }

            GRBVar[] vars = new GRBVar[posiblePlacementsCount];
            GRBLinExpr obj = new GRBLinExpr();
            GRBLinExpr[] constrs = new GRBLinExpr[shouldBeCoveredCellsCount];
            for (int i = 0; i < shouldBeCoveredCellsCount; i++) {
                constrs[i] = new GRBLinExpr();
            }

            long buildingLPStartTime = System.currentTimeMillis();
            for (int i = 0; i < posiblePlacementsCount; i++) {
                long buildingLPSingleStartTime = System.currentTimeMillis();

                vars[i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, posiblePlacements[i].toString());
                obj.addTerm(1.0, vars[i]);
                for (Cell cell : coveredCellsOfPlacements[i]) {
                    if (shouldBeCoveredCellIndexesMap.containsKey(cell)) {
                        constrs[shouldBeCoveredCellIndexesMap.get(cell)].addTerm(1.0, vars[i]);
                    }
                }

                long buildingLPSingleEndTime = System.currentTimeMillis();
                long currentbuildingLPSingleTime = buildingLPSingleEndTime - buildingLPSingleStartTime;
                long sinceStartTime = buildingLPSingleEndTime - buildingLPStartTime;

                double averageBuildingLPSingleTime = 1.0 * sinceStartTime / (i + 1);

                if (showTimeLapse) {
                    System.out.println(
                            "Building LP "
                            + (i + 1)
                            + "/"
                            + posiblePlacementsCount
                            + " - "
                            + currentbuildingLPSingleTime
                            + "ms - "
                            + sinceStartTime
                            + "ms avg. "
                            + averageBuildingLPSingleTime + "ms");
                }
            }
            for (int i = 0; i < shouldBeCoveredCellsCount; i++) {
                model.addConstr(constrs[i], GRB.GREATER_EQUAL, 1.0, shouldBeCoveredCells[i].toString());
            }
            model.setObjective(obj, GRB.MINIMIZE);

//            model.set(GRB.DoubleParam.MIPGap, 0.25); // range 0..1
//            model.set(GRB.DoubleParam.TimeLimit, 600); // seconds
            model.optimize();

            for (int i = 0; i < posiblePlacementsCount; i++) {
                if ((int) vars[i].get(GRB.DoubleAttr.X) == 1) {
                    room.addCameraPlacement(posiblePlacements[i]);
                }
            }

            model.dispose();
            env.dispose();
        } catch (GRBException ex) {
            Logger.getLogger(MinimumCameraPlacementSolverGurobi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
