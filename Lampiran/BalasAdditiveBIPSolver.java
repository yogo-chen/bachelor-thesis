package bip.balasadditive;

import bip.Variable;
import bip.BIPConstraint;
import bip.BIPFunction;
import bip.BIPProblem;
import bip.BIPFeasibilityCheckResult;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Prayogo Cendra
 */
public class BalasAdditiveBIPSolver {

    private final BIPProblem bipProblem;
    private ArrayList<Variable> sortedVariables;

    public BalasAdditiveBIPSolver(BIPProblem bipProblem) {
        this.bipProblem = bipProblem;
    }

    public BalasAdditiveBIPSolverResult solve() {
        ArrayList<Variable> incumbentValueOneVariableList = null;
        double incumbentObjectiveFunctionValue = Double.MAX_VALUE;
        ArrayDeque<Node> nodeStack = new ArrayDeque<>();
        ArrayList<Node> enumeratedNodeList = new ArrayList<>();
        long startSolvingTime = System.currentTimeMillis();

        sortVariablesByCoefficient();

        Node rootNode = new Node(null, null, false);
        nodeStack.push(rootNode);

        while (!nodeStack.isEmpty()) {
            Node node = nodeStack.pop();

            enumeratedNodeList.add(node);

            ArrayList<Variable> valueOneVariableList
                    = node.valueOneVariableList();

            if (isBoundValueWorseThanCurrentIncumbent(node,
                    incumbentObjectiveFunctionValue)) {
                node.setStatus(NodeStatus.FATHOMED_BOUND);
            } else if (!node.isZero()) {
                BIPFeasibilityCheckResult checkResult
                        = bipProblem.checkFeasible(valueOneVariableList);
                if (checkResult.isFeasible()) {
                    node.setStatus(NodeStatus.FATHOMED_FEASIBLE);
                    if (isBetterThanCurrentIncumbent(valueOneVariableList,
                            incumbentObjectiveFunctionValue)) {
                        incumbentObjectiveFunctionValue
                                = checkResult.getObjectiveFunctionValue();
                        incumbentValueOneVariableList = valueOneVariableList;
                    }
                } else {
                    node.setStatus(NodeStatus.INFEASIBLE);
                }
            } else {
                node.setStatus(NodeStatus.INFEASIBLE);
            }

            if (node.getStatus() == NodeStatus.INFEASIBLE) {
                if (isImpossibleToContinue(node)) {
                    node.setStatus(NodeStatus.FATHOMED_IMPOSSIBLE);
                } else {
                    Node[] branchNodes = branch(node);
                    if (branchNodes[0] != null && branchNodes[1] != null) {
                        if (branchNodes[0].getBoundValue()
                                < branchNodes[1].getBoundValue()) {
                            nodeStack.push(branchNodes[1]);
                            nodeStack.push(branchNodes[0]);
                        } else {
                            nodeStack.push(branchNodes[0]);
                            nodeStack.push(branchNodes[1]);
                        }
                    } else if (branchNodes[0] != null) {
                        nodeStack.push(branchNodes[0]);
                    } else if (branchNodes[1] != null) {
                        nodeStack.push(branchNodes[1]);
                    }
                }
            }
        }
        long endSolvingTime = System.currentTimeMillis();

        BalasAdditiveBIPSolverResult result
                = new BalasAdditiveBIPSolverResult(
                        bipProblem,
                        incumbentValueOneVariableList,
                        enumeratedNodeList,
                        endSolvingTime - startSolvingTime
                );
        return result;
    }

    private BIPFunction objectiveFunction() {
        return bipProblem.getObjectiveFunction();
    }

    private int indexOf(Variable variable) {
        return sortedVariables.indexOf(variable);
    }

    private void sortVariablesByCoefficient() {
        BIPFunction objectiveFunction = bipProblem.getObjectiveFunction();
        sortedVariables = objectiveFunction.getVariableList();
        Collections.sort(sortedVariables, new Comparator<Variable>() {
            @Override
            public int compare(Variable o1, Variable o2) {
                double coef1 = objectiveFunction.getCoefficient(o1);
                double coef2 = objectiveFunction.getCoefficient(o2);
                if (coef1 < coef2) {
                    return -1;
                } else if (coef1 > coef2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    private Node[] branch(Node node) {
        Node[] branchNodes = new Node[2];
        int variableIndex = indexOf(node.getVariable());
        if (variableIndex + 1 < sortedVariables.size()) {
            Variable branchesVariable = sortedVariables.get(variableIndex + 1);
            if (variableIndex + 2 < sortedVariables.size()) {
                branchNodes[0] = new Node(node, branchesVariable, true);
                branchNodes[0].setBoundValue(boundValue(branchNodes[0]));
            }
            branchNodes[1] = new Node(node, branchesVariable, false);
            branchNodes[1].setBoundValue(boundValue(branchNodes[1]));
        }
        return branchNodes;
    }

    private double boundValue(Node node) {
        ArrayList<Variable> valueOneVariableList = node.valueOneVariableList();
        if (node.isZero()) {
            valueOneVariableList.add(
                    sortedVariables.get(indexOf(node.getVariable()) + 1)
            );
        }
        return objectiveFunction().getResult(valueOneVariableList);
    }

    private boolean isBoundValueWorseThanCurrentIncumbent(Node node,
            double currentIncumbentObjectiveValue) {
        return node.getBoundValue() >= currentIncumbentObjectiveValue;
    }

    private boolean isBetterThanCurrentIncumbent(
            ArrayList<Variable> valueOneVariableList,
            double currentIncumbentObjectiveValue) {
        return objectiveFunction().getResult(valueOneVariableList)
                < currentIncumbentObjectiveValue;
    }

    private boolean isImpossibleToContinue(Node node) {
        ArrayList<BIPConstraint> constraintList
                = bipProblem.getConstraintList();
        ArrayList<Variable> valueOneVariableList = node.valueOneVariableList();
        ArrayList<Variable> freeVariableList = new ArrayList<>();

        for (int i = indexOf(node.getVariable()) + 1;
                i < sortedVariables.size(); i++) {
            freeVariableList.add(sortedVariables.get(i));
        }

        for (BIPConstraint constraint : constraintList) {
            if (isConstraintImpossibleToSatisfy(
                    constraint,
                    valueOneVariableList,
                    freeVariableList)) {
                return true;
            }
        }
        return false;
    }

    private boolean isConstraintImpossibleToSatisfy(
            BIPConstraint constraint,
            ArrayList<Variable> valueOneVariableList,
            ArrayList<Variable> freeVariableList) {
        BIPFunction leftHandSideFunction = constraint.getLeftHandSideFunction();
        double rightHandSideValue = constraint.getRightHandSideValue();
        double maxLeftHandSideValue = 0.0;
        for (Variable variable : valueOneVariableList) {
            maxLeftHandSideValue
                    += leftHandSideFunction.getCoefficient(variable);
        }
        for (Variable variable : freeVariableList) {
            double variableCoefficient
                    = leftHandSideFunction.getCoefficient(variable);
            if (variableCoefficient > 0) {
                maxLeftHandSideValue += variableCoefficient;
            }
        }
        return maxLeftHandSideValue < rightHandSideValue;
    }
}
