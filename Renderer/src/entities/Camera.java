package src.entities;

public class Camera {
    public static double focalLength = 80.0;
    private double distance = -2.0; 
    private double angleHorizontal = 0.0;
    private double angleVertical = 0.0;

    public double[] getPosition(Player player) {
        double cameraX = player.getX() + distance * Math.cos(angleVertical) * Math.sin(angleHorizontal);
        double cameraY = player.getY() + distance * Math.sin(angleVertical);
        double cameraZ = player.getZ() + distance * Math.cos(angleVertical) * Math.cos(angleHorizontal);

        return new double[]{cameraX, cameraY, cameraZ};
    }

    public double[] transformPoint(double[] point, Player player) {
        double[] transformed = point.clone();
        double[] cameraPosition = getPosition(player);

        transformed[0] -= cameraPosition[0];
        transformed[1] -= cameraPosition[1];
        transformed[2] -= cameraPosition[2];

        transformed = multiplyMatrix(rotationY(-angleHorizontal), transformed);
        transformed = multiplyMatrix(rotationX(-angleVertical), transformed);

        return transformed;
    }

    private double[] multiplyMatrix(double[][] matrix, double[] point) {
        double[] result = new double[point.length];
        for (int i = 0; i < matrix.length; i++) {
            result[i] = 0;
            for (int j = 0; j < point.length; j++) {
                result[i] += matrix[i][j] * point[j];
            }
        }
        return result;
    }

    private double[][] rotationX(double angle) {
        return new double[][]{
            {1, 0, 0},
            {0, Math.cos(angle), -Math.sin(angle)},
            {0, Math.sin(angle), Math.cos(angle)}
        };
    }

    private double[][] rotationY(double angle) {
        return new double[][]{
            {Math.cos(angle), 0, Math.sin(angle)},
            {0, 1, 0},
            {-Math.sin(angle), 0, Math.cos(angle)}
        };
    }

    public double calculateNear(double[][] sceneObjects, double[] cameraPosition) {
        double minDistance = Double.MAX_VALUE;
    
        for (double[] object : sceneObjects) {
            double distance = Math.sqrt(
                Math.pow(object[0] - cameraPosition[0], 2) +
                Math.pow(object[1] - cameraPosition[1], 2) +
                Math.pow(object[2] - cameraPosition[2], 2)
            );
    
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
    
        return Math.max(0.1, minDistance);
    }

    public void rotateAroundPlayer(double deltaAngleHorizontal, double deltaAngleVertical) {
        angleHorizontal = (angleHorizontal + deltaAngleHorizontal) % (2 * Math.PI);
        angleVertical = Math.max(-Math.PI/2, Math.min(Math.PI/2, angleVertical + deltaAngleVertical));
    }
}

