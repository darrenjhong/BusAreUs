package ca.ubc.cs.cpsc210.translink.model;

import ca.ubc.cs.cpsc210.translink.model.exception.StopException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import ca.ubc.cs.cpsc210.translink.util.SphericalGeometry;

import java.util.*;

/**
 * Manages all bus stops.
 *
 * Singleton pattern applied to ensure only a single instance of this class that
 * is globally accessible throughout application.
 */
// TODO: Task 2: Complete all the methods of this class

public class StopManager implements Iterable<Stop> {
    public static final int RADIUS = 10000;
    private static StopManager instance;

    // Use this field to hold all of the stops.
    // Do not change this field or its type, as the iterator method depends on it
    private Map<Integer, Stop> stopMap;
    private LatLon locn;
    private Stop selected;


    /**
     * Constructs stop manager with empty collection of stops and null as the selected stop
     */
    private StopManager() {
        this.stopMap = new HashMap<>();
        this.locn = new LatLon(49.28359,-123.12215); // Burrard and Robson;
    }

    /**
     * Gets one and only instance of this class
     *
     * @return  instance of class
     */
    public static StopManager getInstance() {
        // Do not modify the implementation of this method!
        if(instance == null) {
            instance = new StopManager();
        }

        return instance;
    }

    public Stop getSelected() {
        return this.selected;
    }

    /**
     * Get stop with given number, creating it and adding it to the collection of all stops if necessary.
     * If it is necessary to create a new stop, then provide it with an empty string as its name,
     * and a default location somewhere in the lower mainland as its location.
     *
     * In this case, the correct name and location of the stop will be provided later
     *
     * @param number  the number of this stop
     *
     * @return  stop with given number
     */
    public Stop getStopWithNumber(int number) {
        if (this.stopMap.containsKey(number)){
            return this.stopMap.get(number);

        } else {
                Stop newStop = new Stop(number, "", this.locn);
                this.stopMap.put(number, newStop);
                return this.stopMap.get(number);
            }

    }

    /**
     * Get stop with given number, creating it and adding it to the collection of all stops if necessary,
     * using the given name and location
     *
     * @param number  the number of this stop
     * @param name   the name of this stop
     * @param locn   the location of this stop
     *
     * @return  stop with given number
     */
    public Stop getStopWithNumber(int number, String name, LatLon locn) {
        if (this.stopMap.containsKey(number)){
            return this.stopMap.get(number);

        } else {
                Stop newStop = new Stop(number, name, locn);
                this.stopMap.put(number, newStop);
                return this.stopMap.get(number);
            }
    }


    /**
     * Set the stop selected by user
     *
     * @param selected   stop selected by user
     * @throws StopException when stop manager doesn't contain selected stop
     */
    public void setSelected(Stop selected) throws StopException {
        if (!this.stopMap.containsValue(selected)) {
            throw new StopException("No such stop: " + selected.getNumber() + " " + selected.getName());
        } else {
            this.selected = selected;
        }
    }

    /**
     * Clear selected stop (selected stop is null)
     */
    public void clearSelectedStop() {
       this.selected = null;
    }

    /**
     * Get number of stops managed
     *
     * @return  number of stops added to manager
     */
    public int getNumStops() {
        return this.stopMap.size();
    }

    /**
     * Remove all stops from stop manager
     */
    public void clearStops() {
        this.stopMap.clear();
    }

    /**
     * Find nearest stop to given point.  Returns null if no stop is closer than RADIUS metres.
     *
     * @param pt  point to which nearest stop is sought
     * @return    stop closest to pt but less than RADIUS away; null if no stop is within RADIUS metres of pt
     */
    public Stop findNearestTo(LatLon pt) {
        Stop nextStop = null;

            for (Stop s: StopManager.getInstance()) {
                SphericalGeometry SG = new SphericalGeometry();
                double distBetween = SG.distanceBetween(pt, s.getLocn());
                double maxBound = this.RADIUS;

                if (distBetween <= maxBound){
                    maxBound = distBetween;
                    nextStop = s;
                }
            }
        return nextStop;
        }

    @Override
    public Iterator<Stop> iterator() {
        // Do not modify the implementation of this method!
        return stopMap.values().iterator();
    }
}
