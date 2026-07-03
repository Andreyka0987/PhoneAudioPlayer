package com.example.music_player;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;


public class Main extends Fragment {

    Context context = null;
    SeekBar bar;
    ImageView playButton;
    int buttonState = 0;
    boolean isThreadActive = false;
    Intent musicIntent;
    int currentProgress = 0;
    boolean sliderChecker = false;
    int debug = 0;


    Uri mainUri = null;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        playButton = view.findViewById(R.id.playButton);
        bar = view.findViewById(R.id.musicBar);

        bar.getProgressDrawable().setColorFilter(Color.rgb(255,255,255), PorterDuff.Mode.MULTIPLY);


            if (isForegroundServiceStarted()){
                ForeGroundMusicService.mediaPlayer.pause();
            }
        musicIntent = new Intent(context, ForeGroundMusicService.class);

        Folder.updateInternalLib(context);




        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (buttonState == 0){
                    onPlay(context);


                }else{
                    buttonState--;
                    sliderChecker = false;
                    playButton.setImageResource(R.drawable.play);
                    ForeGroundMusicService.mediaPlayer.pause();

                    if (ForeGroundMusicService.mediaPlayer.getCurrentPosition() > 0) {
//                        Toast.makeText(view.getContext(), String.valueOf(mediaPlayer.getCurrentPosition()), Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });


        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && ForeGroundMusicService.mediaPlayer != null){
                    buttonState = 0;
                    ForeGroundMusicService.mediaPlayer.pause();
                    currentProgress = progress;

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ForeGroundMusicService.mediaPlayer != null) {
                    ForeGroundMusicService.mediaPlayer.seekTo(currentProgress);
                    if (sliderChecker) {
                        buttonState = 1;
                        if (!isForegroundServiceStarted()){
                            context.startForegroundService(musicIntent);
                        }
                        else{
                            ForeGroundMusicService.mediaPlayer.start();
                        }
                    }
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
            Looper.prepare();

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


                ForeGroundMusicService.mediaPlayer = null;
                mainUri = null;
                debug++;
                isThreadActive = false;
                bar.setProgress(0);
                currentProgress = 0;
                Player.updateIndex();
                interrupt();
                onPlay(context);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }


    public void onPlay(Context context){

        if (ForeGroundMusicService.mediaPlayer == null) {
            mainUri = Player.getNextMusic();

            if (!mainUri.toString().equals("10")) {

                ForeGroundMusicService.mediaPlayer = MediaPlayer.create(context, mainUri);

            }
        }

        if (!mainUri.toString().equals("10")) {
            playButton.setImageResource(R.drawable.pause);
            sliderChecker = true;
            bar.setMax(ForeGroundMusicService.mediaPlayer.getDuration());
            buttonState++;
            if (!isForegroundServiceStarted()) {
                context.startForegroundService(musicIntent);
            }
            else{
                ForeGroundMusicService.mediaPlayer.start();
            }

            if (!isThreadActive) {
                new BarThread().start();
                isThreadActive = true;
            }
        }
        else{
            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isForegroundServiceStarted(){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo: activityManager.getRunningServices(Integer.MAX_VALUE)){
            if (ForeGroundMusicService.class.getName().equals(serviceInfo.service.getClassName())){return true;}
        }
        return false;
    }


}