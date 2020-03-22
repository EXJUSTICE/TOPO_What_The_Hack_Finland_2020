package com.xu.liferpg;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xu.liferpg.data.QuestListDBHelper;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;

//DetailActivity's job is to provide an ExpandableListView for that quest.
//Depending on the item highlighted, we can press Attempt to cause a different thing.
//When we launch into DetailActivity, we should attach a boolean to indicate arrival
//http://cdn.journaldev.com/wp-content/uploads/2015/12/android-expandablelistview-example.gif
//TODO (1) questDetails needs some way of identifiying how to solve objective. Maybe first character separated by ? or *
//TODO (2) this would happen at the last separation, requiring a third separation method, since each subobjective has type (text, image, loc)
//TODO need to add in objectives via Sqlite. Column does not exist. This means amending methods too to return this new column

//TODO 11082017 LEGACY CODE DUE TO LAG, MOVING ALL CODE TO DETAILFRAGMENT. NO LONGER IN USE
public class DetailActivity extends AppCompatActivity {
    //DEBUG test variables START
    ArrayList<String> testQuest;
    HashMap<String, ArrayList<String>>allobjs;
    ArrayList<String>obj1;
    ArrayList<String>obj2;
    //DEBUG test variables END
    //DB related initialization
    public SQLiteDatabase db;
    public QuestListDBHelper helper;

    String inputQuest;

    RelativeLayout layout1;
    RelativeLayout layout2;
    RelativeLayout layout3;
    TextView questHeader;
    TextView typeView1;
    TextView typeView2;
    TextView typeView3;
    TextView objView1;
    TextView objView2;
    TextView objView3;
    int objtype=0;


    //Variables for getIntent, questID used to fetch quest from SQL
    //arrived used for enabling buttons
    String questID;
    Boolean arrived;
    String questNom;
    String questDet;
    Button Attempt; //TODO need to link this to clicklistener.
    ArrayList<String>GroupTitlesForListView;
    HashMap<String,ArrayList<String>>ItemTitlesForListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.helper = new QuestListDBHelper(this);
        this.db = this.helper.getWritableDatabase();
        Attempt =(Button)findViewById(R.id.attempt);

        //TODO DEBUG/TEST code:  quest objective storage system in one string
        //TODO first, separate by /, then -
        testQuest= new ArrayList<String>();
        allobjs = new HashMap<>();
        obj1 = new ArrayList<String>();
        obj2 = new ArrayList<String>();

        //TODO fetch the quest details here
        Intent intent= getIntent();
        questID= intent.getStringExtra("questID");
        arrived= intent.getBooleanExtra("arrived",false);
        FetchQuestData(questID);
        CheckArrivalStatus(arrived);

