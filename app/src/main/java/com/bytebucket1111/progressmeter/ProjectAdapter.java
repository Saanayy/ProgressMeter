package com.bytebucket1111.progressmeter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bytebucket1111.progressmeter.activities.ManageProject;
import com.bytebucket1111.progressmeter.modal.Project;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<MyViewHolder> {

    ArrayList<Project> projects;
    Context context;

    public ProjectAdapter(ArrayList<Project> projects, Context context) {
        this.projects = projects;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_project_layout, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final Project project = projects.get(i);
        myViewHolder.tvTitle.setText(project.getTitle());
        myViewHolder.tvStart.setText(project.getStartDate());
        myViewHolder.tvDuration.setText(project.getDuration());

        myViewHolder.cvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myViewHolder.cvCard.getContext(), ManageProject.class);
                String projectData = new Gson().toJson(project);
                intent.putExtra("projectData", projectData);
                myViewHolder.cvCard.getContext().startActivity(intent);

            }
        });
        setAnimation(myViewHolder.itemView, i);

    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    void setAnimation(View view, int position) {

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        view.startAnimation(animation);
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView tvTitle, tvStart, tvDuration;
    CardView cvCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.item_project_layout_projectname);
        tvDuration = itemView.findViewById(R.id.item_project_layout_duration);
        tvStart = itemView.findViewById(R.id.item_project_layout_startdate);
        cvCard = itemView.findViewById(R.id.item_project_layout_card);
    }
}