package com.example.comp3074_groupproject_bestulator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private List<Project> projects;
    private OnProjectClickListener listener;
    private OnDeleteClickListener deleteListener;

    public interface OnProjectClickListener {
        void onProjectClick(Project project);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Project project, int position);
    }

    public ProjectAdapter(List<Project> projects, OnProjectClickListener listener) {
        this.projects = projects;
        this.listener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_item, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.projectNameText.setText(project.getProjectName());
        holder.clientNameText.setText(project.getClientName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProjectClick(project);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(project, position);
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

    public void removeProject(int position) {
        projects.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, projects.size());
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView projectNameText;
        TextView clientNameText;
        ImageButton deleteButton;

        ProjectViewHolder(View itemView) {
            super(itemView);
            projectNameText = itemView.findViewById(R.id.projectNameText);
            clientNameText = itemView.findViewById(R.id.clientNameText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}