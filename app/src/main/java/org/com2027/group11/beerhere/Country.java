package org.com2027.group11.beerhere;

/**
 * Created by Adrien on 05/03/2018.
 */

public class Country {

    String name;
    String continent;

    public Country(String name, String continent){
        this.name = name;
        this.continent = continent;
    }

    public String getName(){

        return this.name;
    }


    public String getContinent() {
        return continent;
    }


}
