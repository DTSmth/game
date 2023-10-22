import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // Screen settings
    final int originalTileSize = 16;
    final int scale = 3;
    final int tileSize = originalTileSize * scale; //48x48 pixels
    final int maxScreenCol = 16;
    final int maxScreenRom = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRom; // 576 pixels

    int fps = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    // Set players default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed= 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenWidth));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/fps; // 0.01666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            long currentTime = System.nanoTime();

            update();

            repaint();

            double remainingTime = nextDrawTime - System.nanoTime();
            remainingTime /= 1000000;

            try {
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void update() {

        if(keyH.upPressed == true) {
            playerY -= playerSpeed;
        } else if (keyH.downPressed == true) {
            playerY += playerSpeed;
        } else if (keyH.leftPressed == true) {
            playerX -= playerSpeed;
        } else if (keyH.rightPressed == true) {
            playerX += playerSpeed;
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.white);

        g2.fillRect(playerX, playerY, tileSize, tileSize);

        g2.dispose();
    }
}
