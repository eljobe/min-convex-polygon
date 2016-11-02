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

First install the application:

```sh
> ./gradlew install
```

Now, you can run it like so:

```sh
> build/install/min-convex-polygon/bin/min-convex-polygon src/test/resources/points-005.txt
```

This launches a GUI which advances through the algorithm one iteration each time you click the mouse.
