package bip;

import java.util.ArrayList;

/**
 *
 * @author Prayogo Cendra
 */
public class BIPConstraint {

    private final BIPFunction leftHandSideFunction;
    private final double rightHandSideValue;

    public BIPConstraint(BIPFunction leftHandSideFunction,
            double rightHandSideValue) {
        this.leftHandSideFunction = leftHandSideFunction;
        this.rightHandSideValue = rightHandSideValue;
    }

    public BIPFunction getLeftHandSideFunction() {
        return leftHandSideFunction;
    }

    public double getRightHandSideValue() {
        return rightHandSideValue;
    }

    public boolean isSatisfied(ArrayList<Variable> valueOneVariableList) {
        return leftHandSideFunction.getResult(valueOneVariableList)
                >= rightHandSideValue;
    }
}
