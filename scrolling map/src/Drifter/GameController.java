package Drifter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameController extends JPanel implements KeyListener, ActionListener{
    Timer timer;
    Graphics g;
    private Map map;
    private Player player;

    public GameController(){

        player = new Player();
        Main.window.add(player);

        map = new Map();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);


        timer = new Timer(20, this);
        timer.start();

    }
    public void paint (Graphics g){
        // Anti aliasing
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //Background
        g.setColor(Color.black);
        g.fillRect(0,0, 5000, 5000);

        Map.draw(g);

        Player.draw(g);

        g.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        player.timeStuff();
        player.playerMovement();

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
