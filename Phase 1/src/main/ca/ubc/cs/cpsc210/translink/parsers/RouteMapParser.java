package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for routes stored in a compact format in a txt file
 */
public class RouteMapParser {
    private String fileName;

    public RouteMapParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parse the route map txt file
     */
    public void parse() {
        DataProvider dataProvider = new FileDataProvider(fileName);
        try {
            String c = dataProvider.dataSourceToString();
            if (!c.equals("")) {
                int posn = 0;
                while (posn < c.length()) {
                    int endposn = c.indexOf('\n', posn);
                    String line = c.substring(posn, endposn);
                    parseOnePattern(line);
                    posn = endposn + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse one route pattern, adding it to the route that is named within it
     * @param str
     *
     * Each line begins with a capital N, which is not part of the route number, followed by the
     * bus route number, a dash, the pattern name, a semicolon, and a series of 0 or more real
     * numbers corresponding to the latitude and longitude (in that order) of a point in the pattern,
     * separated by semicolons. The 'N' that marks the beginning of the line is not part of the bus
     * route number.
     */
    private void parseOnePattern(String str) {
        // TODO: Task 3: Implement this method
        String regex = "N(\\w+)-([^;]+);(.*)";
                 //(route number), (pattern name); (latlon)*;
                 // group 1           group2         group 3 (can be 0 or more)

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        matcher.matches(); // checks the str against the regex

        String rNum = matcher.group(1);
        String pName = matcher.group(2);
        String latlon = matcher.group(3);
        List<LatLon> listLatlon = new LinkedList<>();

        if (!latlon.isEmpty()) {
            Pattern newPattern = Pattern.compile("([^;]+);([^;]+)");
                                                 //lat   ;  lon
            matcher = newPattern.matcher(latlon);

            while (matcher.find()) {
                LatLon newLatlon = new LatLon(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)));

                listLatlon.add(newLatlon);
            }
        }
            else {
                listLatlon.clear();
            }
        storeRouteMap(rNum, pName, listLatlon);
    }

        /**
         * Store the parsed pattern into the named route
         * Your parser should call this method to insert each route pattern into the corresponding route object
         * There should be no need to change this method
         *
         * @param routeNumber       the number of the route
         * @param patternName       the name of the pattern
         * @param elements          the coordinate list of the pattern
         */
    private void storeRouteMap(String routeNumber, String patternName, List<LatLon> elements) {
        Route r = RouteManager.getInstance().getRouteWithNumber(routeNumber);
        RoutePattern rp = r.getPattern(patternName);
        rp.setPath(elements);
    }
}
