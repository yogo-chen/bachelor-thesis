package bip.balasadditive;

import bip.BIPProblem;
import bip.Variable;
import java.util.ArrayList;

/**
 *
 * @author Prayogo Cendra
 */
public class BalasAdditiveBIPSolverResult {

    private final BIPProblem problem;
    private final ArrayList<Variable> valueOneVariableList;
    private final ArrayList<Node> enumeratedNodeList;
    private final long solvingTime;

    public BalasAdditiveBIPSolverResult(
            BIPProblem problem,
            ArrayList<Variable> valueOneVariableList,
            ArrayList<Node> enumeratedNodeList,
            long solvingTime) {
        this.problem = problem;
        this.valueOneVariableList = valueOneVariableList;
        this.enumeratedNodeList = enumeratedNodeList;
        this.solvingTime = solvingTime;
    }

    public BIPProblem getProblem() {
        return problem;
    }

    public ArrayList<Variable> getValueOneVariableList() {
        return valueOneVariableList;
    }

    public ArrayList<Node> getEnumeratedNodeList() {
        return enumeratedNodeList;
    }

    public long getSolvingTime() {
        return solvingTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Balas additive BIP problem solver result:\n");
        sb.append("Variables with value 1 (");
        sb.append(valueOneVariableList.size());
        sb.append("): ");
        for (Variable variable : valueOneVariableList) {
            sb.append(variable);
            sb.append(", ");
        }
        sb.append("\n");
        sb.append("Total enumerated nodes: ");
        sb.append(enumeratedNodeList.size());
        sb.append(" nodes\n");
        sb.append("Solving time: ");
        sb.append(solvingTime);
        sb.append(" ms");
        return sb.toString();
    }
}
