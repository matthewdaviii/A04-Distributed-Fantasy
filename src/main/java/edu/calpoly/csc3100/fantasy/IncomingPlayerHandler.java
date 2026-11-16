package edu.calpoly.csc3100.fantasy;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Processes received MQTT player-state messages independently of the broker.
 *
 * @author CSC 3100 Instructional Team
 * @version 1.0
 */
public final class IncomingPlayerHandler {
  private final String topicPrefix;
  private final String localUsername;
  private final Consumer<PlayerState> receiver;

  public IncomingPlayerHandler(String team, String localUsername,
                               Consumer<PlayerState> receiver) {
    this.topicPrefix = Subscriber.topicPrefix(team);
    this.localUsername = Objects.requireNonNull(localUsername);
    this.receiver = Objects.requireNonNull(receiver);
  }

  public void accept(String topic, byte[] payload) {
    if (topic == null || payload == null || !topic.startsWith(topicPrefix)) {
      return;
    }

    String player = topic.substring(topicPrefix.length());
    if (player.isEmpty() || player.contains("/") || player.equals(localUsername)) {
      return;
    }

    PlayerState state;
    try {
      String json = StandardCharsets.UTF_8.newDecoder()
          .decode(ByteBuffer.wrap(payload)).toString();
      state = PlayerCodec.decode(json);
    } catch (CharacterCodingException | IllegalArgumentException e) {
      return;
    }

    if (player.equals(state.player())) {
      receiver.accept(state);
    }
  }
}