package com.example.music_player;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Folder extends Fragment {

    private static final String MUSIC_URI_FOLDER = "music.txt";
    private Context context = null;
    private final int AUDIO_READ_CODE = 10;
    private static LinearLayout linearLayout;
    Button addToLibrary;
    Button deleteAllMusic;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        linearLayout = view.findViewById(R.id.libraryLayout);
        addToLibrary = view.findViewById(R.id.addToLibrary);
        deleteAllMusic = view.findViewById(R.id.deleteAllButton);

        updateUiLib(context,0);





        addToLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("audio/mpeg");
                startActivityForResult(intent,AUDIO_READ_CODE);





            }
        });

        deleteAllMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fileOutputStream = context.openFileOutput(MUSIC_URI_FOLDER,MODE_PRIVATE);
                    fileOutputStream.write("".getBytes());

                    Player.uriIndex = 0;
                    Player.arrOfStringUri = null;
                    updateUiLib(context,1);


                    Toast.makeText(context,"All files were removed",Toast.LENGTH_SHORT).show();
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == AUDIO_READ_CODE){


            context.getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.getContentResolver().takePersistableUriPermission(data.getData(),Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            String sequenceOfUri = "";
            FileOutputStream fileOutputStream = null;
            BufferedReader bufferedReader = null;

            try {

                FileInputStream fileInputStream = context.openFileInput(MUSIC_URI_FOLDER);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                sequenceOfUri = bufferedReader.readLine();

                fileOutputStream = context.openFileOutput(MUSIC_URI_FOLDER,MODE_PRIVATE);

                if (sequenceOfUri != null) {
                    sequenceOfUri += data.getData().toString() + " ";
                    System.out.println(sequenceOfUri);
                    fileOutputStream.write(sequenceOfUri.getBytes());
                }
                else{
                    sequenceOfUri = data.getData().toString() + " ";
                    System.out.println(sequenceOfUri);
                    fileOutputStream.write(sequenceOfUri.getBytes());
                }





                fileInputStream = context.openFileInput(MUSIC_URI_FOLDER);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                sequenceOfUri = bufferedReader.readLine();
                Player.arrOfStringUri = sequenceOfUri.split(" ");


                fileInputStream.close();
            } catch (IOException e) {
                try {
                    fileOutputStream = context.openFileOutput(MUSIC_URI_FOLDER,MODE_PRIVATE);
                    fileOutputStream.write((data.getData().toString()+" ").getBytes());

                    FileInputStream fileInputStream = context.openFileInput(MUSIC_URI_FOLDER);
                    bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                    sequenceOfUri = bufferedReader.readLine();

                    Player.arrOfStringUri = sequenceOfUri.split(" ");

                    fileInputStream.close();
                    fileInputStream.close();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }

            updateUiLib(context,1);
            updateUiLib(context,0);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folder, container, false);
    }


    public static void updateInternalLib(Context context){

        try {
            String tempSequence = null;
            FileInputStream fileInputStream = null;
            BufferedReader bufferedReader = null;
            fileInputStream = context.openFileInput(MUSIC_URI_FOLDER);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            tempSequence = bufferedReader.readLine();
            if (tempSequence != null)Player.arrOfStringUri = tempSequence.split(" ");
        } catch (IOException e) {
            Toast.makeText(context,"Something went wring",Toast.LENGTH_SHORT).show();
        }
    }

    public static void updateUiLib(Context context, int code){

        if (code == 0) {
            if (Player.arrOfStringUri != null && Player.arrOfStringUri.length >= 1) {
                for (int i = 0; i < Player.arrOfStringUri.length; i++) {
                    Button tempBtn = new Button(context);
                    tempBtn.setText(Player.arrOfStringUri[i]);
                    linearLayout.addView(tempBtn);
                }
            }
        }
        if (code == 1){
            for (int i =2;i<linearLayout.getChildCount();i++){
                linearLayout.removeViewAt(i);
            }
        }


    }



}