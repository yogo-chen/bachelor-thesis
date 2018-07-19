package bip.balasadditive;

import bip.Variable;
import java.util.ArrayList;

/**
 *
 * @author Prayogo Cendra
 */
public class Node {

    private final Node parent;
    private final Variable variable;
    private final boolean isZero;
    private double boundValue;
    private NodeStatus status;

    public Node(Node parent, Variable variable, boolean isZero) {
        this.parent = parent;
        this.variable = variable;
        this.isZero = isZero;
    }

    public Node getParent() {
        return parent;
    }

    public Variable getVariable() {
        return variable;
    }

    public boolean isZero() {
        return isZero;
    }

    public double getBoundValue() {
        return boundValue;
    }

    public void setBoundValue(double boundValue) {
        this.boundValue = boundValue;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public ArrayList<Variable> valueOneVariableList() {
        ArrayList<Variable> valueOneVariableList = new ArrayList<>();
        Node node = this;
        while (node.parent != null) {
            if (!node.isZero) {
                valueOneVariableList.add(node.variable);
            }
            node = node.parent;
        }
        return valueOneVariableList;
    }

    @Override
    public String toString() {
        Node node = this;
        String str = "";
        while (node.parent != null) {
            str = node.variable + (node.isZero ? ":0 " : ":1 ") + str;
            node = node.parent;
        }
        return "Node " + str + status + " " + boundValue;
    }
}
