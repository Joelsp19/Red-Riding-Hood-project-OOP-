import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrandmaHouse extends Entity{
    private CookieResource cookies;
    public GrandmaHouse(
            String id,
            Point position,
            List<PImage> images) {
        super(id,position,images);
        cookies = new CookieResource(0);
    }

    public CookieResource getCookies() {
        return cookies;
    }

    public void setCookies(CookieResource cookies) {
        this.cookies = cookies;
    }
}
