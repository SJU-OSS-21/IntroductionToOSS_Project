package Map_Audio;

import javax.sound.sampled.*;

public class SoundManager {
    private static final int SONG_COUNT = 5;//재생할 노래의 수
    private static Clip[] _clips = new Clip[SONG_COUNT];
    static{
        try{
            for(int i = 0; i < SONG_COUNT; i++){
                AudioInputStream ais = AudioSystem.getAudioInputStream(SoundManager.class.getResource("song"+i+".wav"));
                _clips[i] = AudioSystem.getClip();
                _clips[i].open(ais);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
