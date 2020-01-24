package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteTest {

    private Route r;
    private Route r2;
    private Route r3;
    private RoutePattern rp;
    private RoutePattern rp2;
    private RoutePattern rp3;
    private LatLon latlon;
    private Stop s1;
    private Stop s2;

    @BeforeEach
    public void setup(){
        r = new Route("84");
        rp = new RoutePattern("UBC/VCC STATION", "UBC", "West",r);
        rp2 = new RoutePattern("Broadway", "UBC", "West", r);
        rp3 = new RoutePattern("Hastings", "", "", r);
        latlon = new LatLon(1,1);
        s1 = new Stop(1, "Base", latlon);
        s2 = new Stop(2, "Home", latlon);
    }

    @Test
    public void testRoute(){
        assertEquals("84", r.getNumber());
        r.setName("UBC");
        assertEquals("UBC", r.getName());
        assertEquals("Route 84", r.toString());
    }

    @Test
    public void testStops(){
        r.addStop(s1);
        assertTrue(r.hasStop(s1));
        r.addStop(s2);
        r.removeStop(s2);
        assertFalse(r.hasStop(s2));

        List<Stop> stops = new LinkedList<>();
        stops.add(s1);

        assertEquals(stops, r.getStops());
    }

    @Test
    public void testEquals(){
        r2 = new Route("84");
        r3 = new Route("99");
        assertTrue(r.equals(r2));
        assertFalse(r.equals(r3));

    }


    @Test
    public void testGetPatterns1(){
        r.addPattern(rp);
        assertEquals(rp, r.getPattern("UBC/VCC STATION", "UBC", "West"));
        assertEquals(rp2, r.getPattern("Broadway", "UBC", "West"));
    }

    @Test
    public void testGetPatterns2(){
        r.addPattern(rp);
        assertEquals(rp, r.getPattern("UBC/VCC STATION"));
        assertEquals(rp3, r.getPattern("Hastings"));
    }

    @Test
    public void testGetPatterns3() {
        r.addPattern(rp);
        r.addPattern(rp2);
        r.addPattern(rp3);

        List<RoutePattern> RoutePatterns = new LinkedList<>();
        RoutePatterns.add(rp);
        RoutePatterns.add(rp2);
        RoutePatterns.add(rp3);

        assertEquals(RoutePatterns, r.getPatterns());

    }
}
