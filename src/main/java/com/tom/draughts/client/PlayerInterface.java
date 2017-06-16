package com.tom.draughts.client;

import com.tom.draughts.game.Board;
import com.tom.draughts.game.Turn;

/** Player interface, to be inherited by the AI to play the game. */
public interface PlayerInterface {
  /** A method to fill a given turn with data using the methods exposed in the
   * class definition. Given a Toard b and Turn t, alter the object t such that
   * t is a valid move to be played. 
   *
   * The reason for having to fill in a turn rather than return something is
   * that the turn will contain which colour you're currently playing already. */
  public void fillTurn(Board b, Turn t);
}
