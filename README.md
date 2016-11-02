# Minimum Convex Polygon Algorithm

The challenge is to write an algorithm which starts out with a list of
points and returns an ordered list of those points which make up the
vertices of a polygon which contains all of the points. This polygon
must have the minimum possible area, and should not include three
consecutive points which are on the same line.

The algorithm I used to solve this problem works like this:

```
polygon = pointWithMinimumXCoordinate();
points.removePointWithMinimumXCoordinate();
while(points.hasMorePoints()) {
   farthestPoint = null;
   maximumDistance = 0;
   foreach point in points {
      foreach side in polygon {
         minimumDistance = HUGE_NUMBER;
         if (distanceFromPointToSide(point, side) < minimumDistance) {
            minimumDistance = distanceFromPointToSide(point, side);
         }
      }
      if (minimumDistance < maximumDistance) {
         farthestPoint = point;
         maximumDistance = minimumDistance;
      }
   }
   polygon.insert(farthestPoint);
   points.removePointsContainedBy(polygon);
}
```

In english, that boils down to these easy steps:

 * Start your polygon with the farthest left point.
 * Do these steps until you run out of points:
  * Find the point which is farthest from the closest side of the polygon.
  * Insert that point between the vertices of the polygon that make that closest line.
  * Remove any points from the list which now lie inside the polygon.
 * Print out the vertices of the polygon

## How to build and run the program

This is currently out of date and will be replaced with gradle commands in a few commits. Please hold.

Make sure you are running java 1.5.0 or better:
> java -version
cd to the directory where you unzipped the zip file.
Run the program:
> java -jar MinimumConvexPolygon.jar test/points-005.txt
Each time you click the mouse in the GUI, the program will advance one step.
If you want to run the program and just get the vertices of the polygon as output, run it like this:
> java -jar MinimumConvexPolygon.jar test/points-005.txt -command
