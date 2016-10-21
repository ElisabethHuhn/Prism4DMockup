package com.asc.msigeosystems.prism4d;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asc.msigeosystems.prism4dmockup.R;

import java.util.List;

/**
 * Created by elisabethhuhn on 5/15/2016.
 * patterned after Prism4DProjectAdapter
 */
public class Prism4DPointAdapter extends RecyclerView.Adapter<Prism4DPointAdapter.MyViewHolder> {

    private List<Prism4DPoint> mPointList;

    //implement the ViewHolder as an inner class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView projectID, pointID, pointEasting, pointNorthing, pointElevation, pointDescription;

        public MyViewHolder(View v) {
            super(v);

            projectID        = (TextView) v.findViewById(R.id.pointRowProjectID);
            pointID          = (TextView) v.findViewById(R.id.pointRowPointID);
            pointEasting     = (TextView) v.findViewById(R.id.pointRowEasting);
            pointNorthing    = (TextView) v.findViewById(R.id.pointRowNorthing);
            pointElevation   = (TextView) v.findViewById(R.id.pointRowElevation);
            pointDescription = (TextView) v.findViewById(R.id.pointRowDescription);
        }

    } //end inner class MyViewHolder

    //Constructor for Prism4DPointAdapter
    public Prism4DPointAdapter(List<Prism4DPoint> pointList){
        this.mPointList = pointList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_point_list_row, parent,  false);
        return new MyViewHolder(itemView);

    }

    public void removeItem(int position) {
        Prism4DPoint point = mPointList.get(position);

        mPointList.remove(position);
        notifyItemRemoved(position);

        //but we also need to remove it from the list of all points in all projects
        Prism4DPointsManager pointsManager = Prism4DPointsManager.getInstance();
        pointsManager.remove(point);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        if (mPointList != null ) {
            Prism4DPoint point = mPointList.get(position);

            holder.projectID       .setText(String.valueOf(point.getProjectID()));
            holder.pointID         .setText(String.valueOf(point.getPointID()));
            holder.pointEasting    .setText(Double.toString(point.getPointEasting()));
            holder.pointNorthing   .setText(Double.toString(point.getPointNorthing()));
            holder.pointElevation  .setText(Double.toString(point.getPointElevation()));
            holder.pointDescription.setText(point.getPointDescription().toString());

        } else {

            holder.projectID       .setText("No Points Defined");
            holder.pointID         .setText("");
            holder.pointEasting    .setText("");
            holder.pointNorthing   .setText("");
            holder.pointElevation  .setText("");
            holder.pointDescription.setText("");
        }

    }

    @Override
    public int getItemCount(){

        int returnValue = 0;

        if (mPointList != null) {
            returnValue = mPointList.size();
        }
        return returnValue;
    }


}
