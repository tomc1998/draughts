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
    return this.boardData[x + y * BOARD_SIZE];
  }
}
