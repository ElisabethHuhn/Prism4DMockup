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
 *  - The data store of Points
 *  - Lists of Point on the UI
 *
 *  Adapters follow a pattern. This adapter follows the pattern
 */
public class Prism4DPointAdapter extends RecyclerView.Adapter<Prism4DPointAdapter.MyViewHolder> {
    private static String no_coordinate = "No Location";

    private List<Prism4DPoint> mPointList;

    //implement the ViewHolder as an inner class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView projectID,    pointID,
                        pointEasting, pointNorthing, pointElevation, pointFeatureCode;

        public MyViewHolder(View v) {
            super(v);

            projectID        = (TextView) v.findViewById(R.id.pointRowProjectID);
            pointID          = (TextView) v.findViewById(R.id.pointRowPointID);
            pointEasting     = (TextView) v.findViewById(R.id.pointRowEasting);
            pointNorthing    = (TextView) v.findViewById(R.id.pointRowNorthing) ;
            pointElevation   = (TextView) v.findViewById(R.id.pointRowElevation);
            pointFeatureCode = (TextView) v.findViewById(R.id.pointRowFeatureCode);
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
            holder.pointFeatureCode.setText(point.getPointFeatureCode().toString());

            Prism4DCoordinate coordinate = point.getCoordinate();
            if (coordinate == null){
                holder.pointEasting.setText(no_coordinate);
                holder.pointNorthing.setText(no_coordinate);
                holder.pointElevation.setText(no_coordinate);
            }else {
                CharSequence coordinateType = coordinate.getCoordinateType();
                if ((coordinateType.equals(Prism4DCoordinate.sCoordinateTypeWGS84)) ||
                    (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeNAD83))){
                    holder.pointEasting.setText(String.valueOf(((Prism4DCoordinateLL)coordinate).getLatitude()));
                    holder.pointNorthing.setText(String.valueOf(((Prism4DCoordinateLL)coordinate).getLongitude()));
                    holder.pointElevation.setText(String.valueOf(((Prism4DCoordinateLL)coordinate).getElevation()));
                }else {
                    holder.pointEasting.setText(String.valueOf(((Prism4DCoordinateEN)coordinate).getEasting()));
                    holder.pointNorthing.setText(String.valueOf(((Prism4DCoordinateEN)coordinate).getNorthing()));
                    holder.pointElevation.setText(String.valueOf(((Prism4DCoordinateEN)coordinate).getElevation()));
                }
            }

        } else {

            holder.projectID       .setText(R.string.no_points);
            holder.pointID         .setText("");
            holder.pointFeatureCode.setText("");
            holder.pointEasting.setText(no_coordinate);
            holder.pointNorthing.setText(no_coordinate);
            holder.pointElevation.setText(no_coordinate);

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
