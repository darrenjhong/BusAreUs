package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.exception.RouteException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Parser for bus data
public class BusParser {
    private Bus bus;


    /**
     * Parse buses from JSON response produced by TransLink query.  All parsed buses are
     * added to the given stop.  Bus location data that is missing any of the required
     * fields (RouteNo, Latitude, Longitude, Destination, RecordedTime) is silently
     * ignored and not added to stop. Bus that is on route that does not pass through
     * this stop is silently ignored and not added to stop.
     *
     * @param stop         stop to which parsed buses are to be added
     * @param jsonResponse the JSON response produced by Translink
     * @throws JSONException when:
     *                       <ul>
     *                       <li>JSON response does not have expected format (JSON syntax problem)</li>
     *                       <li>JSON response is not a JSON array</li>
     *                       </ul>
     */
    public static void parseBuses(Stop stop, String jsonResponse) throws JSONException {
        // TODO: implement this method
        JSONArray buses = new JSONArray(jsonResponse);

        for (int index = 0; index < buses.length(); index++) {
            JSONObject bus = buses.getJSONObject(index);

            parseBus(bus, stop);
        }
    }

    public static void parseBus(JSONObject bus, Stop stop) throws JSONException {
        String routeNo = bus.getString("RouteNo");
        String dir = bus.getString("Direction");
        String dest = bus.getString("Destination");
        Double lat = bus.getDouble("Latitude");
        Double lon = bus.getDouble("Longitude");
        String time = bus.getString("RecordedTime");

        Route r = RouteManager.getInstance().getRouteWithNumber(routeNo);
        Bus b = new Bus(r, lat, lon, dest, time);
        b.getRoute().addStop(stop);

        try {
            stop.addBus(b);
        } catch (RouteException e) {
            e.printStackTrace();
        }
    }
}

