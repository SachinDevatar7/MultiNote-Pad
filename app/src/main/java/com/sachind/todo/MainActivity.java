package com.sachind.todo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private TextView noNotes;
    private List<Notes> allNotesCreated = new ArrayList<>();
    private RecyclerView recyclerView;
    private int currentPosition;
    private AddNotes addNotes;
    private NotesUtility notesUtility;
    private List<Notes> newNotesOnTop = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noNotes = findViewById(R.id.noNotesID);
        recyclerView = findViewById(R.id.notesRecyclerID);
        notesUtility = new NotesUtility(allNotesCreated, this);
        recyclerView.setAdapter(notesUtility);
        recyclerView.setLayoutManager(new LinearLayoutManager(this  ));
        new MyAsyncTask(this).execute();
        for (Notes eachNote : allNotesCreated) {
            Log.d("Displaying Records", "Title: "+eachNote.title);
            Log.d("Displaying Records", "Description: "+eachNote.description);
            Log.d("Displaying Records", "Timestamp: "+eachNote.dateOfCreation);
        }
        if(allNotesCreated.size()==0){
            noNotes.setText("No Notes to Display!");
        }else {
            noNotes.setText("");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todomenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.infoID:
                Log.d("MainActivity", "onOptionsItemSelected: InfoOptionSelected");
                Intent infoIntent = new Intent(this, InfoActivity.class);
                startActivity(infoIntent);
                return true;
            case R.id.addID:
                Log.d("MainActivity", "onOptionsItemSelected: Selected Add Option");
                Intent newNotesIntent = new Intent(this, AddNotes.class);
                startActivityForResult(newNotesIntent,123);
                return true;
            case R.id.deleteAllID:
                if(allNotesCreated.size()==0){
                    Toast.makeText(this, "No items to Delete!", Toast.LENGTH_LONG).show();
                }else {
                    AlertDialog.Builder deleteAlert= new AlertDialog.Builder(this);
                    deleteAlert.setTitle("Delete All Notes");
                    deleteAlert.setMessage("Are you sure you want to delete all the Notes?");
                    deleteAlert.setCancelable(true);
                    deleteAlert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            allNotesCreated.removeAll(allNotesCreated);
                            notesUtility.notifyDataSetChanged();
                            noNotes.setText("No Notes to Display!");
                            //                        saveAllCreatedNotes();
                        }
                    });
                    deleteAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    deleteAlert.show();
                }
                return true;

            default:
                Log.d("MainActivity", "onOptionsItemSelected: Default Option");
                return true;
        }
    }

    public List<Notes> getNotesList(){
        return allNotesCreated;
    }

    @Override
    protected void onPause(){
        Log.d("Save_Notes", "Calling Save Notes on Pause(): ");
        saveAllCreatedNotes();
        super.onPause();
    }

    public void saveAllCreatedNotes() {
        Log.d("Save_Notes", "Called Save Notes on Pause(): ");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.jsonFileName), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("notes");
            writeNotesArray(writer);
            writer.endObject();
            writer.close();


        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void writeNotesArray(JsonWriter writer) throws IOException {
        writer.beginArray();
        for (Notes value : allNotesCreated) {
            Log.d("Save_Notes", "Writing into an Array: "+value.title);
            writeNotesObject(writer, value);
        }
        writer.endArray();
    }

    @Override
    protected void onResume(){
        allNotesCreated.size();
        super.onResume();
        notesUtility.notifyDataSetChanged();
        if(allNotesCreated.size()>0){
            noNotes.setText("");
        }
    }

    public void writeNotesObject(JsonWriter writer, Notes val) throws IOException{
        writer.beginObject();
        writer.name("title").value(val.getTitle());
        writer.name("note").value(val.getDescription());
        writer.name("timestamp").value(val.getDateOfCreation());
        writer.endObject();
        Log.d("Save_Notes", "Wrote into an array: "+val.getTitle());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Note_Content", "Request Code: "+requestCode);

        if (requestCode == 123) {
            Log.d("MainActivity", "onActivityResult: Data Received");
            if (resultCode == RESULT_OK) {
                Notes noteContent= (Notes) data.getExtras().getSerializable("NOTE_CONTENT");
                int currentNotesLength = allNotesCreated.size();
                Log.d("Note_Content", "onActivityResult: "+noteContent.getTitle());
                Log.d("Note_Content", "onActivityResult: "+noteContent.getDescription());

                String status = data.getStringExtra("What_to_Do");
                Log.d("Note_Content", "Edit Called: "+status);
                if(status.equals("Do_Nothing")){
//                    Toast.makeText(this, "No Changes were made to: "+noteContent.getTitle()+" Saved!", Toast.LENGTH_LONG).show();
                }
                else if(status.equals("Add_New")){
                    allNotesCreated.add(0, noteContent);
                    Toast.makeText(this, "Note : "+noteContent.getTitle()+" Added Successfully!", Toast.LENGTH_LONG).show();
                    noNotes.setText("");
                }else if(status.equals("No_Changes_Made")){
                    Toast.makeText(this, "No changes were made to: "+noteContent.getTitle()+"", Toast.LENGTH_LONG).show();
                }else if(status.equals("Do_Nothing")){
                    Toast.makeText(this, "Empty Note! Nothing Saved!", Toast.LENGTH_LONG).show();
                }else if(status.equals("Nothing_To_Save")){
                    Toast.makeText(this, "Empty Note! Nothing Saved!", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("Note_Content", "Edit Called: "+noteContent.getTitle());
                    Log.d("Note_Content", "Edit Called: "+noteContent.getDescription());
                    allNotesCreated.remove(currentPosition);
                    allNotesCreated.add(0, noteContent);
                    Toast.makeText(this, ""+noteContent.getTitle()+" Modified Successfully!", Toast.LENGTH_LONG).show();
                    noNotes.setText("");
                }
            }
        }
    /*    notesUtility = new NotesUtility(allNotesCreated, this);
        recyclerView.setAdapter(notesUtility);*/
    }

    public void onclickingEditButton(View view){
        onClick(view);
    }

    public void onClick(View view) {
        currentPosition = recyclerView.getChildLayoutPosition(view);
        Log.d("ONC", "onClick: "+currentPosition);
        Intent intent = new Intent(MainActivity.this, AddNotes.class);
        Log.d("ONC", "onClick: "+allNotesCreated.get(currentPosition).getDescription());
        intent.putExtra("TITLE", allNotesCreated.get(currentPosition).getTitle());
        intent.putExtra("CREATED_DATE", allNotesCreated.get(currentPosition).getDateOfCreation());
        intent.putExtra("DESCRIPTION", allNotesCreated.get(currentPosition).getDescription());
        startActivityForResult(intent, 123);
    }

    public boolean onLongClick(View view) {
        currentPosition = recyclerView.getChildLayoutPosition(view);
        AlertDialog.Builder deleteAlert= new AlertDialog.Builder(this);
        deleteAlert.setTitle("Delete Note");
        deleteAlert.setMessage("Are you sure you want to delete the Note?");
        deleteAlert.setCancelable(true);
        deleteAlert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                allNotesCreated.remove(currentPosition);
                notesUtility.notifyDataSetChanged();
                currentPosition -= 1;
                if(allNotesCreated.size()==0){
                    noNotes.setText("No Notes to Display!");
                }
            }
        });
        deleteAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        deleteAlert.show();
        return false;
    }
}