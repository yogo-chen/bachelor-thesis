package model;

/**
 *
 * @author Prayogo Cendra
 */
public abstract class MinimumCameraPlacementSolver {

    protected final Room room;

    public MinimumCameraPlacementSolver(Room room) {
        this.room = room;
    }

    public abstract void solve();

    protected Angle[] generateDirectionAngles() {
        // type unique coverage
//        int testAngleCount = 360; // every 1 deg
//        Point testPlacingPoint = room.getPreferedPlacingPoints()[0];
//        Angle[] testAngles = new Angle[testAngleCount];
//        Cell[][] testCoveredCellss = new Cell[testAngleCount][];
//        for (int i = 0; i < testAngleCount; i++) {
//            testAngles[i] = new Angle(i * 360 / testAngleCount);
//            testCoveredCellss[i] = room.findCoverage(
//                    new CameraPlacement(testPlacingPoint, testAngles[i])
//            );
//        }
//        boolean isFirstFound = false;
//        int startIdx = 1;
//        int endIdx = 0;
//        int currIdx = 0;
//        Cell[] startCells = testCoveredCellss[0];
//        ArrayList<Integer> idxList = new ArrayList<>();
//        while (true) {
//            boolean check = true;
//            Cell[] currCells = testCoveredCellss[currIdx];
//            if (startCells.length != currCells.length) {
//                check = false;
//            } else if (!new HashSet<>(Arrays.asList(startCells))
//                    .equals(new HashSet<>(Arrays.asList(currCells)))) {
//                check = false;
//            }
//            if (check) {
//                endIdx = currIdx;
//            } else {
//                if (isFirstFound) {
//                    int midIdx = (startIdx + endIdx) / 2;
//                    if (!idxList.contains(midIdx)) {
////                        System.out.println(startIdx + " " + midIdx + " " + endIdx);
//                        idxList.add(midIdx);
//                    } else {
//                        break;
//                    }
//                } else {
//                    isFirstFound = true;
//                }
//                startIdx = currIdx;
//                endIdx = currIdx;
//                startCells = testCoveredCellss[startIdx];
//            }
//            currIdx = (currIdx + 1) % testAngleCount;
//        }
//        int anglesCount = idxList.size();
//        Angle[] directionAngles = new Angle[anglesCount];
//        for (int i = 0; i < idxList.size(); i++) {
//            directionAngles[i] = testAngles[idxList.get(i)];
//            System.out.println(directionAngles[i]);
//        }

//        type-1
//        double cameraWideAngle = specification.getWideAngle().deg();
//        int angleVariations = (int) Math.ceil(360.0 / cameraWideAngle);
//        double angleRotation = 360.0 / angleVariations;
//        double angleOffset = specification.getWideAngle().deg() / 2;
//        Angle[] directionAngles = new Angle[angleVariations];
//
//        for (int i = 0; i < angleVariations; i++) {
//            directionAngles[i] = new Angle(i * angleRotation + angleOffset);
//        }
//        type-2
        int anglesCount = 72;
        Angle[] directionAngles = new Angle[anglesCount];
        for (int i = 0; i < anglesCount; i++) {
            directionAngles[i] = new Angle(i * (360 / anglesCount));
        }
//        type-3
//        Angle[] directionAngles = new Angle[16];
//        double cameraWideAngle = cameraSpecification.getWideAngle().deg();
//        for (int i = 0; i < 4; i++) {
//            directionAngles[i * 4] = new Angle(i * 90);
//            directionAngles[(i * 4) + 1]
//                    = new Angle((cameraWideAngle / 2) + (i * 90));
//            directionAngles[(i * 4) + 2] = new Angle((i * 90) + 45);
//            directionAngles[(i * 4) + 3]
//                    = new Angle(((i + 1) * 90) - (cameraWideAngle / 2));
//        }
        return directionAngles;
    }
}
