/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
	Clip clip;
    URL soundURL[] = new URL[5];

	public Sound() {
		soundURL[0] = getClass().getResource("/rpg/resources/sounds/musicInGame.wav");
		soundURL[1] = getClass().getResource("/rpg/resources/sounds/swordhit.wav");
		soundURL[2] = getClass().getResource("/rpg/resources/sounds/fireball.wav");
		soundURL[3] = getClass().getResource("/rpg/resources/sounds/gameover.wav");
        soundURL[4] = getClass().getResource("/rpg/resources/sounds/victory.wav");
	}
	public void setFile(int i)           // Java Sound File Opening Format
    {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
			System.out.println("Sound loaded successfully.");
        }
        catch (Exception e)
        {
			e.printStackTrace();
            clip = null; // Gán null nếu có lỗi
			System.err.println("Error loading sound file.");
        }
    }

    public void play()
    {
		if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        } else {
            System.err.println("Clip is null. Please load the sound file correctly.");
        }
    }

    public void loop()
    {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop()
    {
            clip.stop();
    }

}

