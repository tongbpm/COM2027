package org.com2027.group11.beerhere;

import org.junit.Test;
import org.com2027.group11.beerhere.beer.Beer;

import java.util.HashSet;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by alexpotter1 on 27/02/2018.
 */

public class BeerTest {

    @Test
    public void defaultConstructorTest() {
        Beer beer = new Beer();
        assertNotNull(beer);
    }

    @Test
    public void parameterisedConstructorTest() {
        String name = "beer 1";
        String imageid = "1234";
        int upvotes = 350;
        int downvotes = 0;
        Beer beer = new Beer(name, imageid, upvotes, downvotes,"1",10,10, new HashSet<String>(), new HashSet<String>());
        assertEquals(beer.name, name);
        assertEquals(beer.imageID, imageid);
        assertEquals(beer.upvotes,  upvotes);
        assertEquals(beer.downvotes, downvotes);
    }

}
