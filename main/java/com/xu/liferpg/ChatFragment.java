package com.xu.liferpg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ChatFragment mockup, no listeners needed, set visibility as required
 */
public class ChatFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView chattitle;
    TextView chat1;
    TextView chat2;
    TextView chat3;
    TextView chat4;
    ImageView chatimg1;
    ImageView chatimg2;
    ImageView chatimg3;
    ImageView chatimg4;
    EditText chatEditText;
    Button sendChat;
    //Counter for responses
    int counter=0;



    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //Initialization
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_chat, container, false);
       chattitle =(TextView)view.findViewById(R.id.chatTitleView);
       chattitle.setText(mParam1);
         chat1=(TextView)view.findViewById(R.id.friendView1);
         chat2= (TextView)view.findViewById(R.id.chatTextView2);
         chat3= (TextView)view.findViewById(R.id.chatTextView3);
         chat4= (TextView)view.findViewById(R.id.friendView4);
         chatimg1=(ImageView)view.findViewById(R.id.chatImgView);
         chatimg2=(ImageView)view.findViewById(R.id.chatImgView2);
         chatimg3=(ImageView)view.findViewById(R.id.chatImgView3);
         chatimg4= (ImageView)view.findViewById(R.id.chatImgView4);
         chatEditText =(EditText)view.findViewById(R.id.chatEditText);
         sendChat = (Button)view.findViewById(R.id.btnChatSend);


         //Set initial visibility
        chat1.setVisibility(View.INVISIBLE);
        chat2.setVisibility(View.INVISIBLE);
        chat3.setVisibility(View.INVISIBLE);
        chat4.setVisibility(View.INVISIBLE);
        chatimg1.setVisibility(View.INVISIBLE);
        chatimg2.setVisibility(View.INVISIBLE);
        chatimg3.setVisibility(View.INVISIBLE);
        chatimg4.setVisibility(View.INVISIBLE);


        //Handing fake chat
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter==0){
                    chat1.setVisibility(View.VISIBLE);
                    chatimg1.setVisibility(View.VISIBLE);
                    chat1.setText(chatEditText.getEditableText());
                    chat2.setVisibility(View.VISIBLE);
                    chatimg2.setVisibility(View.VISIBLE);
                    chat2.setText("...");


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chat2.setText("What's up!");
                            chatEditText.clearComposingText();
                        }
                    }, 2000);
                }



                }

        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

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

}
