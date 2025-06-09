package Map_Audio;

import main.MapPanel;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TileManager {
    private final MapPanel MP;
    private final Tile[] tiles;
    private double scrollOffset = 0;
    private double scrollSpeed = 3.0;

    private final int topBuffer = 5;
    private final int bottomBuffer = 5;
    private final int visibleRows;
    private final int totalRows;
    private final int[] tileRowIndices;

    private final long speedIncreaseInterval = 10_000;   // 10초
    private long lastSpeedIncreaseTime = System.currentTimeMillis();

    private final int switchInterval = 50_000;           // 50초 (밀리초)
    private final Timer switchTimer;                     // Swing Timer

    public int nextTileIndex = 0;                        // 인스턴스 필드로 변경

    public TileManager(MapPanel mp) {
        this.MP = mp;
        this.visibleRows    = mp.maxScreenRow;
        this.totalRows      = topBuffer + visibleRows + bottomBuffer;
        this.tileRowIndices = new int[totalRows];
        this.tiles          = new Tile[3];

        getTileImages();
        initializeTileRows();

        // — Swing Timer 설정: 50초 후 첫 실행, 이후 50초마다 반복 —
        ActionListener switchAction = e -> {
            nextTileIndex = (nextTileIndex + 1) % tiles.length;
            System.out.println("Switched to tile index: " + nextTileIndex);
        };
        switchTimer = new Timer(switchInterval, switchAction);
        switchTimer.setRepeats(true);
        switchTimer.start();
    }

    public void resetMap() {
        scrollOffset          = 0;
        scrollSpeed           = 3.0;
        lastSpeedIncreaseTime = System.currentTimeMillis();
        nextTileIndex         = 0;
        initializeTileRows();

        // 타이머도 재시작
        if (switchTimer.isRunning()) switchTimer.restart();
        else                         switchTimer.start();
    }

    private void initializeTileRows() {
        for (int i = 0; i < totalRows; i++) {
            tileRowIndices[i] = 0;
        }
    }

    private void getTileImages() {
        try {
            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(
                    getClass().getClassLoader().getResource("grass.png"));
            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(
                    getClass().getClassLoader().getResource("sand.png"));
            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(
                    getClass().getClassLoader().getResource("water.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateScroll() {
        long now = System.currentTimeMillis();

        // — 속도 증가 (10초마다) —
        if (now - lastSpeedIncreaseTime >= speedIncreaseInterval) {
            scrollSpeed *= 1.1;
            lastSpeedIncreaseTime = now;
        }

        // — 스크롤 오프셋 갱신 —
        scrollOffset -= scrollSpeed;

        // — 화면 밖으로 나간 만큼 한 행씩 밀어내기 —
        while (scrollOffset <= -MP.tileSize) {
            scrollOffset += MP.tileSize;
            shiftRowsDownWithNewIndex(nextTileIndex);
        }
    }

    private void shiftRowsDownWithNewIndex(int newIndex) {
        for (int i = totalRows - 1; i > 0; i--) {
            tileRowIndices[i] = tileRowIndices[i - 1];
        }
        tileRowIndices[0] = newIndex;
    }

    public void draw(Graphics2D g2) {
        int cols = MP.maxScreenCol;
        for (int row = 0; row < totalRows; row++) {
            int tileIndex = tileRowIndices[row];
            for (int col = 0; col < cols; col++) {
                int x = col * MP.tileSize;
                int y = row * MP.tileSize
                        - (int) scrollOffset
                        - topBuffer * MP.tileSize;
                g2.drawImage(
                        tiles[tileIndex].image,
                        x, y,
                        MP.tileSize, MP.tileSize,
                        null
                );
            }
        }
    }
}