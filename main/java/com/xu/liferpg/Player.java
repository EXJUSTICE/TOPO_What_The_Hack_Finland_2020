package com.xu.liferpg;

import java.util.UUID;

/**
 * Player description, need to make constructor
 */

public class Player {
    UUID  userId;
    int Strength;
    int Intelligence;
    int Agility;
    int Charisma;
    int Endurance;
    int QuestsCompleted;
    String CurrentActiveID;
    //C.A.ID shared with QuestListContract
    public Player(int newStr, int newInt, int newAgi, int newChr, int newEnd, int QuestsDone){
        this.userId=UUID.randomUUID();
        this.Strength=newStr;
        this.Intelligence=newInt;
        this.Agility=newAgi;
        this.Charisma=newChr;
        this.Endurance=newEnd;
        this.QuestsCompleted=QuestsDone;
        this.CurrentActiveID="No Quest Active";
    }

}
