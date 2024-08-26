package me.fulcanelly.grdetector.warn.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

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

  public String pretty() {
    if (type == WarnEvent.CREEPER_EXPLOSION) {
      return "Увага! Гравець " + suspect + " зачепив кріпера, який підірвав блоки на координатах:\n" +
          position() +
          "Під вибух потрапили будівлі таких гравців - " +
          String.join(", ", victims) + "\n" +
          " -- повідомлення може бути помилковим";
    }

    if (type == WarnEvent.TNT_EXPLOSION) {

      return "Отакої... здається завівся гріфер:\n" +
          "гравець під ніком " + suspect + " підірвав TNT на кординатах \n" +
          position() +
          "За цими кординатами були будівлі таких гравців - " + victims.toString() + "\n" +
          explosionCount() +
          " -- повіддомленя може бути помилковим";
    }

    return "невідомо що коїться";
  }
}
