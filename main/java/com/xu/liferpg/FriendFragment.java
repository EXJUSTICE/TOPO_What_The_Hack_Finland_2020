package com.xu.liferpg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentManager fm;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView friend1;
    TextView friend2;
    TextView friend3;
    TextView friend4;

    private OnFriendChosenListener mListener;

    public FriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_friends, container, false);

        friend1= (TextView)view.findViewById(R.id.friendView1);
        friend2=(TextView)view.findViewById(R.id.friendView2);
        friend3= (TextView)view.findViewById(R.id.friendView3);
        friend4= (TextView)view.findViewById(R.id.friendView4);

        friend1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onFriendChosen(friend1.getText().toString());
                }
                launchChatFragmentFromFriendFragment(friend1.getText().toString());
                dismiss();
            }
        });

        friend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onFriendChosen(friend2.getText().toString());
                }
                launchChatFragmentFromFriendFragment(friend2.getText().toString());
                dismiss();
            }
        });
        friend3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onFriendChosen(friend3.getText().toString());
                }
                launchChatFragmentFromFriendFragment(friend3.getText().toString());
                dismiss();
            }
        });
        friend4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onFriendChosen(friend4.getText().toString());
                }
                launchChatFragmentFromFriendFragment(friend4.getText().toString());
                dismiss();
            }
        });

        return view;
    }

    public void launchChatFragmentFromFriendFragment(String nom){
        fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();

        ChatFragment chatfrag = ChatFragment.newInstance(nom);
        chatfrag.show(fm, "ChatFragment");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
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
   public interface OnFriendChosenListener {
        // TODO: Update argument type and name
        void onFriendChosen(String friend);

    }

}
