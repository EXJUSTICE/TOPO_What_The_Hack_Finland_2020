package com.xu.liferpg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * DialogFragment to handle Type2 objectives/ questiontype objectives
 */
public class QuestionFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private String questID;
    private String key2;
    private ArrayList<String>questionAndAnswers;
    String ans1;
    String ans2;
    String question;
    TextView questionView;
    EditText entry;
    Button checkAnswer;
    boolean success =false;

    private OnType2InteractionListener mListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     *
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String questID, String key2) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString("questID", questID);
        args.putString("key2", key2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            key2= getArguments().getString("key2");
            questID = getArguments().getString("questID");
        }
        //questKey2 will consist of format QUESTION-ANSWER
        questionAndAnswers=separateQuestionFromAnswer(key2,"-");
        question= questionAndAnswers.get(0);
        ans1= questionAndAnswers.get(1);
        ans2 = questionAndAnswers.get(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        questionView= (TextView)view.findViewById(R.id.questionView);
        questionView.setText(question);
        entry= (EditText)view.findViewById(R.id.answerView);
        checkAnswer=(Button)view.findViewById(R.id.questionCheckButton);
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (entry.getText().toString().equals(ans1)||entry.getText().toString().equals(ans2)){
                    success=true;
                    mListener.onType2Interaction(true);
                    dismiss();
                }else{
                    Toast.makeText(getActivity(),"Incorrect! Please try again!", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnType2InteractionListener) {
            mListener = (OnType2InteractionListener) context;
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
    public interface OnType2InteractionListener {

        void onType2Interaction(boolean result);
    }
    //TODO key2 is for question. It will consist of question-ans1-ans2
    public ArrayList<String> separateQuestionFromAnswer(String input, String character){
        ArrayList<String>results= new ArrayList<String>();
        String[] splits = input.split(character);
        for (String s:splits){
            results.add(s);
        }

        return results;
    }
}
