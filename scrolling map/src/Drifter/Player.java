package Drifter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

class Player extends JComponent implements MouseMotionListener, MouseListener {

    private Timer timer;

    private static boolean leftMousePressed = false;
    private static boolean rightMousePressed = false;
    private static int mouseX = 0;
    private static float shipphase = 0.33333f;
    private static int mouseY = 0;
    
    private static float correctedMouseX = 0.0f;
    private static float correctedMouseY = 0.0f;

    private static int centerx = Main.screenWidth / 2;
    private static int centery = Main.screenHeight / 2;

    static float playerX = 440.0f;
    static float playerY = -200.0f;
    private static float playerXSpeed = 0.0f;
    private static float playerYSpeed = 0.0f;
    private static float boost = 0.0f;
    private static float maxBoost = 99.0f;
    private static float boostRecharge = 0.0f;
    private static int[][] shipPositions = new int[2][8];
    private boolean reversePlayerXSpeed = false;
    private boolean reversePlayerYSpeed = false;

    static int boostFrame = 0;

    private long lastTime = System.nanoTime();
    private float deltaTime = 0.0f;
    private int counter = 0;

    private boolean justTeleported = false;
    private int counter2 = 0;
    Player() {
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    void timeStuff(){
        long thisTime = System.nanoTime();
        deltaTime = (float)(thisTime -lastTime)/1000000000.0f;
        lastTime = thisTime;
    }

    void playerMovement(){
        float movementSpeed = 18.0f;
        float magnitude;
        boolean collision;
        float friction = 2f;

        //Controls
        if (leftMousePressed) {
            if (boost < maxBoost && boostRecharge < 1) {
                counter++;
                if (counter>5) {
                    boost += maxBoost/25; //0.001f * counter * Math.pow(2000, counter); //maxBoost/30;
                }
            }

        } else {
            counter = 0;
            if (boost >= 1){
                correctedMouseX = mouseX - centerx;
                correctedMouseY = mouseY - centery;
                magnitude = (float) Math.sqrt(correctedMouseX * correctedMouseX + correctedMouseY * correctedMouseY);
                if (magnitude < 1.0f){
                    magnitude = 1.0f;
                }

                playerXSpeed += boost * movementSpeed * (correctedMouseX) / magnitude;
                playerYSpeed += boost * movementSpeed * (correctedMouseY) / magnitude;
                boostRecharge = boost;
            }
            boost = 0;
        }
        if (boost == 0 && boostRecharge > 0) {
            boostRecharge -= maxBoost/30;
        }


        //Map Collisions
        collision = false;
        Polygon shipPoly = new Polygon(shipPositions[0],shipPositions[1], shipPositions[0].length);
        for(int i = 0; i < Map.rectanglePositions.length; i++) {

            //create collision box from map
            Rectangle mappyRect = new Rectangle(Map.rectanglePositions[i][0]-(int)playerX,
                    Map.rectanglePositions[i][1]-(int)playerY,
                    Map.rectanglePositions[i][2], Map.rectanglePositions[i][3]);

            //check for collision
            if (shipPoly.intersects(mappyRect)) {
                collision = true;
                if(centerx <= mappyRect.x || centerx >= mappyRect.x + mappyRect.width){

                    //push player horizontally
                    if (mappyRect.x>centerx) {
                        playerX--;
                    } else if (mappyRect.x + mappyRect.width<centerx) {
                        playerX++;
                    }

                    //move player off wall horizontally
                    playerX -= playerXSpeed * deltaTime;

                    reversePlayerXSpeed = true;

                } else {

                    //push player vertically
                    if (mappyRect.y>centery) {
                        playerY--;
                    } else if (mappyRect.y + mappyRect.height<centery) {
                        playerY++;
                    }

                    //move player off wall vertically
                    playerY -= playerYSpeed * deltaTime;

                    reversePlayerYSpeed = true;

                }
            }
        }
        //physics
        if (!collision) {
            playerX = playerX + (playerXSpeed * deltaTime);
            playerY = playerY + (playerYSpeed * deltaTime);
        }

        playerXSpeed = playerXSpeed * (1.0f - (friction * deltaTime));
        playerYSpeed = playerYSpeed * (1.0f - (friction * deltaTime));


        if (reversePlayerXSpeed) {
            reversePlayerXSpeed = false;
            playerXSpeed = -playerXSpeed/5;
        }
        if (reversePlayerYSpeed) {
            reversePlayerYSpeed = false;
            playerYSpeed = -playerYSpeed/5;
        }

        //wormhole physics
        for (int i = 0; i < Wormholes.wormholeLocations.length; i++) {
            float x = Wormholes.wormholeLocations[i][0] - centerx - playerX;
            float y = (centery - Wormholes.wormholeLocations[i][1]) + playerY;
            float distanceToWormhole = (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            float directionToWormhole = 0.0f;

            if (x >= 0 && y >= 0) {
                directionToWormhole = (float) -Math.atan(y/x);
            } else if (x >= 0 && y < 0) {
                directionToWormhole = (float) (Math.PI/2 - Math.atan(-y/x));
            } else if (x < 0 && y < 0) {
                directionToWormhole = (float) (Math.PI - Math.atan(y/x));
            } else if (x < 0 && y >= 0) {
                directionToWormhole = (float) (-Math.PI/2 - Math.atan(-x/y));
            }

            if (justTeleported) {
                counter2++;
                if (counter2 > 100) {
                    counter2 = 0;
                    justTeleported = false;
                }
            }

            if (distanceToWormhole < 140.0f) {
                playerXSpeed += 10 * Math.cos(directionToWormhole);
                playerYSpeed += 10 * Math.sin(directionToWormhole);
            }
            if (distanceToWormhole < 20.0f && !justTeleported) {
                if ((i+1) % 2 == 0) {
                    playerX = Wormholes.wormholeLocations[i-1][0]-centerx;
                    playerY = Wormholes.wormholeLocations[i-1][1]-centery;
                } else {
                    playerX = Wormholes.wormholeLocations[i+1][0]-centerx;
                    playerY = 130; //Wormholes.wormholeLocations[i+1][1]-centery;
                }
                justTeleported = true;
            }
        }
        if (rightMousePressed) {
            playerX = 0;
            playerY = 0;
        }
    }

    static void ship() {
        correctedMouseX = mouseX - centerx;
        correctedMouseY = mouseY - centery;

        double direction = 0;
        if (correctedMouseX >= 0) {
            direction = Math.atan(correctedMouseY / correctedMouseX);
        } else {
            direction = Math.atan(correctedMouseY / correctedMouseX) + Math.PI;
        }

        if (boostRecharge > 0) {
            shipphase =  (1.0f / 3.0f) - (boostRecharge / (maxBoost*3.0f));
        }
        if (boost > 0) {
            shipphase = (1.0f / 3.0f) - (boost / (maxBoost*3.0f));
        }

        //Ship nose
        int size = 2;
        shipPositions[0][0] = (int)((12* size) * Math.cos(direction)) + centerx;
        shipPositions[1][0] = (int)((12* size) * Math.sin(direction)) + centery;
        //right
        //left
        //back point
        shipPositions[0][4] = (int)((6* size) * Math.cos(direction + Math.PI)) + centerx;
        shipPositions[1][4] = (int)((6* size) * Math.sin(direction + Math.PI)) + centery;

        //pointy point
        //right
        shipPositions[0][2] = (int)((10* size) * Math.cos(direction - Math.PI/4.5)) + centerx;
        shipPositions[1][2] = (int)((10* size) * Math.sin(direction - Math.PI/4.5)) + centery;
        //left
        shipPositions[0][6] = (int)((10* size) * Math.cos(direction + Math.PI/4.5)) + centerx;
        shipPositions[1][6] = (int)((10* size) * Math.sin(direction + Math.PI/4.5)) + centery;


        //wings
        //right
        shipPositions[1][3] = (int)((6* size) * Math.sin(direction - Math.PI/2)) + centery;
        shipPositions[0][3] = (int)((6* size) * Math.cos(direction - Math.PI/2)) + centerx;
        //left
        shipPositions[1][5] = (int)((6* size) * Math.sin(direction + Math.PI/2)) + centery;
        shipPositions[0][5] = (int)((6* size) * Math.cos(direction + Math.PI/2)) + centerx;
        //wing pockets
        //right
        shipPositions[1][1] = (int)((4* size) * Math.sin(direction - Math.PI/2.9)) + centery;
        shipPositions[0][1] = (int)((4* size) * Math.cos(direction - Math.PI/2.9)) + centerx;
        //left
        shipPositions[1][7] = (int)((4* size) * Math.sin(direction + Math.PI/2.9)) + centery;
        shipPositions[0][7] = (int)((4* size) * Math.cos(direction + Math.PI/2.9)) + centerx;

    }

    static void draw(Graphics g) {

//        g.setColor(Color.white);
//        g.drawString("X: "+playerX, 10,10);
//        g.drawString("Y: "+playerY,10,30);

        g.setColor(Color.green);
        ((Graphics2D)g).setStroke(new BasicStroke(1));
        g.fillPolygon(shipPositions[0], shipPositions[1], shipPositions[0].length);

        g.setColor(Color.getHSBColor(shipphase,1,0.75f));
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        Path2D.Double crv1 = new Path2D.Double();
        crv1.moveTo(shipPositions[0][4], shipPositions[1][4]);
        // create new QuadCurve2D.Float
        QuadCurve2D q = new QuadCurve2D.Float();
        // draw QuadCurve2D.Float with set coordinates
        q.setCurve(shipPositions[0][4], shipPositions[1][4], shipPositions[0][3], shipPositions[1][3], shipPositions[0][0], shipPositions[1][0]);
        ((Graphics2D)g).draw(q);

        // create new QuadCurve2D.Float
        QuadCurve2D e = new QuadCurve2D.Float();
        // draw QuadCurve2D.Float with set coordinates
        e.setCurve(shipPositions[0][4], shipPositions[1][4], shipPositions[0][5], shipPositions[1][5], shipPositions[0][0], shipPositions[1][0]);
        ((Graphics2D)g).draw(e);

        g.setColor(Color.red);
        if (leftMousePressed) {
            int bubbleSize = 20;
            //g.fillOval(currentx-5, currenty-5, bubbleSize, bubbleSize);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMousePressed = true;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightMousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            leftMousePressed = false;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightMousePressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
