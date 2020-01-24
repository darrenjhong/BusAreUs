package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoutePatternTest {
    private RoutePattern rp;
    private RoutePattern rp2;
    private RoutePattern rp3;
    private Route r;
    private LatLon lt1;
    private LatLon lt2;
    private List<LatLon> path;

    @BeforeEach
    public void setup(){
        r = RouteManager.getInstance().getRouteWithNumber("84");
        rp = new RoutePattern("UBC/VCC STATION", "UBC", "West",r);
        lt1 = new LatLon(1,2);
        lt2 = new LatLon(1,3);
        path = new LinkedList<LatLon>();
    }

    @Test
    public void testConstructor(){
        assertEquals("UBC/VCC STATION", rp.getName());
        assertEquals("UBC", rp.getDestination());
        assertEquals("West", rp.getDirection());
    }

    @Test
    public void testEquals(){
        rp2 = new RoutePattern("UBC/VCC STATION", "VCC Station", "East", r);
        rp3 = new RoutePattern("Hastings", "UBC", "West", r);
        assertTrue(rp.equals(rp2));
        assertFalse(rp.equals(rp3));

        assertEquals(1589850353, rp.hashCode());
    }

    @Test
    public void testGetPath(){
        path.add(lt1);
        path.add(lt2);
        rp.setPath(path);
        assertEquals(path, rp.getPath());
    }

    @Test
    public void testSetPlaces(){
        rp.setDirection("East");
        assertEquals("East", rp.getDirection());

        rp.setDestination("Home");
        assertEquals("Home", rp.getDestination());
    }
}
