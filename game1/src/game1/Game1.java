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
        public static final int window_h = 500;
        public Color background = new Color(0,50,50);
        public WorldImage theWorld =
                new RectangleImage(new Posn(window_w/2, window_h/2), 
                window_w, window_h, background);
    }
    
//    public WorldImage theWorld = 
//            new RectangleImage(new Posn(0,0),window_w*2,window_h*2,new Red());
    
    public class wall_block implements constants {
        
        Posn location;
        int size;
        
        public wall_block(Posn location, int size) {
            this.location = location;
            this.size = size;
        }
        
        public void start(int y, int size) {
            this.location = new Posn(window_w, y);
            this.size = size;
        }
        
        public void slide_left() {
            this.location = new Posn(this.location.x - 5, this.location.y);
        }
        
    }
    
    public class wall implements constants {
        
        int numblocks;
        wall_block block;
        Random random_int = new Random();
        
        public wall(wall_block block, int numblocks) {
            this.numblocks = numblocks;
            this.block = block;
        }
        
    }
    
    public static class slideyJay implements constants {
        
        //slideyJay only has one important field, being the height
        int jay_y;
        int jay_x = 150;
        
        //constructor
        slideyJay(int jay_y) {
            this.jay_y = jay_y;
        }        
        
        //find where slideyJay is
        public Posn where_is_Jay() {
            return new Posn(jay_x,jay_y);
        }
        
        public boolean is_jay_okay() {
            return true;
        }
        
        public void move(String str) {
            if(str.equals("up")) {
                if(jay_y > 25) {
                    jay_y = jay_y - 25;
                }
            } else if(str.equals("down")) {
                if(jay_y < window_h - 25) {
                    jay_y = jay_y + 25;
                }
            }
        }
        
        public WorldImage jayImage() {
            return new RectangleImage(this.where_is_Jay(), 25, 25, new Blue());
//            return new FromFileImage(this.where_is_Jay(), "jay_face.jpg");
        }
        
    }
    
    public static class playField extends World implements constants{
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
            if(true) {
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
    
    public static class SampleWorld implements constants {
        SampleWorld() {}
        
        slideyJay player = new slideyJay(window_h/2);
        
        playField sampleField = new playField(player);
    }
    
    public static void main(String[] args) {
        System.out.println("test");
        SampleWorld x = new SampleWorld();
        x.sampleField.bigBang(1000, 500);
    }
}
