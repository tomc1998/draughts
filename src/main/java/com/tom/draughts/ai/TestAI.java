package com.tom.draughts.ai;

import com.tom.draughts.client.Client;
import com.tom.draughts.client.PlayerInterface;
import com.tom.draughts.game.Board;
import com.tom.draughts.game.MoveDirection;
import com.tom.draughts.game.PieceColor;
import com.tom.draughts.game.Turn;

/** A test AI for the draughts game. */
public class TestAI implements PlayerInterface {

  public void fillTurn(Board b, Turn t) {
    if (t.getTurnColor() == PieceColor.Black) {
      t.setPiecePosition(1, 2);
      t.movements.add(MoveDirection.DownRight);
    }
    else {
      t.setPiecePosition(0, 5);
      t.movements.add(MoveDirection.UpRight);
    }
  }

  public static void main(String[] args) {
    Client c = new Client(new TestAI());
    c.start();
  }
}
