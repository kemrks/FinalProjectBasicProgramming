import java.util.Random;

/**
 * GameLogic.java
 * This class handles all the rules of the Tic-Tac-Toe game.
 * It does NOT touch the GUI at all — it only works with data.
 *
 * The board is stored as an array of 9 characters:
 *
 *   Index layout:
 *   [0] [1] [2]
 *   [3] [4] [5]
 *   [6] [7] [8]
 *
 *   ' ' = empty cell
 *   'X' = player's symbol
 *   'O' = computer's symbol
 */
public class GameLogic {

    private char[] board; // the 9 cells of the board
    private Random random;

    // All 8 possible winning combinations (by index)
    private int[][] winPatterns = {
        {0, 1, 2}, // top row
        {3, 4, 5}, // middle row
        {6, 7, 8}, // bottom row
        {0, 3, 6}, // left column
        {1, 4, 7}, // middle column
        {2, 5, 8}, // right column
        {0, 4, 8}, // diagonal top-left to bottom-right
        {2, 4, 6}  // diagonal top-right to bottom-left
    };

    public GameLogic() {
        board  = new char[9];
        random = new Random();
        resetBoard();
    }

    /**
     * resetBoard()
     * Clears all cells — sets every cell to ' ' (empty space).
     * Call this at the start of a new game.
     */
    public void resetBoard() {
        for (int i = 0; i < board.length; i++) {
            board[i] = ' ';
        }
    }

    /**
     * makeMove()
     * Places a symbol ('X' or 'O') on the board at the given index.
     *
     * Returns true  if the move was successful.
     * Returns false if the move was invalid (index out of range or cell already taken).
     */
    public boolean makeMove(int cellIndex, char symbol) {
        // Check if the index is within 0-8
        if (cellIndex < 0 || cellIndex >= 9) {
            return false;
        }

        // Check if the cell is already taken
        if (board[cellIndex] != ' ') {
            return false;
        }

        // Place the symbol
        board[cellIndex] = symbol;
        return true;
    }

    /**
     * checkWinner()
     * Checks if the given symbol ('X' or 'O') has won the game.
     *
     * Returns true if that symbol has 3 in a row/column/diagonal.
     */
    public boolean checkWinner(char symbol) {
        for (int i = 0; i < winPatterns.length; i++) {
            int a = winPatterns[i][0];
            int b = winPatterns[i][1];
            int c = winPatterns[i][2];

            if (board[a] == symbol && board[b] == symbol && board[c] == symbol) {
                return true; // found a winning line
            }
        }
        return false;
    }

    /**
     * isDraw()
     * Checks if the board is completely full with no winner.
     * Returns true if all 9 cells are filled.
     */
    public boolean isDraw() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == ' ') {
                return false; // found an empty cell → not a draw yet
            }
        }
        return true; // all cells are filled
    }

    /**
     * computerMove()
     * Makes the computer choose a cell to place 'O'.
     *
     * Strategy (simple but smart):
     *   1. Try to WIN — if computer can win in one move, take it.
     *   2. Try to BLOCK — if player can win in one move, block it.
     *   3. Take CENTER (index 4) if it's free.
     *   4. Otherwise pick a random empty cell.
     *
     * Returns the index chosen, or -1 if no move is possible.
     */
    public int computerMove() {
        // Step 1: Try to win
        int winMove = findBestMove('O');
        if (winMove != -1) {
            return winMove;
        }

        // Step 2: Block the player from winning
        int blockMove = findBestMove('X');
        if (blockMove != -1) {
            return blockMove;
        }

        // Step 3: Take center if free
        if (board[4] == ' ') {
            return 4;
        }

        // Step 4: Pick any random empty cell
        int tries = 0;
        while (tries < 100) {
            int randomCell = random.nextInt(9);
            if (board[randomCell] == ' ') {
                return randomCell;
            }
            tries++;
        }

        return -1; // no empty cell found
    }

    /**
     * findBestMove()
     * Helper for computerMove().
     * Looks for a move that would make the given symbol win immediately.
     * Returns the index of that move, or -1 if none found.
     */
    private int findBestMove(char symbol) {
        for (int i = 0; i < winPatterns.length; i++) {
            int a = winPatterns[i][0];
            int b = winPatterns[i][1];
            int c = winPatterns[i][2];

            // Count how many of these 3 cells belong to `symbol` and how many are empty
            int symbolCount = 0;
            int emptyIndex  = -1;

            if (board[a] == symbol) symbolCount++;
            else if (board[a] == ' ') emptyIndex = a;

            if (board[b] == symbol) symbolCount++;
            else if (board[b] == ' ') emptyIndex = b;

            if (board[c] == symbol) symbolCount++;
            else if (board[c] == ' ') emptyIndex = c;

            // If 2 cells are taken by `symbol` and 1 is empty → take the empty one
            if (symbolCount == 2 && emptyIndex != -1) {
                return emptyIndex;
            }
        }
        return -1;
    }

    /**
     * getBoard()
     * Returns the current board array.
     * The GUI uses this to redraw the buttons.
     */
    public char[] getBoard() {
        return board;
    }
}
