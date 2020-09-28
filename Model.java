package bouncing_balls;



/** 
 * Group : 22
 * 
 * Author 1: Mohamad Qutbuddin Habib
 * Email   : gusqutmo@student.gu.se
 * personnr: 940127
 * 
 * Author 2: Ömer Faruk Simsek
 * email   : gussimsme@student.gu.se
 * personnr: 980509
 * 
 * Intyg:
 * Vi intygar härmed att vi har båda aktivt deltagit i att lösa vatje uppgift. Alla lösningar 
 * är i sin helhet vårt eget arbete, utan att ha tagit del av andra lösningar.
 * 
 */

class Model {
    double areaWidth, areaHeight;
    Ball [] balls;
    final double GRAVITY = -9.82;

    Model(double width, double height) {
        areaWidth = width;
        areaHeight = height;

        // Initialize the model with a few balls
        balls = new Ball[2];
		balls[0] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.2);
		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3);
    }

    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT
        for (Ball b : balls) {
            // detect collision with the border
            if ((b.x < b.radius && b.v.vx < 0) || (b.x > areaWidth - b.radius && b.v.vx > 0)) {
                b.v.vx *= -1; // change direction of ball
            }
            if ((b.y <= b.radius && b.v.vy < 0) || (b.y >= areaHeight - b.radius && b.v.vy > 0)){
                b.v.vy *= -1;
            }

           for (Ball b2 : balls){
               if (b != b2 && ifCollideBalls(b, b2)){
                    ballCollision(b,b2);
                }
            }

            // compute new position according to the speed of the ball
            b.x    += deltaT * b.v.vx;
            b.y    += deltaT * b.v.vy;
            b.v.vy += deltaT * GRAVITY;
        }
    }
    /**
     * 
     * @param b1 First ball
     * @param b2 Second ball
     * @return true or false depending if the distance between both balls are less or equal than the radius for both balls.
     */
    private boolean ifCollideBalls(Ball b1, Ball b2){
        double dist = Math.sqrt(Math.pow(b1.x-b2.x, 2) + Math.pow(b1.y - b2.y, 2));

        return (dist <= b1.radius + b2.radius);
    }
        /**
         * 
         * @param b1 Ball one
         * @param b2 Ball two
         * 
         * We don't return anything but we calculate using linear algebra, the new vector and update it after a collision. 
         */
    private void ballCollision(Ball b1, Ball b2){
        // We calculate the basis of the collision
        double BaseOne[] = {b2.x - b1.x, b2.y - b1.y};
        double BaseTwo[] = {-BaseOne[1], BaseOne[0]};
        double matrix[][] = {BaseOne, BaseTwo};              

        //Scale the inverse basis and compute multiplication with vectors.
        double v1[] = matrixMultiply(matrixInverse(matrix), new double[]{b1.v.vx, b1.v.vy});      
        double v2[] = matrixMultiply(matrixInverse(matrix), new double[]{b2.v.vx, b2.v.vy});      

        if (v1[0] > v2[0]){  // if they move towards each other
            updateVectors(b1,b2,matrix,v1,v2);
        }    
    }

        /**
         * 
         * @param b1 first ball
         * @param b2 second ball
         * @param matrix our basis.
         * @param v1 computed vector 
         * @param v2 computed vector  
         * 
         */
    private void updateVectors(Ball b1, Ball b2, double[][] matrix, double[] v1, double[] v2){
        double i = b1.mass * v1[0] + b2.mass * v2[0];
        double r = -(v2[0] - v1[0]);
        double newV1 = (i - b2.mass * r) / (b1.mass + b2.mass);
        double newV2 = r + (i - b2.mass * r) / (b1.mass + b2.mass);

        // Changing the basis back to standard basis.
        double vector1[] = matrixMultiply(matrix, new double[]{newV1, v1[1]});
        double vector2[] = matrixMultiply(matrix, new double[]{newV2, v2[1]});

        // We set/update the new velocities.
        b1.v.vx = vector1[0];
        b1.v.vy = vector1[1];
        b2.v.vx = vector2[0];
        b2.v.vy = vector2[1];

}
    /**
    * 
    * @param m matrix given to us
    * @param v array giben to us
    * @return the multiplication of them both. (array)
    */
    private static double[] matrixMultiply(double[][] m, double[] v) {
        double[] arr = {m[0][0] * v[0] + m[1][0] * v[1], m[0][1] * v[0] + m[1][1] * v[1]};
        
        return arr;
    }

    /**
    * @param m the matrix containing the base
    * @return the inverse matrix of the given matrix from the parameter.
    */
    private static double[][] matrixInverse(double[][] m) {
        double a = m[0][0];
        double b = m[0][1];
        double c = m[1][0];
        double d = m[1][1];

        //compute the determinant
        double det = calcDeterminant(a, b, c, d);

        // calculate the inverse of matrix 
        double[][] inverse = {{d/det,-b/det},
                             {-c/det, a/det}};
        return inverse;
    }
    private static double calcDeterminant (double a, double b, double c, double d){
            return a*d - b*c;
    }
}