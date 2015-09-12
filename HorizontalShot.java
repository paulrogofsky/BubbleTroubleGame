

import java.awt.*;

public class HorizontalShot extends Shots {

    private static final int INIT_VEL_X = 0;
    private static final int INIT_VEL_Y = -30;

    public HorizontalShot (int init_pos_x, int init_pos_y, 
            int courtWidth, int courtHeight, Color c) {
        super(INIT_VEL_X, INIT_VEL_Y, init_pos_x, init_pos_y, 
                30, 7, courtWidth, courtHeight,c);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawLine(pos_x, pos_y, pos_x + width,pos_y);
        g.drawLine(pos_x, pos_y + 3, pos_x+width, pos_y+3);
        g.drawLine(pos_x,pos_y+6, pos_x+width, pos_y+6);
    }

}