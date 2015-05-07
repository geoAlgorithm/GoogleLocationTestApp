package com.geoalgorithm.googlelocationtestapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alan Poggetti on 05/05/2015.
 */
public class MyArrayAdapter extends BaseAdapter {

    private List<String> mLocations;
    private int resId;
    private Context mContext;

    public MyArrayAdapter(Context context, List<String> mLocations, int resId){

        this.mLocations = mLocations;
        this.resId = resId;
        this.mContext = context;

    }

    @Override
    public int getCount() {

        if(mLocations == null)
            return 0;
        else
            return mLocations.size();
    }

    @Override
    public Object getItem(int position) {
        return mLocations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){

            convertView = View.inflate(mContext,resId,null);

            holder = new ViewHolder();

            holder.textView = (TextView)convertView.findViewById(R.id.textView);
            holder.background = (LinearLayout)convertView.findViewById(R.id.list_item_layout);

            convertView.setTag(holder);

        }else{

            holder = (ViewHolder)convertView.getTag();

        }

        if(position % 2 == 0)
            holder.background.setBackgroundResource(R.drawable.gray_selector);
        else{
            holder.background.setBackgroundResource(R.drawable.white_selector);
        }
        holder.textView.setText(mLocations.get(position));

        return convertView;
    }

    private class ViewHolder{
        TextView textView;
        LinearLayout background;
    }
}
