package com.jukbox;

import com.jukbox.UI.BaseUI;

import javax.sound.sampled.*;
import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        BaseUI baseUI = new BaseUI();
        baseUI.displayMenu();

    }
}
