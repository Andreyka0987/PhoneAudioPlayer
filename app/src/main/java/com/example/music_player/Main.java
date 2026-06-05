package com.example.music_player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main extends Fragment {

    Context context = null;
    SeekBar bar;
    ImageView playButton;
    int buttonState = 0;
    boolean isThreadActive = false;
    MediaPlayer mediaPlayer;
    int currentProgress = 0;
    boolean sliderChecker = false;
    int debug = 0;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();


        playButton = view.findViewById(R.id.playButton);
        bar = view.findViewById(R.id.musicBar);

        bar.getProgressDrawable().setColorFilter(Color.rgb(255,255,255), PorterDuff.Mode.MULTIPLY);





        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(Player.getNextMusic().toString());


                if (buttonState == 0){
                    if (mediaPlayer == null) {
                        Uri tempUri = Player.getNextMusic();
                        mediaPlayer = MediaPlayer.create(view.getContext(), tempUri);
                    }


                    playButton.setImageResource(R.drawable.pause);
                    sliderChecker = true;
                    bar.setMax(mediaPlayer.getDuration());
                    buttonState++;
                    mediaPlayer.start();


                    if (!isThreadActive){
                        new BarThread().start();
                        isThreadActive = true;
                    }





                }else{
                    buttonState--;
                    sliderChecker = false;
                    playButton.setImageResource(R.drawable.play);
                    mediaPlayer.pause();

                    if (mediaPlayer.getCurrentPosition() > 0) {
//                        Toast.makeText(view.getContext(), String.valueOf(mediaPlayer.getCurrentPosition()), Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });


        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    buttonState = 0;
                    mediaPlayer.pause();
                    currentProgress = progress;

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(currentProgress);
                if (sliderChecker) {
                    buttonState = 1;
                    mediaPlayer.start();
                }

            }
        });


    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main,container,false);

    }

    public class BarThread extends Thread {

        @Override
        public void run() {
            super.run();

            try {
                while (true) {

                    if (buttonState == 1) {
                        sleep(100);
                        currentProgress+=100;

                            if (currentProgress >= bar.getMax()) {

                                bar.setProgress(bar.getMax());
                                buttonState--;

                                break;

                            }


                        bar.setProgress(currentProgress);
                    }
                }


                mediaPlayer = null;
                debug++;
                isThreadActive = false;
                bar.setProgress(0);
                currentProgress = 0;
                Player.updateIndex();
                interrupt();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }




}