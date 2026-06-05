package com.example.music_player;

import android.net.Uri;

import java.net.URI;

public class Player {

    private static int uriIndex = 0;
    static Uri nexUrlToPlay = null;
    static String[] arrOfStringUri;

    //   ab = Uri.parse(a);

    public static int resizeSizeOfTheFile(int size){
        return (int)Math.floor(size/1000)*1000;
    }

    public static Uri getNextMusic(){
        return Uri.parse(arrOfStringUri[uriIndex]);
    }

    public static void updateIndex(){
        uriIndex++;
    }




}
