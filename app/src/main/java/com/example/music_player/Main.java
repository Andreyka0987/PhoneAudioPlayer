package com.example.music_player;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
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


public class Main extends Fragment {

    SeekBar bar;
    ImageView playButton;
    int buttonState = 0;
    boolean isThreadActive = false;
    MediaPlayer mediaPlayer;
    int max;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MediaPlayer mediaPlayer = MediaPlayer.create(view.getContext(),R.raw.videoplayback);
        max = Player.resizeSizeOfTheFile(mediaPlayer.getDuration());

        playButton = view.findViewById(R.id.playButton);
        bar = view.findViewById(R.id.musicBar);




        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), String.valueOf(buttonState), Toast.LENGTH_LONG).show();

                if (buttonState == 0){

                    playButton.setImageResource(R.drawable.pause);

                    bar.setMax(max);
                    buttonState++;
                    mediaPlayer.start();


                    if (!isThreadActive){
                        new BarThread().start();
                        isThreadActive = true;
                    }



                    Toast.makeText(view.getContext(),"!",Toast.LENGTH_SHORT).show();

                }else{
                    buttonState--;
                    playButton.setImageResource(R.drawable.play);
                    mediaPlayer.pause();

                    if (mediaPlayer.getCurrentPosition() > 0) {
                        Toast.makeText(view.getContext(), String.valueOf(Player.resizeSizeOfTheFile(mediaPlayer.getDuration())), Toast.LENGTH_SHORT).show();
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
            bar.setProgress(0);
            int currentProgressTemp = 0;
            int currentProgress = 0;
            try {
                while (true) {
                    if (buttonState == 1) {
                        sleep(1);
                        currentProgressTemp++;

                        if (currentProgress >= bar.getMax()){
                            bar.setProgress(bar.getMax());
                            buttonState--;
                            break;
                        }
                        if (buttonState == 1 && currentProgressTemp == 1000){
                        bar.setProgress(currentProgress);
                        currentProgress+=currentProgressTemp;
                        currentProgressTemp = 0;
                        }
                    }
                }

                sleep(1000);
                isThreadActive = false;
                bar.setProgress(0);
                interrupt();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }




}