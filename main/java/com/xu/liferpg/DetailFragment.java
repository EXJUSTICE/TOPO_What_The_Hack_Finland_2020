package com.xu.liferpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 * For passing arrayLists
 * https://stackoverflow.com/questions/33748732/pass-arraylist-from-activity-to-customdialog
 *
 *Depends on Clicked type of exercise, we should pass result back to activity thorough listener, and start something
 *https://stackoverflow.com/questions/23122557/create-a-new-dialogfragment-from-another-dialogfragment-instantiated-in-another
 */
public class DetailFragment extends DialogFragment{
    // TODO 2408 added QRcode


    private String questName;
    private ArrayList<String> questObjectives;
    private String questID;
    Bundle incoming;

    private OnFragmentInteractionListener mListener;

    RelativeLayout layout1;
    RelativeLayout layout2;
    RelativeLayout layout3;
    RelativeLayout layout4;
    TextView questHeader;
    TextView typeView1;
    TextView typeView2;
    TextView typeView3;
    TextView typeView4;
    TextView objView1;
    TextView objView2;
    TextView objView3;
    TextView objView4;
    CheckBox cBox1;
    CheckBox cBox2;
    CheckBox cBox3;
    CheckBox cBox4;
    boolean obj1Completion;
    boolean obj2Completion;
    boolean obj3Completion;
    boolean obj4Completion;

    int objType;
    Button attempt;
    SharedPreferences questData;
    SharedPreferences.Editor questEditor;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String name, ArrayList<String>objectives, String QuestID) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("questID",QuestID);
        args.putSerializable("objectives", objectives);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fetch args passed from mapactivity
        if (getArguments() != null) {
            incoming=getArguments();
            questName=incoming.getString("name","INVALID NAME");
            questObjectives=(ArrayList<String>)incoming.getSerializable("objectives");
            questID= incoming.getString("questID");

            //Load the data into views- done in onCreateView




        }

        questData=getActivity().getSharedPreferences("questData",Context.MODE_PRIVATE);
        questEditor=questData.edit();
    }

    public void loadDataIntoViews(){
        questHeader.setText(questName);
        typeView1.setText("Objective 1");
        typeView2.setText("Objective 2");
        typeView3.setText("Objective 3");
        typeView4.setText("QR Code");
        objView1.setText(questObjectives.get(0).toString());
        objView2.setText(questObjectives.get(1).toString());
        objView3.setText(questObjectives.get(2).toString());
        //objView4.setText(questObjectives.get(3).toString());

    }

    public void checkCompletion(){
       obj1Completion= questData.getBoolean("questID"+"obj1",false);
        obj2Completion=questData.getBoolean("questID"+"obj2",false);
        obj3Completion=questData.getBoolean("questID"+"obj3",false);
        //obj4Completion =questData.getBoolean("questID"+"obj4",false);

        cBox1.setChecked(obj1Completion);
        cBox2.setChecked(obj2Completion);
        cBox3.setChecked(obj3Completion);
        cBox4.setChecked(obj4Completion);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.content_detail, container, false);

        layout1 = (RelativeLayout)view.findViewById(R.id.layout1);
        layout2 = (RelativeLayout)view.findViewById(R.id.layout2);
        layout3 = (RelativeLayout)view.findViewById(R.id.layout3);
        layout4 = (RelativeLayout)view.findViewById(R.id.layout4);
        layout4.setVisibility(View.INVISIBLE);
        questHeader = (TextView)view.findViewById(R.id.detailQuestName);
        typeView1 = (TextView)view.findViewById(R.id.typeView1);
        typeView2 = (TextView)view.findViewById(R.id.typeView2);
        typeView3 = (TextView)view.findViewById(R.id.typeView3);
        typeView4 = (TextView)view.findViewById(R.id.typeView4);
        typeView4.setVisibility(View.INVISIBLE);
        objView1= (TextView)view.findViewById(R.id.objView1);
        objView2= (TextView)view.findViewById(R.id.objView2);
        objView3= (TextView)view.findViewById(R.id.objView3);
        objView4= (TextView)view.findViewById(R.id.objView4);
        objView4.setVisibility(View.INVISIBLE);
        cBox1 = (CheckBox)view.findViewById(R.id.cBox1);
        cBox2 =(CheckBox)view.findViewById(R.id.cBox2);
        cBox3= (CheckBox)view.findViewById(R.id.cBox3);
        cBox4 = (CheckBox)view.findViewById(R.id.cBox4);
        cBox4.setVisibility(View.INVISIBLE);
        //TODO all boxes should be set disabled. Does this affect whats going on

        cBox1.setEnabled(false);
        cBox2.setEnabled(false);
        cBox3.setEnabled(false);
        cBox4.setEnabled(false);
        checkCompletion();
        loadDataIntoViews();

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1.setBackgroundColor(getResources().getColor(R.color.highlight));
                layout2.setBackgroundColor(getResources().getColor(R.color.white));
                layout3.setBackgroundColor(getResources().getColor(R.color.white));
                layout4.setBackgroundColor(getResources().getColor(R.color.white));
                objType=0;


            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout2.setBackgroundColor(getResources().getColor(R.color.highlight));
                layout1.setBackgroundColor(getResources().getColor(R.color.white));
                layout3.setBackgroundColor(getResources().getColor(R.color.white));
                layout4.setBackgroundColor(getResources().getColor(R.color.white));
                objType=1;

            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout3.setBackgroundColor(getResources().getColor(R.color.highlight));
                layout2.setBackgroundColor(getResources().getColor(R.color.white));
                layout1.setBackgroundColor(getResources().getColor(R.color.white));
                layout4.setBackgroundColor(getResources().getColor(R.color.white));
                objType=2;

            }
        });
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1.setBackgroundColor(getResources().getColor(R.color.white));
                layout2.setBackgroundColor(getResources().getColor(R.color.white));
                layout3.setBackgroundColor(getResources().getColor(R.color.white));
                layout4.setBackgroundColor(getResources().getColor(R.color.highlight));
            }
        });

        attempt =(Button)view.findViewById(R.id.attempt);
        attempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass objtype to parent activity
                //TODO need to check completion status when clicking ATTEMPT
                mListener.onFragmentInteraction(objType);
                //mListener.onFragmentInteraction(objType);
                dismiss();
        }});




        return view;
    }

    /*TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO method includes both type of objective being attempted, and whether
        //TODO 1908 Temporarily modifying fragmentInteraction to set all to True while debugging, shoult include int type in finla version

        void onFragmentInteraction(int type);
        //void onFragmentInteraction(int type);
    }
}
