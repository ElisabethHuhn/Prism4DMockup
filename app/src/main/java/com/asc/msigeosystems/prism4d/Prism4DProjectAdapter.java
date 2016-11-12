package com.asc.msigeosystems.prism4d;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asc.msigeosystems.prism4dmockup.R;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by Elisabeth Huhn on 5/8/2016.
 *
 * An adapter sits between the Data Model and a list on the UI
 * However, our Data Model classes all have managers: so...
 * The Adapter uses the Project Manager to actually touch a project instance
 */
public class Prism4DProjectAdapter extends RecyclerView.Adapter<Prism4DProjectAdapter.MyViewHolder> {

    //Remember, only the ProjectManager may actually CRUD a member of this list
    private List<Prism4DProject> mProjectList;

    //implement the ViewHolder as an inner class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView projectName, projectID, projectLastModified, projectSize;

        public MyViewHolder(View v) {
            super(v);

            projectName         = (TextView) v.findViewById(R.id.projectRowName);
            projectID           = (TextView) v.findViewById(R.id.projectRowID);
            projectLastModified = (TextView) v.findViewById(R.id.projectRowLastModified);
            projectSize         = (TextView) v.findViewById(R.id.projectRowSize);
        }

    } //end inner class MyViewHolder

    //Constructor for Prism4DProjectAdapter
    public Prism4DProjectAdapter(List<Prism4DProject> projectList){
        this.mProjectList = projectList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_project_list_row, parent,  false);
        return new MyViewHolder(itemView);

    }

    public void removeItem(int position) {
        //This list is used locally as well as in the project container,
        // so we only have to remove it once
        //TODO Hide this in the projectManager, ie call the projectManager to delete the project from the list
        mProjectList.remove(position);
        notifyItemRemoved(position);
        //this line below gives you the animation and also updates the
        //list items after the deleted item
        notifyItemRangeChanged(position, getItemCount());


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        if (mProjectList != null ) {
            //Assume the size of the project is the number of points in the project
            Prism4DProject project = mProjectList.get(position);

            Prism4DPointManager pointsManager = Prism4DPointManager.getInstance();
            int numberPoints = pointsManager.getSize(project.getProjectID());

            holder.projectName.        setText(project.getProjectName());
            holder.projectID.          setText(String.valueOf(project.getProjectID()));
            holder.projectLastModified.setText(
                    DateFormat.getDateInstance().format(project.getProjectLastModified()));
            //number of points in this project
            holder.projectSize.        setText(String.valueOf(numberPoints));

        } else {
            holder.projectName.setText(R.string.no_projects);
            holder.projectID.setText("");
            holder.projectLastModified.setText("");

            holder.projectSize.setText("0");
        }

    }

    @Override
    public int getItemCount(){

        int returnValue = 0;

        if (mProjectList != null) {
            returnValue = mProjectList.size();
        }
        return returnValue;
    }
}
