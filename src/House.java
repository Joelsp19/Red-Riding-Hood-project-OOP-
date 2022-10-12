import processing.core.PImage;

import java.util.*;
import java.util.stream.Collectors;

public class House extends Entity{

    //one cookie = one sugar, one wheat, one wood

    private List<Resource> resources;

    public House(
            String id,
            Point position,
            List<Resource> resources,
            List<PImage> images) {
        super(id,position,images);
        this.resources = resources;
    }

    public void addCookie(){

        if (resources.get(0).getQuantity() > 0 && resources.get(1).getQuantity() > 0 && resources.get(2).getQuantity() > 0){
            //find the min value
            int min =findMin();
            //subtract the min value from all the resources
            for(int i=0; i< resources.size()-1;i++){
                resources.get(i).setQuantity(resources.get(i).getQuantity()-min);
            }
            //adds the min value to the cookie count
            resources.get(3).setQuantity(resources.get(3).getQuantity()+min);
        }
    }

    private int findMin(){
        int min = resources.get(0).getQuantity();
        for (int i=0; i<resources.size()-1;i++){
            int val = resources.get(i).getQuantity();
            if (val<min){
                min = val;
            }
        }
        return min;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void addResources(List<Resource> resources) {
        for (int i=0;i< resources.size();i++){
            this.resources.get(i).setQuantity(this.resources.get(i).getQuantity() + resources.get(i).getQuantity());
        }
    }

    public void addUpdateResources(List<Resource> resources){
        addResources(resources);
        addCookie();
    }

    public void transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (resources.get(3).getQuantity() > 0) {
            FullHouse house = Factory.createFullHouse(getId(), getPosition(),getResources(), imageStore.getImageList(Functions.HOUSE_KEY));
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            world.addEntity(house);
        }
    }

    public CookieResource getCookies() {
        return (CookieResource) resources.get(3);
    }

    public void setCookies(CookieResource cookies) {
        this.resources.set(3, cookies);
    }


}

