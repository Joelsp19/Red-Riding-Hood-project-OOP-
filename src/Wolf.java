import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Wolf extends MovingEntity {

    public static final int WOLF_ACTION_PERIOD_MAX = 800;
    public static final int WOLF_ANIMATION_PERIOD_MAX = 400;
    public static final int WOLF_ACTION_PERIOD_MIN = 500;
    public static final int WOLF_ANIMATION_PERIOD_MIN = 100;



    public Wolf(
            String id,
            Point position,
            List<PImage> images,
            int resourceCount,
            int actionPeriod,
            int animationPeriod) {
        super(id,position,images,actionPeriod,animationPeriod);
    }

    //from ActivityEntity interface
    public void execute(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {

        Optional<Entity> target =
                findNearest(world, getPosition(), new ArrayList<>(Arrays.asList(RedRidingHood.class)));

        if (target.isPresent()) {
            //if catch redRidingHood
            if (moveTo( world, target.get(), scheduler)) {
                VirtualWorld.worldEvent.growEvent(world,imageStore);
                if(getActionPeriod() < Wolf.WOLF_ACTION_PERIOD_MAX){
                   setActionPeriod(getActionPeriod() + 100);
                }
                if(getAnimationPeriod() < Wolf.WOLF_ANIMATION_PERIOD_MAX){
                    setAnimationPeriod(getAnimationPeriod() + 100);
                }

//                System.out.println("wolf action" + getActionPeriod());
//                System.out.println("wolf animation" + getAnimationPeriod());


            }
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
                (x)-> world.withinBounds(x) && (!world.isOccupied(x)),
                (x,y) -> x.adjacent(y),
                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);

        //if no path
        if (compList.size() ==0) {
            return getPosition();
        }

        //return the first element in the computed list
        return compList.get(0);
    }

}