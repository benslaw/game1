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
class Testeez {

    //thanks Jay!
    public static void check(String label, Object x, Object y) throws Exception {
        if (x != y) {
            throw new Exception("\n" + label + ": " + x + " should equal " + y + " but it don't :(");
        }
    }
}

public class Game1 {

    interface constants {
        //set of constants about the world that are used through all classes
        
        public static final int window_w = 1000;
        public static final int window_h = 500;
        int numblocks = window_h/25;
        public Color background = new Color(255, 255, 255);
        Random rand = new Random();
        public WorldImage theWorld =
                new RectangleImage(new Posn(window_w/2, window_h/2), 
                window_w, window_h, background);
    }
    
    public static class single_wall implements constants {
        
        ArrayList<Posn> oneWall = new ArrayList<Posn>();
        int y_coord_hole;
        
        //constructor
        public single_wall(ArrayList<Posn> oneWall, int theHole) {
            this.oneWall = oneWall;
            this.y_coord_hole = theHole;
        }
        
        //random integer generator
        public int randomInt(int min, int max) {
            return rand.nextInt((max - min) + 1) + min;
        }
        
        //build_one_wall --> takes no inputs, returns a single_wall which contains
        //an ArrayList of Posns representing blocks and a integer representing 
        //the y-coordinate of the gap in the wall that the player should aim for
        public single_wall build_one_wall() {
            int gap = randomInt(1,numblocks-1);
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
        
        //slideWall() --> iteratively scrolls through the contents of a single_wall's
        //ArrayList<Posn> and decreases the x coordinate by 25, represenging a slide 
        //to the left. Returns a new instance of a single_wall with the new ArrayList
        //of the modified positions and and old gap integer
        public single_wall slideWall() {
            ArrayList<Posn> newWall = new ArrayList<Posn>();
            for(int i = 0; i < oneWall.size(); i++) {
                newWall.add(new Posn(oneWall.get(i).x - 25, oneWall.get(i).y));
            }
            return new single_wall(newWall, y_coord_hole);
        }
        
    }
    
    public static class all_walls implements constants {
        
        ArrayList<single_wall> currentWalls = new ArrayList<single_wall>();
        int counter = 6;
        int frequency = 7;
        Color randCol = randomColor();
        
        //constructor
        public all_walls(ArrayList<single_wall> currentWalls) {
            this.currentWalls = currentWalls;
        }
        
        //collect_walls() --> takes no inputs. if the counter % frequency = 0, a new
        //wall will be added to the collection of currentWalls, else just return
        //currentWalls
        public ArrayList<single_wall> collect_walls() {
            counter++;
            single_wall newWall = new single_wall(new ArrayList<Posn>(), 0);
            if(counter % frequency == 0) {
                currentWalls.add(newWall.build_one_wall());
                return currentWalls;
            } else {
                return currentWalls;
            }
        }
        
        //random integer generator
        public int randomInt(int min, int max) {
            return rand.nextInt((max - min) + 1) + min;
        }
        
        //random color generator. 0-->254 is selected so the color has 0 probability
        //of matching the background color, which is 255,255,255
        public Color randomColor() {
            return new Color(randomInt(0,254), randomInt(0,254), randomInt(0,254));
        }
        
        //walls_image --> takes an ArrayList of single_walls as input, and for
        //each single_wall, scrolls through the ArrayList of positions and creates
        //a block at each recorded position. These blocks are then overlayed on
        //the background, and the new WorldImage is returned
        public WorldImage walls_image(ArrayList<single_wall> walls) {
            RectangleImage temp;
            WorldImage newWorld = theWorld;
            for (int i = 0; i < walls.size(); i++) {
                for (int j = 0; j < numblocks; j++) {
                    temp = new RectangleImage(walls.get(i).oneWall.get(j), 25, 25, randCol);     
                    newWorld = newWorld.overlayImages(temp);
                }
            }
            return newWorld;
        }
        
