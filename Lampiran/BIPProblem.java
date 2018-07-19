package bip;

import java.util.ArrayList;

/**
 *
 * @author Prayogo Cendra
 */
public class BIPProblem {

    private final BIPFunction objectiveFunction;
    private final ArrayList<BIPConstraint> constraintList;

    public BIPProblem(BIPFunction objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
        this.constraintList = new ArrayList<>();
    }

    public void addConstraint(BIPConstraint constraint) {
        constraintList.add(constraint);
    }

    public ArrayList<BIPConstraint> getConstraintList() {
        return constraintList;
    }

    public BIPFunction getObjectiveFunction() {
        return objectiveFunction;
    }

    public BIPFeasibilityCheckResult checkFeasible(
            ArrayList<Variable> valueOneVariableList) {
        ArrayList<BIPConstraint> unsatisfiedConstraintList = new ArrayList<>();
        double objectiveFunctionValue = getObjectiveFunction()
                .getResult(valueOneVariableList);

        for (int i = 0; i < constraintList.size(); i++) {
            BIPConstraint constraint = constraintList.get(i);
            if (!constraint.isSatisfied(valueOneVariableList)) {
                unsatisfiedConstraintList.add(constraint);
            }
        }

        return new BIPFeasibilityCheckResult(
                this,
                valueOneVariableList,
                objectiveFunctionValue,
                unsatisfiedConstraintList);
    }
}
