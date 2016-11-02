package com.technodabble.polygon;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PointFileReader {
    
    public static Set<Point2D> readPointsFromFile(File pointFile) {
        Set<Point2D> points = new HashSet<Point2D>();
        try {
            BufferedReader buff = new BufferedReader( new FileReader(pointFile));
            String line = null;
            while((line = buff.readLine()) != null) {
                String[] coords = line.split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                points.add(new Point2D.Double(x,y));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return points;
    }

}
