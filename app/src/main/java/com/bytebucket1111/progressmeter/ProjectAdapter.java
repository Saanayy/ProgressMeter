package com.bytebucket1111.progressmeter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytebucket1111.progressmeter.modal.Project;



import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<MyViewHolder>{

    ArrayList<Project> projects;
    Context context;

    public ProjectAdapter(ArrayList<Project> projects, Context context) {
        this.projects = projects;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_project_layout,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Project project = projects.get(i);
        myViewHolder.tvTitle.setText(project.getTitle());
        myViewHolder.tvStart.setText(project.getStartDate());
        myViewHolder.tvDuration.setText(project.getDuration());

    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView tvTitle, tvStart, tvDuration;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.item_project_layout_projectname);
        tvDuration = itemView.findViewById(R.id.item_project_layout_duration);
        tvStart = itemView.findViewById(R.id.item_project_layout_startdate);
    }
}