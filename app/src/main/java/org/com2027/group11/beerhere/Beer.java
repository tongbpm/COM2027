package org.com2027.group11.beerhere;

class Beer {

    private String title;
    private int imageId;
    private int rating;

    public Beer(String title, int imageId, int rating){

        this.title = title;
        this.imageId = imageId;
        this.rating = rating;

    }

    public String getTitle(){
        return this.title;
    }

    public int getImageId(){
        return this.imageId;
    }

    public int getRating(){
        return rating;
    }

}