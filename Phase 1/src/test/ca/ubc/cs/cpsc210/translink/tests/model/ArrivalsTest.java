package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test Arrivals
 */
public class ArrivalsTest {
    private Route r;
    private Arrival a;
    private Arrival t;

    @BeforeEach
    public void setup() {
        r = RouteManager.getInstance().getRouteWithNumber("43");
        a = new Arrival(23, "Home", r);
        t = new Arrival(43, "Home", r);
    }

    @Test
    public void testConstructor() {
        assertEquals(23, a.getTimeToStopInMins());
        assertEquals(r, a.getRoute());
        assertEquals("Home", a.getDestination());
    }

    @Test
    public void testCompareTo(){
        assertEquals(20, t.compareTo(a));
    }

    @Test
    public void testGetStatus(){
        a.setStatus("On Schedule");
        assertEquals("On Schedule", a.getStatus());
    }

}
