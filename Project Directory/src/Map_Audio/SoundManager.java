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
    // 특정 노래 재생
    public static synchronized void play(int index, float volume) {
        if (index < 0 || index >= SONG_COUNT) return;

        Clip clip = _clips[index];
        if (clip != null) {
            clip.stop(); // 재생 중인 경우 멈춤
            setVolume(clip, volume);
            clip.setFramePosition(0); // 재생 위치를 처음으로
            clip.start(); // 재생
        }
    }
    // 특정 사운드 루프 재생
    public static synchronized void loop(int index, boolean continuous, int loopCount) {
        if (index < 0 || index >= SONG_COUNT) return;

        Clip clip = _clips[index];
        if (clip != null) {
            clip.stop(); // 현재 재생 중인 클립을 멈춤
            clip.setFramePosition(0); // 재생 위치 초기화

            if (continuous) {
                clip.loop(Clip.LOOP_CONTINUOUSLY); // 무한 반복
            } else {
                clip.loop(loopCount); // 지정된 횟수만큼 반복
            }
        }
    }
    public static void setVolume(Clip clip, float volume) {
        if (clip == null) return;

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float)(Math.log10(volume) * 20);
        gainControl.setValue(dB);
    }
}
