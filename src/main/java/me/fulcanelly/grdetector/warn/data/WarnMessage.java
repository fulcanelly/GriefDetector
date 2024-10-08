package me.fulcanelly.grdetector.warn.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import static me.fulcanelly.tgbridge.utils.UsefulStuff.escapeMarkdown;;

@With
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarnMessage {

  String suspect;
  List<String> victims;
  WarnEvent type;
  SimpleLocation start;
  SimpleLocation end;
  int times = 1;

  String position() {
    return String.format(
        "x: %d..%d y: %d..%d z: %d..%d світ: %s\n\n",
        start.x, end.x,
        start.y, end.y,
        start.z, end.z, start.worldType.toString());
  }

  String explosionCount() {
    if (times == 1) {
      return "";
    }
    return "вибухів зафіксовано - " + times + "\n";
  }

  public String prettyMarkdown() {
    if (type == WarnEvent.CREEPER_EXPLOSION) {
      return "Увага! Гравець " + escapeMarkdown(suspect) + " зачепив кріпера, який підірвав блоки на координатах:\n" +
          position() +
          "Під вибух потрапили будівлі таких гравців - " +
          String.join(", ", escapeMarkdown(victims.toString())) + "\n" +
          " -- повідомлення може бути помилковим";
    }

    if (type == WarnEvent.TNT_EXPLOSION) {

      return "Отакої... здається завівся гріфер:\n" +
          "гравець під ніком " + escapeMarkdown(suspect) + " підірвав TNT на кординатах \n" +
          position() +
          "За цими кординатами були будівлі таких гравців - " + escapeMarkdown(victims.toString()) + "\n" +
          explosionCount() +
          " -- повіддомленя може бути помилковим";
    }

    return "невідомо що коїться";
  }
}
