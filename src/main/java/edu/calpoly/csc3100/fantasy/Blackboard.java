package edu.calpoly.csc3100.fantasy;

import java.beans.PropertyChangeSupport;
import java.util.*;

/**
 * Player state and change notifications. The application confines access to the Swing EDT.
 * @author CSC 3100 Instructional Team
 * @version 1.0
 */
public final class Blackboard extends PropertyChangeSupport {
  public static final String PLAYERS = "players";
  public static final int COLUMNS = 16;
  public static final int ROWS = 11;
  private final Map<String,PlayerState> players = new LinkedHashMap<>();
  private final GamePublisher publisher;
  private String localUsername;

  public Blackboard(GamePublisher publisher) {
    super(Blackboard.class);
    this.publisher = Objects.requireNonNull(publisher);
  }

  private void store(PlayerState state) {
    PlayerState previous = players.put(state.player(), state);
    firePropertyChange(new java.beans.PropertyChangeEvent(this, PLAYERS, previous, state));
  }

  public PlayerState getPlayer(String username) { return players.get(username); }

  public Collection<PlayerState> players() { return List.copyOf(players.values()); }

  public void addLocalPlayer(PlayerState state) {
    localUsername = state.player();
    store(state);
    publisher.publish(state);
  }

  public void moveLocal(String username, int dx, int dy) {
    if (localUsername == null || !localUsername.equals(username)) {
      return;
    }

    boolean horizontal = dy == 0 && (dx == -1 || dx == 1);
    boolean vertical = dx == 0 && (dy == -1 || dy == 1);
    if (!horizontal && !vertical) {
      return;
    }

    PlayerState current = players.get(username);
    int x = current.x() + dx;
    int y = current.y() + dy;
    if (x < 0 || x >= COLUMNS || y < 0 || y >= ROWS) {
      return;
    }

    PlayerState moved = new PlayerState(
        current.player(), current.type(), x, y, current.equipment());
    store(moved);
    publisher.publish(moved);
  }

  public void applyRemote(PlayerState state) {
    if (!state.player().equals(localUsername)) store(state);
  }

  public void equipLocal(String username, String item) {
    // TODO: append supported equipment to the local snapshot if absent.
    // Store the changed snapshot and publish it once.
  }

  public void unequipLocal(String username, String item) {
    // TODO: remove the requested item from the local snapshot if present.
    // Preserve the order of the other items, store the snapshot, and publish it once.
  }
}