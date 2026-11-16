package edu.calpoly.csc3100.fantasy;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentAcceptanceTest {
  @Test
  void decodedEquipmentBuildsACharacterWithBothBonuses() {
    PlayerState original = new PlayerState(
        "alice", "warrior", 15, 10, List.of("shield", "shoes", "sword"));

    PlayerState decoded = PlayerCodec.decode(PlayerCodec.encode(original));
    Character character = CharacterFactory.create(decoded);

    assertEquals(original, decoded);
    assertEquals(15, character.attack());
    assertEquals(10, character.defense());
  }

  @Test
  void rejectsCoordinatesEncodedAsStrings() {
    String json = """
        {"player":"bob","type":"warrior","x":"4","y":7,"equipment":[]}
        """;

    assertThrows(IllegalArgumentException.class, () -> PlayerCodec.decode(json));
  }

  @Test
  void rejectsInvalidUtf8AndStillDeliversTheNextSnapshot() {
    List<PlayerState> received = new ArrayList<>();
    IncomingPlayerHandler handler =
        new IncomingPlayerHandler("student-tests", "alice", received::add);

    // A lone continuation byte cannot appear by itself in UTF-8.
    String invalidJson = """
        {"player":"b\u0080b","type":"warrior","x":2,"y":4,"equipment":[]}
        """;
    handler.accept("csc3100/a04/student-tests/players/b\uFFFDb",
        invalidJson.getBytes(StandardCharsets.ISO_8859_1));
    assertEquals(List.of(), received);

    PlayerState valid = new PlayerState("bob", "warrior", 2, 4, List.of());
    handler.accept("csc3100/a04/student-tests/players/bob",
        PlayerCodec.encode(valid).getBytes(StandardCharsets.UTF_8));

    assertEquals(List.of(valid), received);
  }
}