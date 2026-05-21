package javaapplication9;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Sound {
    private static Map<String, Clip> soundCache = new HashMap<>();
    
    public static void play(String filename) {
        try {
            // طريقة 1: البحث عن الملف من مجلد المشروع
            File soundFile = new File("sounds/" + filename);
            System.out.println("Looking for: " + soundFile.getAbsolutePath()); // للتصحيح
            
            if (!soundFile.exists()) {
                // طريقة 2: البحث من classpath
                URL soundUrl = Sound.class.getResource("/sounds/" + filename);
                if (soundUrl == null) {
                    soundUrl = Sound.class.getResource("sounds/" + filename);
                }
                
                if (soundUrl != null) {
                    playFromURL(soundUrl, filename);
                    return;
                }
                
                System.out.println("Sound file not found: " + filename);
                return;
            }
            
            // تشغيل الملف
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = soundCache.get(filename);
            
            if (clip == null || !clip.isOpen()) {
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                soundCache.put(filename, clip);
            } else {
                clip.setFramePosition(0);
            }
            
            clip.start();
            System.out.println("Playing sound: " + filename); // للتصحيح
            
        } catch (Exception e) {
            System.out.println("Sound error for " + filename + ": " + e.getMessage());
        }
    }
    
    private static void playFromURL(URL url, String filename) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = soundCache.get(filename);
            
            if (clip == null || !clip.isOpen()) {
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                soundCache.put(filename, clip);
            } else {
                clip.setFramePosition(0);
            }
            
            clip.start();
            System.out.println("Playing sound from URL: " + filename);
            
        } catch (Exception e) {
            System.out.println("URL Sound error: " + e.getMessage());
        }
    }
}