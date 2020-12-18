package com.sachind.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddNotes extends AppCompatActivity {

    public TextView notes_title;
    public TextView notes_description;
    public List<Notes> allNotesCreated = new ArrayList<Notes>();
    public MainActivity mainActivity;
    public String currentTitle="";
    public String currentescription="";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_notes);
        notes_title = findViewById(R.id.titleID);
        notes_description = findViewById(R.id.descriptionID);
        Intent notesIntent = getIntent();
        if(notesIntent.hasExtra("TITLE") && notesIntent.hasExtra("DESCRIPTION")){
            currentTitle = notesIntent.getStringExtra("TITLE");
            notes_title.setText(currentTitle);
            currentescription = notesIntent.getStringExtra("DESCRIPTION");
            notes_description.setText(currentescription);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savenotes, menu);
        return true;
    }


    @Override
    public void onBackPressed(){
        String notesTitle = notes_title.getText().toString();
        String notedDescription = notes_description.getText().toString();
        if(notesTitle.isEmpty() && notedDescription.isEmpty()){
            Toast.makeText(this, "Empty Note! Nothing to Save ", Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }        else if(notesTitle.equals(currentTitle) && notedDescription.equals(currentescription)){
            Toast.makeText(this, "No Changes were made to: "+notesTitle, Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }
        else{
            AlertDialog.Builder saveNoteWhenBackPressed = new AlertDialog.Builder(this);
            saveNoteWhenBackPressed.setTitle("Save Notes");
            saveNoteWhenBackPressed.setMessage("Do you want to save the Note: "+notesTitle+" that you Modified?");
            saveNoteWhenBackPressed.setCancelable(true);
            saveNoteWhenBackPressed.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    saveNotes();
                }
            });
            saveNoteWhenBackPressed.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveNoteWhenBackPressed.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_notes_id:
                Log.d("AddNotes Activity", "onOptionsItemSelected: save notes selected");
                saveNotes();
                return true;
            default:
                Log.d("Default", "onOptionsItemSelected: Default Option");
                return true;
        }
    }

    public void saveNotes(){

        Log.d("AddNotesActivity", "onSaveButtonPressed: Notes Title"+notes_title.getText());
        Log.d("AddNotesActivity", "onSaveButtonPressed: Desription"+notes_description.getText());
        Notes newNote = new Notes();
        newNote.setTitle(notes_title.getText().toString());
        newNote.setDescription(notes_description.getText().toString());
        newNote.setDateOfCreation(Calendar.getInstance().getTime().toString());
        Intent newNoteIntent = new Intent();
        newNoteIntent.putExtra("NOTE_CONTENT", newNote);
        if(notes_title.getText().toString().isEmpty() && notes_description.getText().toString().isEmpty()) {
            newNoteIntent.putExtra("What_to_Do", "Do_Nothing");
            Toast.makeText(this, "Empty Note! Nothing Saved", Toast.LENGTH_LONG).show();
        }
        else if(currentTitle.isEmpty() && currentescription.isEmpty()){
            Log.d("AddNotesActivity", "onSaveButtonPressed: Add New");
            newNoteIntent.putExtra("What_to_Do", "Add_New");
        }
        else if(currentTitle.equals(notes_title.getText().toString()) && currentescription.equals(notes_description.getText().toString())){
            Log.d("AddNotesActivity", "onSaveButtonPressed: No Changes were made");
            newNoteIntent.putExtra("What_to_Do", "No_Changes_Made");
        }else if((notes_title.getText().toString().isEmpty()) && (notes_description.getText().toString().isEmpty())){
            Log.d("AddNotesActivity", "onSaveButtonPressed: No Changes were made");
            newNoteIntent.putExtra("What_to_Do", "Nothing_To_Save");
        }
        else{
            Log.d("AddNotesActivity", "onSaveButtonPressed: Edit");
            newNoteIntent.putExtra("What_to_Do", "Edit");
        }
        setResult(RESULT_OK, newNoteIntent);
        finish();
    }

}