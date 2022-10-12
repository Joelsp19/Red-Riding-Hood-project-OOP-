import processing.core.PImage;

import java.util.*;
import java.util.stream.Collectors;

public class Fairy extends MovingEntity{

    public Fairy(
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

        Optional<Entity> fairyTarget =
                findNearest(world, getPosition(), new ArrayList<Class>(Arrays.asList(Stump.class, WheatStump.class, SugarStump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if(VirtualWorld.worldEvent != null && VirtualWorld.worldEvent.getPtPressed()!=null){
                boolean inEffect = false;
                for (int x = -1 * VirtualWorld.worldEvent.getEffectArea(); x <= VirtualWorld.worldEvent.getEffectArea(); x++) {
                    for (int y = -1 * VirtualWorld.worldEvent.getEffectArea(); y <=VirtualWorld.worldEvent.getEffectArea(); y++) {
                        Point pt= new Point(VirtualWorld.worldEvent.getPtPressed().x + x, VirtualWorld.worldEvent.getPtPressed().y + y);
                        if (getPosition().equals(pt)){
                            inEffect = true;
                        }
                    }
                }

                if(inEffect){
                    transform(world,scheduler,imageStore);
                }
            }

            if (moveTo( world, fairyTarget.get(), scheduler)) {
                //check if in proximity of world event

                AnimationEntity sapling;
                if (fairyTarget.get().getClass() == WheatStump.class){
                    sapling = Factory.createWheatSapling("wheatSapling_" + getId(), tgtPos,
                            imageStore.getImageList(Functions.WHEAT_SAPLING_KEY));
                }else if (fairyTarget.get().getClass() == SugarStump.class) {
                    sapling = Factory.createSugarSapling("sugarSapling_" + getId(), tgtPos,
                            imageStore.getImageList(Functions.SUGAR_SAPLING_KEY));
                }else{
                        sapling = Factory.createSapling("sapling_" + getId(), tgtPos,
                                imageStore.getImageList(Functions.SAPLING_KEY));
                }

                world.addEntity(sapling);
                sapling.scheduleActions( world,scheduler, imageStore);
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
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
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

        if(VirtualWorld.worldEvent.getEffectArea() >1){
            Guardian guardian = Factory.createGuardian(getId(), getPosition(),getActionPeriod(),getAnimationPeriod(), imageStore.getImageList(Functions.GUARDIAN_KEY));
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            world.addEntity(guardian);
            guardian.scheduleActions(world,scheduler,imageStore);
        }

    }


}

