package com.sachind.todo;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesUtility extends RecyclerView.Adapter<notesViewHolder> {

    public List<Notes> allCreatedNotes;
    public MainActivity mainActivity;
    public NotesUtility(List<Notes> notesList, MainActivity mainActivity){
        this.allCreatedNotes = notesList;
        this.mainActivity = mainActivity;
    }
    public Notes currentNote;
    @NonNull
    @Override
    public notesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
        itemView.setOnClickListener((View.OnClickListener) mainActivity);
        itemView.setOnLongClickListener((View.OnLongClickListener) mainActivity);
        return new notesViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull notesViewHolder holder, int position) {
        currentNote = allCreatedNotes.get(position);
        holder.notes_title.setText(currentNote.title);
        GradientDrawable drawable = (GradientDrawable)holder.linearLayout.getBackground();
        if(position%5 == 0){
            drawable.setColor(Color.parseColor("#FFD700"));
        }
        if(position%5 == 1){
            drawable.setColor(Color.parseColor("#778899"));
        }
        if(position%5 == 2){
            drawable.setColor(Color.parseColor("#FFA07A"));
        }
        if(position%5 == 3){
            drawable.setColor(Color.parseColor("#ADFF2F"));
        }
        if(position%5 == 4){
            drawable.setColor(Color.parseColor("#008B8B"));
        }
        if(currentNote.equals(null)){

        }else {
            if(currentNote.description.length()>80){
                StringBuilder dummyDesc = new StringBuilder();
                dummyDesc.append(currentNote.description.substring(0,79));
                dummyDesc.append("...");
                holder.notes_description.setText(dummyDesc.toString());
            }else {
                holder.notes_description.setText(currentNote.description);
            }
            holder.notes_created_date.setText(currentNote.dateOfCreation);
        }
    }

    @Override
    public int getItemCount() {
        mainActivity.getSupportActionBar().setTitle("Multi-Note( "+allCreatedNotes.size()+" )");
        return allCreatedNotes.size();
    }
}
