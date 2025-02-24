package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {

    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop         stop to which parsed arrivals are to be added
     * @param jsonResponse the JSON response produced by Translink
     * @throws JSONException                when:
     *                                      <ul>
     *                                      <li>JSON response does not have expected format (JSON syntax problem)</li>
     *                                      <li>JSON response is not an array</li>
     *                                      </ul>
     * @throws ArrivalsDataMissingException when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {
        // TODO: Task 4: Implement this method
        JSONArray arrivals = new JSONArray(jsonResponse);

        for (int index = 0; index < arrivals.length(); index++) {
            String routeName = arrivals.getJSONObject(index).getString("RouteName");
            String routeNo = arrivals.getJSONObject(index).getString("RouteNo");
            JSONArray schedules = arrivals.getJSONObject(index).getJSONArray("Schedules");

            parseSchedules(schedules, stop, routeNo);
        }
    }

    public static void parseSchedules(JSONArray schedules, Stop stop, String routeNo) throws JSONException, ArrivalsDataMissingException {

        for (int index = 0; index < schedules.length(); index++) {
            int expCount = schedules.getJSONObject(index).getInt("ExpectedCountdown");
            String dest = schedules.getJSONObject(index).getString("Destination");
            String status = schedules.getJSONObject(index).getString("ScheduleStatus");

            if (expCount == 0 || dest.isEmpty() || status.isEmpty()) {
                throw new ArrivalsDataMissingException("Fields Missing");
            } else {
                Route r = RouteManager.getInstance().getRouteWithNumber(routeNo);
                Arrival a = new Arrival(expCount, dest, r);

                a.setStatus(status);
                stop.addArrival(a);
            }
        }
    }
}
