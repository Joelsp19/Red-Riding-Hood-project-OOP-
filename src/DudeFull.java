import processing.core.PImage;

import java.util.*;

public class DudeFull extends MovingEntity{

    private int resourceLimit;
    private List<Resource> resources;
    private final int resourceCount = 0;


    public DudeFull(
            String id,
            Point position,
            List<PImage> images,
            List<Resource> resources,
            int resourceLimit,
            int actionPeriod,
            int animationPeriod) {
        super(id,position,images,actionPeriod,animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resources = resources;
    }

    public void execute(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {

        Optional<Entity> fullTarget =
                findNearest(world, getPosition(), new ArrayList<>(Arrays.asList(House.class, FullHouse.class)));

        if (fullTarget.isPresent() && moveTo(world,
                fullTarget.get(), scheduler)) {
            if (fullTarget.get().getClass() == FullHouse.class){
                ((FullHouse)fullTarget.get()).addUpdateResources(resources);
            }else {
                ((House) fullTarget.get()).addUpdateResources(resources);
                ((House) fullTarget.get()).transform(world, scheduler, imageStore);
            }
            transform(world, scheduler, imageStore);

        } else {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }


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
                (x)-> world.withinBounds(x) && (!world.isOccupied(x) || world.getOccupancyCell(x).getClass()==(Stump.class) || world.getOccupancyCell(x).getClass()==(WheatStump.class)|| world.getOccupancyCell(x).getClass()==(SugarStump.class)),
                (x,y) -> x.adjacent(y),
                PathingStrategy.CARDINAL_NEIGHBORS);

        //if no path
        if (compList.size() ==0) {
            return getPosition();
        }

        //return the first element in the computed list
        return compList.get(0);

    }


    public int getResourceCount() {
        return resourceCount;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }


    public void transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        ActivityEntity miner = Factory.createDudeNotFull(getId(),
                getPosition(), getActionPeriod(),
                getAnimationPeriod(),
                resourceLimit,
                getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(world,scheduler, imageStore);
    }


}

