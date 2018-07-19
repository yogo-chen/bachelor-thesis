package bip;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Prayogo Cendra
 */
public class BIPFunction {

    private final HashMap<Variable, Double> variableCoefMap;

    public BIPFunction() {
        this.variableCoefMap = new HashMap<>();
    }

    public ArrayList<Variable> getVariableList() {
        return new ArrayList<>(variableCoefMap.keySet());
    }

    public void addVariable(Variable variable, double coefficient) {
        variableCoefMap.put(variable, coefficient);
    }

    public double getCoefficient(Variable variable) {
        return variableCoefMap.getOrDefault(variable, 0.0);
    }

    public double getResult(ArrayList<Variable> valueOneVariableList) {
        double result = 0.0;
        for (Variable variable : valueOneVariableList) {
            result += getCoefficient(variable);
        }
        return result;
    }
}
