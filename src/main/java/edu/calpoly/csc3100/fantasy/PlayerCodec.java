package edu.calpoly.csc3100.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Converts player snapshots to and from JSON.
 * @author CSC 3100 Instructional Team
 * @version 1.0
 */
public final class PlayerCodec {
  private static final ObjectMapper MAPPER = new ObjectMapper()
      .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
  private static final Set<String> SUPPORTED_EQUIPMENT = Set.of("sword", "shield", "shoes");

  private PlayerCodec() { }

  public static String encode(PlayerState state) {
    try {
      return MAPPER.writeValueAsString(state);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Cannot encode player", e);
    }
  }

  public static PlayerState decode(String json) {
    JsonNode snapshot;
    try {
      snapshot = MAPPER.readTree(json);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Invalid player JSON", e);
    }

    if (snapshot == null || !snapshot.isObject()) {
      throw new IllegalArgumentException("Expected a player snapshot object");
    }

    JsonNode player = snapshot.path("player");
    if (!player.isTextual() || player.textValue().isBlank()) {
      throw new IllegalArgumentException("Player name must be nonblank");
    }

    JsonNode type = snapshot.path("type");
    if (!"warrior".equals(type.textValue())) {
      throw new IllegalArgumentException("Unsupported character type");
    }

    int x = readCoordinate(snapshot.path("x"), "x", Blackboard.COLUMNS);
    int y = readCoordinate(snapshot.path("y"), "y", Blackboard.ROWS);

    JsonNode items = snapshot.path("equipment");
    if (!items.isArray()) {
      throw new IllegalArgumentException("Equipment must be an array");
    }

    List<String> equipment = new ArrayList<>();
    for (JsonNode item : items) {
      String id = item.textValue();
      if (id == null || !SUPPORTED_EQUIPMENT.contains(id) || equipment.contains(id)) {
        throw new IllegalArgumentException("Invalid or duplicate equipment: " + item);
      }
      equipment.add(id);
    }

    return new PlayerState(player.textValue(), type.textValue(), x, y, equipment);
  }

  private static int readCoordinate(JsonNode value, String axis, int limit) {
    if (!value.isIntegralNumber() || !value.canConvertToInt()) {
      throw new IllegalArgumentException(axis + " must be an integer");
    }

    int coordinate = value.intValue();
    if (coordinate < 0 || coordinate >= limit) {
      throw new IllegalArgumentException(axis + " is outside the board");
    }

    return coordinate;
  }
}