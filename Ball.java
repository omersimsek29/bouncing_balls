package bouncing_balls;

public class Ball {
/**
     * Position, speed, and radius of the ball. You may wish to add other attributes.
     */
    double x, y, radius;
    Vector v;
    double mass;

    Ball(double x, double y, double vx, double vy, double r) {
        this.x = x;
        this.y = y;
        this.v = new Vector (vx,vy);
        this.radius = r;
        this.mass = (4/3)*Math.PI*(Math.pow(r, 3))*4;
    }
}
