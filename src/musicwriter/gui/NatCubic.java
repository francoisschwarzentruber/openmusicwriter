/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui;

/**
 *
 * @author Ancmin
 */


public class NatCubic extends ControlCurve {





/* calculates the natural cubic spline that interpolates
y[0], y[1], ... y[n]
The first segment is returned as
C[0].a + C[0].b*u + C[0].c*u^2 + C[0].d*u^3 0<=u <1
the other segments are in C[1], C[2], ...  C[n-1] */

  static Cubic[] calcNaturalCubic(int n, int[] x) {
    float[] gamma = new float[n+1];
    float[] delta = new float[n+1];
    float[] D = new float[n+1];
    int i;
    /* We solve the equation
       [2 1       ] [D[0]]   [3(x[1] - x[0])  ]
       |1 4 1     | |D[1]|   |3(x[2] - x[0])  |
       |  1 4 1   | | .  | = |      .         |
       |    ..... | | .  |   |      .         |
       |     1 4 1| | .  |   |3(x[n] - x[n-2])|
       [       1 2] [D[n]]   [3(x[n] - x[n-1])]

       by using row operations to convert the matrix to upper triangular
       and then back sustitution.  The D[i] are the derivatives at the knots.
       */

    gamma[0] = 1.0f/2.0f;
    for ( i = 1; i < n; i++) {
      gamma[i] = 1/(4-gamma[i-1]);
    }
    gamma[n] = 1/(2-gamma[n-1]);

    delta[0] = 3*(x[1]-x[0])*gamma[0];
    for ( i = 1; i < n; i++) {
      delta[i] = (3*(x[i+1]-x[i-1])-delta[i-1])*gamma[i];
    }
    delta[n] = (3*(x[n]-x[n-1])-delta[n-1])*gamma[n];

    D[n] = delta[n];
    for ( i = n-1; i >= 0; i--) {
      D[i] = delta[i] - gamma[i]*D[i+1];
    }

    /* now compute the coefficients of the cubics */
    Cubic[] C = new Cubic[n];
    for ( i = 0; i < n; i++) {
      C[i] = new Cubic((float)x[i], D[i], 3*(x[i+1] - x[i]) - 2*D[i] - D[i+1],
		       2*(x[i] - x[i+1]) + D[i] + D[i+1]);
    }
    return C;
  }


  
  /* draw a cubic spline */
  @Override
  public void paint(Graphics g){
    Polygon p = getPolygonCourbe(pts);
    for(int i = 0; i < p.getNbPoints() - 1; i++)
    {
        g.setPenWidth( Math.max(0.8f, 4.0f*(1.0f - ((float) Math.abs(2*i - p.getNbPoints())) / (float) p.getNbPoints())));
        g.drawLine(p.getX(i), p.getY(i), p.getX(i+1), p.getY(i+1));
    }
   // g.drawPolyline(p.xpoints, p.ypoints, p.npoints);
    
  }




    
  final static int STEPS = 12;



  static Polygon getPolygonCourbe(Polygon pointsChauds)
  {
     if (pointsChauds.getNbPoints() >= 2) {
         
         
      Cubic[] X = calcNaturalCubic(pointsChauds.getNbPoints()-1, pointsChauds.getXs());
      Cubic[] Y = calcNaturalCubic(pointsChauds.getNbPoints()-1, pointsChauds.getYs());

      /* very crude technique - just break each segment up into steps lines */
      Polygon p = RegionFactory.createPolygon();
      p.addPoint((int) Math.round(X[0].eval(0)),
		 (int) Math.round(Y[0].eval(0)));
      for (int i = 0; i < X.length; i++) {
	for (int j = 1; j <= STEPS; j++) {
	  float u = j / (float) STEPS;
	  p.addPoint(Math.round(X[i].eval(u)),
	             Math.round(Y[i].eval(u)));
	}
      }
      return p;
    }
     else
         return RegionFactory.createPolygon();
  }

    @Override
    public Area getArea() {
        Polygon p = getPolygonCourbe(pts);

        Area area = RegionFactory.createEmptyRegion();

        for(int i = 0; i < p.getNbPoints()-1; i++)
        {
            Point A = new Point(p.getX(i), p.getY(i));
            Point B = new Point(p.getX(i+1), p.getY(i+1));

            Polygon segmentplein = RegionFactory.createPolygon();

            segmentplein.addPoint(A.x - 5, A.y + 5);
            segmentplein.addPoint(A.x - 5, A.y - 5);
            segmentplein.addPoint(B.x + 5, B.y - 5);
            segmentplein.addPoint(B.x + 5, B.y + 5);

            area.add(RegionFactory.createRegion(segmentplein));
        }

        return area;
    }
}
