package com.sachind.todo;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyAsyncTask extends AsyncTask<Void, Void, Void> { //  <Parameter, Progress, Result>

    private MainActivity mainActivity;

    public MyAsyncTask(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected Void doInBackground(Void... voids) {


        try {

            InputStream is = mainActivity.getApplicationContext().openFileInput(mainActivity.getString(R.string.jsonFileName));
            JsonReader reader = new JsonReader(new InputStreamReader(is, mainActivity.getString(R.string.encoding)));
            String name;

            reader.beginObject();

            while (reader.hasNext()) {
                name = reader.nextName();
                if (name.equals("notes")) {

                    reader.beginArray();
                    while (reader.hasNext()) {
                        Notes tempNotes = new Notes();
                        reader.beginObject();
                        while(reader.hasNext()) {
                            name = reader.nextName();
                            Log.d("MyAsyncTask", "doInBackground: if it has a Name: "+name);
                            if (name.equals("title")) {
                                String title = reader.nextString();
                                Log.d("MyAsyncTask", "doInBackground: If it has title: "+title.getClass().getName());
                                tempNotes.setTitle(title+"");
                            } else if (name.equals("note")) {
                                String description = reader.nextString();
                                Log.d("MyAsyncTask", "doInBackground: If it has description"+description);
                                tempNotes.setDescription(description);
                            } else if (name.equals("timestamp")) {
                                String timeStamp = reader.nextString();
                                Log.d("MyAsyncTask", "doInBackground: If it has Timestamp"+timeStamp);
                                tempNotes.setDateOfCreation(timeStamp);
                            } else {
                                reader.skipValue();
                            }
                            Log.d("MyAsyncTask", "doInBackground: Read one line");
                        }
                        reader.endObject();
                        mainActivity.getNotesList().add(tempNotes);

                    }
                    reader.endArray();
                }
                else{
                    reader.skipValue();
                }

            }
            reader.endObject();

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: "+e);
        }
//        return note_list;
        return null;
    }
}
