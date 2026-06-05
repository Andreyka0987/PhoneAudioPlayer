package com.example.music_player;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Folder extends Fragment {

    Context context = null;
    private final int AUDIO_READ_CODE = 10;
    LinearLayout linearLayout;
    Button addToLibrary;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        linearLayout = view.findViewById(R.id.libraryLayout);
        addToLibrary = view.findViewById(R.id.addToLibrary);


        addToLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("audio/mpeg");
                startActivityForResult(intent,AUDIO_READ_CODE);


            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == AUDIO_READ_CODE){
            String sequenceOfUri = null;
            FileOutputStream fileOutputStream = null;
            BufferedReader bufferedReader = null;

            try {

                FileInputStream fileInputStream = context.openFileInput("music.txt");
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                sequenceOfUri = bufferedReader.readLine();

                fileOutputStream = context.openFileOutput("music.txt",MODE_PRIVATE);

                if (sequenceOfUri == null){
                    fileOutputStream.write((data.getData().toString()+" ").getBytes());
                }
                else{

                    sequenceOfUri = bufferedReader.readLine()+data.getData().toString()+" ";
                    System.out.println(sequenceOfUri);
                    fileOutputStream.write(sequenceOfUri.getBytes());



                }

                fileInputStream = context.openFileInput("music.txt");
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                sequenceOfUri = bufferedReader.readLine();
                Player.arrOfStringUri = sequenceOfUri.split(" ");

                fileInputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                try {
                    fileOutputStream = context.openFileOutput("music.txt",MODE_PRIVATE);
                    fileOutputStream.write((data.getData().toString()+" ").getBytes());

                    FileInputStream fileInputStream = context.openFileInput("music.txt");
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    sequenceOfUri = bufferedReader.readLine();

                    Player.arrOfStringUri = sequenceOfUri.split(" ");
                    inputStreamReader.close();
                    fileInputStream.close();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folder, container, false);
    }
}