        layout1= (RelativeLayout)findViewById(R.id.layout1);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.highlight));
                objtype=0;
            }
        });
        layout2= (RelativeLayout)findViewById(R.id.layout2);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.highlight));
                objtype=2;
            }
        });
        layout3 = (RelativeLayout)findViewById(R.id.layout3);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.highlight));
                objtype=3;
            }
        });

        typeView1= (TextView)findViewById(R.id.typeView1);
        typeView2=(TextView)findViewById(R.id.typeView2);
        typeView3=(TextView)findViewById(R.id.typeView3);
        objView1 =(TextView)findViewById(R.id.objView1);
        objView2 =(TextView)findViewById(R.id.objView2);
        objView3 =(TextView)findViewById(R.id.objView3);


        //TESTCODE BEGIN
        inputQuest= "Navigation subquest-Go to this place-Just go here ffs/Photo subquest-Take a photo of the red carp-Check it via image recognition";

        testQuest = separateTitleFromObjs(inputQuest,"/");
        allobjs = separateSubQuests(testQuest,"-");
        //TESTCODE END

        //TODO initialization code for ExpandableListView

        questHeader = (TextView)findViewById(R.id.detailQuestName);





        Attempt =(Button)findViewById(R.id.attempt);
        Attempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check objtype integer, and do what you need to do
                //if successful, set checkbox, or backgroundcolor green

            }
        });
        //insert clicklistner




    }
        //TODO separates the Quest and Objective quests out from the actual ----- QUESTNAME IS ALREADY THERE?!?!
    public ArrayList<String>separateTitleFromObjs(String input, String character){
        ArrayList<String>results= new ArrayList<String>();
            String[] splits = input.split(character);
            for (String s:splits){
                results.add(s);
            }

        return results;
    }

    public HashMap<String,ArrayList<String>> separateSubQuests(ArrayList<String> inputs, String character){
        HashMap<String, ArrayList<String>>results = new HashMap<>();;
        String tempholder;
        ArrayList<String>resultsholder = new ArrayList();

        for (int i = 0;i<inputs.size();i++){
            //get the dash split objective-subobjective combo
            tempholder = inputs.get(i);
            //split them by character
            String[] splits = tempholder.split(character);
            //for all, add to the resultsholder
            for (String s:splits){
                resultsholder.add(s);
            }
            //place into hashmap, and null everything
            results.put("obj"+i,resultsholder);
            //reset temporary holders
            resultsholder.clear();
            tempholder=null;

        }
        return results;
    }
    //TOdo SQL method to fetch the quest's objectives, should be a single entry, NEEDS TEST
    //https://stackoverflow.com/questions/12473194/get-a-single-row-from-table
    //https://stackoverflow.com/questions/1243199/how-to-perform-an-sqlite-query-within-an-android-application
    public void FetchQuestData(String questID){

        String table ="questlist";
        String[] columns= {"questID","questName","objectives"};
        String selection = "questID =?";
        String[] selectionArgs = {questID};
        String groupBy=null;
        String having = null;
        String orderBy = null;



        String  questName= "";
        String questDetails ="";

        Cursor cursor = db.query("questlist", columns, selection, selectionArgs,null,null,null,null);

        if(cursor!= null){
            cursor.moveToFirst();
            questNom= cursor.getString(1);
            questDet= cursor.getString(2);

        }

    }

    //TODO Final step in separation, used to check for type of quest
    public int checkObjectiveType (String entry){
        int result=0;
        if (entry.contains("*NAV*")){
            result = 0;
        }else if(entry.contains("*PHT*")){
            result =1;
        }else if(entry.contains("*TXT*")){
            result =2;
        }

        return result;

    }
    //TODO Populate ExpandableListView after FetchQuestData
    public void populateListView(String QuestName, String QuestDetails){
        questHeader.setText(QuestName);
        ArrayList<String>Objectives = separateTitleFromObjs(QuestDetails,"/");
        HashMap<String, ArrayList<String>> subObjectives= separateSubQuests(Objectives, "-");
        for (int i =0;i<subObjectives.size();i++){
            ArrayList<String>temp = subObjectives.get("obj"+i);

            //TODO i) cannot yet populate, need to look at how the adapter handles group titles and items and modify adapter
            //TODO ii) To take ONLY ONE HASHMAP
            //TODO iii) DONE! Check method below, setUpListViewInputs. Need to link it to adapter and test.
            String checkContains = temp.get(0);
            //TODO return the int using checkObjectiveType, then do something with it, not sure what or how
            //TODO maybe the above isnt necessary: simply start the group header with NAVIGATION, PHOTO, or QUIZ?

        }

    }

    //TODO Due to the way the Adapter is set up, we need to extract the titles out as a separate ArrayList,
    // TODO While deleting the original entry from that list
    //TODO remove the Type Checker and simply use the Header Navigation, Photo, Quiz etc.

    public void setUpListViewInputs(HashMap<String, ArrayList<String>>items){
        ArrayList <String>titles= new ArrayList<String>();
        HashMap<String,ArrayList<String>> convertedHash = new HashMap<String,ArrayList<String>>();

        //Fetch the first entry from both  and add to results
        String title1 = items.get("obj0").get(0);
        String title2 = items.get("obj1").get(0);
        titles.add(title1);
        titles.add(title2);

        //Add leftover items into HashMap
        convertedHash.put(title1,items.get("obj0"));
        convertedHash.put(title2,items.get("obj1"));

        //Copy to variables for use in ExpandedlistView Adapter
        GroupTitlesForListView = titles;
        ItemTitlesForListView=  convertedHash;

    }


    public void CheckArrivalStatus(boolean yesno){
        if (yesno ==false){
            //TODO disable button here
            Attempt.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();
        Intent goback = new Intent(this, MapActivity.class);
        startActivity(goback);
        finish();
    }


}
