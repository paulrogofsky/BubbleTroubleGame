import java.awt.Color;


public class Shots extends GameObj {
    public Color color;
    public Shots (int init_v_x, int init_v_y, int init_pos_x, int init_pos_y, 
            int w, int h,
            int courtWidth, int courtHeight, Color c) {
        super(init_v_x,init_v_y,init_pos_x,init_pos_y,w,h,courtWidth,courtHeight
                ,0);
        color = c;
    }
}
