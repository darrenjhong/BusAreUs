package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;

    public RouteParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse route data from the file and add all route to the route manager.
     *
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }
    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)
     *     <li>JSON data is not an array
     * </ul>
     * If a JSONException is thrown, no routes should be added to the route manager
     *
     * @throws RouteDataMissingException when
     * <ul>
     *  <li>JSON data is missing RouteNo, Name, or Patterns element for any route</li>
     *  <li>The value of the Patterns element is not an array for any route</li>
     *  <li>JSON data is missing PatternNo, Destination, or Direction element for any route pattern</li>
     * </ul>
     * If a RouteDataMissingException is thrown, all correct routes are first added to the route manager.
     */

    public void parseRoutes(String jsonResponse)
            throws JSONException, RouteDataMissingException {
        // TODO: Task 4: Implement this method
        JSONArray routes = new JSONArray(jsonResponse);

        for (int index = 0; index < routes.length(); index++) {
            String name = routes.getJSONObject(index).getString("Name");
            String routeNo = routes.getJSONObject(index).getString("RouteNo");
            JSONArray patterns = routes.getJSONObject(index).getJSONArray("Patterns");

            if (name.isEmpty() || routeNo.isEmpty() || patterns == null) {
                throw new RouteDataMissingException("Fields Missing");
            } else {
                Route r = RouteManager.getInstance().getRouteWithNumber(routeNo);
                r.setName(name);
                parsePattern(patterns, r);
            }
        }
    }


    public void parsePattern(JSONArray routePatterns, Route route) throws JSONException, RouteDataMissingException {

        for (int index = 0; index < routePatterns.length(); index++) {
            String patNo = routePatterns.getJSONObject(index).getString("PatternNo");
            String dest = routePatterns.getJSONObject(index).getString("Destination");
            String dir = routePatterns.getJSONObject(index).getString("Direction");

            if (patNo.isEmpty() || dest.isEmpty() || dir.isEmpty()) {
                throw new RouteDataMissingException("Fields Missing");
            } else {
                RoutePattern rp = new RoutePattern(patNo, dest, dir, route);
                route.addPattern(rp);
            }
        }

    }

}