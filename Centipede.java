import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * This program is a modified reproduction of the Atari classic:  Centipede!
 *
 * Students should not need to make modifications to this file.
 *
 * @author Andrew Nuxoll
 *
 * ------------------------------
 * last updated:  Spring 2015
 * ------------------------------
 */
public class Centipede implements MouseListener, MouseMotionListener
{

   /*======================================================================
    * Instance Variables
    *----------------------------------------------------------------------
    */
    //diameter of one centipede segment
    private static final int SEG_DIAM = CentipedeHelper.SEG_DIAM;
    
    private static Random rand = new Random();

    //Size of the board (pixels per side)
    private static final int WINDOW_SIZE = CentipedeHelper.WINDOW_SIZE;

    // These variables are used for double buffering
    private Canvas myCanvas = null;
    private BufferStrategy strategy = null;

    //This is how long to wait between frames
    private int frameDelay = CentipedeHelper.DELAY;
    public static final int MIN_DELAY = 20; //minimum frame delay

    //These variables define the centipede's position
    private int numSegs = 15; //number of segments
    private int centX = 0;     //x-position of the last segment
    private int centY = 0;     //y-position of same
    private int delta = SEG_DIAM; //how far the centipede moves each time

    //These variables define the ship's position
    private int shipX = WINDOW_SIZE / 2;
    private int shipY = WINDOW_SIZE * 4 / 5;
    private int laserLen = 0;   //length of the ship's laser beam

    //These variables track the mushrooms
    private Vector<Point> shrooms = new Vector<Point>();  //the location of all the mushrooms
    private int numToAdd = 3; //how many mushrooms to add to field each time (increases)

    //set this boolean to stop the game (win/lose)
    private boolean stop = false;

    //the game uses an instance of this class which is completed by the
    //student
    private CentipedeHelper chelp = new CentipedeHelper();
    
    /*======================================================================
     * Methods
     *----------------------------------------------------------------------
     */

    /**
     * the contructor allocates the board and sets all the spaces therein to
     * zero.
     */
    public Centipede(Canvas initCanvas)
    {
        //save a pointer to the graphics canvas
        myCanvas = initCanvas;

        //listen for mouse events
        myCanvas.addMouseListener(this);
        myCanvas.addMouseMotionListener(this);

        //make the mouse cursor invisible
        //(code from: http://stackoverflow.com/questions/1984071/how-to-hide-cursor-in-a-swing-application)
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg, new Point(0, 0), "blank cursor");
        initCanvas.setCursor(blankCursor);

