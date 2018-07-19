package bip;

import java.util.ArrayList;

/**
 *
 * @author Prayogo Cendra
 */
public class BIPFeasibilityCheckResult {

    private final BIPProblem bipProblem;
    private final ArrayList<Variable> valueOneVariableList;
    private final double objectiveFunctionValue;
    private final ArrayList<BIPConstraint> unsatisfiedConstraintList;
    private final boolean isFeasible;

    public BIPFeasibilityCheckResult(
            BIPProblem bipProblem,
            ArrayList<Variable> valueOneVariableList,
            double objectiveFunctionValue,
            ArrayList<BIPConstraint> unsatisfiedConstraintList) {
        this.bipProblem = bipProblem;
        this.valueOneVariableList = valueOneVariableList;
        this.objectiveFunctionValue = objectiveFunctionValue;
        this.unsatisfiedConstraintList = unsatisfiedConstraintList;
        this.isFeasible = unsatisfiedConstraintList.isEmpty();
    }

    public BIPProblem getBIPProblem() {
        return bipProblem;
    }

    public ArrayList<Variable> getValueOneVariableList() {
        return valueOneVariableList;
    }

    public double getObjectiveFunctionValue() {
        return objectiveFunctionValue;
    }

    public ArrayList<BIPConstraint> getUnsatisfiedConstraintList() {
        return unsatisfiedConstraintList;
    }

    public boolean isFeasible() {
        return isFeasible;
    }
}
