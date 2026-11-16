package edu.calpoly.csc3100.fantasy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;

/**
 * Visual footwear that preserves the wrapped character’s stats.
 *
 * @author CSC 3100 Instructional Team
 * @version 1.0
 */
public final class Shoes implements Character {

  private final Character wrapped;

  public Shoes(Character wrapped) {
    this.wrapped = Objects.requireNonNull(wrapped);
  }

  @Override
  public void draw(Graphics2D g, int x, int y) {
    wrapped.draw(g, x, y);

    g.setColor(new Color(130, 65, 35));
    g.fillRoundRect(x + 11, y + 33, 8, 5, 3, 3);
    g.fillRoundRect(x + 21, y + 33, 8, 5, 3, 3);
  }

  @Override
  public int attack() {
    return wrapped.attack();
  }

  @Override
  public int defense() {
    return wrapped.defense();
  }

  @Override
  public String type() {
    return wrapped.type();
  }
}