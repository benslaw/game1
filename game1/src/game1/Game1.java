/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game1;
import javalib.impworld.*;
import javalib.colors.*;
import javalib.worldcanvas.*;
import javalib.worldimages.*;
import java.awt.Color;
import java.util.Random;
/**
 *
 * @author Ben
 */
public class Game1 {

    interface constants {
        public static final int window_w = 1000;
        public static final int window_h = 1000;
        public Color background = new Color(0,0,0);
        public WorldImage theWorld =
                new RectangleImage(new Posn(window_w/2, window_h/2), 
                window_w, window_h, background);
    }
    
//    public WorldImage theWorld = 
//            new RectangleImage(new Posn(0,0),window_w*2,window_h*2,new Red());
    
    public class slideyJay implements constants {
        
        //slideyJay only has one important field, being the height
        int jay_y;
        int jay_x = 50;
        
        //constructor
        slideyJay(int jay_y) {
            this.jay_y = jay_y;
        }        
        
        //find where slideyJay is
        public Posn where_is_Jay() {
            return new Posn(jay_x,jay_y);
        }
        
        public void move(String str) {
            if(str.equals("up")) {
                jay_y = jay_y + 5;
            } else if(str.equals("down")) {
                jay_y = jay_y - 5;
            }
        }
        
        public WorldImage jayImage() {
            return new FromFileImage(this.where_is_Jay(), "jay_face.jpg");
        }
        
    }
    
    public class playField extends World implements constants{
        slideyJay Jay;
        
        public playField(slideyJay Jay) {
            this.Jay = Jay;
        }
        
        public void onKeyEvent(String str) {
            Jay.move(str);
        }
        
        public WorldImage makeImage() {
            return new RectangleImage(new Posn(window_w/2, window_h/2), 
                    window_w, window_h, background).
                    overlayImages(this.Jay.jayImage());
        }
        
        public WorldEnd gameOver() {
            if(/*slidey jay collides with a wall*/) {
                return new WorldEnd(true, new OverlayImages(this.makeImage(),
                        new TextImage(new Posn(window_w/2, window_h/2),
                        "You killed Jay!", new Red())));
            } else {
                return new WorldEnd(false, this.makeImage());
            }
        }
        
        
    }
    
//    public WorldImage makeImage() {
//        return theWorld;
//    }
    
    
    
    public static void main(String[] args) {
        System.out.println("test");
        
    }
}
