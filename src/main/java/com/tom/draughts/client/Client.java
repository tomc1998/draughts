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
  public static void main(String[] args) {
    final Logger logger = LoggerFactory.getLogger(Client.class);
    Board b = new Board();
    logger.debug(b.toString());

    // Construct a turn, check if valid
    Turn t = new Turn();
    t.setPiecePosition(1, 2);
    t.movements.add(MoveDirection.DownRight);

    logger.debug(t.isTurnValid(b).toString());
  }
}

