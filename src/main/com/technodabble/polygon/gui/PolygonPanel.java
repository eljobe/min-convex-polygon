package com.technodabble.polygon.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JPanel;

import com.technodabble.polygon.Solver;

public class PolygonPanel extends JPanel {
    public static final long serialVersionUID = 1;
    
    private Solver solver = null;
    private boolean done = false;
    
    public PolygonPanel(Solver solver) {
        super();
        this.solver = solver;
    }

    public void paintComponent(Graphics g) {
        clear(g);
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform cacheTransform = g2d.getTransform();
        g2d.rotate(Math.PI);
        g2d.translate(-250, -250);
        g2d.scale(-1, 1);        
        
        Line2D xAxis = new Line2D.Double(-245,0,245,0);
        Line2D yAxis = new Line2D.Double(0,-245,0,245);
        g2d.draw(yAxis);
        g2d.draw(xAxis);
        
        drawArrow(0,245,1,g2d);
        drawArrow(245,0,2,g2d);
        drawArrow(0,-245,3,g2d);
        drawArrow(-245,0,4,g2d);
                
        g2d.setColor(Color.RED);
        drawPoints(solver.getPoints(), g2d);
        
        //g2d.setColor(Color.GREEN);
        //drawPoint(solver.getPolygonCenter(),g2d);
        
        g2d.setColor(Color.BLUE);
        drawPoints(solver.getPolygon().getVertices(), g2d);
        Polygon bigPolygon = scaleUpPolygon(solver.getPolygon());
        g2d.draw(bigPolygon);
        
        if(done) {
            String finished = "Finished!";
            g2d.setTransform(cacheTransform);
            g2d.setColor(Color.RED);
            int scale = 8;
            g2d.scale(scale, scale);
            Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(finished, 0, finished.length(), g2d);
            double strWidth = bounds.getWidth();
            int xpos = (int)((500/2)/scale -(strWidth/2));
            int ypos = (int) (500/scale) - 1;
            g2d.drawString(finished, xpos, ypos );
        }
    }
    
    public void setDone(boolean newValue) {
        done = newValue;
    }
    
    private Polygon scaleUpPolygon(Polygon polygon) {
        int[] xCoords = new int[polygon.npoints];
        int[] yCoords = new int[polygon.npoints];
        int size = polygon.npoints;
        
        for(int i = 0; i < size; i++) {
            xCoords[i] = polygon.xpoints[i]*10;
            yCoords[i] = polygon.ypoints[i]*10;
        }
        
        return new Polygon(xCoords, yCoords, size);
    }
    
    private void drawPoints(Collection<Point2D> points, Graphics2D g2d) {
        Iterator<Point2D> iter = points.iterator();
        while(iter.hasNext()) {
            drawPoint(iter.next(), g2d);
        }
    }

    private void drawPoint(Point2D point, Graphics2D g2d) {
        double x = point.getX()*10;
        double y = point.getY()*10;
        Ellipse2D littlePoint = getPoint(x,y);
        g2d.draw(littlePoint);
        g2d.fill(littlePoint);
    }
    
    private void drawArrow(int x, int y, int orientation, Graphics2D g2d ) {
        Polygon arrow = new Polygon();
        int size = 5;
        int xFactor = 1;
        int yFactor = 1;
        boolean invertOnY = true;
        
        switch (orientation) {
        case 1:
            yFactor = -1;
            break;
        case 2:
            xFactor = -1;
            invertOnY = false;
            break;
        case 3:
            break;
        case 4:
            invertOnY = false;
            break;
        default:
            throw new IllegalArgumentException("Orientation should be 1,2,3, or 4.");            
        }
        
        arrow.addPoint(x, y);
        arrow.addPoint(x+(size*xFactor), y+(size*yFactor));
        if(invertOnY){
            xFactor = -xFactor;
        } else {
            yFactor = -yFactor;
        }
        arrow.addPoint(x+(size*xFactor), y+(size*yFactor));
        g2d.draw(arrow);
        g2d.fill(arrow);
    }
    
    public Ellipse2D getPoint (double x, double y) {
        return new Ellipse2D.Double(x-2,y-2,4,4);
    }

      // super.paintComponent clears offscreen pixmap,
      // since we're using double buffering by default.
      protected void clear(Graphics g) {
        super.paintComponent(g);
      }
}
