package com.tom.draughts.client;

import com.tom.draughts.game.Board;
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
  }
}

