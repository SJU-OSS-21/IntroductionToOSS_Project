package Map_Audio;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SoundManager {
    private static final int SONG_COUNT = 10;
    private static final URL[] soundURLs = new URL[SONG_COUNT];

    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private static final Map<Integer, Clip> activeClips = new HashMap<>();
    public static int MainSceneSOUNDID;//오디오 정지를 위한 아이디 반환
    public static int GameSceneSOUNDID;
    public static int GameOverSceneSOUNDID;
    static {
        for (int i = 0; i < SONG_COUNT; i++) {//사운드를 클립이 아닌 주소로 저장
            soundURLs[i] = SoundManager.class.getResource("/GameRes/song" + i + ".wav");
            if (soundURLs[i] == null) {
                System.err.println("사운드 파일 없음: /song" + i + ".wav");
            }
        }
    }

    //일반 재생 (한 번만)
    public static synchronized int play(int index, float volume) {//음원 1회 재생

        return playOrLoop(index, volume, false, 0);
    }

    // 루프 재생
    public static synchronized int loop(int index, float volume, boolean continuous, int loopCount) {//음원 반복 재생
        return playOrLoop(index, volume, continuous, loopCount);
    }

    //공통 재생 처리 (재사용)
    private static int playOrLoop(int index, float volume, boolean loop, int loopCount) {
        if (index < 0 || index >= SONG_COUNT || soundURLs[index] == null) return -1;

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURLs[index]);
            Clip clip = AudioSystem.getClip();//주소를 재생시킬 clip 생성
            clip.open(ais);
            setVolume(clip, volume);//원하는 볼륨으로 설정

            int id = idCounter.getAndIncrement();
            activeClips.put(id, clip);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    activeClips.remove(id);
                }
            });

            if (loop) {//loop위한 코드
                if (loopCount <= 0) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.loop(loopCount);
                }
            } else {//일반 재생
                clip.start();
            }

            return id;//id 반환
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //id를 받아 정지 (루프든 일반이든)
    public static synchronized void stop(int id) {
        Clip clip = activeClips.get(id);
        if (clip != null) {
            clip.stop();
            clip.close();
            activeClips.remove(id);
        }
    }

    // 모든 클립 정지 및 해제
    public static synchronized void stopAll() {
        // 먼저 모든 clip을 복사해서 안전하게 순회하고,
        // 이후 전체 맵 초기화
        List<Clip> clipsToStop = new ArrayList<>(activeClips.values());

        for (Clip clip : clipsToStop) {
            if (clip != null) {
                if (clip.isRunning()) clip.stop();
                if (clip.isOpen()) clip.close();
            }
        }

        activeClips.clear(); //마지막에 초기화
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