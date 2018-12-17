package Drifter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Player extends JComponent implements MouseMotionListener, MouseListener {

    boolean leftMousePressed = false;
    boolean rightMousePressed = false;
    private static int mouseX = 0;
    private static int mouseY = 0;
    
    static float correctedMouseX = 0.0f;
    static float correctedMouseY = 0.0f;

    static int centerx = Main.screenWidth / 2;
    static int centery = Main.screenHeight / 2;

    public static float playerX = 0.0f;
    public static float playerY = 0.0f;
    float playerXSpeed = 0.0f;
    float playerYSpeed = 0.0f;
    float movementSpeed = 18.0f;
    float friction = 0.4f;

    static double direction = 0;
    static int[][] shipPositions = new int[4][2];

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

        shipPositions[0][0] = (int)(12 * Math.cos(direction)) + centerx;
        shipPositions[0][1] = (int)(12 * Math.sin(direction)) + centery;
        shipPositions[1][0] = (int)(6 * Math.cos(direction + Math.PI/2)) + centerx;
        shipPositions[1][1] = (int)(6 * Math.sin(direction + Math.PI/2)) + centery;
        shipPositions[2][0] = (int)(6 * Math.cos(direction + Math.PI)) + centerx;
        shipPositions[2][1] = (int)(6 * Math.sin(direction + Math.PI)) + centery;
        shipPositions[3][0] = (int)(6 * Math.cos(direction - Math.PI/2)) + centerx;
        shipPositions[3][1] = (int)(6 * Math.sin(direction - Math.PI/2)) + centery;
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
        oneSecondCount += deltaTime;
        if (oneSecondCount >= 0.2f) {
            oneSecondCount = 0.0f;
        }
    }

    public static void draw (Graphics g) {
        ship();
        g.setColor(Color.green);
        ((Graphics2D)g).setStroke(new BasicStroke(3));
        g.drawLine(shipPositions[0][0], shipPositions[0][1], shipPositions[1][0], shipPositions[1][1]);
        g.drawLine(shipPositions[1][0], shipPositions[1][1], shipPositions[2][0], shipPositions[2][1]);
        g.drawLine(shipPositions[2][0], shipPositions[2][1], shipPositions[3][0], shipPositions[3][1]);
        g.drawLine(shipPositions[3][0], shipPositions[3][1], shipPositions[0][0], shipPositions[0][1]);
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
