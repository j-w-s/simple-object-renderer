package src.entities;

public class Player {
    private double x, y, z;  // position
    private double rotationX, rotationY;  // orientation angles

    private static final double MOVEMENT_SPEED = 0.5;

    public Player(double startX, double startY, double startZ) {
        this.x = startX;
        this.y = startY;
        this.z = startZ;
    }

    public void moveForward() {
        z += MOVEMENT_SPEED * Math.cos(rotationY);
        x += MOVEMENT_SPEED * Math.sin(rotationY);
    }
    
    public void moveBackward() {
        z -= MOVEMENT_SPEED * Math.cos(rotationY);
        x -= MOVEMENT_SPEED * Math.sin(rotationY);
    }
    
    public void moveLeft() {
        z += MOVEMENT_SPEED * Math.sin(rotationY);
        x -= MOVEMENT_SPEED * Math.cos(rotationY);
    }
    
    public void moveRight() {
        z -= MOVEMENT_SPEED * Math.sin(rotationY);
        x += MOVEMENT_SPEED * Math.cos(rotationY);
    }    

    // getters for position and rotation
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public double getRotationX() { return rotationX; }
    public double getRotationY() { return rotationY; }
}
