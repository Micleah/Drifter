package Drifter;
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
import java.awt.*;

public class Map {

<<<<<<< Updated upstream
    public static int[][] squarePositions = {{-200,-200},{200,200},{200,-200},{-200,200},
            {-350,200},{-200,350},{350,-200},{200,-350}};

    static int[][][] bigOlArray = new int[squarePositions.length][4][2];
    int squareSize = 50;
    static int centerx = Main.screenWidth / 2;
    static int centery = Main.screenHeight / 2;

    public Map(){
        for (int i=0; i< bigOlArray.length; i++){

            bigOlArray [i][0][0] = squarePositions[i][0]-squareSize;
            bigOlArray [i][0][1] = squarePositions[i][1]+squareSize;

            bigOlArray [i][1][0] = squarePositions[i][0]+squareSize;
            bigOlArray [i][1][1] = squarePositions[i][1]+squareSize;

            bigOlArray [i][2][0] = squarePositions[i][0]+squareSize;
            bigOlArray [i][2][1] = squarePositions[i][1]-squareSize;

            bigOlArray [i][3][0] = squarePositions[i][0]-squareSize;
            bigOlArray [i][3][1] = squarePositions[i][1]-squareSize;

        }


    }
    public static void draw(Graphics g) {

        g.setColor(Color.white);
        for (int i = 0; i < bigOlArray.length; i++) {
            for (int j = 0; j < bigOlArray[0].length; j++) {
                if (j == bigOlArray[0].length - 1) {
                    g.drawLine(centerx+bigOlArray[i][j][0]-(int)Player.playerX,
                            centery-bigOlArray[i][j][1]-(int)Player.playerY,
                            centerx+bigOlArray[i][0][0]-(int)Player.playerX,
                            centery-bigOlArray[i][0][1]-(int)Player.playerY);
                } else {
                    g.drawLine(centerx+bigOlArray[i][j][0]-(int)Player.playerX,
                            centery-bigOlArray[i][j][1]-(int)Player.playerY,
                            centerx+bigOlArray[i][j + 1][0]-(int)Player.playerX,
                            centery-bigOlArray[i][j + 1][1]-(int)Player.playerY);
                }
            }
=======
    public static int[][] rectanglePositions = {{-200,-200,100,100},{200,200,100,100},{200,-200,100,100},
            {-200,200,100,100},{-350,200,100,100},{-200,350,100,100},{350,-200,100,100},{200,-350,100,100},
            {-400,50,100,10},{-400,0,100,10}};

    private static int centerx = Main.screenWidth / 2;
    private static int centery = Main.screenHeight / 2;

    public Map(){
        for (int i = 0; i < rectanglePositions.length; i++) {
            rectanglePositions[i][0] += centerx;
            rectanglePositions[i][1] = centery - rectanglePositions[i][1];
        }
    }

    public static void draw(Graphics g) {

        g.setColor(Color.white);
        for (int i = 0; i < rectanglePositions.length; i++) {
            g.drawRect(rectanglePositions[i][0]-(int)Player.playerX, rectanglePositions[i][1]-(int)Player.playerY,
                    rectanglePositions[i][2],rectanglePositions[i][3]);
>>>>>>> Stashed changes
        }
    }
}

