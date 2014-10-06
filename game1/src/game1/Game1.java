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
import java.util.*;
/**
 *
 * @author Ben
 */
public class Game1 {

    interface constants {
        public static final int window_w = 1000;
        public static final int window_h = 500;
        int numblocks = window_h/25;
        public Color background = new Color(255, 255, 255);
        public WorldImage theWorld =
                new RectangleImage(new Posn(window_w/2, window_h/2), 
                window_w, window_h, background);
    }
    
//    public static class wall_block implements constants {
//        
//        Posn location;
//        
//        public wall_block(Posn location, int size) {
//            this.location = location;
//        }
//        
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
        
//    }
    
    public static class single_wall implements constants {
        
        ArrayList<Posn> oneWall = new ArrayList<Posn>();
        int y_coord_hole;
        Random rand = new Random();
        
        public single_wall(ArrayList<Posn> oneWall, int theHole) {
            this.oneWall = oneWall;
            this.y_coord_hole = theHole;
        }
        
        public int randomInt(int min, int max) {
            return rand.nextInt((max - min) + 1) + min;
        }
        
        public single_wall build_one_wall() {
            int gap = randomInt(0,numblocks);
            int y_coord = 12;
            for(int i = 0; i <= numblocks; i++) {
                if (i == gap) {
                    y_coord_hole = y_coord;
                    y_coord = y_coord + 25;
                } else {
                    oneWall.add(new Posn(window_w, y_coord));
                    y_coord = y_coord + 25;
                }
            }
            return new single_wall(oneWall, y_coord_hole);
        }
        
        public int one_wall_x(ArrayList<Posn> x) {
            return x.get(1).x;
        }
        
        public int wall_gap(single_wall theWall) {
            return theWall.y_coord_hole;
        }
        
        public single_wall slideWall() {
            ArrayList<Posn> newWall = new ArrayList<Posn>();
            System.out.println("slidewall 1");
            for(int i = 0; i < oneWall.size(); i++) {
                System.out.println("slidewall 2");
                newWall.add(new Posn(oneWall.get(i).x - 25, oneWall.get(i).y));
                System.out.println(oneWall.get(i).x - 25);
            }
            System.out.println("slidewall 3");
            return new single_wall(newWall, y_coord_hole);
        }
        
    }
    
    public static class all_walls implements constants {
        
        ArrayList<single_wall> currentWalls = new ArrayList<single_wall>();
        int counter = 19;
        
        public all_walls(ArrayList<single_wall> currentWalls) {
            this.currentWalls = currentWalls;
        }
        
        public ArrayList<single_wall> collect_walls() {
            counter++;
            single_wall newWall = new single_wall(new ArrayList<Posn>(), 0);
            if(counter % 20 == 0) {
                currentWalls.add(newWall.build_one_wall());
                return currentWalls;
            } else {
                return currentWalls;
            }
        }
        
        public WorldImage walls_image(ArrayList<single_wall> walls) {
            RectangleImage temp;
            WorldImage newWorld = theWorld;
            for (int i = 0; i < walls.size(); i++) {
                for (int j = 0; j < numblocks; j++) {
                    temp = new RectangleImage(walls.get(i).oneWall.get(j), 25, 25, new Green());     
                    newWorld = newWorld.overlayImages(temp);
                }
            }
            return newWorld;
        }
        
    }
    
//    public static class wall implements constants {
//
////        wall_block block;
//        int numblocks = window_h/25;
//        Random random_int = new Random();
//        WorldImage wall = theWorld;
//
//        public wall(wall_block block) {
////            this.block = block;
//        }
//
//        public int randomInt(int min, int max) {
//            return random_int.nextInt((max - min) + 1) + min;
//        }
//        
//        public WorldImage build_wall(wall_block block) {
//            int gap = randomInt(0,numblocks);
//            int y_coord = 12;
//            for(int i = 0; i <= numblocks; i++) {
//                if (i == gap) {
//                    y_coord = y_coord + 25;
//                } else {
//                    wall = wall.overlayImages(
//                            new RectangleImage(new Posn(window_w, y_coord), 
//                            25, 25, new Green()));
//                    y_coord = y_coord + 25;
//                }
//            }
//            return wall;
//        }
//        
//        public void slide_wall() {
//            wall.moveTo(new Posn(wall.pinhole.x - 25, wall.pinhole.y));
//        }
//        
//        public int wall_edge() {
//            return wall.pinhole.x;
//        }
//        
//        //store a wall as a data structure with a position, append to worldImage 
//        //in a loop, keep posns in an array and map slide to the posns to reflect
//        //movement
//        
//    }
    
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
        
        public int jay_edge() {
            return (jay_x + 13);
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
//        wall_block building;
        single_wall theWall = new single_wall(new ArrayList<Posn>(), 0);
        all_walls theWalls = new all_walls(new ArrayList<single_wall>());
//        int counter = 0;
        
        public playField(slideyJay Jay, single_wall Wall, all_walls theWalls) {
            this.Jay = Jay;
            this.theWall = Wall;
            this.theWalls = theWalls;
        }
        
        public void onTick() {
            for (int i = 0; i < theWalls.currentWalls.size(); i++) {
                theWall = this.theWalls.currentWalls.get(i).slideWall();
                this.theWalls.currentWalls.remove(i);
                this.theWalls.currentWalls.add(theWall);
//                this.makeImage();
            }
            System.out.println(Jay.jay_edge());
            System.out.println(theWalls.currentWalls.get(0).oneWall.get(0).x);
        }
        
        public void onKeyEvent(String str) {
            Jay.move(str);
        }
        
        public WorldImage makeImage() {
//            System.out.println("theWalls is" + theWalls.)
//            theWalls.collect_walls();
            return theWalls.walls_image(theWalls.collect_walls()).
                    overlayImages(this.Jay.jayImage());
//            //wait function instead of counter?
//            counter++;
//            if (counter % 20 == 0) {
//                return new RectangleImage(new Posn(window_w / 2, window_h / 2),
//                        window_w, window_h, background).
//                        overlayImages(this.theWall.build_one_wall(), this.Jay.jayImage());
//            } else {
//                return theWorld.overlayImages(theWall.wall, this.Jay.jayImage());
//                return new RectangleImage(new Posn(window_w / 2, window_h / 2),
//                        window_w, window_h, background).
//                        overlayImages(this.Jay.jayImage());
//            }
        }
        
        public boolean collisionHuh() {
//            System.out.println(Jay.jay_edge());
//            System.out.println(theWalls.currentWalls.get(1).oneWall.get(1).x);
            return (Jay.jay_edge() == theWalls.currentWalls.get(1).oneWall.get(1).x);
        }
        
        public WorldEnd gameOver() {
            if(collisionHuh()) {
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
        single_wall oneWall = new single_wall(new ArrayList<Posn>(), 0);
        all_walls theWall = new all_walls(new ArrayList<single_wall>());
        
        playField sampleField = new playField(player, oneWall, theWall);
        
        
    }
    
    public static void main(String[] args) {
        System.out.println("test");
 //       System.out.println()
        SampleWorld x = new SampleWorld();
        x.sampleField.bigBang(1000, 500, 1);
    }
}
