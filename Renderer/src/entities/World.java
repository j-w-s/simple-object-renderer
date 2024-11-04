package src.entities;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class World {
    private Player player;
    private Camera camera;
    private List<double[][]> objectList;  
    private double[][] vertices;   
    private int[][] edges;    
    private static double fieldOfView = 75.0;
    private double[][] cachedAllVertices;
    private long lastVerticesUpdate = 0;
    private static final long updateInterval = 100; // update interval in ms to prevent unnecessary calculations

    public World() {
        player = new Player(0, 0, 30);
        camera = new Camera();

        loadObjFile("C:/Users/Space/OneDrive/Desktop/Renderer/src/entities/eyeball.obj");


        objectList = new ArrayList<>();
        initializeObjects();
    }

    private void loadObjFile(String filePath) {
        List<double[]> vertexList = new ArrayList<>();
        List<int[]> edgeList = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("v ")) {
                    String[] tokens = line.split("\\s+");
                    double[] vertex = new double[]{
                        Double.parseDouble(tokens[1]),
                        Double.parseDouble(tokens[2]),
                        Double.parseDouble(tokens[3])
                    };
                    vertexList.add(vertex);
                } else if (line.startsWith("f ")) {
                    String[] tokens = line.split("\\s+");
                    int[] face = new int[tokens.length - 1];
                    for (int i = 1; i < tokens.length; i++) {
                        String[] vertexData = tokens[i].split("/"); 
                        face[i - 1] = Integer.parseInt(vertexData[0]) - 1; 
                    }
                    for (int i = 0; i < face.length; i++) {
                        int next = (i + 1) % face.length;
                        edgeList.add(new int[]{face[i], face[next]});
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("OBJ file not found: " + filePath);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in OBJ file.");
        }

        vertices = vertexList.toArray(new double[0][0]);
        edges = edgeList.toArray(new int[0][0]);
    }

    private void initializeObjects() {
        double[][] positions = {
            {2, 0, 2},
        };

        for (double[] pos : positions) {
            objectList.add(createObject(pos[0], pos[1], pos[2]));
        }
    }

    private double[][] createObject(double x, double y, double z) {
        double[][] objectVertices = new double[vertices.length][3];
        for (int i = 0; i < vertices.length; i++) {
            objectVertices[i][0] = vertices[i][0] + x;
            objectVertices[i][1] = vertices[i][1] + y;
            objectVertices[i][2] = vertices[i][2] + z;
        }
        return objectVertices;
    }

    public Player getPlayer() {
        return player;
    }

    public Camera getCamera() {
        return camera;
    }

    public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
        g2d.setColor(Color.GREEN);
    
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastVerticesUpdate > updateInterval || cachedAllVertices == null) {
            List<double[]> allVertices = new ArrayList<>();
            for (double[][] objectVertices : objectList) {
                for (double[] vertex : objectVertices) {
                    allVertices.add(vertex);
                }
            }
            cachedAllVertices = allVertices.toArray(new double[0][0]);
            lastVerticesUpdate = currentTime;
        }
    
        // pre-calculate camera position once per frame
        double[] cameraPosition = camera.getPosition(player);
        double nearPlane = camera.calculateNear(cachedAllVertices, cameraPosition);
        
        double aspectRatio = (double) screenWidth / screenHeight;
        double fovRad = Math.toRadians(fieldOfView);
        double scale = Math.tan(fovRad / 2) * Camera.focalLength;
    
        for (double[][] objectVertices : objectList) {
            Point[] projectedPoints = new Point[objectVertices.length];
    
            for (int i = 0; i < objectVertices.length; i++) {
                double[] transformedPoint = camera.transformPoint(objectVertices[i], player);
                // use pre-calculated near plane and camera position
                projectedPoints[i] = projectPoint(transformedPoint, screenWidth, screenHeight, 
                                               scale, aspectRatio, nearPlane);
            }
    
            for (int[] edge : edges) {
                Point point1 = projectedPoints[edge[0]];
                Point point2 = projectedPoints[edge[1]];
                if (point1 != null && point2 != null) {
                    g2d.drawLine(point1.x, point1.y, point2.x, point2.y);
                }
            }
        }
    }

    private Point projectPoint(double[] point3D, int screenWidth, int screenHeight, 
                             double scale, double aspectRatio, double near) {
        double zAdjusted = Math.max(near, point3D[2]);
        
        double zInverse = 1.0 / zAdjusted;
        double x2d = (point3D[0] * zInverse * scale * aspectRatio) + screenWidth / 2.0;
        double y2d = (point3D[1] * zInverse * scale) + screenHeight / 2.0;
        
        return new Point((int) x2d, (int) y2d);
    }
}