        //slide_all_walls() --> takes no inputs. for each wall in currentWalls,
        //the wall is stored in a temporary variable, then the slideWall() is
        //called on it and added to the array at the same index the wall was
        //pulled from. finally, the original wall is removed from currentWalls
        //ends by returning currentWalls shifted to the left
        public ArrayList<single_wall> slide_all_walls() {
            for(int i = 0; i < currentWalls.size(); i++) {
                single_wall temp = currentWalls.get(i);
                currentWalls.add(i, temp.slideWall());
                currentWalls.remove(temp);
            }
            return currentWalls;
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
        
        //not super necessary as jay_x can just be called, but this just
        //returns jay's x coordinate
        public int jay_edge() {
            return jay_x;
        }
        
        //move function --> takes a string as input. if string is up, jay moves
        //one block up on the screen. if string is down, jay moves one block
        //down on the screen. movement in the left and right directions is
        //prohibited, and therefore not included.
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
        
        //jayImage() --> returns a new WorldImage of jay read from the a file
        //at the location specified by where_is_Jay(). if the file cannot be found,
        //switch to the commented line above it to produce a blue square in place
        //of the image of Jay
        public WorldImage jayImage() {
//            return new RectangleImage(this.where_is_Jay(), 25, 25, new Blue());
            return new FromFileImage(this.where_is_Jay(), "jay_face_resized.jpg");
        }
        
    }
    
    public static class playField extends World implements constants{
        
        slideyJay Jay;
        single_wall theWall = new single_wall(new ArrayList<Posn>(), 0);
        all_walls theWalls = new all_walls(new ArrayList<single_wall>());
        int marker = 0;
        int score = 0;
        
        //constructor
        public playField(slideyJay Jay, single_wall Wall, all_walls theWalls) {
            this.Jay = Jay;
            this.theWall = Wall;
            this.theWalls = theWalls;
        }
        
        //onTick() --> function built into the impworlds package. for each tick
        //of the clock, first check if the first wall in currentWalls is at the
        //edge of the screen, if so remove it from currentWalls. this forces the
        //wall at index 1 to assume index position 0. then, slide all walls, and
        //call collect_walls() to check if a new wall should be added to the
        //screen
        public void onTick() {
            if(theWalls.currentWalls.get(0).oneWall.get(0).x == 0) {
                theWalls.currentWalls.remove(0);
            }
            this.theWalls.slide_all_walls();
            this.theWalls.collect_walls();
        }
        
        //onKeyEvent() --> impworlds function, reads in a key event and converts
        //it to a string to be used by other functions. in this case, read the
        //key, convert it to a string, and call move on Jay
        public void onKeyEvent(String str) {
            Jay.move(str);
        }
        
        //makeImage() --> essentially a draw function. the if statement serves
        //to initialize the currentWalls when the game starts. scoreBox is a 
        //text image that appears at the lower right of the game screen and
        //displays a running total of the player's score. then return a WorldImage
        //comprised of the currentWalls overlayed with Jay and the scoreBox.
        public WorldImage makeImage() {
            if(marker == 0) {
                theWalls.collect_walls();
                marker = 1;
            }
            TextImage scoreBox = new TextImage(new Posn(window_w-50,
                    window_h-25), "Score: " + score, new Red());
            return theWalls.walls_image(theWalls.currentWalls).
                    overlayImages(this.Jay.jayImage(), scoreBox);
        }
        
        //collisionHuh() --> boolean function used to determine whether the game
        //ends. if jay's x coordinate matches the x coordinate of wall 0 (which
        //is the only wall jay will ever be allowed to collide with) and jay's
        //y coordinate matches that of wall 0's gap, then the player successfully
        //slid jay through the wall; the score should be incremented and return
        //false. if the x coordinates matach but the y coordinate of jay and the
        //gap don't match, then there was a collision and return true. otherwise,
        //jay must but somewhere between walls, and therefore a collision didn't
        //occur
        public boolean collisionHuh() {
            if(Jay.jay_edge() == theWalls.currentWalls.get(0).oneWall.get(0).x
                    && Jay.jay_y == theWalls.currentWalls.get(0).y_coord_hole) {
                score++;
                return false;
            } else if(Jay.jay_edge() == theWalls.currentWalls.get(0).oneWall.get(0).x){
                return true;
            } else {
                return false;
            }
        }
        
        //worldEnds() --> required by impworlds to determine when the game ends.
        //if there was a collision, the game ends and the screen reports that
        //the player killed jay, as well as the player's score. otherwise,
        //the game continues as normal.
        public WorldEnd worldEnds() {
            if(this.collisionHuh()) {
                return new WorldEnd(true, new OverlayImages(this.makeImage(),
                        new TextImage(new Posn(window_w/2, window_h/2),
                        "You killed Jay!", new Red()).overlayImages(
                        new TextImage(new Posn(window_w/2, window_h/2+25),
                        "Score: " + score, new Red()))));
            } else {
                return new WorldEnd(false, this.makeImage());
            }
        }
        
    }
    
    public static class SampleWorld implements constants {
        
        //constructor
        SampleWorld() {}
        
        static slideyJay player = new slideyJay(window_h - 13);
        static single_wall oneWall = new single_wall(new ArrayList<Posn>(), 0);
        static all_walls theWall = new all_walls(new ArrayList<single_wall>());
        
