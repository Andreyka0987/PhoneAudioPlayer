package com.example.music_player;

import android.net.Uri;

import java.net.URI;

public class Player {

    public static int uriIndex = 0;
    static Uri nexUrlToPlay = null;
    static String[] arrOfStringUri = null;

    //   ab = Uri.parse(a);

    public static int resizeSizeOfTheFile(int size){
        return (int)Math.floor(size/1000)*1000;
    }

    public static Uri getNextMusic(){
        Uri tempUri = null;
        if (arrOfStringUri != null && arrOfStringUri.length >= 1)tempUri = Uri.parse(arrOfStringUri[uriIndex]);

        Uri warningUri = Uri.parse("10");

        if (tempUri == null)return warningUri;

        return tempUri;
    }

    public static void updateIndex(){
        if (arrOfStringUri != null && uriIndex != arrOfStringUri.length-1) {
            uriIndex++;
        }else {
            uriIndex = 0;
        }
    }




}
