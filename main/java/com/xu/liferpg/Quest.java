package com.xu.liferpg;

import com.google.android.gms.maps.model.LatLng;

/**
 * Quest class is designed to hold the details of each object on the map
 * Storing of locations
 * http://wptrafficanalyzer.in/blog/storing-and-retrieving-locations-in-sqlite-from-google-maps-android-api-v2/
 */

public class Quest {
    String ID;
    //ID shared with QuestListContract
    String name;
    //Name of Quest
    String description;
    //local area
    String region;
    //TODO keys (keywords) used as answers to questions. Consult documentation for more, but questions should be 2nd and 3rd subobjectives
    String key1;
    String key2;
    String key3;
    //Description of Quest
    /*Following are all used to represent stat rewards, disabled for now
    int Intelligence;
    int Agility;
    int Charisma;
    int Endurance;
    */

    Double lat;
    Double lng;
    //Hyphen and special character separated text for objectives, including identification
    String objectives;
    LatLng point;
    boolean completed;
    int minLevel;
    String questType;

    public Quest(String newID,  String newName, String newDescription, String newRegion,
                 Double newlat, Double newlng,String objet, String newkey1, String newkey2, String newkey3, boolean complete, int minLevel, String type){
        this.ID = newID;
        this.name=newName;
        this.description=newDescription;
        this.region =newRegion;
        this.lat=newlat;
        this.lng=newlng;
        this.objectives=objet;
        this.point= new LatLng(lat,lng);
        this.key1=newkey1;
        this.key2=newkey2;
        this.key3= newkey3;
        //completition is false by default
        this.completed=complete;
        this.minLevel = minLevel;
        this.questType = type;
    }

}
