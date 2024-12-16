package net.yochu.christmas.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.InputStream;

public class SoundUtils {

    public static float getSoundDuration(InputStream soundFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            long frameLength = audioInputStream.getFrameLength();
            float frameRate = audioInputStream.getFormat().getFrameRate();
            return frameLength / frameRate; // Duration in seconds
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 on error
        }
    }
}
