package Drifter;

import java.awt.*;

public class Wormholes {

    private static float angle = 0.0f;
    private static float previousAngle = 0.0f;
    private static float maxLength = 80;
    private static float length = maxLength;

    static int[][] wormholeLocations = {{-300,100},{-540,-140}};

    static private int[][][] wormholePoints = new int[wormholeLocations.length][2][60];
    static private int[][][] shiftedWormholePoints = new int[wormholeLocations.length][2][60];


    Wormholes(){
        for (int i = 0; i <wormholeLocations.length; i++) {
            wormholeLocations[i][0] += Map.centerx;
            wormholeLocations[i][1] = Map.centery - wormholeLocations[i][1];
        }

        createWormholePoints();
    }

    static void createWormholePoints() {
        int lengthChange;
        previousAngle = angle;
        for (int i = 0; i < wormholePoints.length; i++) {
            lengthChange = 2;
            for (int j = 0; j < wormholePoints[0][0].length; j++) {
                if (j % 5 == 0) {
                    length = maxLength;
                    lengthChange = 2;
                }

                wormholePoints[i][0][j] = (int) (length * Math.cos(angle)) + wormholeLocations[i][0];
                wormholePoints[i][1][j] = (int) (length * Math.sin(angle)) + wormholeLocations[i][1];

                angle += Math.PI / 6;
                length -= lengthChange;
                lengthChange += 4;
            }
        }
        angle = previousAngle - 0.01f;
    }

    static void moveWormholes() {
        for (int i = 0; i < wormholePoints.length; i++) {
            for (int j = 0; j < wormholePoints[0][0].length; j++) {
                shiftedWormholePoints[i][0][j] = wormholePoints[i][0][j] - (int)Player.playerX;
                shiftedWormholePoints[i][1][j] = wormholePoints[i][1][j] - (int)Player.playerY;
            }
        }
    }

    static void draw(Graphics g){

        for (int i = 0; i < wormholePoints.length; i++) {
            g.setColor(Color.MAGENTA);
            g.fillPolygon(shiftedWormholePoints[i][0], shiftedWormholePoints[i][1], wormholePoints[0][0].length);
            g.setColor(Color.black);
            g.fillOval(wormholeLocations[i][0]-(int)Player.playerX-45,
                    wormholeLocations[i][1]-(int)Player.playerY-45,
                    90,90);
        }
    }
}
