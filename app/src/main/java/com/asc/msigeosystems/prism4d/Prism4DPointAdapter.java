package com.asc.msigeosystems.prism4d;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asc.msigeosystems.prism4dmockup.R;

import java.util.List;

/**
 * Created by Elisabeth Huhn on 5/15/2016.
 * patterned after Prism4DProjectAdapter
 * The Adapter manages the connection between
 *  - The data store of Projects (either in memory or on DB)
 *  - Lists of projects on the UI
 *
 *  Adapters follow a pattern which is defined in the design documents
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

    public void removeItem(int position, int projectID) {
        Prism4DPoint point = mPointList.get(position);

        //Can't maintain the project's point list directly
        //Have to ask the pointManager to do it
        Prism4DPointManager pointsManager = Prism4DPointManager.getInstance();
        pointsManager.removePoint(projectID, point);

        notifyItemRemoved(position); //update the UI


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        if (mPointList != null ) {
            Prism4DPoint point = mPointList.get(position);

            holder.projectID       .setText(String.valueOf(point.getForProjectID()));
            holder.pointID         .setText(String.valueOf(point.getPointID()));

            holder.pointDescription.setText(point.getPointDescription().toString());

        } else {

            holder.projectID       .setText(R.string.no_points);
            holder.pointID         .setText("");

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
