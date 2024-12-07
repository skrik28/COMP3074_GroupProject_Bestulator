package com.example.comp3074_groupproject_bestulator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import java.util.List;

public class DailyProjectAdapter extends RecyclerView.Adapter<DailyProjectAdapter.ProjectViewHolder> {
    private List<Project> projects;
    private OnProjectClickListener listener;

    public interface OnProjectClickListener {
        void onProjectClick(Project project);
    }

    public DailyProjectAdapter(List<Project> projects, OnProjectClickListener listener) {
        this.projects = projects;
        this.listener = listener;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_list_item, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.projectNameText.setText(project.getProjectName());
        holder.clientNameText.setText(project.getClientName());

        // Set category indicator color
        int categoryColor = ProjectCalendarDecorator.getCategoryColor(project.getCategory());
        holder.categoryIndicator.setBackgroundColor(categoryColor);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProjectClick(project);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public void updateProjects(List<Project> newProjects) {
        this.projects = newProjects;
        notifyDataSetChanged();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView projectNameText;
        TextView clientNameText;
        View categoryIndicator;

        ProjectViewHolder(View itemView) {
            super(itemView);
            projectNameText = itemView.findViewById(R.id.projectNameText);
            clientNameText = itemView.findViewById(R.id.clientNameText);
            categoryIndicator = itemView.findViewById(R.id.categoryIndicator);
        }
    }
}