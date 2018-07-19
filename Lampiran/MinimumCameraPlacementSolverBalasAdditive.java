package model;

import bip.BIPConstraint;
import bip.BIPFunction;
import bip.BIPProblem;
import bip.balasadditive.BalasAdditiveBIPSolver;
import bip.Variable;
import bip.balasadditive.BalasAdditiveBIPSolverResult;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 *
 * @author Prayogo Cendra
 */
public class MinimumCameraPlacementSolverBalasAdditive extends MinimumCameraPlacementSolver {

    public MinimumCameraPlacementSolverBalasAdditive(Room room, int possibleOrientation) {
        super(room, possibleOrientation);
    }

    @Override
    public void solve() {
        Cell[] shouldBeCoveredCells = room.getShouldBeCoveredCell();
        Point[] preferedPlacingPoints = room.getPreferedPlacingPoints();
        Angle[] directionAngles = generateDirectionAngles();

        int preferedPlacingPointsCount = preferedPlacingPoints.length;
        int shouldBeCoveredCellsCount = shouldBeCoveredCells.length;
        int directionAnglesCount = directionAngles.length;

        HashMap<Cell, Integer> shouldBeCoveredCellIndexMap
                = new HashMap<>(shouldBeCoveredCellsCount);
        for (int i = 0; i < shouldBeCoveredCellsCount; i++) {
            shouldBeCoveredCellIndexMap.put(shouldBeCoveredCells[i], i);
        }

        HashMap<CameraPlacement, Cell[]> possiblePlacementsCoverageMap
                = new HashMap<>();
        ArrayList<CameraPlacement> possiblePlacementList = new ArrayList<>();

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
                    Cell[] correlatedCells
                            = new Cell[correlatedCellList.size()];
                    correlatedCellList.toArray(correlatedCells);
                    possiblePlacementsCoverageMap.put(placement,
                            correlatedCells);
                    possiblePlacementList.add(placement);
                }
            }
        }

        int possiblePlacementsCount = possiblePlacementList.size();
        System.out.println(possiblePlacementsCount + " possible placements");
        System.out.println(
                "Solution combination count: "
                + (new DecimalFormat("0.######E0", DecimalFormatSymbols
                        .getInstance(Locale.ROOT))).format(
                        (new BigInteger("2"))
                        .pow(possiblePlacementsCount)));
        System.out.println("Solving problem...");

        BIPFunction objectiveFunction = new BIPFunction();
        Variable[] variables = new Variable[possiblePlacementsCount];
        BIPFunction[] constraintLeftHandSideFunctions
                = new BIPFunction[shouldBeCoveredCellsCount];
        BIPConstraint[] constraints
                = new BIPConstraint[shouldBeCoveredCellsCount];
        BIPProblem bipProblem = new BIPProblem(objectiveFunction);
        BalasAdditiveBIPSolver solver = new BalasAdditiveBIPSolver(bipProblem);

        HashMap<Variable, CameraPlacement> variablePlacementMap
                = new HashMap<>();

        for (int i = 0; i < shouldBeCoveredCellsCount; i++) {
            constraintLeftHandSideFunctions[i] = new BIPFunction();
            constraints[i]
                    = new BIPConstraint(constraintLeftHandSideFunctions[i], 1);

            bipProblem.addConstraint(constraints[i]);
        }

        for (int i = 0; i < possiblePlacementsCount; i++) {
            CameraPlacement placement = possiblePlacementList.get(i);

            variables[i] = new Variable("x(" + placement.getPlacingPoint()
                    + ", " + placement.getDirectionAngle() + ")");
            objectiveFunction.addVariable(variables[i], 1);

            for (Cell cell : possiblePlacementsCoverageMap.get(placement)) {
                constraintLeftHandSideFunctions[shouldBeCoveredCellIndexMap
                        .get(cell)].addVariable(variables[i], 1);
            }

            variablePlacementMap.put(variables[i], placement);
        }

        BalasAdditiveBIPSolverResult result = solver.solve();
        for (Variable variable : result.getValueOneVariableList()) {
            room.addCameraPlacement(variablePlacementMap.get(variable));
        }
        System.out.println(result);
    }
}
