

import java.awt.*;
import java.util.*;

public class Bubble extends GameObj {
    
    private int bounceVelocity;
    public BubbleType type;
    private Color color;
    private static final int bubbleVelocity = 3;
    private static final Color [] colors = {
        Color.BLUE,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.PINK,Color.RED};
    private static final int [] bounceVelocities = {-11,-15,-18,-20,-21,-22};
    
    public Bubble (int size, int init_pos_x, int init_pos_y, int init_vel_y,
            int courtWidth, int courtHeight, BubbleType type, int mult) {
        super (mult*bubbleVelocity,init_vel_y,init_pos_x,init_pos_y,
                20*(size-1)+8,20*(size-1)+8,courtWidth,courtHeight,0);
        color = colors[size-1];
        this.type = type;
        bounceVelocity = bounceVelocities[size-1];
        if (type == BubbleType.REGULAR ) 
            a_y = 1;
        else if (type == BubbleType.BOUNCY) {
            a_y = 1; 
            bounceVelocity = -29;
        }
        else if (type == BubbleType.UPWARDBOUNCE) {
            a_y = -1; 
            bounceVelocity = 29;
            }
        else if (type == BubbleType.BOX) {
            v_y = -6; 
            v_x = 6*mult;
        }
    }
    
    @Override
    public void bounce (Direction d) {
        if (d == null) return;
        switch (d) {
        case UP:   
            if (type == BubbleType.UPWARDBOUNCE) v_y = bounceVelocity;
            else v_y = Math.abs(v_y+a_y); 
            break; 
        case DOWN:  
            if (type == BubbleType.REGULAR || type == BubbleType.BOUNCY) 
                v_y = bounceVelocity;
            else v_y = -Math.abs(v_y-a_y);
            break;
        case LEFT:  v_x = -Math.abs(v_x); break;
        case RIGHT: v_x = Math.abs(v_x); break;
        }
    }
    
    @Override
    public void clip(){
        if (pos_x < 0) {
            pos_x = 0;  
            v_x = Math.abs(v_x);
            }
        else if (pos_x > max_x) {
            pos_x = max_x; 
            v_x = -Math.abs(v_x);
        }
        if (pos_y < 0) {
            pos_y = 0; 
            if (type == BubbleType.UPWARDBOUNCE) v_y = bounceVelocity;
            else v_y = Math.abs(v_y+a_y);
        }
        else if (pos_y > max_y) {
            pos_y = max_y;
            if (type == BubbleType.REGULAR || type == BubbleType.BOUNCY) 
                v_y = bounceVelocity;
            else v_y = bounceVelocity;
         }
    }
    public ArrayList <Bubble> splitted (int velUp, int cw, int ch) {
        int size = (height-8)/20;
        ArrayList <Bubble>splits = new ArrayList <Bubble>();
        if (size < 1 || size > 6) return splits;
        int vel_y = -10;
        if (type == BubbleType.REGULAR) vel_y = velUp;
        splits.add(new Bubble (size,pos_x,pos_y,vel_y,cw,ch,type,1));
        splits.add(new Bubble(size,pos_x,pos_y,vel_y,cw,ch,type,-1));
        return splits;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        if (type == BubbleType.BOX) g.fillRect(pos_x,pos_y,width,height);
        else g.fillOval(pos_x, pos_y, width, height); 
    }
    

}