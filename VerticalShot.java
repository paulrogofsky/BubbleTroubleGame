

import java.awt.*;

public class VerticalShot extends Shots {

    public int endPosY;

    public VerticalShot (int init_pos_x, int init_pos_y, 
            int courtWidth, int courtHeight, Color c) {
        super(0, -15, init_pos_x, init_pos_y, 
                3, 1 , courtWidth, courtHeight,c);
        endPosY=init_pos_y;
    }
    
    @Override
    public void move () {
        endPosY += v_y;
        height = pos_y-endPosY;
    }  
    
    public boolean intersects (GameObj obj) {
        return (obj.pos_x <= pos_x && obj.pos_x + obj.width>= pos_x && 
            (obj.pos_y <= pos_y&& obj.pos_y + obj.height >=endPosY));
    }
    
    public void clip () { 
        if (pos_x < 0) pos_x = 0;
        else if (pos_x > max_x) pos_x = max_x;

        if (pos_y < 0) pos_y = 0;
        else if (pos_y > max_y) pos_y = max_y;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawLine(pos_x, pos_y, pos_x,endPosY); 
        g.drawLine(pos_x-2, endPosY+2, pos_x, endPosY);
        g.drawLine(pos_x+2,endPosY+2,pos_x,endPosY);
    }

}