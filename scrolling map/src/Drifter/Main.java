package Drifter;
import javax.swing.*;


public class Main {

    static int screenWidth = 1200;
    static int screenHeight = 700;

    static JFrame window = new JFrame();

    public static void main(String[] args) {

        GameController gameController = new GameController();
        window.setBounds(10, 10, 1200, 700);
        window.setTitle("Drifter");
        window.setResizable(false);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(gameController);
    }
}
