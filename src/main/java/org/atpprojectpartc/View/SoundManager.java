package org.atpprojectpartc.View;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
    private MediaPlayer mediaPlayer;

    public void playSound(String path, boolean loop) {
        stop();
        try {
            Media media = new Media(getClass().getResource(path).toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            if (loop) {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            }
            mediaPlayer.play();
        } catch (NullPointerException e) {
            System.err.println("ERROR: Audio file not found: " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
