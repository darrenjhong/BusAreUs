package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.model.exception.StopException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Test the StopManager
 */
public class StopManagerTest {

    @BeforeEach
    public void setup() {
        StopManager.getInstance().clearStops();
    }

    @Test
    public void testBasic() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop r = StopManager.getInstance().getStopWithNumber(9999);
        assertEquals(s9999, r);
    }
    @Test
    public void testMore(){
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop r = StopManager.getInstance().getStopWithNumber(9999, "My house", new LatLon(-49.2, 123.2));

        assertEquals(s9999, r);

    }

    @Test
    public void testSelected(){
        Stop s1 = new Stop(100, "Home", new LatLon(1, 1));
        Stop r = StopManager.getInstance().getStopWithNumber(100);

        try {
            StopManager.getInstance().setSelected(s1);
        } catch (StopException e) {
            fail("StopException Should not be thrown");
        }

        assertEquals(s1, StopManager.getInstance().getSelected());


        StopManager.getInstance().clearSelectedStop();

    }

    @Test
    public void testSelectedFail(){
        Stop s1 = new Stop(100, "Home", new LatLon(1, 1));

        try {
            StopManager.getInstance().setSelected(s1);
            fail("StopException e");
        } catch (StopException e){
            // expected
        }
    }

    @Test
    public void testFindNearestTo(){
        Stop s = new Stop(100, "Home", new LatLon(1, 1));
        StopManager.getInstance().getStopWithNumber(100, "Home", new LatLon(1, 1));

        assertEquals(s, StopManager.getInstance().findNearestTo(new LatLon(1,1)));
    }
}
