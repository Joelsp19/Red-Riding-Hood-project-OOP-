import processing.core.PImage;

import java.util.*;
import java.util.stream.Collectors;

public class DudeNotFull extends MovingEntity{

    private int resourceLimit;
    private List<Resource> resources;
    private int resourceCount;


    public DudeNotFull(
            String id,
            Point position,
            List<PImage> images,
            List<Resource> resourceCount,
            int resourceLimit,
            int actionPeriod,
            int animationPeriod) {
        super(id,position,images,actionPeriod,animationPeriod);
        this.resources = resourceCount;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resources.stream().map(s->s.getQuantity()).reduce(0, Integer::sum); //should be 0
    }


    //from ActivityEntity interface
    public void execute(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {

        Optional<Entity> target =
                findNearest(world,  getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class, Wheat.class, WheatSapling.class, Sugar.class, SugarSapling.class)));

        if (!target.isPresent() || !moveTo( world,
                target.get(),
                scheduler)
                || !transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }

    //from MovingEntity interface
    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if ( getPosition().adjacent(target.getPosition())) {
            if (target.getClass() == Wheat.class){
                resources.get(0).setQuantity(resources.get(0).getQuantity() + 1);
            }else if(target.getClass() == Sugar.class) {
                resources.get(1).setQuantity(resources.get(0).getQuantity() + 1);
            }else {
                resources.get(2).setQuantity(resources.get(2).getQuantity() + 1);
            }
            setResourceCount(resourceCount + 1);
            ((ChoppableEntity)target).setHealth(((ChoppableEntity)target).getHealth() - 1);
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


    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (resourceCount >= resourceLimit) {
            AnimationEntity miner = Factory.createDudeFull(getId(),
                    getPosition(), getActionPeriod(),
                    getAnimationPeriod(),
                    resources,
                    resourceLimit,
                    getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(world,scheduler, imageStore);

            return true;
        }

        return false;
    }


    //from ResourceEntity interface
    public int getResourceCount() {
        return resourceCount;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    public void setResourceCount(int resourceCount){
        this.resourceCount = resourceCount;
    }


}

