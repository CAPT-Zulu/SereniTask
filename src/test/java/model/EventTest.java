package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventTest {
    public Event createTestEvent() {
        // Create entry for the event and return it
        return new Event(
                "Test ID",
                "Test Event",
                "Test description",
                "Test location",
                LocalDateTime.now(), // Start time
                LocalDate.now(), // Start date
                LocalDate.now(), // End date
                500,
                false,
                false,
                "default",
                "",
                ""
        );
        // Events will be cleaned up by the garbage collector
    }

    @Test
    public void testEventConstructor() {
        // Test the constructor of the Event class
        Event event = createTestEvent();
        assertNotNull(event, "Event should not be null");
        assertEquals("Test Event", event.getTitle(), "Event title should match");
        assertEquals("Test description", event.getDescription(), "Event description should match");
        assertEquals("Test location", event.getLocation(), "Event location should match");
        // Start time - To be implemented, requires saving dates
        // Start date - To be implemented, requires saving dates
        // End date - To be implemented, requires saving dates
        assertEquals(500, event.getDuration(), "Event duration should match");
        assertFalse(event.getFullDay(), "Event should not be full day");
        assertFalse(event.getStaticPos(), "Event should not have static position");
        assertEquals("default", event.getCalendar(), "Event calendar should match");
        assertEquals("", event.getRecurrenceRules(), "Event recurrence rules should match");
        assertEquals("", event.getRecurrenceEnd(), "Event recurrence end should match");
    }

    @Test
    public void testEventSetters() {
        // Test the setters of the Event class
        Event event = createTestEvent();
        event.setTitle("New Title");
        event.setDescription("New Description");
        event.setLocation("New Location");
        // Start time - To be implemented, requires saving dates
        // Start date - To be implemented, requires saving dates
        // End date - To be implemented, requires saving dates
        event.setDuration(1000);
        event.setFullDay(true);
        event.setStaticPos(true);
        event.setCalendar("New Calendar");
        event.setRecurrenceRules("New Recurrence Rules");
        event.setRecurrenceEnd("New Recurrence End");

        // Verify the changes
        assertEquals("New Title", event.getTitle(), "Event title should match");
        assertEquals("New Description", event.getDescription(), "Event description should match");
        assertEquals("New Location", event.getLocation(), "Event location should match");
        // Start time - To be implemented, requires saving dates
        // Start date - To be implemented, requires saving dates
        // End date - To be implemented, requires saving dates
        assertEquals(1000, event.getDuration(), "Event duration should match");
        assertTrue(event.getFullDay(), "Event should be full day");
        assertTrue(event.getStaticPos(), "Event should have static position");
        assertEquals("New Calendar", event.getCalendar(), "Event calendar should match");
        assertEquals("New Recurrence Rules", event.getRecurrenceRules(), "Event recurrence rules should match");
        assertEquals("New Recurrence End", event.getRecurrenceEnd(), "Event recurrence end should match");
    }

    @Test
    public void testInvalidEventConstructor() {
        // Test recurrence_rules and date_time
        // To be implemented
    }
}