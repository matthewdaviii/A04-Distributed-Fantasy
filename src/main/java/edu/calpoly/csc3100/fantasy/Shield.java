package edu.calpoly.csc3100.fantasy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;

/**
 * Draws a shield and adds 5 defense to the wrapped character.
 *
 * @author CSC 3100 Instructional Team
 * @version 1.0
 */
public final class Shield implements Character {

  private final Character wrapped;

  public Shield(Character wrapped) {
    this.wrapped = Objects.requireNonNull(wrapped);
  }

  @Override
  public void draw(Graphics2D g, int x, int y) {
    wrapped.draw(g, x, y);

    g.setColor(new Color(35, 85, 150));
    g.fillOval(x + 2, y + 13, 12, 17);

    g.setColor(Color.YELLOW);
    g.fillOval(x + 6, y + 19, 4, 4);
  }

  @Override
  public int attack() {
    return wrapped.attack();
  }

  @Override
  public int defense() {
    return wrapped.defense() + 5;
  }

  @Override
  public String type() {
    return wrapped.type();
  }
}