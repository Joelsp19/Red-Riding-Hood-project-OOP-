import processing.core.PImage;

import java.util.*;

public class RedRidingHood extends MovingEntity {

    public static final int RRH_ACTION_PERIOD_MIN = 100;
    public static final int RRH_ANIMATION_PERIOD_MIN = 80;


    private int cookieCount;
    private List<Entity> visited;
    private List<Point> path = new LinkedList<>();
    private int pathSteps = 0;

    public RedRidingHood(
            String id,
            Point position,
            List<PImage> images,
            int resourceCount,
            int actionPeriod,
            int animationPeriod) {
        super(id,position,images,actionPeriod,animationPeriod);
        cookieCount = resourceCount;
        visited = new ArrayList<Entity>();
    }

    //from ActivityEntity interface
    public void execute(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {

        boolean houseSearch = true;
        Optional<Entity> target;
        if (VirtualWorld.worldEvent != null && VirtualWorld.worldEvent.getPtPressed()!=null) {
            houseSearch = true;
            if (cookieCount > 0) {
                target = findNearest(world, getPosition(), new ArrayList<>(Arrays.asList(GrandmaHouse.class)));
                if (target.isPresent()) {
                    houseSearch = false;
                    if (moveTo(world, target.get(), scheduler)) {
                        GrandmaHouse h = ((GrandmaHouse) target.get());
                        //gives grandma the cookies
                        h.setCookies(new CookieResource(cookieCount + h.getCookies().getQuantity()));
                        //set rrh cookie count to 0
                        cookieCount = 0;
                        //reduces the world event effect
                        VirtualWorld.worldEvent.reduceEvent(world,imageStore);
                        //increases the wolf's speed
                        Wolf wolf = null;
                        for (Entity entity : world.getEntities()) {
                            if (entity.getClass() == Wolf.class) {
                                wolf = (Wolf) entity;
                            }
                        }
                        if (wolf != null) {
                            if (wolf.getActionPeriod() > Wolf.WOLF_ACTION_PERIOD_MIN) {
                                wolf.setActionPeriod(wolf.getActionPeriod() - 50);
                            }
                            if (wolf.getAnimationPeriod() > Wolf.WOLF_ANIMATION_PERIOD_MIN) {
                                wolf.setAnimationPeriod(wolf.getAnimationPeriod() - 50);
                            }
                        }

//                        System.out.println("wolf action:: " + wolf.getActionPeriod());
//                        System.out.println("wolf animation:: " + wolf.getAnimationPeriod());

                        //for debugging
//                        System.out.println("grandma's cookies: " + h.getCookies().getQuantity());
                    }
                }
            }
        }
        if (houseSearch){
            target = findNearest(world, getPosition(), new ArrayList<>(Arrays.asList(FullHouse.class)));
            if (target.isPresent()) {
                if (moveTo(world, target.get(), scheduler)) {
                    visited.add(target.get());
                    FullHouse h = ((FullHouse) target.get());
                    cookieCount += h.getCookies().getQuantity();
                    h.getCookies().setQuantity(0);
                    h.transform(world, scheduler, imageStore); //transforms a full house into a house
                }
            }else{
                searchHouse(world,imageStore, scheduler);
            }

        }


        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                getActionPeriod());

    }


    private void searchHouse(WorldModel world,
                             ImageStore imageStore,
                             EventScheduler scheduler){
        Optional<Entity> target = findNearest(world, getPosition(), new ArrayList<>(Arrays.asList(House.class)));
        if (target.isPresent()) {
            // keep going until house is reached
            if (moveTo(world, target.get(), scheduler)){
                visited.add(target.get());

            }
        }
    }

    //from MovingEntity interface
    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {

        if (getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            if (path.isEmpty() || pathSteps ==0){
                path = nextListPosition(world, target.getPosition());
                pathSteps = Math.min(5,path.size());
            }
            Point nextPos = path.get(0);
            path.remove(nextPos);

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    path.add(0,nextPos);
                    nextPos = getPosition();
                }
                world.moveEntity(this, nextPos);
                pathSteps--;
            }
            return false;
        }

    }
    public Point nextPosition(WorldModel world, Point destPos){
        return getPosition();
    }


    public List<Point> nextListPosition(
            WorldModel world, Point destPos) {

        //PathingStrategy strat = new SingleStepPathingStrategy();
        PathingStrategy strat = new AStarPathingStrategy();
        List<Point> compList = strat.computePath(
                getPosition(),
                destPos,
                (x)-> world.withinBounds(x) && (!world.isOccupied(x)),
                (x,y) -> x.adjacent(y),
                PathingStrategy.CARDINAL_NEIGHBORS);

        //if no path
        if (compList.size() ==0) {
            List<Point> compList2 = strat.computePath(
                    getPosition(),
                    destPos,
                    (x)-> world.withinBounds(x) && (!world.isOccupied(x)),
                    (x,y) -> x.adjacent(y),
                    PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);
            if(compList2.size()==0){
                return new LinkedList<Point>(Arrays.asList(getPosition()));
            }
            return compList2;
        }

        //return the first element in the computed list
        return compList;
    }

    public void setCookieCount(int cookieCount) {
        this.cookieCount = cookieCount;
    }

    public int getCookieCount() {
        return cookieCount;
    }

    @Override
    public Optional<Entity> findNearest(
            WorldModel world, Point pos, List<Class> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind : kinds) {
            for (Entity entity : world.getEntities()) {
                if (entity.getClass() == kind && !visited.contains(entity)) {
                    ofType.add(entity);
                }
            }
            if (ofType.isEmpty() && !(kinds.get(0) == FullHouse.class)){ //if we can't find any houses, then remove an item from the visited list
                if (!visited.isEmpty()){
                    visited.remove(0);
                }
            }
        }
        return nearestEntity(ofType, pos);
    }

}