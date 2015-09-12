

import java.awt.*;

public class PowerUp extends GameObj {

    private static final int INIT_VEL_X = 0;
    private static final int INIT_VEL_Y = 0;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 40;
    public Color color;
    private PowerUpListener pul;
    private int expirationTime;
    public int time_start;
    private String text;

    public PowerUp (int init_pos_x, int init_pos_y, int courtWidth, 
            int courtHeight, Color c, int time_start, int expirationTime, 
            PowerUpListener pul, String text) {
        super(INIT_VEL_X, INIT_VEL_Y, init_pos_x, init_pos_y, 
                WIDTH, HEIGHT, courtWidth, courtHeight, 1);
        color = c;
        this.pul = pul;
        this.expirationTime = expirationTime;
        this.time_start = time_start;
        this.text = text;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(pos_x, pos_y, width, height); 
        g.setColor(Color.BLACK);
        g.drawString(text, pos_x, pos_y);
    }
    
    public boolean hasExpired(int currTime) {
        return expirationTime<=currTime-time_start;
    }
    
    public void act () {
        pul.act();
    }

}