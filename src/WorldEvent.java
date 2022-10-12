import java.awt.*;
import java.util.Random;

public class WorldEvent {
    public static int EFFECT_AREA_BOUNDS = 5;

    private Point ptPressed;
    private int effectArea;



    public WorldEvent(){
        ptPressed = null;
        this.effectArea = 1;
    }

    public boolean createWorldEvent(WorldModel world, EventScheduler scheduler, ImageStore imageStore, Point p){
        boolean addBckgrd;
        Random rand = new Random();
        int actionPeriod = 700;
        int animationPeriod = 200;
        int randNum = rand.nextInt(4);

        //check if wolf position contains an entity
        Point wolfPt = p;
        if (randNum==0) {
            wolfPt = new Point(p.x + 0, p.y - 1);
        }else if(randNum ==1){
            wolfPt = new Point(p.x + 1, p.y + 0);
        }else if(randNum ==2){
            wolfPt = new Point(p.x -1, p.y + 0);
        }else if(randNum ==3){
            wolfPt = new Point(p.x + 0, p.y + 1);
        }

        if(world.isOccupied(wolfPt)) {
            return false;
        }

        for(int x=-1; x<=1;x++){
            for (int y=-1;y<=1;y++){
                addBckgrd = true;
                Point pt = new Point(p.x + x, p.y + y);
                if (pt.equals(wolfPt)) {
                    addBckgrd = false;
                    Wolf wolf =  Factory.createWolf("wolf_" + pt.x + "_" + pt.y,pt,actionPeriod,animationPeriod,0, imageStore.getImageList(Functions.WOLF_KEY));
                    world.addEntity(wolf);
                    wolf.scheduleActions(world,scheduler,imageStore);
                }
                if (x==0 && y==0){
                    addBckgrd = false;
                    GrandmaHouse gHouse = new GrandmaHouse("gHouse_" + p.x + "_" + p.y, p, imageStore.getImageList(Functions.GHOUSE_KEY));
                    world.addEntity(gHouse);
                }
                if (addBckgrd) {
                    world.setBackground(pt, new Background("cloud_" + pt.x + "_" + pt.y, imageStore.getImageList(Functions.CLOUD_KEY)));
                }
            }
        }
        return true;


    }

    public void growEvent(WorldModel world, ImageStore imageStore){
        //System.out.println(effectArea);
        if((effectArea < EFFECT_AREA_BOUNDS)) {
            effectArea ++;
            boolean addBckgrd;
            for (int x = -1 * effectArea; x <= effectArea; x++) {
                for (int y = -1 * effectArea; y <= effectArea; y++) {
                    addBckgrd = true;
                    if (x == 0 && y == 0) {
                        addBckgrd = false;
                    }
                    if (addBckgrd) {
                        Point pt = new Point(ptPressed.x + x, ptPressed.y + y);
                        world.setBackground(pt, new Background("cloud_" + pt.x + "_" + pt.y, imageStore.getImageList(Functions.CLOUD_KEY)));
                    }
                }
            }
        }
    }
    public void reduceEvent(WorldModel world, ImageStore imageStore){
        if(effectArea > 1) {
            boolean addBckgrd;
            for (int x = -1 * effectArea; x <= effectArea; x++) {
                for (int y = -1 * effectArea; y <= effectArea; y++) {
                    addBckgrd = true;
                    if (x == 0 && y == 0) {
                        addBckgrd = false;
                    }
                    if (addBckgrd) {
                        Point pt = new Point(ptPressed.x + x, ptPressed.y + y);
                        world.setBackground(pt, new Background("grass" + pt.x + "_" + pt.y, imageStore.getImageList("grass")));
                    }
                }
            }

            effectArea--;
            for (int x = -1 * effectArea; x <= effectArea; x++) {
                for (int y = -1 * effectArea; y <= effectArea; y++) {
                    addBckgrd = true;
                    if (x == 0 && y == 0) {
                        addBckgrd = false;
                    }
                    if (addBckgrd) {
                        Point pt = new Point(ptPressed.x + x, ptPressed.y + y);
                        world.setBackground(pt, new Background("cloud" + pt.x + "_" + pt.y, imageStore.getImageList(Functions.CLOUD_KEY)));
                    }
                }
            }

        }
    }
    public Point getPtPressed() {
        return ptPressed;
    }

    public void setPtPressed(Point ptPressed) {
        this.ptPressed = ptPressed;
    }

    public int getEffectArea() {
        return effectArea;
    }

    public void setEffectArea(int effectArea) {
        this.effectArea = effectArea;
    }
}
