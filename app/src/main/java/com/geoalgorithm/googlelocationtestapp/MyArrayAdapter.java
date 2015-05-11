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
    private int selectedRow;

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

    public void setSelectedRow(int row){
        selectedRow = row;
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

        if(position == selectedRow){

            holder.background.setBackgroundResource(R.drawable.current_row_selector);
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.white));

        } else if(position % 2 == 0) {
            holder.background.setBackgroundResource(R.drawable.gray_selector);
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.black));
        }else{
            holder.background.setBackgroundResource(R.drawable.white_selector);
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        holder.textView.setText(mLocations.get(position));

        return convertView;
    }

    private class ViewHolder{
        TextView textView;
        LinearLayout background;
    }
}
