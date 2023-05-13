package com.example.modaldialogs_fileprocessing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class ModalDialogsFileProcessingRecyclerViewAdapter extends RecyclerView.Adapter<ModalDialogsFileProcessingRecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<File> fileList;

    public ModalDialogsFileProcessingRecyclerViewAdapter(Context context, List<File> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = fileList.get(position);
        holder.fileNameTextView.setText(file.getName());
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView fileNameTextView;

        public ViewHolder(@NonNull View view) {
            super(view);
            fileNameTextView = view.findViewById(R.id.textView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                File file = fileList.get(position);
                ((MainActivity) context).showFileContents(file);
            }
        }
    }
}
