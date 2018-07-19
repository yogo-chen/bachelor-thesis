package model;

import java.util.ArrayList;

/**
 *
 * @author Prayogo Cendra
 */
public class CellMatrix {

    protected final int rows,
            columns,
            innerRows,
            innerColumns,
            verticalMargins,
            horizontalMargins,
            startRow,
            startColumn;
    protected final Dimension cellDimension;
    protected final Cell[][] cellsMatrix;
    protected final ArrayList<Cell> innerCellList;

    public CellMatrix(
            int innerRows,
            int innerColumns,
            int verticalMargins,
            int horizontalMargins,
            Dimension cellDimension
    ) {
        this.innerRows = innerRows;
        this.innerColumns = innerColumns;
        this.verticalMargins = verticalMargins;
        this.horizontalMargins = horizontalMargins;
        this.rows = innerRows + 2 * verticalMargins;
        this.columns = innerColumns + 2 * horizontalMargins;
        this.startRow = verticalMargins;
        this.startColumn = horizontalMargins;
        this.cellDimension = cellDimension;
        this.cellsMatrix = new Cell[rows][columns];
        this.innerCellList = new ArrayList();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double cellWidth = cellDimension.getWidth();
                double cellLength = cellDimension.getLength();

                Point topLeftCornerPoint = new Point(
                        (j - startColumn) * cellWidth,
                        (i - startRow) * cellLength
                );
                Point centerPoint = new Point(
                        (j - startColumn + 0.5) * cellWidth,
                        (i - startRow + 0.5) * cellLength
                );
                boolean innerCell
                        = i >= startRow
                        && i < startRow + innerRows
                        && j >= startColumn
                        && j < startColumn + innerColumns;

                cellsMatrix[i][j] = new Cell(
                        cellDimension,
                        topLeftCornerPoint,
                        centerPoint
                );

                if (innerCell) {
                    innerCellList.add(cellsMatrix[i][j]);
                }
            }
        }
    }

    public int getInnerRows() {
        return innerRows;
    }

    public int getInnerColumns() {
        return innerColumns;
    }

    public Dimension getCellDimension() {
        return cellDimension;
    }

    public Cell[][] getCellsMatrix() {
        return cellsMatrix;
    }

    public Cell[] getInnerCells() {
        Cell[] innerCells = new Cell[innerCellList.size()];
        innerCellList.toArray(innerCells);
        return innerCells;
    }

    public boolean isInnerCell(Cell cell) {
        return innerCellList.contains(cell);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CELL MATRIX\nTotal cells: ");
        sb.append(rows);
        sb.append("x");
        sb.append(columns);
        sb.append("\nInner cells: ");
        sb.append(innerRows);
        sb.append("x");
        sb.append(innerColumns);
        sb.append("\nCell dimension: ");
        sb.append(cellDimension);
        sb.append("\n\nCELLS\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sb.append(cellsMatrix[i][j]);
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
