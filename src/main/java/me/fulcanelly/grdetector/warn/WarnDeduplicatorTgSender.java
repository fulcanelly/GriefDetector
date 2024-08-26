package me.fulcanelly.grdetector.warn;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.fulcanelly.grdetector.warn.data.WarnEvent;
import me.fulcanelly.grdetector.warn.data.WarnMessage;
import me.fulcanelly.tgbridge.tapi.TGBot;
import me.fulcanelly.tgbridge.tools.MainConfig;

@RequiredArgsConstructor
public class WarnDeduplicatorTgSender {
  final TGBot bot;
  final Long targetChatId;

  Map<String, Long> timeToSendByName = new HashMap<>();
  Map<String, WarnMessage> messagesBySuspectName = new HashMap<>();

  public void start() {
    new Thread(this::run).start();
  }

  @SneakyThrows
  void run() {
    while (true) {
      Thread.sleep(500);
      handle();
    }
  }

  List<String> getMessagesReadyForSend() {
    var currentTime = System.currentTimeMillis();

    return timeToSendByName.entrySet().stream()
        .filter(pair -> pair.getValue() < currentTime)
        .map(Entry::getKey)
        .toList();
  }

  synchronized void handle() {
    var ready = getMessagesReadyForSend();
    if (ready.isEmpty()) {
      return;
    }
    for (var msgKey : ready) {
      var msg = messagesBySuspectName.get(msgKey);
      send(msg);

      timeToSendByName.remove(msgKey);
      messagesBySuspectName.remove(msgKey);
    }
  }

  synchronized public void addMessage(WarnMessage message) {

    var suspect = message.getSuspect();
    var key = suspect + ":" + message.getType().toString();

    if (messagesBySuspectName.get(key) == null) {
      messagesBySuspectName.put(key, message);
      timeToSendByName.put(key, System.currentTimeMillis() + 5000);

      return;
    } else {
      timeToSendByName.put(key, System.currentTimeMillis() + 5000);
      var oldMessage = messagesBySuspectName.get(key);

      oldMessage.setTimes(oldMessage.getTimes() + 1);
      oldMessage.setStart(oldMessage.getStart().min(message.getStart()));
      oldMessage.setEnd(oldMessage.getEnd().max(message.getEnd()));
      oldMessage.setVictims(
          Stream.concat(
              oldMessage.getVictims().stream(),
              message.getVictims().stream())
              .distinct()
              .toList());
      // oldMessage.setEnd();
    }

  }

  void send(WarnMessage message) {
    bot.sendMessage(targetChatId, message.pretty());
  }
}
