package com.technodabble.polygon;

import java.awt.Polygon;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExpandablePolygon extends Polygon {
    
    static final long serialVersionUID = 1;
    
    public ExpandablePolygon() {
        super();
    }
    
    public ExpandablePolygon(int[] xCoords, int[] yCoords, int size) {
        super(xCoords,yCoords,size);
    }    

    public boolean contains(Point2D point) {
        boolean retval = false;
        if(super.contains(point)){
            retval = true;
        } else {
            int[] xCoords = new int[npoints];
            int[] yCoords = new int[npoints];
            double[] coords = new double[6];
            int index = 0;
            PathIterator iter = getPathIterator(null);
            while (!iter.isDone()) {
                int type = iter.currentSegment(coords);
                if (type == PathIterator.SEG_MOVETO) {
                    xCoords[index] = (int) coords[0];
                    yCoords[index] = (int) coords[1];
                    index++;
                } else if (type == PathIterator.SEG_LINETO ||
                           type == PathIterator.SEG_CLOSE) {
                    Line2D segment = new Line2D.Double(xCoords[index - 1],
                                                       yCoords[index - 1],
                                                       coords[0],
                                                       coords[1]);
                    if (segment.ptSegDist(point) == 0) {
                        retval = true;
                    }
                    if (type != PathIterator.SEG_CLOSE) {
                        xCoords[index] = (int) coords[0];
                        yCoords[index] = (int) coords[1];
                        index++;
                    }
                }
                iter.next();
            }
        }
        return retval;
    }
    
    public void addPoint(Point2D newPoint) {
        Line2D closest = getClosestSide(newPoint);
        
        int newSize = npoints + 1;
        int[] xCoords = new int[newSize];
        int[] yCoords = new int[newSize];
        
        boolean pointInserted = false;
        int index = 0;
        
        if(npoints == 0) {
            xCoords[index] = (int)newPoint.getX();
            yCoords[index] = (int)newPoint.getY();
            index++;
        }
        List<Point2D> vertices = getVertices();
        if(!vertices.isEmpty()) {
            xCoords[index] = (int)vertices.get(index).getX();
            yCoords[index] = (int)vertices.get(index).getY();
            index++;
        }
        Iterator<Line2D> sideIter = getSides().iterator();
        while(sideIter.hasNext()){
            Line2D currSide = sideIter.next();
            if(currSide.getP1().equals(closest.getP1()) &&
                    currSide.getP2().equals(closest.getP2())){
                xCoords[index] = (int)newPoint.getX();
                yCoords[index] = (int)newPoint.getY();
                index++;
                pointInserted = true;
            }
            if(index < newSize){
                xCoords[index] = (int)currSide.getP2().getX();
                yCoords[index] = (int)currSide.getP2().getY();
                index++;
            }
        }
        if(index == newSize - 1 && !pointInserted){
            xCoords[index] = (int)newPoint.getX();
            yCoords[index] = (int)newPoint.getY();
            index++;
        }
        reset();
        for(int i = 0; i < newSize; i++) {
            addPoint(xCoords[i], yCoords[i]);
        }
    }
    
    public double ptShapeDist(Point2D point) {
        double distance = -1;
        Line2D closest = getClosestSide(point);
        if(closest == null) {
            distance = point.distance(getVertices().get(0));
        } else {
            distance = closest.ptSegDist(point);
        }
        return distance;
    }
    
    public Line2D getClosestSide(Point2D point){
        Line2D closest = null;
        double minDistance = -1;
        Iterator<Line2D> sideIter = getSides().iterator();
        while(sideIter.hasNext()){
            Line2D currSide = sideIter.next();
            double currDistance = currSide.ptSegDist(point);
            if (minDistance == -1 ||
                    minDistance > currDistance) {
                minDistance = currDistance;
                closest = currSide;
            }
        }
        return closest;
    }
    
    public List<Line2D> getSides() {
        List<Line2D> sides = new LinkedList<Line2D>();
        int[] xCoords = xpoints;
        int[] yCoords = ypoints;
        int size = npoints;
        if (npoints <= 1) {
            return sides;
        }
        for(int i = 1; i < size; i++) {
            sides.add(new Line2D.Double(xCoords[i-1], yCoords[i-1],
                                        xCoords[i], yCoords[i]));
        }
        sides.add(new Line2D.Double(xCoords[size-1], yCoords[size-1],
                                    xCoords[0], yCoords[0]));    
        return sides;
    }

    public List<Point2D> getVertices() {
        List<Point2D> retval = new LinkedList<Point2D>();
        PathIterator iter = this.getPathIterator(null);
        double[] coords = new double[6];
        while (!iter.isDone()) {
            int type = iter.currentSegment(coords);
            if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO) {
                retval.add(new Point2D.Double(coords[0], coords[1]));
            }
            iter.next();
        }
        return retval;
    }
}
