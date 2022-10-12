import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class Guardian extends MovingEntity{


    public Guardian(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod) {
        super(id,position,images,actionPeriod,animationPeriod);
    }


    //from ActivityEntity interface
    public void execute(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {

        Optional<Entity> guardianTarget =
                findNearest(world, getPosition(), new ArrayList<Class>(Arrays.asList(RedRidingHood.class)));
        Point curPos = getPosition();
        if (guardianTarget.isPresent()) {
            if (moveTo( world, guardianTarget.get(), scheduler)) {
                //give rrh a powerup
                //if the max powerup is reached, then gives her a cookie
                boolean giveCookies = false;
                RedRidingHood rrh =(RedRidingHood)guardianTarget.get();
                if (rrh.getActionPeriod() > RedRidingHood.RRH_ACTION_PERIOD_MIN){
                    rrh.setActionPeriod(rrh.getActionPeriod() - 20);
                }else{
                    giveCookies = true;
                }
                if (rrh.getAnimationPeriod() > RedRidingHood.RRH_ANIMATION_PERIOD_MIN){
                    rrh.setAnimationPeriod(rrh.getAnimationPeriod() - 20);
                }else{
                    giveCookies = true;
                }

//                System.out.println("rrh action:: " + rrh.getActionPeriod());
//                System.out.println("rrh animation:: " + rrh.getAnimationPeriod());

                if (giveCookies){
                    rrh.setCookieCount(rrh.getCookieCount() + 1);
                }
                transform(world,scheduler,imageStore);
            }
        }

        if(curPos == getPosition()){
            transform(world,scheduler,imageStore);
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                getActionPeriod());
    }


    //from MovingEntity interface
    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }
                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public Point nextPosition(
            WorldModel world, Point destPos) {

        //PathingStrategy strat = new SingleStepPathingStrategy();
        PathingStrategy strat = new AStarPathingStrategy();

        List<Point> compList = strat.computePath(
                getPosition(),
                destPos,
                (x)-> world.withinBounds(x) && !world.isOccupied(x),
                (x,y) -> x.adjacent(y),
                PathingStrategy.CARDINAL_NEIGHBORS);

        //if no path
        if (compList.size() ==0) {
            return getPosition();
        }

        //return the first element in the computed list
        return compList.get(0);
    }

    public void transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        Fairy fairy = Factory.createFairy(getId(), getPosition(),getActionPeriod(),getAnimationPeriod(), imageStore.getImageList(Functions.FAIRY_KEY));
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        world.addEntity(fairy);
        fairy.scheduleActions(world,scheduler,imageStore);
    }

}
