package Drifter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


public class Player extends JComponent implements MouseMotionListener, MouseListener {

    static boolean leftMousePressed = false;
    static boolean rightMousePressed = false;
    private static int mouseX = 0;
    public static float shipphase;
    private static int mouseY = 0;
    
    static float correctedMouseX = 0.0f;
    static float correctedMouseY = 0.0f;

    static int centerx = Main.screenWidth / 2;
    static int centery = Main.screenHeight / 2;

    public static float playerX = 0.0f;
    public static float playerY = 0.0f;
    static float playerXSpeed = 0.0f;
    static float playerYSpeed = 0.0f;
    float movementSpeed = 18.0f;
    float friction = 0.4f;

    static double direction = 0;
    static int[][] shipPositions = new int[2][8];
    static int size = 4;

    long thisTime;
    private long lastTime = System.nanoTime();
    private float deltaTime = 0.0f;
    private float oneSecondCount = 0.0f;

    public Player() {
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    static void ship () {
        correctedMouseX = mouseX - centerx;
        correctedMouseY = mouseY - centery;

        if (correctedMouseX >= 0) {
            direction = Math.atan(correctedMouseY / correctedMouseX);
        } else {
            direction = Math.atan(correctedMouseY / correctedMouseX) + Math.PI;
        }
        if (leftMousePressed) {
            if (shipphase > 0) {
                shipphase -= 0.01;
            }
        } else {
            if (shipphase < 0.333333) {
                shipphase += 0.01;
            }
        }


        //Ship nose
        shipPositions[0][0] = (int)((12*size) * Math.cos(direction)) + centerx;
        shipPositions[1][0] = (int)((12*size) * Math.sin(direction)) + centery;
            //right
            //left
        //back point
        shipPositions[0][4] = (int)((6*size) * Math.cos(direction + Math.PI)) + centerx;
        shipPositions[1][4] = (int)((6*size) * Math.sin(direction + Math.PI)) + centery;

        //pointy point
            //right
        shipPositions[0][2] = (int)((10*size) * Math.cos(direction - Math.PI/4.5)) + centerx;
        shipPositions[1][2] = (int)((10*size) * Math.sin(direction - Math.PI/4.5)) + centery;
            //left
        shipPositions[0][6] = (int)((10*size) * Math.cos(direction + Math.PI/4.5)) + centerx;
        shipPositions[1][6] = (int)((10*size) * Math.sin(direction + Math.PI/4.5)) + centery;


        //wings
            //right
        shipPositions[1][3] = (int)((6*size) * Math.sin(direction - Math.PI/2)) + centery;
        shipPositions[0][3] = (int)((6*size) * Math.cos(direction - Math.PI/2)) + centerx;
            //left
        shipPositions[1][5] = (int)((6*size) * Math.sin(direction + Math.PI/2)) + centery;
        shipPositions[0][5] = (int)((6*size) * Math.cos(direction + Math.PI/2)) + centerx;
        //wing pockets
            //right
        shipPositions[1][1] = (int)((4*size) * Math.sin(direction - Math.PI/2.9)) + centery;
        shipPositions[0][1] = (int)((4*size) * Math.cos(direction - Math.PI/2.9)) + centerx;
            //left
        shipPositions[1][7] = (int)((4*size) * Math.sin(direction + Math.PI/2.9)) + centery;
        shipPositions[0][7] = (int)((4*size) * Math.cos(direction + Math.PI/2.9)) + centerx;
    }


    public void playerMovement(){
        //Controls
        if (leftMousePressed == true) {
            System.out.println(playerXSpeed);
            correctedMouseX = mouseX - centerx;
            correctedMouseY = mouseY - centery;
            float magnitude = (float) Math.sqrt(correctedMouseX * correctedMouseX + correctedMouseY * correctedMouseY);
            if (magnitude < 1.0f){
                magnitude = 1.0f;
            }
            playerXSpeed += movementSpeed * (correctedMouseX) / magnitude;
            playerYSpeed += movementSpeed * (correctedMouseY) / magnitude;
        }

        //physics
        playerX = playerX + (playerXSpeed * deltaTime);
        playerY = playerY + (playerYSpeed * deltaTime);

        playerXSpeed = playerXSpeed * (1.0f - (friction * deltaTime));
        playerYSpeed = playerYSpeed * (1.0f - (friction * deltaTime));

    }

    public void timeStuff(){
        thisTime = System.nanoTime();
        deltaTime = (float)(thisTime -lastTime)/1000000000.0f;
        lastTime = thisTime;
    }

    public static void draw (Graphics g) {
        int currentx = shipPositions[0][4];
        int currenty = shipPositions[1][4];
        ship();
        g.setColor(Color.green);
        ((Graphics2D)g).setStroke(new BasicStroke(1));
        g.drawPolygon(shipPositions[0], shipPositions[1], shipPositions[0].length);
        g.setColor(Color.getHSBColor(shipphase,1,0.75f));
        ((Graphics2D) g).setStroke(new BasicStroke(5));
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
            g.fillOval(currentx-5, currenty-5, bubbleSize, bubbleSize);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMousePressed = true;
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
            rightMousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            leftMousePressed = false;
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
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
