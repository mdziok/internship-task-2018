package pl.codewise.internships;

import org.junit.Test;

import java.time.LocalTime;
import java.util.Comparator;

import static org.junit.Assert.*;

public class MessageQueueTest {

    @Test
    public void testCreateSnapshot() {
        MainService service = new MainService();
        Snapshot.getInstance().clearSnapshot();

        assertNotNull(service.snapshot());
    }

    @Test
    public void testTimeValidation() {
        Message message = new Message("UA", 404);
        LocalTime messageTime = message.getTime();
        LocalTime firstTime = messageTime.plusMinutes(1);
        LocalTime secondTime = messageTime.plusMinutes(7);

        assertTrue(message.isValid(firstTime));
        assertFalse(message.isValid(secondTime));
    }

    @Test
    public void testAddMessages() {
        MainService service = new MainService();
        Snapshot.getInstance().clearSnapshot();
        service.add(new Message("UA", 404));

        assertEquals(service.snapshot().getSnapshot().getMessages().size(), 1);
    }

    @Test
    public void testNumberOfError() {
        MainService service = new MainService();
        Snapshot.getInstance().clearSnapshot();
        service.add(new Message("UA", 404));
        service.add(new Message("UA", 200));

        assertEquals(service.numberOfErrorMessages(), 1);
    }

    @Test
    public void testMaximumSizeOfSnapshot() {
        MainService service = new MainService();
        Snapshot.getInstance().clearSnapshot();
        for (int i = 0; i < 110; i++) {
            service.add(new Message("UA", i));
            assertTrue(Snapshot.getInstance().getMessages().size() <= 100);
        }
    }

    @Test
    public void testRemoveTheOldest() {
        MainService service1 = new MainService();
        Snapshot.getInstance().clearSnapshot();

        Message firstMessage = new Message("UA", 200);
        service1.add(firstMessage);
        for (int i = 0; i < 200; i++) {
            service1.add(new Message("UA", 200));
        }
        Message oldestMessage = service1.snapshot().getMessages().stream().max(Comparator.comparing(Message::getTime)).get();

        assertFalse(firstMessage.getTime().isAfter(oldestMessage.getTime()));
    }


    @Test
    public void testMultipleUsersAdd() {
        MainService service1 = new MainService();
        MainService service2 = new MainService();
        Snapshot.getInstance().clearSnapshot();

        for (int i = 0; i < 20; i++) {
            service1.add(new Message("UA", 200));
            service2.add(new Message("UA", 404));
        }

        assertEquals(service1.snapshot().getMessages().size(), 40);
    }

    @Test
    public void testMultipleUsers() {
        MainService service1 = new MainService();
        MainService service2 = new MainService();
        Snapshot.getInstance().clearSnapshot();

        for (int i = 0; i < 70; i++) {
            service1.add(new Message("UA", 200));
            service2.add(new Message("UA", 404));
        }

        assertEquals(service1.snapshot().getMessages().size(), 100);
    }

}

