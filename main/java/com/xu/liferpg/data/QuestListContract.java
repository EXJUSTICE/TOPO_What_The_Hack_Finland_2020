package com.xu.liferpg.data;

/**
 * Schema  file for all quests in the system, stored locally in SQLite
 * TODO need to add in objectives
 */

public class QuestListContract {

    public class QuestListEntry{
        public static final String COLUMN_ID ="questid";
        //Universal ID for each quest, should serve as sort key of primary key?
        public static final String COLUMN_REGION="region";
        //Region allows us to divide quests on to the local city etc, should serve as partition key of primary?
        public static final String COLUMN_NAME="questname";
        //Actual quest name
        public static final String COLUMN_DETAILS="details";
        //All the details in a quest. Separated by dashes and hyphens
        public static final String COLUMN_LATITUDE= "latitude";
        public static final String COLUMN_LONGITUDE= "longitude";
        //String-converted coordinates
        public static final String COLUMN_OBJECTIVES = "objectives";
        //objectives is used in DetailActivity, its a combination of questname, type, and objective/subobjectives separated by hyphen and comma (??)

        //Key1 is imageanalysis
        public static final String COLUMN_KEYWORD1 ="key1";
        //key2 is questionans
        public static final String COLUMN_KEYWORD2="key2";
        //question3 is qrcode
        public static final String COLUMN_KEYWORD3 ="key3";
        public static final String COLUMN_COMPLETED="completed";
        //Keywords for checking user presen
        public static final String COLUMN_LEVEL = "questlevel";
        //QuestLevel is used to judge unlocks
}
}
