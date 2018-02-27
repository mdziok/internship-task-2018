package pl.codewise.internships;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Snapshot {

    private List<Message> messages;

    private Snapshot() {
        messages = new LinkedList<>();
    }

    public static Snapshot getInstance() {
        return SnapshotHolder.INSTANCE;
    }

    private static class SnapshotHolder {
        private static final Snapshot INSTANCE = new Snapshot();
    }

    public synchronized void add(Message message) {
        messages.add(message);
        while (messages.size() > 100) {
            removeTheOldest();
        }
    }

    public Snapshot getSnapshot() {
        updateSnapshot();
        return this;
    }

    private synchronized void updateSnapshot() {
        if (!messages.isEmpty()) {
            LocalTime now = LocalTime.now();
            while (messages.size() > 100) {
                removeTheOldest();
            }
            while (!isTheOldestValid(now)) {
                removeTheOldest();
            }
        }
    }

    public synchronized List<Message> getMessages() {
        return new LinkedList<>(messages);
    }

    public synchronized long numberOfErrorMessages() {
        updateSnapshot();
        return messages.stream().filter(message -> message.getErrorCode() > 299).count();
    }

    public void clearSnapshot() {
        messages.clear();
    }

    private void removeTheOldest() {
        messages.remove(messages.stream().max(Comparator.comparing(Message::getTime)).orElseGet(null));
    }

    private boolean isTheOldestValid(LocalTime now) {
        Message m = messages.stream().max(Comparator.comparing(Message::getTime)).orElseGet(null);
        if (m != null) {
            if (m.isValid(now)) {
                return true;
            }
        }
        return false;
    }
}
