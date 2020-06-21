import java.awt.*;

/**
 * This class contains methods you will write for the Centipede game.
 *
 * @author Jonathan Liao
 *
 * @version 2.10.2015 WITH NUXOLL EMAIL EDIT!!!
 */

public class CentipedeHelper
{
        //diameter of one centipede segment
        public static final int SEG_DIAM = 25;
    
        //Size of the canvas (pixels per side)
        public static final int WINDOW_SIZE = 500;
    
        //How many ms to delay between frames.  Change this number to make your
        //centipede speed up or slow down
        public static final int DELAY = 200;
    
        /**
         * drawCentipede
         *
         * draws a centipede on the screen.  See the assignment for more details. 
         *
         * @param  canvas    the Graphics object for this game
         * @param  x,y       coordinates of the last centipede segment
         * @param  numSegs   the total number of segments in the centipede
         *                   (counting the head).  Must be a number between 1
         *                   and 15.  Each segment's diameter is set by SEG_DIAM
         * @param  left      'true' if the back of the centipede
         *                   is marching left (or 'false' for right)
         */
    
        public static final int RIGHT_BORDER = WINDOW_SIZE/SEG_DIAM * SEG_DIAM;
    
        public void drawCentipede(Graphics canvas, int x, int y, int numSegs, boolean left) {

            
            //TODO: Replace this with your own code
            if(left == false) {
                for (int i = numSegs; i > 0; i--) {
                    x += SEG_DIAM; // each time a segment is added, the x value is moved up by SEG_DIAM. Thus, the x value now measures the location of the FRONT segment.
                    
                    if(x >= RIGHT_BORDER) { //if the head reaches the window boundry of the right, the segments bound to go offscreen will be retified and redrawn as shown below
                        
                        if (i == 1) {
                            canvas.setColor(Color.orange);
                            canvas.fillOval(x - (2*( ((x-RIGHT_BORDER)+SEG_DIAM)/SEG_DIAM ) - 1) * SEG_DIAM, y + SEG_DIAM,SEG_DIAM, SEG_DIAM);
                        }
                        else {
                            canvas.setColor(Color.black); //mainbody
                            canvas.fillOval(x - (2*( ((x-RIGHT_BORDER)+SEG_DIAM)/SEG_DIAM ) - 1) * SEG_DIAM, y + SEG_DIAM,SEG_DIAM, SEG_DIAM);
                            
                            canvas.setColor(Color.red); //decor body paint
                            canvas.fillOval(x - (2*( ((x-RIGHT_BORDER)+SEG_DIAM)/SEG_DIAM ) - 1) * SEG_DIAM + (int)(SEG_DIAM/2), y + SEG_DIAM,(int)(SEG_DIAM/2), (int)(SEG_DIAM/2));
                        }
                        
                    }
                    else { //if the head is not about nearing the border and no turning is neccesarry, the centipede will be drawn going right as usual
                        
                        if (i == 1) { //draw the head
                            canvas.setColor(Color.orange);
                            canvas.fillOval(x,y,SEG_DIAM, SEG_DIAM); 
                        } 
                        else{
                            canvas.setColor(Color.black); //mainbody
                            canvas.fillOval(x,y,SEG_DIAM, SEG_DIAM);
                            
                            canvas.setColor(Color.red); //decor body paint
                            canvas.fillOval(x,y,(int)(SEG_DIAM/2), (int)(SEG_DIAM/2));
                        }
                    }
                }
                        
            }
            
            if(left == true) {
                for (int i = numSegs; i > 0; i--) {
                    x -= SEG_DIAM; // each time a segment is added, the x value is moved down by SEG_DIAM. Thus, the x value now measures the location of the FRONT segment.
                    
                    if(x < 0) { //if the head reaches the window boundry of the left at 0, the segments bound to go offscreen will be retified and redrawn as shown below
                        
                        if (i == 1) {//draw the head
                            canvas.setColor(Color.orange);
                            canvas.fillOval(x + (2*( ((0-x))/SEG_DIAM ) - 1) * SEG_DIAM, y + SEG_DIAM,SEG_DIAM, SEG_DIAM);
                        }
                        else {
                            canvas.setColor(Color.black); //mainbody
                            canvas.fillOval(x + (2*( ((0-x))/SEG_DIAM ) - 1) * SEG_DIAM, y + SEG_DIAM,SEG_DIAM, SEG_DIAM);
                            
                            canvas.setColor(Color.red); //decor body paint
                            canvas.fillOval(x + (2*( ((0-x))/SEG_DIAM ) - 1) * SEG_DIAM, y + SEG_DIAM,(int)(SEG_DIAM/2), (int)(SEG_DIAM/2));
                        }
                    }
                    else {
                      if (i == 1) { //draw the head
                            canvas.setColor(Color.orange);
                            canvas.fillOval(x,y,SEG_DIAM, SEG_DIAM); 
                        }   
                        else{
                            canvas.setColor(Color.black); //mainbody
                            canvas.fillOval(x,y,SEG_DIAM, SEG_DIAM);
                            
                            canvas.setColor(Color.red); //decor body paint
                            canvas.fillOval(x + (int)(SEG_DIAM/2),y,(int)(SEG_DIAM/2), (int)(SEG_DIAM/2));
                        }
                    }
                }
                        
            }
        }
       
    //drawCentipede
            
        /**
         * hitsCentipede
         *
         * determines whether a laser shot will hit the centipede
         *
         *
         * @param  laserX    x-coordinate of the laser
         * @param  centX     x-coordinate of the last centipede segment
         * @param  numSegs   the total number of segments in the centipede
         *                   (counting the head).  Must be a number between 1
         *                   and 15.  
         * @param  left      'true' if the back half of the centipede
         *                   is marching left (or 'false' for right)
         *
         * @return true if the laser will hit one of the centipede's segments and
         *         false otherwise
         */
        public boolean hitsCentipede(int laserX, int centX, int numSegs, boolean left)
        {
            if(left == false) {
                int x = centX + numSegs*SEG_DIAM; //x is the coordinate of the head
                
                    if(x >= RIGHT_BORDER) { //if the centipede is turning on the right side
                        
                        
                        if(laserX >= centX && laserX <= RIGHT_BORDER || laserX >= (x - (2*( ((x-RIGHT_BORDER)+SEG_DIAM)/SEG_DIAM ) - 1) * SEG_DIAM) && laserX <= RIGHT_BORDER) {
                            return true;
                        }
                        
                    }                   
                    else if(laserX >= centX && laserX <= x)
                    { //if the head is not about nearing the border and no turning is neccesarry, the centipede will be drawn going right as usual
                        return true;
                    }
                    else //no hit
                        return false;                
           
            }
            
            if(left == true) {
                int x = centX - numSegs*SEG_DIAM; //x is the coordinate of the head
                
                    if(x < 0) { //if the centipede is turning on the left side

                        if(laserX >= 0 && laserX <= centX || laserX < x + (2*( ((0-x))/SEG_DIAM ) - 1) * SEG_DIAM && laserX >= 0) {
                            return true;
                        }
                        
                    }                   
                    else if(laserX >= centX && laserX <= x)
                    { //if the head is not about nearing the border and no turning is neccesarry, the centipede will be drawn going right as usual
                        return true;
                    }
                    else //no hit
                        return false;                
                        
            }
            
            
            return false;
        }
        

}