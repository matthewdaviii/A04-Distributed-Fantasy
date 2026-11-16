package edu.calpoly.csc3100.fantasy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;

/**
 * Draws a sword and adds 5 attack to the wrapped character.
 *
 * @author CSC 3100 Instructional Team
 * @version 1.0
 */
public final class Sword implements Character {

  private final Character wrapped;

  public Sword(Character wrapped) {
    this.wrapped = Objects.requireNonNull(wrapped);
  }

  @Override
  public void draw(Graphics2D g, int x, int y) {
    wrapped.draw(g, x, y);

    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(x + 32, y + 8, 3, 17);

    g.setColor(new Color(180, 120, 30));
    g.fillRect(x + 29, y + 24, 9, 3);
    g.fillRect(x + 32, y + 27, 3, 5);
  }

  @Override
  public int attack() {
    return wrapped.attack() + 5;
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