package org.com2027.group11.beerhere;

import org.junit.Test;
import org.com2027.group11.beerhere.beer.Beer;

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
        Beer beer = new Beer(name);
        assertEquals(beer.beerName, name);
        assertEquals(beer.beerUpvotes,  0);
        assertEquals(beer.beerDownvotes, 0);
    }

}
