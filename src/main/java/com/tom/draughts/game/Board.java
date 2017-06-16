package com.tom.draughts.game;

import com.tom.draughts.game.BoardCell;

/** The board class. This is a 3x3 draughts board (though i've seen pictures
 * for 5x5). */
public class Board {
  public static final int BOARD_SIZE = 8;

  /** 8x8 grid to represent the board data. */
  public BoardCell[] boardData = new BoardCell[BOARD_SIZE*BOARD_SIZE];

  /** Create a new board, setting out the pieces to start a game. */
  public Board() {
    // Set up black side (top 3 rows)
    for (int ii = 0; ii < 3; ++ii) {
      for (int jj = 0; jj < 8; ++jj) {
        if ((jj + 1 - ii) % 2 == 0) {
          boardData[jj + ii * BOARD_SIZE] = BoardCell.Black;
        }
        else {
          boardData[jj + ii * BOARD_SIZE] = BoardCell.Empty;
        }
      }
    }

    // Set up 2 empty rows
    for (int ii = 3; ii < 5; ++ii) {
      for (int jj = 0; jj < 8; ++jj) {
        boardData[jj + ii * BOARD_SIZE] = BoardCell.Empty;
      }
    }

    // Set up white side (bottom 3 rows)
    for (int ii = 5; ii < 8; ++ii) {
      for (int jj = 0; jj < 8; ++jj) {
        if ((jj + 1 - ii) % 2 == 0) {
          boardData[jj + ii * BOARD_SIZE] = BoardCell.White;
        }
        else {
          boardData[jj + ii * BOARD_SIZE] = BoardCell.Empty;
        }
      }
    }
  }

  /** Returns a nicely formatted board as ASCII art. */
  @Override
  public String toString() {
    StringBuilder res = new StringBuilder();
    res.append("\n");
    for (int ii = 0; ii < 8; ++ii) {
      for (int jj = 0; jj < 8; ++jj) {
        BoardCell cell = boardData[jj + ii * BOARD_SIZE];
        switch (cell) {
          case White:
            res.append("w");
            break;
          case WhiteKing:
            res.append("W");
            break;
          case Black:
            res.append("b");
            break;
          case BlackKing:
            res.append("B");
            break;
          case Empty:
            res.append(".");
            break;
        }
        res.append(" ");
      }
      res.append("\n");
    }
    return res.toString();
  }

  /** Gets the cell value at a given x, y position */
  public BoardCell getCell(int x, int y) {
    return boardData[x + y * BOARD_SIZE];
  }

  /** Moves a given board cell value from the given start position to the given
   * end position. This will overwrite any values in the destination, and set
   * the starting destination to 'empty'.
   */
  public void moveCell(int sx, int sy, int ex, int ey) {
    boardData[ex + ey * BOARD_SIZE] = boardData[sx + sy * BOARD_SIZE];
    boardData[sx + sy * BOARD_SIZE] = BoardCell.Empty;
  }

  /** Sets a cell to a value */
  public void setCell(int x, int y, BoardCell value) {
    boardData[x + y * BOARD_SIZE] = value;
  }

  /** Remove a piece at a given position, and removes it */
  public BoardCell removePiece(int x, int y) {
    BoardCell cell = getCell(x, y);
    setCell(x, y, BoardCell.Empty);
    return cell;
  }

  public class TurnInvalidException extends RuntimeException {}

  /** Function to apply a given turn to the board. This will alter the board's
   * state. This function checks whether the turn is valid first, and if it
   * isn't will throw and exception.*/
  public void applyTurn(Turn t) throws TurnInvalidException {
    // Check turn is valid
    if (t.isTurnValid(this) != Turn.TurnValidResult.Valid) { 
      throw new TurnInvalidException();
    }

    // Check if we're just moving once, whether we can just shift the peice
    // rather than perform a take (i.e. moving into an empty space).
    if (t.movements.size() == 1) {
      MoveDirection m = t.movements.get(0);
      switch (m) {
        case UpLeft:
          // If movement is to empty square, just move it and return early
          if (getCell(t.pieceX - 1, t.pieceY - 1) == BoardCell.Empty) { 
            moveCell(t.pieceX, t.pieceY, t.pieceX - 1, t.pieceY - 1);
          } 
          break;
        case UpRight:
          if (getCell(t.pieceX + 1, t.pieceY - 1) == BoardCell.Empty) { 
            moveCell(t.pieceX, t.pieceY, t.pieceX + 1, t.pieceY - 1);
          } 
          break;
        case DownLeft:
          if (getCell(t.pieceX - 1, t.pieceY + 1) == BoardCell.Empty) { 
            moveCell(t.pieceX, t.pieceY, t.pieceX - 1, t.pieceY + 1);
          } 
          break;
        case DownRight:
          if (getCell(t.pieceX + 1, t.pieceY + 1) == BoardCell.Empty) { 
            moveCell(t.pieceX, t.pieceY, t.pieceX + 1, t.pieceY + 1);
          } 
          break;
      }
    }

    // We haven't moved, therefore process moves as takes
    int currX = t.pieceX, currY = t.pieceY;
    for (MoveDirection m : t.movements) {
      int moveY = 0, moveX = 0;
      switch (m) {
        case UpLeft:
          moveX = -2; moveY = -2; break;
        case UpRight:
          moveX = 2; moveY = -2; break;
        case DownLeft:
          moveX = -2; moveY = 2; break;
        case DownRight:
          moveX = 2; moveY = 2; break;
      }
      removePiece(currX - moveX/2, currY - moveY/2);
      currX += moveX; currY += moveY;
    }

    // Finally, move the piece
    moveCell(t.pieceX, t.pieceY, currX, currY);
  }
}
