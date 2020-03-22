package com.xu.liferpg;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TODO custom adapter that handles both titles and items in expandable list
 http://www.journaldev.com/9942/android-expandablelistview-example-tutorial
 TODO need to modify our own hashmap method to share identification somehow, or will "obj +i" work?.
 TODO 11082017 removed from use
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> expandableListTitle;
    private HashMap<String,ArrayList<String>> expandableListDetail;

    public CustomExpandableListAdapter(Context context,ArrayList<String> expandableListTit,
                                       HashMap<String,ArrayList<String>>expandableListDet){
        this.context= context;
        this.expandableListTitle=expandableListTit;

        this.expandableListDetail=expandableListDet;
    }
    //Function for fetching subitem texts
    //Works together with the getChildView and getChildGroup
    @Override
    public Object getChild(int listPosition, int expandedListPosition){
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
        //TODO fetch the correct ListTitle first, then find the corresponding ArrayList in Hashmap
        //TODO finally get the subitem from that ArrayList you want
        //TODO we need to apply this to our set up
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition){
        return expandedListPosition;
    }

    //Method that sets the text for the subitems, uses getChild() and list_item xml
    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    //Method that sets the text for main items, uses getChild() and list_group xml

    @Override
    public View getGroupView (int listPosition, boolean isExpanded,
                              View convertView, ViewGroup parent ){
        String listTitle= (String)getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    //Gets the title string
    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }





    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}





