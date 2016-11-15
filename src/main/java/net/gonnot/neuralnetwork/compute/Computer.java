package net.gonnot.neuralnetwork.compute;
import net.gonnot.neuralnetwork.matrix.Matrix;
@SuppressWarnings("WeakerAccess")
public class Computer {
    public static Matrix transpose(Matrix matrix) {
        return new Matrix() {
            @Override
            public double value(int row, int column) {
                return matrix.value(column, row);
            }


            @Override
            public int columns() {
                return matrix.rows();
            }


            @Override
            public int rows() {
                return matrix.columns();
            }
        };
    }


    public static Matrix multiplyBy(Double value, Matrix matrix) {
        return new Matrix() {
            @Override
            public double value(int row, int column) {
                return matrix.value(row, column) * value;
            }


            @Override
            public int columns() {
                return matrix.columns();
            }


            @Override
            public int rows() {
                return matrix.rows();
            }
        };
    }


    public static Matrix mergeTopBottom(Matrix top, Matrix bottom) {
        if (top.columns() != bottom.columns()) {
            throw new IllegalArgumentException();
        }
        return new Matrix() {
            @Override
            public double value(int row, int column) {
                if (row <= top.rows()) {
                    return top.value(row, column);
                }
                return bottom.value(row - top.rows(), column);
            }


            @Override
            public int columns() {
                return top.columns();
            }


            @Override
            public int rows() {
                return top.rows() + bottom.rows();
            }
        };
    }


    public static Matrix multiply(Matrix matrixA, Matrix matrixB) {
        return new Matrix() {
            @Override
            public double value(int row, int column) {
                double result = 0.;
                for (int i = 1; i <= matrixB.rows(); i++) {
                    double valueB = matrixB.value(i, column);
                    double valueA = matrixA.value(row, i);
                    result += valueB * valueA;
                }
                return result;
            }


            @Override
            public int columns() {
                return matrixB.columns();
            }


            @Override
            public int rows() {
                return matrixA.rows();
            }
        };
    }


    public static Matrix minus(Matrix matrixA, Matrix matrixB) {
        return new Matrix() {
            @Override
            public double value(int row, int column) {
                return matrixA.value(row, column) - matrixB.value(row, column);
            }


            @Override
            public int columns() {
                return matrixA.columns();
            }


            @Override
            public int rows() {
                return matrixA.rows();
            }
        };
    }


    public static String toString(Matrix matrix) {
        if (matrix.columns() == 0 && matrix.rows() == 0) {
            return "empty Matrix";
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 1; i <= matrix.rows(); i++) {
            for (int j = 1; j <= matrix.columns(); j++) {
                builder.append("    ").append(matrix.value(i, j));
            }
            builder.append('\n');
        }

        return builder.toString();
    }


    public static SubMatrixBuilder subMatrix(Matrix matrix) {
        return new SubMatrixBuilder(matrix);
    }


    public static class SubMatrixBuilder {
        private final Matrix matrix;


        public SubMatrixBuilder(Matrix matrix) {
            this.matrix = matrix;
        }


        public SubMatrixBuilderFinalPhase rows(int from, int to) {
            return new SubMatrixBuilderFinalPhase(matrix, from, to);
        }


        public SubMatrixBuilderFinalPhase rows(int to) {
            return rows(1, to);
        }


        public SubMatrixBuilderFinalPhase allRows() {
            return rows(1, -1);
        }
    }
    public static class SubMatrixBuilderFinalPhase {
        private final Matrix matrix;
        private final int fromRow;
        private final int toRow;


        public SubMatrixBuilderFinalPhase(Matrix matrix, int fromRow, int toRow) {
            this.matrix = matrix;
            this.fromRow = fromRow;
            this.toRow = toRow;
        }


        public Matrix columns(int from, int to) {
            return new SubMatrix(matrix, fromRow, toRow, from, to);
        }


        public Matrix columns(int to) {
            return columns(1, to);
        }


        public Matrix allColumns() {
            return columns(1, -1);
        }
    }
    private static class SubMatrix implements Matrix {
        private final Matrix matrix;
        private final int fromRow;
        private final int toRow;
        private final int fromColumn;
        private final int toColumn;


        public SubMatrix(Matrix matrix, int fromRow, int toRow, int fromColumn, int toColumn) {
            this.matrix = matrix;
            this.fromRow = fromRow;
            this.toRow = toRow >= 0 ? toRow : matrix.rows();
            this.fromColumn = fromColumn;
            this.toColumn = toColumn >= 0 ? toColumn : matrix.columns();

            if (this.toColumn > this.matrix.columns() || this.fromColumn > this.toColumn) {
                throw new IllegalArgumentException(String.format("Column Range out of bounds. Specified [%d..%d] but Matrix is [1..%d]", this.fromColumn, this.toColumn, this.matrix.columns()));
            }
            if (this.toRow > this.matrix.rows() || this.fromRow > this.toRow) {
                throw new IllegalArgumentException(String.format("Row Range out of bounds. Specified [%d..%d] but Matrix is [1..%d]", this.fromRow, this.toRow, this.matrix.rows()));
            }
        }


        @Override
        public double value(int row, int column) {
            return matrix.value(row + fromRow - 1, column + fromColumn - 1);
        }


        @Override
        public int columns() {
            return toColumn - fromColumn + 1;
        }


        @Override
        public int rows() {
            return toRow - fromRow + 1;
        }
    }
}
