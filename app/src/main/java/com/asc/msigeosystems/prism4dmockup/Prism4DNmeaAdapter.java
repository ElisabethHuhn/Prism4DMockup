package com.asc.msigeosystems.prism4dmockup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by elisabethhuhn on 5/15/2016.
 * patterned after Prism4DProjectAdapter
 */
public class Prism4DNmeaAdapter extends RecyclerView.Adapter<Prism4DNmeaAdapter.MyViewHolder> {

    private List<Prism4DNmea> mNmeaList;

    //implement the ViewHolder as an inner class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nmeaSentence;

        public MyViewHolder(View v) {
            super(v);

            nmeaSentence = (TextView) v.findViewById(R.id.nmeaRowSentence);
        }

    } //end inner class MyViewHolder

    //Constructor for Prism4DNmeaAdapter
    public Prism4DNmeaAdapter(List<Prism4DNmea> nmeaList){
        this.mNmeaList = nmeaList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_nmea_list_row, parent,  false);
        return new MyViewHolder(itemView);

    }

    public void removeItem(int position) {
        mNmeaList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        if (mNmeaList != null ) {
            Prism4DNmea nmea = mNmeaList.get(position);

            holder.nmeaSentence.setText(String.valueOf(nmea.getNmeaSentence()));

        } else {

            holder.nmeaSentence.setText(R.string.skyplot_no_nema_found);
        }

    }
    @Override
    public int getItemCount(){

        int returnValue = 0;

        if (mNmeaList != null) {
            returnValue = mNmeaList.size();
        }
        return returnValue;
    }


}
