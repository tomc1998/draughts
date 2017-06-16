package com.tom.draughts.game;

import java.util.ArrayList;

/** A class to represent a turn taken by a player. */
public class Turn {
  /**
   * The list of movements to be taken this turn
   */
  public ArrayList<MoveDirection> movements = new ArrayList<>();

  /** The color of the side we're playing as in this turn */
  private PieceColor turnColor = PieceColor.Black;

  /** The x position on the board of the piece we're moving. */
  public int pieceX = 0;
  /** The y position on the board of the piece we're moving. */
  public int pieceY = 0;

  /** Sets the position of the piece we're moving this turn. Piece is
   * identified by an X and Y position on the board. */
  public void setPiecePosition(int posX, int posY) {
    this.pieceX = posX;
    this.pieceY = posY;
  }

  /** An enum to represent the result returned by the isTurnValid function. */
  public enum TurnValidResult {
    Valid, /** Move is valid */
    CellEmpty, /** The cell doesn't contain a piece to move */
    WrongColor, /** For moving the wrong color piece */
    NotKing, /** The piece is being moved backwards but isn't a king */
    MoveOOB, /** A movement is out of bounds */
    TakeInvalid, /** Trying to make a move to take a piece which isn't there / is the wrong color */
    SpaceBlocked; /** Space is blocked for taking */

    @Override
    public String toString() {
      switch (this) {
        case Valid: 
          return "Valid";
        case CellEmpty: 
          return "CellEmpty";
        case WrongColor: 
          return "WrongColor";
        case NotKing: 
          return "NotKing";
        case MoveOOB: 
          return "MoveOOB";
        case TakeInvalid: 
          return "TakeInvalid";
        case SpaceBlocked: 
          return "SpaceBlocked";
      }
      return "";
    }
  }

  /** Returns whether or not the turn is valid on the given board */
  public TurnValidResult isTurnValid(Board board) {
    BoardCell piece = board.getCell(pieceX, pieceY);
    // This turn can't be value if the board cell is empty
    if (piece == BoardCell.Empty) { return TurnValidResult.CellEmpty; }

    // Check we're moving the right colour of piece
    if (turnColor == PieceColor.Black && (piece != BoardCell.Black 
          && piece != BoardCell.BlackKing) || 
        turnColor == PieceColor.White && (piece != BoardCell.White 
          && piece != BoardCell.WhiteKing)) {
      // This can't be a valid turn, we're moving the wrong colour piece
      return TurnValidResult.WrongColor;
    }

    // Check we're moving in the right direction if we're not a king
    for (MoveDirection m : movements) {
      if (piece == BoardCell.Black && (m == MoveDirection.UpLeft 
            || m == MoveDirection.UpRight) ||
          piece == BoardCell.White && (m == MoveDirection.DownLeft 
            || m == MoveDirection.DownRight)) {
        return TurnValidResult.NotKing; // We can't move backwards until we're a king
      }
    }

    // If we're only making 1 movement, we can exit with true early if it's
    // into an empty space (not a take).
    if (movements.size() == 1) {
      MoveDirection m = movements.get(0);
      switch (m) {
        case UpLeft:
          // Check movement is in board bounds
          if (pieceX - 1 < 0 || pieceY - 1 < 0) { 
            return TurnValidResult.MoveOOB; 
          } 
          if (board.getCell(pieceX - 1, pieceY - 1) == BoardCell.Empty) { 
            return TurnValidResult.Valid; 
          } 
          break;
        case UpRight:
          if (pieceX + 1 >= Board.BOARD_SIZE || pieceY - 1 < 0) { 
            return TurnValidResult.MoveOOB; 
          } 
          if (board.getCell(pieceX + 1, pieceY - 1) == BoardCell.Empty) { 
            return TurnValidResult.Valid; 
          } 
          break;
        case DownLeft:
          if (pieceX - 1 < 0 || pieceY + 1 >= Board.BOARD_SIZE) { 
            return TurnValidResult.MoveOOB; 
          } 
          if (board.getCell(pieceX - 1, pieceY + 1) == BoardCell.Empty) { 
            return TurnValidResult.Valid; 
          } 
          break;
        case DownRight:
          if (pieceX + 1 >= Board.BOARD_SIZE || pieceY + 1 >= Board.BOARD_SIZE) { 
            return TurnValidResult.MoveOOB; 
          } 
          if (board.getCell(pieceX + 1, pieceY + 1) == BoardCell.Empty) { 
            return TurnValidResult.Valid; 
          } 
          break;
      }
    }

    // Now check for each movement that we're jumping over a valid enemy piece,
    // if we have more than 1 movement in this turn
    ArrayList<Integer> positionsJumped = new ArrayList<>();
    // Keep track of current position of piece
    int currX = pieceX, currY = pieceY;
    for (MoveDirection m : movements) {
      BoardCell c = null; // Get the board cell we're trying to jump over this turn
      int moveX = 0, moveY = 0; // How much we're moving with this turn
      switch (m) {
        case UpLeft:
          moveX = -2; moveY = -2;
          if (currX - 2 < 0 || currY - 2 < 0) { // Check movement is in board bounds
            return TurnValidResult.MoveOOB; 
          } 
          c = board.getCell(currX - 1, currY - 1); // Now get the piece we're jumping over
          break;
        case UpRight:
          moveX = 2; moveY = -2;
          if (currX + 2 >= Board.BOARD_SIZE || currY - 2 < 0) { 
            return TurnValidResult.MoveOOB; 
          } 
          c = board.getCell(currX + 1, currY - 1);
          break;
        case DownLeft:
          moveX = -2; moveY = 2;
          if (currX - 2 < 0 || currY + 2 >= Board.BOARD_SIZE) { 
            return TurnValidResult.MoveOOB; 
          } 
          c = board.getCell(currX - 1, currY + 1);
          break;
        case DownRight:
          moveX = 2; moveY = 2;
          if (currX + 2 >= Board.BOARD_SIZE || currY + 2 >= Board.BOARD_SIZE) { 
            return TurnValidResult.MoveOOB; 
          } 
          c = board.getCell(currX + 1, currY + 1);
          break;
      }
      // Check we haven't already jumped this position
      for (Integer ii : positionsJumped) {
        if (ii.intValue() == (currX + moveX/2 + (currY + moveY/2) * Board.BOARD_SIZE)) {
          return TurnValidResult.TakeInvalid;
        }
      }
      // Check we're jumping over something, can't be empty
      if (c == BoardCell.Empty) { return TurnValidResult.TakeInvalid; } 
      // Check we're jumping over the right colour
      if (turnColor == PieceColor.Black && (c == BoardCell.Black || c == BoardCell.BlackKing) ||
          turnColor == PieceColor.White && (c == BoardCell.White || c == BoardCell.WhiteKing)) {
        return TurnValidResult.TakeInvalid;
      }
      // Check the space we're jumping into is empty
      if (board.getCell(currX + moveX, currY + moveY) != BoardCell.Empty) {
        return TurnValidResult.SpaceBlocked;
      }

      // If we're here the movement is valid, alter currX and currY for the
      // next loop, using the moveX and moveY we calculated earlier. Also, add
      // to the positionsJumped array.
      positionsJumped.add(currX + moveX/2 + (currY + moveY/2) * Board.BOARD_SIZE);
      currX += moveX;
      currY += moveY;
    }

    return TurnValidResult.Valid;
  }
}
