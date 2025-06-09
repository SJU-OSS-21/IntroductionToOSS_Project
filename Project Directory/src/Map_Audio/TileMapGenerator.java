package Map_Audio;

import UI_Scene.InGameManager;
import main.MapPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;


public class TileMapGenerator {
    private final MapPanel MP;
    private final Tile[] tiles;//타일 배열

    private double scrollOffset = 0;
    private double scrollSpeed = 3.0;

    private final int topBuffer = 5;
    private final int bottomBuffer = 5;
    private final int visibleRows;
    private final int totalRows;
    private final int[] tileRowIndices;

    private final long speedIncreaseInterval = 10_000;   // 10초
    private long lastSpeedIncreaseTime;

    private final long switchInterval = 50_000;         // 50초
    private long switchElapsedTime;
    private long lastUpdateTime;

    public int nextTileIndex = 0;    // 인스턴스 필드

    public TileMapGenerator(MapPanel mp) {
        this.MP = mp;
        this.visibleRows = mp.maxScreenRow;
        this.totalRows = topBuffer + visibleRows + bottomBuffer;
        this.tileRowIndices = new int[totalRows];
        this.tiles = new Tile[3];

        getTileImages();
        initializeTileRows();

        long now = System.currentTimeMillis();
        this.lastSpeedIncreaseTime = now;
        this.lastUpdateTime = now;
        this.switchElapsedTime = 0;
    }

    public void resetMap() {
        scrollOffset = 0;
        scrollSpeed = 3.0;
        nextTileIndex = 0;
        initializeTileRows();

        long now = System.currentTimeMillis();
        this.lastSpeedIncreaseTime = now;
        this.lastUpdateTime = now;
        this.switchElapsedTime = 0;
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
                    getClass().getClassLoader().getResource("GameRes/grass.png"));
            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(
                    getClass().getClassLoader().getResource("GameRes/sand.png"));
            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(
                    getClass().getClassLoader().getResource("GameRes/water.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 프레임마다 호출: pause 시에는 게임 시간(타일 전환/속도 증가/스크롤) 모두 멈춤
     */
    public void updateScroll() {
        long now = System.currentTimeMillis();
        boolean paused = InGameManager.getInstance().isPaused();

        if (paused) {
            // pause 동안은 게임 시간 동결: 타이머 기준 시간만 갱신
            System.out.println("game stop");

            lastUpdateTime = now;
            lastSpeedIncreaseTime = now;
            return;
        }

        // — 게임 시간이 흐를 때만 dt 누적 —
        long dt = now - lastUpdateTime;
        lastUpdateTime = now;

        // — 50초마다 타일 전환 —
        switchElapsedTime += dt;
        if (switchElapsedTime >= switchInterval) {
            switchElapsedTime -= switchInterval;
            nextTileIndex = (nextTileIndex + 1) % tiles.length;
            System.out.println("Switched to tile index: " + nextTileIndex);
        }

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