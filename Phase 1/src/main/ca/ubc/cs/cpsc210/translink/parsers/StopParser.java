package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

/**
 * A parser for the data returned by Translink stops query
 */
public class StopParser {

    private String filename;

    public StopParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse stop data from the file and add all stops to stop manager.
     *
     */
    public void parse() throws IOException, StopDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseStops(dataProvider.dataSourceToString());
    }
    /**
     * Parse stop information from JSON response produced by Translink.
     * Stores all stops and routes found in the StopManager and RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)</li>
     *     <li>JSON data is not an array</li>
     * </ul>
     * If a JSONException is thrown, no stops should be added to the stop manager
     * @throws StopDataMissingException when
     * <ul>
     *  <li> JSON data is missing Name, StopNo, Routes or location (Latitude or Longitude) elements for any stop</li>
     * </ul>
     * If a StopDataMissingException is thrown, all correct stops are first added to the stop manager.
     */

    public void parseStops(String jsonResponse)
            throws JSONException, StopDataMissingException {
        // TODO: Task 4: Implement this method
        JSONArray stops = new JSONArray(jsonResponse);

        for (int index = 0; index < stops.length(); index++) {
            String name = stops.getJSONObject(index).getString("Name");
            int stopNo = stops.getJSONObject(index).getInt("StopNo");
            Double lat = stops.getJSONObject(index).getDouble("Latitude");
            Double lon = stops.getJSONObject(index).getDouble("Longitude");
            String routes = stops.getJSONObject(index).getString("Routes");

            if (stopNo == 0 || name.isEmpty() || lat == 0 || lon == 0 || routes.isEmpty()){
                throw new StopDataMissingException("Fields Missing");
            } else {
                Stop s = StopManager.getInstance().getStopWithNumber(stopNo);
                LatLon locn = new LatLon(lat, lon);

                s.setName(name);
                s.setLocn(locn);
            }
            parseRoutes(routes, stopNo);
        }

    }

    public void parseRoutes(String routes, int stopNo){
             Route r = RouteManager.getInstance().getRouteWithNumber(routes);
             Stop s = StopManager.getInstance().getStopWithNumber(stopNo);

             StopManager.getInstance().getStopWithNumber(stopNo).addRoute(r);
             RouteManager.getInstance().getRouteWithNumber(routes).addStop(s);
    }
}
