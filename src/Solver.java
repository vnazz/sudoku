import javafx.util.Pair;

/**
 * Created by Victoria on 7/27/16.
 */
public class Solver {
    private int dim = 9;
    private int[][] board;

    /**
     * Create a new Solver with an empty board
     */
    public Solver() {
        board = new int[dim][dim];
    }

    /**
     * Prints out the solved board or an error message
     * @param board a 2d array of ints
     */
    public void solve(int[][] board) {
        Pair<Boolean, Pair<Integer, Integer>> validBoard = isBoardValid(board);
        if (!validBoard.getKey()) {
            System.out.format("The given board is invalid at (%d, %d)",validBoard.getValue().getKey(), validBoard.getValue().getValue());
        } else if (isSolved(board)) {
            printBoard(this.board);
        } else {
            System.out.println("The given board has no solution");
        }
    }

    /**
     * Solves the given board through recursion
     * @param board a 2d array of ints
     * @return whether or not the board was solved
     */
    private boolean isSolved(int[][] board) {
        Pair<Integer, Integer> empty = findNextEmptyLocation(board);
        int row = empty.getKey();
        int col = empty.getValue();

        // board is complete
        if (row == -1) {
            this.board = board;
            return true;
        }

        for (int num = 1; num <= dim; num++) {
            if (isMoveValid(row, col, num, board)) {
                board[row][col] = num;

                if (isSolved(board)) {
                    this.board = board;
                    return true;
                } else {
                    board[row][col] = 0;
                }
            }
        }

        return false;
    }

    /**
     * Prints out the given board
     * @param board a 2d array of ints
     */
    public void printBoard(int[][] board) {
        for (int r = 0; r < dim; r++) {
            for (int c = 0; c < dim; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.print("\n");
        }
    }

    /**
     * @return the solver's board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Finds the first empty location in the given board
     * @param board a 2d array of ints
     * @return the row, col location of an unfilled space
     */
    private Pair<Integer, Integer> findNextEmptyLocation(int[][] board) {
        for (int r = 0; r < dim; r++) {
            for (int c = 0; c < dim; c++) {
                if (board[r][c] == 0) {
                    return new Pair<>(r, c);
                }
            }
        }
        return new Pair<>(-1, -1);
    }

    /**
     * Checks to see is the given board follows the sudoku rules
     * @param board a 2d array of ints
     * @return a boolean stating whether the board is valid and a pair of ints showing
     *          where the board is invalid
     */
    private Pair<Boolean, Pair<Integer, Integer>> isBoardValid(int[][] board) {
        for (int r = 0; r < dim; r++) {
            for (int c = 0; c < dim; c++) {
                if (board[r][c] != 0) {
                    int num = board[r][c];
                    board[r][c] = 0;
                    if (!isMoveValid(r, c, num, board) || num > 9) {
                        return new Pair<>(false, new Pair<>(r, c));
                    } else {
                        board[r][c] = num;
                    }
                }
            }
        }
        return new Pair<>(true, new Pair<>(-1, -1));
    }

    /**
     * Checks to see if the given number can be placed in the given location on the given board
     * @param row an int between [0, 9)
     * @param col an int between [0, 9)
     * @param num an int between [0, 9)
     * @param board a 2d array of ints
     * @return whether or not the move follows the guidelines of sudoku
     */
    private boolean isMoveValid(int row, int col, int num, int[][] board) {
        return isNumInRow(row, num, board) && isNumInCol(col, num, board) && isNumInSquare(row, col, num, board);
    }

    /**
     *
     * @param row an int between [0, 9)
     * @param num an int between [0, 9)
     * @param board a 2d array of ints
     * @return whether or not there is an int of the same value in the given row
     */
    private boolean isNumInRow(int row, int num, int[][] board) {
        for (int c = 0; c < dim; c++) {
            if (board[row][c] == num) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param col an int between [0, 9)
     * @param num an int between [0, 9)
     * @param board a 2d array of ints
     * @return whether or not there is an int of the same value in the given column
     */
    private boolean isNumInCol(int col, int num, int[][] board) {
        for (int r = 0; r < dim; r++) {
            if (board[r][col] == num) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param row an int between [0, 9)
     * @param col an int between [0, 9)
     * @param num an int between [0, 9)
     * @param board a 2d array of ints
     * @return whether or not there is an int of the same value in the square corresponding to the
     *         given row and column
     */
    private boolean isNumInSquare(int row, int col, int num, int[][] board) {
        Pair<Integer, Integer> corner = getSquareCorner(row, col);
        for (int r = corner.getKey(); r < corner.getKey() + 3; r++) {
            for (int c = corner.getValue(); c < corner.getValue() + 3; c++) {
                if (board[r][c] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param row an int between [0, 9)
     * @param col an int between [0, 9)
     * @return row and column of the upper left corner of the square corresponding to the given
     *         row and column
     */
    private Pair<Integer, Integer> getSquareCorner(int row, int col) {
        return new Pair<>(getCorner(row), getCorner(col));
    }

    /**
     *
     * @param num an int between [0, 9)
     * @return the number corresponding to the upper left corner of a square
     */
    private int getCorner(int num) {
        if (num <= 2) {
            return 0;
        } else if (num <= 5) {
            return 3;
        } else {
            return 6;
        }
    }
}
