//package Map_Audio;
//
//import javax.sound.sampled.*;
//import java.io.IOException;
//import java.net.URL;
//
//public class SoundManager {
//    private static final int SONG_COUNT = 9;//재생할 노래의 수
//    private static Clip[] _clips = new Clip[SONG_COUNT];
//    private static final URL[] soundURLs = new URL[SONG_COUNT];
//    static{
////        try{
////            for(int i = 0; i < SONG_COUNT; i++){
////                AudioInputStream ais = AudioSystem.getAudioInputStream(SoundManager.class.getResource("/song"+i+".wav"));
////                _clips[i] = AudioSystem.getClip();
////                _clips[i].open(ais);
////            }
////        }catch(Exception e){
////            e.printStackTrace();
////        }
//        for (int i = 0; i < SONG_COUNT; i++) {
//            soundURLs[i] = SoundManager.class.getResource("/song" + i + ".wav");
//            if (soundURLs[i] == null) {
//                System.err.println("사운드 파일 없음: /song" + i + ".wav");
//            }
//        }
//    }
//    // 특정 노래 재생
//    public static synchronized void play(int index, float volume) {
//        if (index < 0 || index >= SONG_COUNT || soundURLs[index] == null) return;
//        try {
//            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURLs[index]);
//            Clip clip = AudioSystem.getClip();
//            clip.open(ais);
//
//            setVolume(clip, volume);
//            clip.start();
//
//            // ✅ 종료되면 자동으로 자원 해제
//            clip.addLineListener(event -> {
//                if (event.getType() == LineEvent.Type.STOP) {
//                    clip.close();
//                }
//            });
//        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//            e.printStackTrace();
//        }
////        Clip clip = _clips[index];
////        if (clip != null) {
////            clip.stop(); // 재생 중인 경우 멈춤
////            setVolume(clip, volume);
////            clip.setFramePosition(0); // 재생 위치를 처음으로
////            clip.start(); // 재생
////        }
//    }
//    // 특정 사운드 루프 재생
//    public static synchronized void loop(int index, boolean continuous, int loopCount) {
//        if (index < 0 || index >= SONG_COUNT) return;
//
//        Clip clip = _clips[index];
//        if (clip != null) {
//            clip.stop(); // 현재 재생 중인 클립을 멈춤
//            clip.setFramePosition(0); // 재생 위치 초기화
//
//            if (continuous) {
//                clip.loop(Clip.LOOP_CONTINUOUSLY); // 무한 반복
//            } else {
//                clip.loop(loopCount); // 지정된 횟수만큼 반복
//            }
//        }
//    }
//    // 특정 노래 정지
//    public static synchronized void stop(int index) {
//        if (index < 0 || index >= SONG_COUNT) return;
//
//        Clip clip = _clips[index];
//        if (clip != null && clip.isRunning()) {
//            clip.stop();
//        }
//    }
//    public static void setVolume(Clip clip, float volume) {
//        if (clip == null) return;
//
//        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//        float dB = (float)(Math.log10(volume) * 20);
//        gainControl.setValue(dB);
//    }
//}
package Map_Audio;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SoundManager {
    private static final int SONG_COUNT = 9;
    private static final URL[] soundURLs = new URL[SONG_COUNT];

    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private static final Map<Integer, Clip> activeClips = new HashMap<>();
    public static int MainSceneSOUNDID;
    public static int GameSceneSOUNDID;
    static {
        for (int i = 0; i < SONG_COUNT; i++) {
            soundURLs[i] = SoundManager.class.getResource("/song" + i + ".wav");
            if (soundURLs[i] == null) {
                System.err.println("사운드 파일 없음: /song" + i + ".wav");
            }
        }
    }

    //일반 재생 (한 번만)
    public static synchronized int play(int index, float volume) {
        return playOrLoop(index, volume, false, 0);
    }

    // 루프 재생
    public static synchronized int loop(int index, float volume, boolean continuous, int loopCount) {
        return playOrLoop(index, volume, continuous, loopCount);
    }

    //공통 재생 처리 (재사용)
    private static int playOrLoop(int index, float volume, boolean loop, int loopCount) {
        if (index < 0 || index >= SONG_COUNT || soundURLs[index] == null) return -1;

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURLs[index]);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            setVolume(clip, volume);

            int id = idCounter.getAndIncrement();
            activeClips.put(id, clip);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    activeClips.remove(id);
                }
            });

            if (loop) {
                if (loopCount <= 0) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.loop(loopCount);
                }
            } else {
                clip.start();
            }

            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //정지 (루프든 일반이든)
    public static synchronized void stop(int id) {
        Clip clip = activeClips.get(id);
        if (clip != null) {
            clip.stop();
            clip.close();
            activeClips.remove(id);
        }
    }

    //볼륨 설정
    private static void setVolume(Clip clip, float volume) {
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log10(Math.max(volume, 0.0001)) * 20);
            gainControl.setValue(dB);
        } catch (IllegalArgumentException ignored) {
        }
    }
}