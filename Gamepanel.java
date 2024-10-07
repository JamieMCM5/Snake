import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class Gamepanel extends JPanel implements ActionListener{

    //Declares all needed variables for the game.
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;   //Basically the amount of squares in the grid
    static final int DELAY = 150;   //How fast the delay is between moves, higher number = slower game
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    Gamepanel(){
        //Default setup for game panel. Background colour, panel size etc.
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        //Makes a new apple, starts the timer and sets Boolean running to True
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(running){
                /*Makes a grid to easily divide screen size
            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            } */
            //Draws the apple
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            
            //Draws the head and body at the index 0 of our X and Y postition arrays
            for (int i = 0; i < bodyParts; i++) {

                if(i == 0){
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else
                {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                g.setColor(Color.RED);
                g.setFont(new Font("Ink Free", Font.BOLD, 30));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " +applesEaten))/2, g.getFont().getSize());
                
            }
        }else{
            gameOver(g);
        }
        
    }

    public void newApple(){
        //Makes a random X and Y for the apple within the screen grid
        appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

        
    }

    public void move(){

        for (int i = bodyParts; i > 0 ; i--) {

            x[i] = x[i-1];
            y[i] = y[i-1];            
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkApple(){
        if((x[0] == appleX)&&(y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkColl(){
        //This checks if the head collided with the body
        for (int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i])&&(y[0] == y[i])){
                running = false;
            }
        }


        //This checks if head touches left border
        if(x[0] < 0){
            running = false;
        }

        //This checks if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }

        //This checks if head touches top border
        if(y[0] < 0){
            running = false;
        }

        //This checks if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        //If any of the collisions happen, turn bool running to false and stop the timer.
        if(running == false){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //Game over text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        //Score text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " +applesEaten))/2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(running){
            move();
            checkApple();
            checkColl();
        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

            //Checks if the Left/Right/Up/Down keys are pressed and moves them accordingly. Also stops player turning 180 back on themselves.
            //E.g if player is moving right, prevent turning directly left.
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';                        
                    }
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}