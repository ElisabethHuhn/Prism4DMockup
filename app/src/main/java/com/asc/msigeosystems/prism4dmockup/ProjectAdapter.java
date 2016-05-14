package com.asc.msigeosystems.prism4dmockup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by elisabethhuhn on 5/8/2016.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {

    private List<Prism4DMockupProject> mProjectList;

    //implement the ViewHolder as an inner class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView projectName, projectID, projectLastModified, projectSize;

        public MyViewHolder(View v) {
            super(v);

            projectName         = (TextView) v.findViewById(R.id.projectRowNameLabel);
            projectID           = (TextView) v.findViewById(R.id.projectRowIDInput);
            projectLastModified = (TextView) v.findViewById(R.id.projectRowLastModified);
            projectSize         = (TextView) v.findViewById(R.id.projectRowSize);
        }

    } //end inner class MyViewHolder

    //Constructor for ProjectAdapter
    public ProjectAdapter(List<Prism4DMockupProject> projectList){
        this.mProjectList = projectList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_project_list_row, parent,  false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        if (mProjectList != null ) {
            Prism4DMockupProject project = mProjectList.get(position);

            holder.projectName.setText(project.getProjectName());
            holder.projectID.setText(String.valueOf(project.getProjectID()));
            holder.projectLastModified.setText(project.getProjectLastModified().toString());
            //todo size of project yet to be determined
            holder.projectSize.setText("TBD");
        } else {
            holder.projectName.setText("No projects defined");
            holder.projectID.setText("");
            holder.projectLastModified.setText("");
            //todo size of project yet to be determined
            holder.projectSize.setText("TBD");
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
