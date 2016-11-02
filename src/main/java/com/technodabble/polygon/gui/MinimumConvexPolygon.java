package com.technodabble.polygon.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.technodabble.polygon.PointFileReader;
import com.technodabble.polygon.Solver;

public class MinimumConvexPolygon extends MouseAdapter {
    
    public static final long serialVersionUID = 1;
    private Solver theSolver = null;
    private PolygonPanel drawingPane = null;
    
    public MinimumConvexPolygon(Solver solver) {
        theSolver = solver;
    }
    
    public Container createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        
        drawingPane = new PolygonPanel(theSolver);
        drawingPane.setPreferredSize(new Dimension(500,500));
        drawingPane.setBackground(Color.WHITE);
        drawingPane.addMouseListener(this);
        
        contentPane.add(drawingPane, BorderLayout.CENTER);

        return contentPane;
    }
    
    public void mouseReleased(MouseEvent me) {
        drawingPane.setDone(theSolver.processNextPoint());
        drawingPane.repaint();
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI(Solver solver) {
        //Create and set up the window.
        JFrame frame = new JFrame("Minimum Convex Polygon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MinimumConvexPolygon application = new MinimumConvexPolygon(solver);
        frame.setContentPane(application.createContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private static void usage() {
        StringBuffer msg = new StringBuffer();
        final String nl = System.getProperty("line.separator");
        msg.append("MinimumConvexPolygon Usage:").append(nl).
                   append(" java -jar MinimumConvexPolygon.jar filename [-command]");
        System.err.println(msg.toString());
    }

    /**
     * @param args
     *            The single argument for this program should be the data file
     *            to use which has one entry on each line which is the
     *            coordinates of a point in the x,y plane.
     */
    public static void main(String[] args) {
        if (!validateArgs(args)) {
            usage();
        } else {
            Set<Point2D> points = PointFileReader.readPointsFromFile(new File(args[0]));
            final Solver my_solver = new Solver(points);
            if(args.length == 2) {
                my_solver.solve();
                System.out.println(my_solver);
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        createAndShowGUI(my_solver);
                    }
                });
            }
        }
    }
    
    private static boolean validateArgs(String [] args){
        boolean retval = true;
        if(args.length < 1) {
            return false;
        }
        if(args.length > 2) {
            return false;
        }
        File f = new File(args[0]);
        if(!f.exists()) {
            return false;
        }
        if(args.length == 2 && !args[1].equals("-command")){
            return false;
        }
        return retval;
    }
}
