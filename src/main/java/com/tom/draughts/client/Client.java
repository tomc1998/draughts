package com.tom.draughts.client;

import com.tom.draughts.game.Board;
import com.tom.draughts.game.MoveDirection;
import com.tom.draughts.game.Turn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Client class - use this as the main class to run a client (it contains a
 * main() function).
 */
public class Client {

  final Logger logger = LoggerFactory.getLogger(Client.class);

  private PlayerInterface player;

  /** Create a new client with a given player interface as the player */
  public Client(PlayerInterface p) { player = p; }

  /** Start the client communicating with the server */
  public void start() {

    // Function doesn't really do anything, just create a board and have this
    // player play against itself.
    Board b = new Board();
    logger.debug(b.toString());

    boolean isBlack = false;
    while(true) {
      // Construct a turn, then apply to the board
      Turn t;
      if (isBlack) { t = Turn.createBlackTurn(); }
      else { t = Turn.createWhiteTurn(); }
      player.fillTurn(b, t);
      b.applyTurn(t);
      logger.debug(b.toString());

      isBlack = !isBlack;
    }
  }
}

