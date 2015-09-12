import java.awt.Color;
import java.util.*;
import java.awt.*;


public class Wall extends GameObj {
    
    private final Color c = Color.GRAY;
    public WallType type;
    public boolean opened;
    public Direction dir;
    
    public Wall (int v_x, int v_y,int pos_x,int pos_y,int w, int h,
            int court_width, int court_height, WallType type, Direction dir) {
        super (v_x,v_y,pos_x,pos_y,w,h,court_width,court_height,0);
        this.type = type;
        opened = false;
        this.dir = dir;
    }
    
    @Override
    public void move () {
        if (canAdd()) {
            height += v_y;
            width += v_x;
        }
    }
    
    private boolean canAdd () {
        return !(pos_y + v_y >= max_y) && 
                !(pos_x + v_x >= max_x);
    }
   
    public void draw (Graphics g) {
        g.setColor(c);
        if (opened) {
            g.drawRect(pos_x, pos_y +height-55, width, 55);
            g.fillRect(pos_x, pos_y, width, height-55);
        }
        else g.fillRect(pos_x, pos_y, width, height);
    }
    
    public boolean openWall (ArrayList<Bubble> pops) {
        for (Bubble p : pops) {
            if ((dir == Direction.UP && p.pos_y > pos_y ) || 
                    (dir == Direction.RIGHT && p.pos_x  < pos_x) ||
                    (dir == Direction.LEFT && p.pos_x > pos_x )) 
                return false;
        }
        return true;
    }

}
