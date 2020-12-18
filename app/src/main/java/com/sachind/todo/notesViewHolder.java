package com.sachind.todo;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class notesViewHolder extends RecyclerView.ViewHolder {

    public TextView notes_title;
    public TextView notes_description;
    public TextView notes_created_date;
    public ConstraintLayout linearLayout;
    public notesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_title = itemView.findViewById(R.id.notesTitle);
        notes_title.setPaintFlags(notes_title.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        linearLayout=(ConstraintLayout) itemView.findViewById(R.id.notesLayoutID);
        notes_description = itemView.findViewById(R.id.notesDescription);
        notes_created_date = itemView.findViewById(R.id.notesTimestamp);
    }
}
