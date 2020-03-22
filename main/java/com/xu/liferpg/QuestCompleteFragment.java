package com.xu.liferpg;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 .
 */
public class QuestCompleteFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Need to learn how to adjust progressBar
    private String questID;
    private String questName;
    private int currentLevel;
    private int currentEXP;
    private int totalEXP;
    //quest EXP by default is 250, shows levelling
    private int questEXP;


    private TextView congratulationDescription;
    private TextView startEXPView;
    private TextView endEXPView;
    private ProgressBar progressBar;

    private OnQuestCompleteListener mListener;
    SharedPreferences playerData;
    SharedPreferences.Editor dataEditor;

    public QuestCompleteFragment() {
        // Required empty public constructor
    }

    /**
     *
     */
    // TODO: Rename and change types and number of parameters
    public static QuestCompleteFragment newInstance(String questID, String questNom, int totalEXP, int questEXP) {
        QuestCompleteFragment fragment = new QuestCompleteFragment();
        Bundle args = new Bundle();
        args.putString("questID",questID);
        args.putString("questName",questNom);
        args.putInt("questEXP",questEXP);


        args.putInt("totalEXP",totalEXP);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args= getArguments();
            questID= args.getString("questID");
            questName= args.getString("questName");

            questEXP = args.getInt("questEXP");
            totalEXP = args.getInt("totalEXP");
        }

        playerData = getActivity().getSharedPreferences("playerData", Context.MODE_PRIVATE);
        dataEditor= playerData.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_quest_complete, container, false);
        congratulationDescription= (TextView)view.findViewById(R.id.congratulationDescriptionView);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        startEXPView= (TextView)view.findViewById(R.id.startEXPView);
        endEXPView =(TextView)view.findViewById(R.id.endEXPView);
        //Should divide by 1000 and find remainder, thats the EXP we want
        checkLevelandSetProgress();
        endEXPView.setText("Lvl:"+currentLevel);
        startEXPView.setText("Lvl:"+currentLevel +1);





        return view;
    }



    public void checkLevelandSetProgress(){
        //Mmodulus to calculate remainder EXP to go before levelup
        currentEXP = totalEXP%1000;
        //Get the number of full levels
        currentLevel = (totalEXP-currentEXP)/1000;
        congratulationDescription.setText("Quest "+questName+ "completed.\n\n Level:"+currentLevel);
        progressBar.setProgress(currentEXP);
        setProgressAnimate(progressBar,currentEXP+questEXP);
        //Handling level up
        if(currentEXP+questEXP>=1000){
            Toast.makeText(getActivity(),"You levelled up!",Toast.LENGTH_LONG).show();
            currentLevel = currentLevel+1;
            totalEXP = totalEXP+currentEXP+questEXP;
        }
        dataEditor.putInt("playerLevel",currentLevel);
        dataEditor.putInt("totalEXP",totalEXP);
        dataEditor.commit();
        mListener.onQuestCompleteInteraction(questID);

    }
    //Animation method- TEST THIS
    //https://stackoverflow.com/questions/6097795/make-a-progressbar-update-smoothly

    private void setProgressAnimate(ProgressBar pb, int progressTo)
    {
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), progressTo);
        animation.setDuration(1000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuestCompleteListener) {
            mListener = (OnQuestCompleteListener) context;
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
    public interface OnQuestCompleteListener {
        // TODO: Update argument type and name
        void onQuestCompleteInteraction(String questID);
        //We need to go through the markers and modify their colors
    }
}