        playField sampleField = new playField(player, oneWall, theWall);
        
        //testing function that utilizes Testeez above
        public static void test() throws Exception {
            SampleWorld s = new SampleWorld();
            //tick walls check: on initialization, the number of current walls should equal the number of the
            //counter divided by the frequency rounded down. once one set of walls has passed, the number of walls on 
            //screen will always be at most (window_w/25)/frequency.
            Testeez.check("tick walls", s.theWall.currentWalls.size(), (int) Math.floor(theWall.counter / theWall.frequency));
//            int jay_before_move_y = (int) s.sampleField.Jay.jay_y;
//            s.sampleField.Jay.move("up");
//            System.out.println((int)jay_before_move_y);
//            System.out.println((int)s.sampleField.Jay.jay_y + 25);
//            Testeez.check("player moves",(int) jay_before_move_y, (int) s.sampleField.Jay.jay_y + 25);
//            s.sampleField.Jay.move("down");
//            Testeez.check("player moves on down edge",(int) jay_before_move_y, (int) s.sampleField.Jay.jay_y);
            //move Jay to the top of the screen
            for(int i = 0; i < numblocks; i++) {
                s.sampleField.Jay.move("up");
            }
            //player on top of screen: check to see if the player has reached the upper bound of the playfield
            Testeez.check("player on top of screen", s.sampleField.Jay.jay_y, (int) 12.5);
            //player moves up on edge: check to see that even if an "up" movement is called, the player does
            //not move any farther up on the screen
            s.sampleField.Jay.move("up");
            Testeez.check("player moves up on edge", s.sampleField.Jay.jay_y, (int) 12.5);
            //tick the world and makeImages such to create a situation where the player ocmes into contact
            //with the first wall
            for(int i = 0; i < 34; i++) {
                s.sampleField.makeImage();
                s.sampleField.onTick();
            }
            //tick walls: here, the property still holds as the screen is not full of walls yet
            Testeez.check("tick walls", s.theWall.currentWalls.size(), (int) Math.floor(theWall.counter / theWall.frequency));
            //the following five tests (successful pass, score increment, player hit wall, succesful
            //worldEnd and player between walls) are intended to test the player's collision with a wall. 
            //there are three possible collision tests:
            //successful pass --> player and wall have same x coordinate, and player y coordinate matches the
            //gap in the wall. collision status is false, and the score should increment once the player is
            //through the gate.
            //player hit wall --> player and wall have same x coordinate, but player y coordinate does not match
            //the gap in the wall. collision status is true, and the worldEnd function should return a true value
            //player between walls --> in any instance when the player x coordinate does not match a wall x
            //coordinate, the player must be between walls, and therefore collision status is false.
            if(s.sampleField.Jay.jay_y == s.sampleField.theWalls.currentWalls.get(0).y_coord_hole
                    && s.sampleField.Jay.jay_edge() == s.sampleField.theWalls.currentWalls.get(0).oneWall.get(0).x) {
                Testeez.check("successful pass", s.sampleField.collisionHuh(), false);
                s.sampleField.makeImage();
                s.sampleField.onTick();
                Testeez.check("score increment", s.sampleField.score, 1);
            } else if(s.sampleField.Jay.jay_y != s.sampleField.theWalls.currentWalls.get(0).y_coord_hole
                    && s.sampleField.Jay.jay_edge() == s.sampleField.theWalls.currentWalls.get(0).oneWall.get(0).x){
                Testeez.check("player hit wall", s.sampleField.collisionHuh(), true);
                Testeez.check("successful worldEnd", s.sampleField.worldEnds().worldEnds, true);
            } else {
                Testeez.check("player between walls", s.sampleField.collisionHuh(), false);
            }
        }
 
    }
    
    public static class ActualWorld implements constants {

        //constructor
        ActualWorld() {}
        
        static slideyJay player = new slideyJay(window_h - 13);
        static single_wall oneWall = new single_wall(new ArrayList<Posn>(), 0);
        static all_walls theWall = new all_walls(new ArrayList<single_wall>());
        playField sampleField = new playField(player, oneWall, theWall);
        
        //note: this world is identical to SampleWorld, and only serves to create
        //an instance of the game independent from the testing instance that
        //starts from scratch and is accessible by the player.
        
    }

    
    public static void main(String[] args) throws Exception {
        Game1.SampleWorld.test();
        System.out.println("All tests passed!");
        ActualWorld x = new ActualWorld();
        x.sampleField.bigBang(1000, 500, 0.5);
    }
}
