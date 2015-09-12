

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite extends GameObj {

    private static final int INIT_VEL_X = 0;
    private static final int INIT_VEL_Y = 0;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;
    
    private static BufferedImage img;

    public Sprite (int init_pos_x, int init_pos_y, 
            int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, init_pos_x, init_pos_y, 
                WIDTH, HEIGHT, courtWidth, courtHeight, 0);
        try {
            if (img == null) {
                img = ImageIO.read(new File("Herth_2.png"));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, pos_x, pos_y, WIDTH, HEIGHT, null); 
    }

}