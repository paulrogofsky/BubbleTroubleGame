

import java.awt.*;

public class Person extends GameObj {

    private static final int INIT_VEL_X = 0;
    private static final int INIT_VEL_Y = 0;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 30;
    public Color color;

    public Person (int init_pos_x, int init_pos_y, 
            int courtWidth, int courtHeight, Color c) {
        super(INIT_VEL_X, INIT_VEL_Y, init_pos_x, init_pos_y, 
                WIDTH, HEIGHT, courtWidth, courtHeight, 0);
        color = c;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawLine(pos_x+10,pos_y-10,pos_x+10,pos_y-20);
        g.drawLine(pos_x , pos_y, pos_x+10,pos_y-10);
        g.drawLine(pos_x+10,pos_y-10,pos_x+20,pos_y);
        g.drawOval(pos_x+8,pos_y-25,5,5);
    }

}