package Map_Audio;

import main.MapPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TileManager {
    MapPanel MP;
    Tile[] tiles;

    // 1) scrollOffset을 double로 변경
    private double scrollOffset = 0;

    // 2) scrollSpeed를 double로 변경 (초기값 2.0)
    private double scrollSpeed = 5.0;

    private final int topBuffer = 5;
    private final int bottomBuffer = 5;
    private final int visibleRows;
    private final int totalRows;

    private int[] tileRowIndices;

    // --- 타일 변경 관련 타이머 변수 ---
    private long lastSwitchTime = System.currentTimeMillis();


    private final long switchInterval = 30000;  // 30초마다 맵 변경
    public static int nextTileIndex = 0;



    // 3) “속도 증가”용 타이머 변수 추가
    private long lastSpeedIncreaseTime = System.currentTimeMillis();
    private final long speedIncreaseInterval = 10000; // 10초마다

    public TileManager(MapPanel mp) {
        this.MP = mp;
        this.visibleRows = MP.maxScreenRow;
        this.totalRows = topBuffer + visibleRows + bottomBuffer;
        this.tileRowIndices = new int[totalRows];
        this.tiles = new Tile[3];

        getTileImages();
        initializeTileRows();
    }

    private void initializeTileRows() {
        // 초기에는 전부 0번(예: grass)로 채워놓거나,
        // 원하는 패턴이 있으면 직접 채워넣어도 됩니다.
        for (int i = 0; i < totalRows; i++) {
            tileRowIndices[i] = 0;
        }
    }

    public void getTileImages() {
        try {
            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(getClass().getClassLoader().getResource("grass.png"));
            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(getClass().getClassLoader().getResource("sand.png"));
            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(getClass().getClassLoader().getResource("water.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateScroll() {
        long now = System.currentTimeMillis();

        // 4) “10초가 지났으면” scrollSpeed를 10% 증가
        if (now - lastSpeedIncreaseTime >= speedIncreaseInterval) {
            scrollSpeed *= 1.1; // 기존 속도의 10% 추가 (ex. 2.0 → 2.2)
            lastSpeedIncreaseTime = now;
        }

        // 5) 먼저 scrollOffset 갱신 (double 간 차감)
        scrollOffset -= scrollSpeed;

        // 6) “30초가 지났으면” 다음 번에 들어올 타일 인덱스를 미리 준비해 둔다.
        if (now - lastSwitchTime >= switchInterval) {
            nextTileIndex = (nextTileIndex + 1) % 3;
            lastSwitchTime = now;
        }

        // 7) scrollOffset이 -tileSize 이하가 되면, 한 행씩 내려주고
        //    그때마다 새롭게 들어올 행(0번 인덱스)은 nextTileIndex로 설정
        while (scrollOffset <= -MP.tileSize) {
            scrollOffset += MP.tileSize;
            shiftRowsDownWithNewIndex(nextTileIndex);
        }
    }

    /**
     * 행을 한 칸씩 아래로 내리고, 새로 들어올 맨 위 행에는 newIndex를 넣어준다.
     */
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
                // scrollOffset이 double이므로, 그리기 시 int로 캐스팅
                int y = row * MP.tileSize - (int) scrollOffset - topBuffer * MP.tileSize;
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