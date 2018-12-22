package Drifter;

import java.awt.*;
import java.util.Random;

public class Map {

    static int centerx = Main.screenWidth / 2;
    static int centery = Main.screenHeight / 2;

    private static int[][] stars = new int[1200][3];

    public static int[][] rectanglePositions = {
            //top horizontal wall
            {-680,480,40,40},{-640,465,200,10},
            {-440,480,40,40},{-400,465,200,10},{-200,480,40,40},{-160,465,200,10},{40,480,40,40},
            {80,465,200,10},{280,480,40,40},{320,465,200,10},{520,480,40,40},
            //leftmost connection between hallways
            {-665,440,10,200},{-400,225,200,10},{-200,240,40,40},{-425,-40,10,200},{-440,-240,40,40},
            {-425,200,10,200},{-440,240,40,40},{-680,240,40,40},{-880,225,200,10},
            {-920,240,40,40},{-905,200,10,200},{-920,0,40,40},{-905,-40,10,200},{-920,-240,40,40},
            {-640,-15,200,10},{-680,-240,40,40},{-665,-40,10,200},
            {-905,-280,10,200},{-680,0,40,40},
            //middle horizontal wall
            {-440,0,40,40},{-400,-15,200,10},{-200,0,40,40},{-160,-15,200,10},{40,0,40,40},
            {80,-15,200,10},{280,0,40,40},{320,-15,200,10},{520,0,40,40},
            //rightmost wall
            {535,440,10,200},{520,240,40,40},{535,200,10,200},
            //bottom horizontal wall
            {-920,-480,40,40},{-880,-495,200,10},{-680,-480,40,40},{-640,-495,200,10},
            {-440,-480,40,40},{-400,-495,200,10},{-200,-480,40,40},{-160,-495,200,10},{40,-480,40,40},
            {80,-495,200,10},{280,-480,40,40},{320,-495,200,10},{520,-480,40,40}

    };

    private static int[][] backgroundRectangles = {
            //inside background
            {-180,460,720,960},{-660,460,481,241},{-900,220,481,241},
            {-900,-20,241,241},{-900,-260,721,241},{-420,-20,241,241},
            //airfield
            {-185,200,10,200},{-182,200,4,200},{-640,-255,200,10},{-640,-258,200,4},{535,-40,10,440},{538,-40,4,440}
    };
    private static Color[] backgroundRectangleColors = {
            //inside background
            Color.getHSBColor(0.7f,0.4f,0.2f),
            Color.getHSBColor(0.7f,0.4f,0.2f),Color.getHSBColor(0.7f,0.4f,0.2f),
            Color.getHSBColor(0.7f,0.4f,0.2f),Color.getHSBColor(0.7f,0.4f,0.2f),
            Color.getHSBColor(0.7f,0.4f,0.2f),
            //airfield
            Color.getHSBColor(0.6f,0.7f,0.9f),Color.getHSBColor(0.6f,0.2f,1f),
            Color.getHSBColor(0.6f,0.7f,0.9f),Color.getHSBColor(0.6f,0.2f,1f),
            Color.getHSBColor(0.6f,0.7f,0.9f),Color.getHSBColor(0.6f,0.2f,1f),
    };


    Map(){
        Random random = new Random();

        //center map to screen
        for (int i = 0; i < rectanglePositions.length; i++) {
            rectanglePositions[i][0] += centerx;
            rectanglePositions[i][1] = centery - rectanglePositions[i][1];
        }
        for(int i = 0; i < backgroundRectangles.length; i++) {
            backgroundRectangles[i][0] += centerx;
            backgroundRectangles[i][1] = centery - backgroundRectangles[i][1];
        }

        //randomly position stars
        for(int i = 0; i < stars.length; i++){

            stars[i][0] = random.nextInt(Main.screenWidth*2)-Main.screenWidth/2;
            stars[i][1] = random.nextInt(Main.screenHeight*2)-Main.screenHeight/2;
            int size = random.nextInt(10);
            if (size > 8) {
                size = 3;
            } else if (size > 5) {
                size = 2;
            } else {
                size = 1;
            }
            stars[i][2] =  size;

        }
    }

    static void draw(Graphics g) {
        //Stars
        g.setColor(Color.white);
        for (int[] star : stars) {
            ((Graphics2D) g).setStroke(new BasicStroke(star[2]));
            g.drawLine(star[0] - (int) (Player.playerX / 35.0f), star[1] - (int) (Player.playerY / 35.0f),
                    star[0] - (int) (Player.playerX / 35.0f), star[1] - (int) (Player.playerY / 35.0f));
        }

        //background rectangles
        for(int i = 0; i < backgroundRectangles.length; i++) {
            g.setColor(backgroundRectangleColors[i]);
            g.fillRect(backgroundRectangles[i][0]-(int)Player.playerX,
                    backgroundRectangles[i][1]-(int)Player.playerY,
                    backgroundRectangles[i][2],backgroundRectangles[i][3]);
        }

        //Rectangle Fill
        g.setColor(Color.gray);
        for (int[] rectanglePosition : rectanglePositions) {
            g.fillRect(rectanglePosition[0] - (int) Player.playerX, rectanglePosition[1] - (int) Player.playerY,
                    rectanglePosition[2], rectanglePosition[3]);
        }
        //Rectangle Lines
        g.setColor(Color.darkGray);
        ((Graphics2D)g).setStroke(new BasicStroke(2));
        for (int[] rectanglePosition : rectanglePositions) {
            g.drawRect(rectanglePosition[0] - (int) Player.playerX, rectanglePosition[1] - (int) Player.playerY,
                    rectanglePosition[2], rectanglePosition[3]);
        }
    }
}