        //add some initial mushrooms
        addMushrooms();
        
    }//Centipede ctor

    /**
     * addMushrooms
     *
     * adds mushrooms to the field.  Each time this is called, more
     * mushrooms are added
     *
     */
    private void addMushrooms()
    {
        
        for(int i = 0; i < this.numToAdd; ++i)
        {
            int x = this.rand.nextInt(WINDOW_SIZE - 2 * SEG_DIAM);
            int y = this.rand.nextInt(WINDOW_SIZE * 4 / 5  - SEG_DIAM);
            this.shrooms.add(new Point(x,y));
        }

        this.numToAdd++;  //more next time

    }//addMushrooms

    /**
     * drawMushrooms
     *
     * draws all the mushrooms listed in this.shrooms
     *
     * @param  canvas    the Graphics object for this game
     */
    private void drawMushrooms(Graphics canvas)
    {
        //A nice purple mushroomy color
        canvas.setColor(new Color(180,90,180));
        
        //Sometimes if the user fires the laser just as the screen is
        //being redrawn the program can be removing a mushroom just as it is
        //trying to draw it.  This try-catch prevents the program from crashing
        //when that happens.
        boolean tryAgain = true;
        try
        {
            for(Point loc : this.shrooms)
            {
                canvas.fillOval(loc.x+2, loc.y+2, SEG_DIAM-4, SEG_DIAM/2);
                canvas.fillRect(loc.x+8, loc.y+8, SEG_DIAM-16, SEG_DIAM-8);
            }

            tryAgain = false;
        }
        catch(ConcurrentModificationException cme) { }
            
    }//drawMushrooms
    
    /**
     * drawShip
     *
     * draws the human controlled ship on the screen.  If the ship is firing the
     * laser is drawn as well
     *
     * @param  canvas    the Graphics object for this game
     * @param  x,y       coordinates of the ship
     * @param  laserLen  how far up to draw the laser.  A value of zero
     *                   indicates no laser
     */
    public void drawShip(Graphics canvas, int x, int y, int laserLen)
    {
        //define the ship's shape
        Polygon shipShape = new Polygon();
        shipShape.addPoint(x + SEG_DIAM / 2, y);
        shipShape.addPoint(x + SEG_DIAM, y + SEG_DIAM);
        shipShape.addPoint(x, y + SEG_DIAM);
        
        
        //draw the ship
        canvas.setColor(Color.black);
        canvas.fillPolygon(shipShape);

        //draw the laser if present
        if (laserLen > 0)
        {
            int startX = x + SEG_DIAM / 2;
            int startY = y;
            int endX = startX;
            int endY = startY - laserLen;
            canvas.setColor(Color.red);
            canvas.drawLine(startX, startY, endX, endY);
        }
        
        
    }//drawShip
       
    
    /**
     * drawFrame
     *
     * This methods draws the current state of the simulation on a given canvas
     *
     * @param  canvas    the Graphics object for this game
     */
    public void drawFrame(Graphics canvas)
    {
        drawMushrooms(canvas);

        canvas.setColor(Color.blue);
        this.chelp.drawCentipede(canvas, this.centX, this.centY,
                                 this.numSegs, (delta < 0));

        drawShip(canvas, this.shipX, this.shipY, this.laserLen);

        //Did the player win?
        if (this.numSegs <= 0)
        {
            this.stop = true;

            //Announce win to user
            canvas.setColor(Color.blue);
            canvas.setFont(new Font("TimesRoman", Font.PLAIN, SEG_DIAM));
            canvas.drawString("You Won!", 200,200);
        }

        //Did the player lose
        else if (this.centY >= this.shipY)
        {
            this.stop = true;

            //Announce loss to user
            canvas.setColor(Color.blue);
            canvas.setFont(new Font("TimesRoman", Font.PLAIN, SEG_DIAM));
            canvas.drawString("You Lose.", 200, 200);
        }            

        
    }//drawFrame


    /**
     * updateState
     *
     * updates the current state of the game for the next frame
     */
    private void updateState()
    {
        //move the butt
        centX += this.delta;

        //if the centipede is off the screen, move the other way instead and
        //move down one row
        if ((centX < 0) || (centX > (WINDOW_SIZE - SEG_DIAM))) 
        {
            this.delta = -this.delta;
            centX += this.delta;
            centY += SEG_DIAM;
        }

        //turn off the laser
        this.laserLen = 0;
        
    }//updateState

    /**
     * fireLaser
     *
     * this method updates the game state when the laser is fired
     */
    private void fireLaser()
    {
        //By default, the laser goes to the top of the screen
        this.laserLen = WINDOW_SIZE;
        
        //Calculate laser's x position
        int laserX = this.shipX + SEG_DIAM / 2;
        
        //Calculate which mushroom will be hit by the laser
        Point hit = null;
        for(Point shroom : shrooms)
        {
            if (shroom.x > laserX) continue;  //too far right
            if (shroom.x + SEG_DIAM < laserX) continue; //too far left

            //If this shroom closest to ship then it's best so far
            if ((hit == null) || (hit.y < shroom.y))
            {
                hit = shroom;
            }
        }//for

        //Did we hit a shroom?
        if (hit != null)
        {
            laserLen = this.shipY - hit.y - SEG_DIAM / 2;
            this.shrooms.remove(hit);
        }

        //Did we hit the centipede?
        else if (chelp.hitsCentipede(laserX, this.centX,
                                     this.numSegs, (this.delta < 0)))
        {
            this.numSegs--;

            //Move centipede forward so the lost segment seems to
            //fall off the back of it
            centX += this.delta;
        }
        else //ok we missed
        {
            //more mushrooms
            addMushrooms();

            //faster centipede
            this.frameDelay -= 10;
            if (this.frameDelay < MIN_DELAY)
            {
                this.frameDelay = MIN_DELAY;
            }
        }

    }//fireLaser
    
    /**
     * this method gets called whenever the user clicks the mouse button.  We
     * use this to fire the laser
     */
    public void mousePressed(MouseEvent e)
    {
        //fire the laser
        fireLaser();
        
        //Show to user
        updateDisplay();

    }//mousePressed

    /**
     * this method gets called whenever the user moves the mouse.  We use this
     * to update the position of the ship
     */
    public void mouseMoved(MouseEvent event)
    {
        //Ship's position is fixed for a time when you fire laser
        if (this.laserLen > 0) return;
        
        //Change ship's position (only x-position can change)
        this.shipX = event.getX() % (WINDOW_SIZE - SEG_DIAM);

        //Show to user
        updateDisplay();
    }

    /**
     * these mouse events we don't care about so do nothing (empty method) 
     */
    public void mouseDragged(MouseEvent event) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

    /**
     * updateDisplay
     *
     * can be called to redraw the current frame if something changes between
     * ticks 
     *
     */
    private void updateDisplay()
    {
        //Retrieve the canvas used for double buffering
        Graphics hiddenCanvas = strategy.getDrawGraphics();

        //Start with a black pen on a white background
        hiddenCanvas.setColor(Color.white);
        hiddenCanvas.fillRect(0,0,WINDOW_SIZE,WINDOW_SIZE);
        hiddenCanvas.setColor(Color.black);

        //draw the picture
        drawFrame(hiddenCanvas);

        //Display the new canvas to the user
        strategy.show();
    }//updateDisplay
     

    /**
     * this method contains the main run loop for the game
     *
     */
    public void run()
    {
        //Setup double-buffering
        myCanvas.createBufferStrategy(2);
        strategy = myCanvas.getBufferStrategy();

        //For the rest of the game just keep creating new generations
        while(! this.stop)
        {
            //Pause for a moment so the user can see the current step
            try
            {
                Thread.sleep(frameDelay);
            }
            catch(Exception e)
            {
                //don't care if the sleep fails
            }

            //Calculate the new position of game elements
            updateState();

            //show the user
            updateDisplay();

            
        }//while

    }//run


    /**
     * This method creates a window frame and displays the Centipede
     * simulation inside of it.
     */
    public static void main(String[] args)
    {
        //Create a properly sized window for this program
        final JFrame myFrame = new JFrame();
        myFrame.setSize(WINDOW_SIZE, WINDOW_SIZE);
        
        JOptionPane.showMessageDialog(null, 
                                    "Welcome to the Centipede Game made by Jonathan Liao.\nControl the ship by moving the mouse and click to fire to destroy the centipede!");
                                    
        JOptionPane.showMessageDialog(null, 
                                    "Good luck!");
        //Tell this Window to close when someone presses the close button
        myFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                };
            });

        //Display a canvas in the window
        Canvas myCanvas = new Canvas();
        myFrame.getContentPane().add(myCanvas);
        myFrame.setVisible(true);

        //Init the sim
        Centipede myCentipedeGame = new Centipede(myCanvas);
        myCentipedeGame.run();

    }//main

}//class Centipede

