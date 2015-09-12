import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")

public class GameCourt extends JPanel {

    private final ArrayList <PowerUp> possiblePowerUps;
    private ArrayList <Bubble> currentBubbles;
    private ArrayList <Wall> currentWalls;
    private ArrayList <PowerUp> powerUps; 
    private Shots shots;
    private shotMode shotmode;
    private Sprite sprite;
    private final ArrayList <Integer> startPositions;
    private final ArrayList <Integer> maxPowerUps;
    private final ArrayList <Integer> timeGiven;
    private int totalPops;
    
    public int lives;
    public int level;
    private int currTime;
    private int points;
    public Timer timer;
    private JLabel status;
    private ArrayList <Integer> hiScores;
    
    public boolean playing = false;  
    public static final int COURT_WIDTH = 1000;
    public static final int COURT_HEIGHT = 500;
    private static final int SPRITE_VELOCITY = 7;
    private static final int INTERVAL = 40; 
    public final int totalLevels = 18;
    public boolean instructions;
    public boolean highScores;

    public GameCourt(JLabel status){
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        timer = new Timer(INTERVAL, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                tick();
            }
        });
        timer.start();

        setFocusable(true);

        addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    sprite.v_x = -SPRITE_VELOCITY;
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    sprite.v_x = SPRITE_VELOCITY;
                else if (e.getKeyCode() == KeyEvent.VK_SPACE && shots == null) {
                    if (shotmode == shotMode.VERTICAL ) 
                        shots =new VerticalShot(sprite.pos_x+15,sprite.pos_y+42,
                                COURT_WIDTH,COURT_HEIGHT,Color.GRAY);
                    if (shotmode == shotMode.HORIZONTAL )
                        shots = new HorizontalShot(sprite.pos_x+15,sprite.pos_y,
                                COURT_WIDTH,COURT_HEIGHT,Color.GRAY);
                }
            }
            public void keyReleased(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_LEFT || 
                        e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    sprite.v_x = 0;
                    sprite.v_y = 0;
                }
            }
        });
        //sprite time
        sprite = new Sprite 
                (COURT_WIDTH/2-15,COURT_HEIGHT-50,COURT_WIDTH,COURT_HEIGHT);
        //High Scores
        hiScores = new ArrayList <Integer> ();
        //POWERUP TIME
        possiblePowerUps = new ArrayList <PowerUp> ();  
        startPositions = new ArrayList <Integer> ();
        maxPowerUps = new ArrayList <Integer> ();
        timeGiven = new ArrayList <Integer> ();
        this.status = status;
        maxPowerUps.add(4);maxPowerUps.add(4);maxPowerUps.add(5);
        maxPowerUps.add(5);maxPowerUps.add(6);maxPowerUps.add(6);
        maxPowerUps.add(6);maxPowerUps.add(6);maxPowerUps.add(6);
        maxPowerUps.add(7);maxPowerUps.add(7);maxPowerUps.add(7);
        maxPowerUps.add(7);maxPowerUps.add(7);maxPowerUps.add(7);
        maxPowerUps.add(7);maxPowerUps.add(7);maxPowerUps.add(7);
        startPositions.add(500);startPositions.add(500);startPositions.add(500);
        startPositions.add(500);startPositions.add(100);startPositions.add(500);
        startPositions.add(500);startPositions.add(100);startPositions.add(500);
        startPositions.add(400);startPositions.add(500);startPositions.add(500);
        startPositions.add(450);startPositions.add(20);startPositions.add(500);
        startPositions.add(50);startPositions.add(20);startPositions.add(50);
        listPowerUps();
        timeGiven.add(15);timeGiven.add(22);timeGiven.add(30);
        timeGiven.add(30);timeGiven.add(36);timeGiven.add(15);
        timeGiven.add(25);timeGiven.add(60);timeGiven.add(80);
        timeGiven.add(80);timeGiven.add(60);timeGiven.add(60);
        timeGiven.add(90);timeGiven.add(90);timeGiven.add(90);
        timeGiven.add(90);timeGiven.add(90);timeGiven.add(90);
        reset();
    }
    
    public void listPowerUps () {
        possiblePowerUps.add(
                new PowerUp (0,0,COURT_WIDTH,COURT_HEIGHT,Color.YELLOW,0,130,
                        new PowerUpListener () {
                            public void act () {
                                currTime-=120;
                            }
        },"More Time"));
        possiblePowerUps.add(
                new PowerUp (0,0,COURT_WIDTH,COURT_HEIGHT,Color.BLUE,0,130,
                        new PowerUpListener () {
                            public void act () {
                                shotmode = shotMode.VERTICAL;
                            }
        },"Vertical/Slow Shot"));       
        possiblePowerUps.add(
                new PowerUp (0,0,COURT_WIDTH,COURT_HEIGHT,Color.ORANGE,0,130,
                        new PowerUpListener () {
                            public void act () {
                                shotmode = shotMode.HORIZONTAL;
                            }
        },"Horizontal/Fast Shot"));        
        possiblePowerUps.add(
                new PowerUp (0,0,COURT_WIDTH,COURT_HEIGHT,Color.CYAN,0,130,
                        new PowerUpListener () {
                            public void act () {
                                points+=100;
                            }
        },"100 points"));     
        possiblePowerUps.add(
                new PowerUp (0,0,COURT_WIDTH,COURT_HEIGHT,Color.GREEN,0,130,
                        new PowerUpListener () {
                            public void act () {
                                points+=300;
                            }
        },"300 Points"));       
        possiblePowerUps.add(
                new PowerUp (0,0,COURT_WIDTH,COURT_HEIGHT,Color.YELLOW,0,130,
                        new PowerUpListener () {
                            public void act () {
                                points+=1000;
                            }
        },"1000 Points"));       
        possiblePowerUps.add(
                new PowerUp (0,0,COURT_WIDTH,COURT_HEIGHT,Color.YELLOW,0,130,
                        new PowerUpListener () {
                            public void act () {
                                lives++;
                            }
        },"1 Life"));             
    }
    

    /** (Re-)set the state of the game to its initial state.
     */
    public void nextLevel (boolean next) {
        if (next) level ++;            
        else lives--; 
        timer.setInitialDelay(1000);
        timer.restart();
        sprite.pos_x = startPositions.get(level-1);
        currentBubbles = new ArrayList<Bubble> ();
        currentWalls = new ArrayList <Wall> ();
        currentWalls.add(new Wall (0,0,2,2,5,COURT_HEIGHT-4,COURT_WIDTH,
                COURT_HEIGHT,WallType.STATIONARY,null));
        currentWalls.add(new Wall (0,0,993,2,5,COURT_HEIGHT-4,COURT_WIDTH,
                COURT_HEIGHT,WallType.STATIONARY,null));
        currentWalls.add(new Wall (0,0,2,2,COURT_WIDTH-4,5,COURT_WIDTH,
                COURT_HEIGHT,WallType.STATIONARY,null));
        currentWalls.add(new Wall (0,0,2,493,COURT_WIDTH-4,5,
                COURT_WIDTH,COURT_HEIGHT,WallType.STATIONARY,null));
        shotmode = shotMode.VERTICAL;
        totalPops=0;
        shots = null;
        powerUps = new ArrayList <PowerUp> ();
        currTime = 0;
        playing = true;
        requestFocusInWindow();
        switch(level) {
        case 1: 
            currentBubbles.add(new Bubble (2, 7, 360, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            break;
        case 2:             
            currentBubbles.add(new Bubble (3, 7, 290, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            break;
        case 3: 
            currentBubbles.add(new Bubble (4, 7, 220, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            break;
        case 4: 
            currentBubbles.add(new Bubble (3, 260, 290, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,-1)); 
            currentBubbles.add(new Bubble (3, 740, 290, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            break;
        case 5:
            currentBubbles.add(new Bubble (3, 160, 290, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR, 1)); 
            currentBubbles.add(new Bubble (4, 660, 220, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentWalls.add(new Wall(0,0,COURT_WIDTH/2-50,2,100,COURT_HEIGHT-4,
                   COURT_WIDTH,COURT_HEIGHT,WallType.OPENABLE,Direction.RIGHT));
            break;
        case 6:
            currentBubbles.add(new Bubble (1, 50, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentBubbles.add(new Bubble (1, 150, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentBubbles.add(new Bubble (1, 250, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentBubbles.add(new Bubble (1, 750, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,-1)); 
            currentBubbles.add(new Bubble (1, 850, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,-1)); 
            currentBubbles.add(new Bubble (1, 950, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,-1)); 
            currentWalls.add(new Wall (0,1,2,2,COURT_WIDTH-4
                    ,1,COURT_WIDTH,COURT_HEIGHT,WallType.MOVABLE,null));
            break;
        case 7: 
            currentBubbles.add(new Bubble (1, 150, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentBubbles.add(new Bubble (1, 170, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentBubbles.add(new Bubble (1, 270, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentBubbles.add(new Bubble (1, 290, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentBubbles.add(new Bubble (1, 310, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentBubbles.add(new Bubble (1, 690, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,-1)); 
            currentBubbles.add(new Bubble (1, 710, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,-1));
            currentBubbles.add(new Bubble (1, 730, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,-1)); 
            currentBubbles.add(new Bubble (1, 830, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,-1)); 
            currentBubbles.add(new Bubble (1, 850, 430, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,-1)); 
            currentWalls.add(new Wall (0,0,2,2,COURT_WIDTH-4,360,
                    COURT_WIDTH,COURT_HEIGHT,WallType.STATIONARY,null));
            break;
        case 8: 
            currentBubbles.add(new Bubble (2, 100, 360, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentBubbles.add(new Bubble (3, 350, 290, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentBubbles.add(new Bubble (5, 600, 150, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            currentWalls.add(new Wall (0,0,200,2,75,COURT_HEIGHT-4,
                    COURT_WIDTH,COURT_HEIGHT,WallType.DESTROYABLE,
                    Direction.RIGHT));
            currentWalls.add(new Wall (0,0,500,2,75,COURT_HEIGHT-4,
                    COURT_WIDTH,COURT_HEIGHT,WallType.DESTROYABLE,
                    Direction.RIGHT));
            break;
        case 9:
            currentBubbles.add(new Bubble (3, 30, 290, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,0)); 
            currentBubbles.add(new Bubble (3, 110 , 290, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,0));
            currentBubbles.add(new Bubble (3, 190 , 290, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,0));
            currentBubbles.add(new Bubble (3, 760 , 290, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,0));
            currentBubbles.add(new Bubble (3, 840 , 290, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,0));
            currentBubbles.add(new Bubble (3, 920, 290, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,0));
            currentBubbles.add(new Bubble (4, 20 , 220, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1));
            break;
        case 10:
            currentBubbles.add(new Bubble (4,7,220,0,COURT_WIDTH,
                    COURT_HEIGHT,BubbleType.REGULAR,1));
            currentBubbles.add(new Bubble (3,480,290,0,COURT_WIDTH,
                    COURT_HEIGHT,BubbleType.REGULAR,0));
            currentBubbles.add(new Bubble (4,920,220,0,COURT_WIDTH,
                    COURT_HEIGHT,BubbleType.REGULAR,-1));
            break;
        case 11:
            currentBubbles.add(new Bubble (6, 20, 90, 0, COURT_WIDTH, 
                    COURT_HEIGHT,BubbleType.REGULAR,1)); 
            break;
        case 12:
            currentBubbles.add(new Bubble (2,40,70,0,COURT_WIDTH,
                    COURT_HEIGHT,BubbleType.BOUNCY,1));
            currentBubbles.add(new Bubble (2,90,70,0,COURT_WIDTH,
                    COURT_HEIGHT,BubbleType.BOUNCY,1));
            currentBubbles.add(new Bubble (2,140,70,0,COURT_WIDTH,
                    COURT_HEIGHT,BubbleType.BOUNCY,1));
            currentBubbles.add(new Bubble (2,190,70,0,COURT_WIDTH,
                    COURT_HEIGHT,BubbleType.BOUNCY,1));
            currentBubbles.add(new Bubble (2,920,70,0,COURT_WIDTH,
                    COURT_HEIGHT,BubbleType.BOUNCY,-1));
            currentBubbles.add(new Bubble (2,870,70,0,COURT_WIDTH,
                    COURT_HEIGHT,BubbleType.BOUNCY,-1));
            currentBubbles.add(new Bubble (2,820,70,0,COURT_WIDTH,
                    COURT_HEIGHT,BubbleType.BOUNCY,-1));
            currentBubbles.add(new Bubble (2,770,70,0,COURT_WIDTH,
                    COURT_HEIGHT,BubbleType.BOUNCY,-1));
            break;
        case 13:
            currentBubbles.add(new Bubble (4,10,220,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,-1));
            currentBubbles.add(new Bubble (4,170,220,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,-1));
            currentBubbles.add(new Bubble (3,330,290,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,-1));
            currentBubbles.add(new Bubble (3,590,290,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,-1));
            currentBubbles.add(new Bubble (4,750,220,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,-1));
            currentBubbles.add(new Bubble (4,910,220,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,-1));
            currentWalls.add(new Wall (0,0,100,2,50,COURT_HEIGHT-4,COURT_WIDTH,
                    COURT_HEIGHT,WallType.OPENABLE,null));
            currentWalls.add(new Wall (0,0,250,2,50,COURT_HEIGHT-4,COURT_WIDTH,
                    COURT_HEIGHT,WallType.OPENABLE,null));
            currentWalls.add(new Wall (0,0,450,2,50,COURT_HEIGHT-4,COURT_WIDTH,
                    COURT_HEIGHT,WallType.OPENABLE,null));
            currentWalls.add(new Wall (0,0,650,2,50,COURT_HEIGHT-4,COURT_WIDTH,
                    COURT_HEIGHT,WallType.OPENABLE,null));
            currentWalls.add(new Wall (0,0,825,2,50,COURT_HEIGHT-4,COURT_WIDTH,
                    COURT_HEIGHT,WallType.OPENABLE,null));
            for (int i = 4 ; i < 9 ; i ++) currentWalls.get(i).opened = true;
            break;
        case 14: 
            currentBubbles.add(new Bubble (4,100,220,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,1));
            currentBubbles.add(new Bubble (5,400,150,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,1));
            currentBubbles.add(new Bubble (6,800,90,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,1));
            currentWalls.add(new Wall (0,0,300,2,50,COURT_HEIGHT-4,COURT_WIDTH,
                    COURT_HEIGHT,WallType.OPENABLE,Direction.RIGHT));
            currentWalls.add(new Wall (0,0,700,2,50,COURT_HEIGHT-4,COURT_WIDTH,
                    COURT_HEIGHT,WallType.OPENABLE,Direction.RIGHT));    
            break;
        case 15: 
            currentBubbles.add(new Bubble (3,20,290,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOX,1));
            currentBubbles.add(new Bubble (4,200,220,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOX,-1));
            currentBubbles.add(new Bubble (4,400,220,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOX,-1));      
            currentBubbles.add(new Bubble (4,850,220,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOX,-1));
            break;
        case 16: 
            currentBubbles.add(new Bubble (3,200,220,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOX,-1));
            currentBubbles.add(new Bubble (3,500,220,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOX,-1));
            currentBubbles.add(new Bubble (4,800,280,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.UPWARDBOUNCE,1));
            currentWalls.add(new Wall (0,0,300,2,50,COURT_HEIGHT-4,COURT_WIDTH,
                    COURT_HEIGHT,WallType.DESTROYABLE,Direction.RIGHT));
            currentWalls.add(new Wall (0,0,600,2,50,COURT_HEIGHT-4,COURT_WIDTH,
                    COURT_HEIGHT,WallType.DESTROYABLE,Direction.RIGHT));      
            break;
        case 17:
            currentBubbles.add(new Bubble (2,100,70,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOUNCY,1));
            currentBubbles.add(new Bubble (2,300,70,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOUNCY,1)); 
            currentBubbles.add(new Bubble (2,500,70,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOUNCY,1));
            currentBubbles.add(new Bubble (3,700,290,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,-1));
            currentBubbles.add(new Bubble (3,900,290,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,-1));
            currentBubbles.add(new Bubble (3,150,100,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOX,-1));
            currentBubbles.add(new Bubble (3,850,290,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.UPWARDBOUNCE,-1));
            break;
        case 18:
            currentBubbles.add(new Bubble (2,200,400,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOUNCY,1));
            currentBubbles.add(new Bubble (2,800,400,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOUNCY,-1));
            currentBubbles.add(new Bubble (2,700,400,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.REGULAR,-1));
            currentBubbles.add(new Bubble(4,200,100,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOX,1));
            currentBubbles.add(new Bubble(4,500,100,0,COURT_WIDTH,COURT_HEIGHT,
                    BubbleType.BOUNCY,1));
            currentWalls.add(new Wall (0,0,2,250,COURT_WIDTH-4,50,COURT_WIDTH,
                    COURT_HEIGHT,WallType.DESTROYABLE,Direction.UP));
        default: break;
        }
    }
    
    public void reset () {
        level = 0;
        lives = 5;
        points = 0;
        playing = false;
        instructions = false;
        highScores = false;
    }

    /**
     * This method is called every time the timer defined
     * in the constructor triggers.
     */
   
    private void tick(){
        if (lives != 0 && playing && currentBubbles.size() != 0 
                && timeGiven.get(level-1) >currTime/25 && level <= totalLevels){
            boolean spriteMove = true;
            ArrayList <Wall> removableW = new ArrayList <Wall> ();
            for (Wall w : currentWalls) {
                if (shots != null && shots.intersects(w)) shots = null;
                if (w.type == WallType.MOVABLE) {
                    w.move(); 
                }
                else if ((w.type == WallType.OPENABLE || 
                        w.type == WallType.DESTROYABLE) && !w.opened &&
                        w.openWall(currentBubbles)) w.opened = true;
                else if (w.type == WallType.DESTROYABLE) {
                    if (w.openWall(currentBubbles)) {
                        w.opened = true;
                        removableW.add(w);                        
                    }
                }
                if (!w.opened && w.type!= WallType.STATIONARY && 
                        sprite.willIntersect(w)) spriteMove = false;
            }     
            if (spriteMove) sprite.move();
            if (shots != null ) shots.move();
            for (Wall w : removableW) currentWalls.remove(w);
            ArrayList <Bubble> removableP = new ArrayList <Bubble> (),
                    addableP = new ArrayList <Bubble> ();
            for (Bubble p : currentBubbles ) {
                int cntr = 0;
                for (Wall w : currentWalls) {
                    cntr ++;
                    Direction d = p.hitObjMine(w);
                    if (d==Direction.UP&&p.type==BubbleType.REGULAR&&cntr<=4) {
                        points += (p.width + 7)*5;
                        removableP.add(p);
                    }
                    else {p.bounce(d);}
                }
                p.move();
                if (sprite.intersects(p)) {
                    nextLevel(false);
                    return;
                }
                if (shots != null && shots.intersects(p)) {
                    int velUp = shots.v_y;
                    if (velUp == -30)  velUp = -23;
                    ArrayList <Bubble> splits = p.splitted
                            (shots.v_y/5-11,COURT_WIDTH, COURT_HEIGHT);
                    for (Bubble p_new : splits) {
                        addableP.add(p_new);
                        totalPops++;
                    }
                    if (totalPops%6 == 2 && splits.size()!=0) {
                        PowerUp newPU = possiblePowerUps.get
                                ((int)(Math.random()*maxPowerUps.get(level-1)));
                        newPU.pos_x = p.pos_x; newPU.pos_y = p.pos_y; 
                        newPU.time_start = currTime;
                        powerUps.add(newPU);               
                    }
                    shots = null;
                    removableP.add(p);
                    points += (p.width + 7)*5;
                }
            }
            for (Bubble p : removableP) currentBubbles.remove(p);
            for (Bubble p : addableP) currentBubbles.add(p);
             
            ArrayList <PowerUp> removablePU = new ArrayList <PowerUp> ();
            for (PowerUp pu : powerUps) {
                if (pu.hasExpired (currTime)) removablePU.add(pu);
                pu.move();
                if (sprite.intersects(pu)) {
                    pu.act();
                    removablePU.add(pu);
                }
            }
            for (PowerUp pu : removablePU) powerUps.remove(pu);
            status.setText("Lives: " + lives + ".  Level: " + level + ". "
                    + "Points: " + points + ".  Time: " + 
                    (timeGiven.get(level-1)-currTime/25) + ".");
            currTime +=1;
        } 
        else if (lives!= 0 && playing && currentBubbles.size() == 0 
                && timeGiven.get(level-1) > currTime/25 && level < totalLevels){
            points += (timeGiven.get(level-1)-currTime/25)*100; 
            nextLevel (true);
        }
        else if (lives!= 0 && playing && timeGiven.get(level-1) <= currTime/25 
                && level <= totalLevels) nextLevel (false);
        else if (lives ==0 && playing && level <= totalLevels) {
            status.setText("Sorry you lost. Try again."); 
            hiScores.add(points);
            reset(); 
        }
        else if (playing && level >= totalLevels) {
            status.setText("Congrats on winning. Play again."); 
            hiScores.add(points);
            reset();
        }
        repaint ();
    }

    @Override 
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (playing) {
            if (shots!= null) shots.draw(g);
            for (PowerUp pu : powerUps) pu.draw(g);
            for (Wall w: currentWalls) w.draw(g);
            for (Bubble p : currentBubbles) p.draw(g);
            sprite.draw(g);
        }
        else if (instructions) {
            g.drawString("BUTTONS: Press PLAY to play, PAUSE to pause in game,"
                    + "QUIT to quit, NEXT LEVEL to go to next level, "
                    + "PREVIOUS LEVEL to go to previous level, GIVE UP "
                    + "to give up, QUIT to exit",15,15);
            g.drawString("game, and INSTRUCTIONS to read instructions. ",10,30);
            g.drawString("KEYS: Press LEFT ARROW to go left, "
                    + "RIGHT ARROW to go right, and SPACE to shoot. ",10,45);
            g.drawString("GAMEPLAY: The goal is to pop all the bubbles "
                    + "in the given time. You get 5 total lives. There are "
                    + "powerups in game, movable walls, and a few different "
                    + "types of bubbles.",10,60);
            g.drawString("COOL FEATURES: include movable walls, destroyable "
                    + "walls,"
                    + "openable walls, powerups, and four different types of "
                    + "bubbles.  "
                    + "The sprite is cool as well. The levels are cool too.", 
                    10,75);
            g.drawString("There are 11 total levels.  "
                    + "HOPE YOU ENJOY THIS ADDICTING GAME!! GOOD LUCK."
                    + "JUMP THROUGH THE LEVELS TO SEE MY FEATURES."
                    + "THE GAME INCREASES IN DIFFICULTY.",10,150);
        }
        else if (highScores) {
            Collections.sort(hiScores);
            for (int i = 0 ; i < hiScores.size(); i ++ ) {
                g.drawString(i+1 + ":  " + hiScores.get(i),10,15*(i+2));
            }
            if (hiScores.size() == 0) g.drawString("NO HIGH SCORES",10,15);
            else g.drawString ("HIGH SCORES",10,15);
        }
    }
    
    public void cont () {
        playing = true;
        requestFocusInWindow();
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(COURT_WIDTH,COURT_HEIGHT);
    }
}
