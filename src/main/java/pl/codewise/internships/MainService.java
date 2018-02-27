package pl.codewise.internships;

public class MainService implements MessageQueue {

    private Snapshot snapshot = Snapshot.getInstance();

    public void add(Message message) {
        snapshot.add(message);
    }

    public Snapshot snapshot() {
        return snapshot.getSnapshot();
    }

    public long numberOfErrorMessages() {
        return snapshot.numberOfErrorMessages();
    }
}
