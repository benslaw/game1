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
        public Color background = new Color(0,0,0);
        public WorldImage theWorld =
                new RectangleImage(new Posn(window_w/2, window_h/2), 
                window_w, window_h, background);
    }
    
    public static class wall_block implements constants {
        
        Posn location;
        
        public wall_block(Posn location, int size) {
            this.location = location;
        }
        
//        public RectangleImage wall_block_build(Posn location, int w, int h, Color color) {
//            return new RectangleImage(location, w, h, color);
//        }
        
//        public void start(int y, int size) {
//            this.location = new Posn(window_w, y);
//            this.size = size;
//        }
        
//        public void slide_left() {
//            this.location = new Posn(this.location.x - 25, this.location.y);
//        }
        
    }
    
    public static class wall implements constants {

//        wall_block block;
        int numblocks = window_h/25;
        Random random_int = new Random();
        WorldImage wall = theWorld;

        public wall(wall_block block) {
//            this.block = block;
        }

        public int randomInt(int min, int max) {
            return random_int.nextInt((max - min) + 1) + min;
        }
        
        public WorldImage build_wall(wall_block block) {
            int gap = randomInt(0,numblocks);
            int y_coord = 12;
            for(int i = 0; i <= numblocks; i++) {
                if (i == gap) {
                    y_coord = y_coord + 25;
                } else {
                    wall = wall.overlayImages(
                            new RectangleImage(new Posn(window_w, y_coord), 
                            25, 25, new Green()));
                    y_coord = y_coord + 25;
                }
            }
            return wall;
        }
        
        public void slide_wall() {
            wall.moveTo(new Posn(wall.pinhole.x - 25, wall.pinhole.y));
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
                if(jay_y >= 25) {
                    jay_y = jay_y - 25;
                }
            } else if(str.equals("down")) {
                if(jay_y <= window_h - 25) {
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
        wall_block building;
        wall theWall;
        int counter = 0;
        
        public playField(slideyJay Jay, wall Wall) {
            this.Jay = Jay;
            this.theWall = Wall;
        }
        
        public void onTick() {
            this.theWall.slide_wall();
        }
        
        public void onKeyEvent(String str) {
            Jay.move(str);
        }
        
        public WorldImage makeImage() {
            if (counter % 4 == 0) {
                return new RectangleImage(new Posn(window_w / 2, window_h / 2),
                        window_w, window_h, background).
                        overlayImages(this.theWall.build_wall(building), this.Jay.jayImage());
            } else {
                return new RectangleImage(new Posn(window_w / 2, window_h / 2),
                        window_w, window_h, background).
                        overlayImages(this.Jay.jayImage());
            }
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
    
    public static class SampleWorld implements constants {
        SampleWorld() {}
        
        slideyJay player = new slideyJay(window_h - 13);
        wall_block block = new wall_block(new Posn(window_w, 0), 5);
        wall theWall = new wall(block);
        
        playField sampleField = new playField(player, theWall);
        
        
    }
    
    public static void main(String[] args) {
        System.out.println("test");
        SampleWorld x = new SampleWorld();
        x.sampleField.bigBang(1000, 500, 1);
    }
}
