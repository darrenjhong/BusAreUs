package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.exception.RouteException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StopTest {
    private Stop s;
    private Stop s2;
    private Stop s3;
    private LatLon latlon;
    private LatLon latlon2;
    private Route r;
    private Route r2;
    private Arrival a;
    private Arrival a2;
    private Bus b;
    private Bus b2;


    @BeforeEach
    public void setup(){
        latlon = new LatLon(1,1);
        latlon2 = new LatLon(1,2);
        s = new Stop(100,"Renfrew", latlon);
        r = new Route("16");
        r2 = new Route("99");
        a = new Arrival(10,"Arbutus", r);
        b = new Bus(r, 1,1, "Arbutus", "1:00");
        b2 = new Bus(r2, 1, 2,"Broadway", "1:00");

    }

    @Test
    public void testStop(){
        assertEquals("Renfrew", s.getName());
        assertEquals(latlon, s.getLocn());
        assertEquals(100, s.getNumber());
        s.setName("Rupert");
        assertEquals("Rupert", s.getName());
        s.setLocn(latlon2);
        assertEquals(latlon2, s.getLocn());
    }

    @Test
    public void testRoutes(){
        s.addRoute(r);
        s.addRoute(r2);
        s.removeRoute(r);

        Set<Route> Routes = new HashSet<>();
        Routes.add(r2);

        assertEquals(Routes, s.getRoutes());
        assertTrue(s.onRoute(r2));
        assertFalse(s.onRoute(r));
    }

    @Test
    public void testArrivals(){
        a2 = new Arrival(15,"Arbutus", r);

        s.addArrival(a2);
        s.addArrival(a);

        List<Arrival> Arrivals = new LinkedList<>();
        Arrivals.add(a);
        Arrivals.add(a2);

        assertEquals(Arrivals, s.getArrivals());

        Arrivals.clear();
        s.clearArrivals();

        assertEquals(Arrivals, s.getArrivals());
    }

    @Test
    public void testBuses() throws RouteException {
        s.addRoute(r);
        s.addBus(b);

        List<Bus> Bus = new LinkedList<>();
        Bus.add(b);

        assertEquals(Bus, s.getBuses());

        s.clearBuses();
        Bus.clear();

        assertEquals(Bus, s.getBuses());
    }

    @Test
    public void testBusFail(){
        s.addRoute(r);

        try {
            s.addBus(b2);
            fail ("RouteException thrown");
        } catch (RouteException e){
            //expected
        }

        List<Bus> Bus = new LinkedList<>();

        assertEquals(Bus, s.getBuses());
    }

    @Test
    public void testEquals(){
        s2 = new Stop(100, "Renfrew", latlon);
        s3 = new Stop(99, "Renfrew", latlon2);

        assertTrue(s.equals(s2));
        assertFalse(s.equals(s3));

        assertEquals(100, s.hashCode());
    }
}
