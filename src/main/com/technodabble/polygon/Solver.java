package com.technodabble.polygon;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The Solver class encapsulates the algorithm of processing the
 * points and creating the minimum convex polygon which contains
 * all of the points.
 */
public class Solver {
    private Set<Point2D> thePoints = new HashSet<Point2D>();
    private ExpandablePolygon thePolygon = new ExpandablePolygon();
    
    public Solver(Set<Point2D> points) {
        thePoints = points;
    }
    
    public void cleanPoints() {
        Set<Point2D> pointsToRemove = new HashSet<Point2D>();
        Iterator<Point2D> iter = thePoints.iterator();
        while (iter.hasNext()) {
            Point2D point = iter.next();
            if(thePolygon.contains(point)){
                pointsToRemove.add(point);
            }
        }
        thePoints.removeAll(pointsToRemove);
    }
    
    private Point2D farthestPointFromPolygon() {
        if(thePolygon.npoints == 0) {
            return minPoint(thePoints);
        }
        Point2D farthest = null;
        double maxDistance = -1;
        Iterator<Point2D> iter = thePoints.iterator();
        while(iter.hasNext()){
            Point2D nextPoint = iter.next();
            double distance = thePolygon.ptShapeDist(nextPoint);
            if(distance > maxDistance){
                maxDistance = distance;
                farthest = nextPoint;
            }
        }
        return farthest;
    }

    public Set<Point2D> getPoints() {
        return thePoints;
    }

    public ExpandablePolygon getPolygon() {
        return thePolygon;
    }

    private Point2D minPoint(Set<Point2D> pointSet) {
        Point2D min = null;
        Iterator<Point2D> iter = pointSet.iterator();
        while (iter.hasNext()) {
            Point2D next = iter.next();
            if (min == null || min.getX() > next.getX() ||
                min.getX() == next.getX() && min.getY() > next.getY()) {
                min = next;
            }
        }
        return min;
    }
        
    public String toString() {
        StringBuffer buff = new StringBuffer("Polygon Points");
        appendPoints(thePolygon.getVertices(),buff);
        return buff.toString();
    }

    private void appendPoints(Collection<Point2D> points, StringBuffer buff) {
        Iterator<Point2D> iter = points.iterator();
        while (iter.hasNext()) {
            Point2D nextPoint = iter.next();
            buff.append(System.getProperty("line.separator"));
            buff.append(nextPoint.getX() + "," + nextPoint.getY());
        }
    }

    public boolean processNextPoint() {
        if(!thePoints.isEmpty()){
            Point2D nextPoint = farthestPointFromPolygon();
            if (!thePolygon.contains(nextPoint)) {
                thePolygon.addPoint(nextPoint);
            }
            cleanPoints();
        }
        return thePoints.isEmpty();
    }

    public void solve() {
        while (!processNextPoint());
    }
}
