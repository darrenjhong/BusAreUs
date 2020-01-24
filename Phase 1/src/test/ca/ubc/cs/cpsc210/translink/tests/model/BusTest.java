package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BusTest {
    private Route r;
    private Bus b;
    private LatLon o;

    @BeforeEach
    public void setup(){
        r = RouteManager.getInstance().getRouteWithNumber("14");
        b = new Bus(r, 49.264067, -123.167150, "HASTINGS","12:52:08 pm");
        o = new LatLon(49.264067, -123.167150);
    }

    @Test
    public void testBus(){
        assertEquals(r, b.getRoute());
        assertEquals(o, b.getLatLon());
        assertEquals("HASTINGS", b.getDestination());
        assertEquals("12:52:08 pm", b.getTime());
    }
}
