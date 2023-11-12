package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class AgendaTest {
    Agenda agenda;
    
    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // January 5, 2021
    LocalDate jan_5_2021 = LocalDate.of(2021, 1, 5);

    // November 1st, 2020, 22:30
    LocalDateTime nov_1__2020_22_30 = LocalDateTime.of(2020, 11, 1, 22, 30);

    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);

    // A simple event
    // November 1st, 2020, 22:30, 120 minutes
    Event simple = new Event("Simple event", nov_1__2020_22_30, min_120);

    // A Weekly Repetitive event ending at a given date
    RepetitiveEvent fixedTermination = new FixedTerminationEvent("Fixed termination weekly", nov_1__2020_22_30, min_120, ChronoUnit.WEEKS, jan_5_2021);

    // A Weekly Repetitive event ending after a give number of occurrrences
    RepetitiveEvent fixedRepetitions = new FixedTerminationEvent("Fixed termination weekly", nov_1__2020_22_30, min_120, ChronoUnit.WEEKS, 10);
    
    // A daily repetitive event, never ending
    // November 1st, 2020, 22:30, 120 minutes
    RepetitiveEvent neverEnding = new RepetitiveEvent("Never Ending", nov_1__2020_22_30, min_120, ChronoUnit.DAYS);

    @BeforeEach
    public void setUp() {
        agenda = new Agenda();
        agenda.addEvent(simple);
        agenda.addEvent(fixedTermination);
        agenda.addEvent(fixedRepetitions);
        agenda.addEvent(neverEnding);
    }
    
    @Test
    public void testMultipleEventsInDay() {
        assertEquals(4, agenda.eventsInDay(nov_1_2020).size(), "Il y a 4 événements ce jour là");
        assertTrue(agenda.eventsInDay(nov_1_2020).contains(neverEnding));
    }

    @Test
    public void testFindByTitle() {
        // 0 event
        String title = "No event";
        ArrayList<Event> byTitle = new ArrayList<Event>();
        assertEquals(byTitle, agenda.findByTitle(title), "il y a 0 event de ce nom dans l'agenda");

        // 1 event
        String title2 = "Simple event";
        byTitle.add(simple);
        assertEquals(byTitle, agenda.findByTitle(title2), "il y a 1 event de ce nom dans l'agenda");

        // pluseurs event
        LocalDateTime nov_1__2020_23_30 = LocalDateTime.of(2020, 11, 1, 23, 30);
        Event e = new Event("Simple event", nov_1__2020_23_30, min_120);
        agenda.addEvent(e);
        byTitle.add(e);
        assertEquals(byTitle, agenda.findByTitle(title2), "il y a 2 event de ce nom dans l'agenda");
    }

    @Test
    public void testisFreeFor() {
        // nov_1__2020_22_30 => 2H (00h30) date de simple

        LocalDateTime nov_1__2020_21_30 = LocalDateTime.of(2020, 11, 1, 21, 30);
        LocalDateTime nov_1__2020_23_30 = LocalDateTime.of(2020, 11, 1, 23, 30);
        LocalDateTime nov_1__2020_22_00 = LocalDateTime.of(2020, 11, 1, 22, 00);
        LocalDateTime nov_2__2020_02_00 = LocalDateTime.of(2020, 11, 1, 2, 00);
        Duration min_30 = Duration.ofMinutes(30);
        Duration min_240 = Duration.ofMinutes(240);


        Event e = new Event("start before", nov_1__2020_23_30, min_120);
        assertEquals(false,agenda.isFreeFor(e), "simple commence avant e mais finit pendant e");

        Event e1 = new Event("Same start ", nov_1__2020_22_30, min_120);
        assertEquals(false,agenda.isFreeFor(e1), "simple et e commence en mm temps");

        Event e2 = new Event("Start during simple", nov_1__2020_23_30, min_120);
        assertEquals(false,agenda.isFreeFor(e2), "simple commence pendant e finit après e");

        Event e3 = new Event("Out", nov_1__2020_22_00, min_240);
        assertEquals(false,agenda.isFreeFor(e3), "e commence avant simple finit après simple");

        Event e4 = new Event("In", nov_1__2020_23_30, min_30);
        assertEquals(false,agenda.isFreeFor(e4), "e commence pdt simple finit pdt simple");

        Event e5 = new Event("Commence et finit av", nov_1__2020_21_30, min_30);
        assertEquals(true,agenda.isFreeFor(e5), "e commence et finit avant simple");

        Event e6 = new Event("Commence apres la fin de simple", nov_2__2020_02_00, min_30);
        assertEquals(true,agenda.isFreeFor(e6), "e commence et finit apres simple");

    }




}
