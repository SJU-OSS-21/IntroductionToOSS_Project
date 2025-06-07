package UI_Scene;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Random;

public class UIPlayerPanel extends JPanel {

    private Image playerImage;
    private final int panelWidth;
    private final int panelHeight;

    private int imageW, imageH;
    private Point centerPoint;

    private Timer interpolateTimer;
    private Timer moveTimer;

    private final Random random = new Random();

    // 기본 설정 값
    private static final int INTERPOLATE_DELAY = 16;
    private static final int MIN_MOVE_DISTANCE = 80;
    private static final float SPEED_PER_MS = 0.25f; // 한 ms에 0.25픽셀 이동 (ex. 400px 거리 = 1600ms)

    public UIPlayerPanel(String imagePath, int centerX, int centerY, int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        setOpaque(false);

        loadImage(imagePath);

        imageW = playerImage.getWidth(null);
        imageH = playerImage.getHeight(null);

        centerPoint = new Point(centerX - imageW / 2, centerY - imageH / 2);
        Point startPoint = new Point(centerPoint.x, panelHeight + imageH);

        setBounds(startPoint.x, startPoint.y, imageW, imageH);
        setLocation(startPoint);

        SwingUtilities.invokeLater(() -> startInterpolatedMove(startPoint, centerPoint, this::scheduleNextRandomMove));
    }

    private void loadImage(String path) {
        URL imageUrl = getClass().getClassLoader().getResource(path);
        if (imageUrl == null) throw new IllegalArgumentException("이미지 로딩 실패: " + path);
        playerImage = new ImageIcon(imageUrl).getImage();
    }

    private void startInterpolatedMove(Point from, Point to, Runnable onComplete) {
        double distance = from.distance(to);
        int duration = (int) (distance / SPEED_PER_MS); // 총 시간 (ms)
        int steps = Math.max(1, duration / INTERPOLATE_DELAY);

        final int[] step = {0};
        interpolateTimer = new Timer(INTERPOLATE_DELAY, e -> {
            step[0]++;
            float t = step[0] / (float) steps;
            int x = (int) (from.x + t * (to.x - from.x));
            int y = (int) (from.y + t * (to.y - from.y));
            setLocation(x, y);

            if (step[0] >= steps) {
                interpolateTimer.stop();
                setLocation(to);
                if (onComplete != null) onComplete.run();
            }
        });
        interpolateTimer.start();
    }

    private void scheduleNextRandomMove() {
        int delay = 3000 + random.nextInt(1000); // 3~4초 후 이동
        moveTimer = new Timer(delay, e -> {
            moveTimer.stop();
            Point current = getLocation();
            Point next = generateNewRandomPosition(current);
            startInterpolatedMove(current, next, this::scheduleNextRandomMove);
        });
        moveTimer.setRepeats(false);
        moveTimer.start();
    }

    private Point generateNewRandomPosition(Point current) {
        Point candidate;
        int distance;
        do {
            int newX = random.nextInt(panelWidth - imageW);
            int newY = random.nextInt(panelHeight - imageH);
            candidate = new Point(newX, newY);
            distance = (int) current.distance(candidate);
        } while (distance < MIN_MOVE_DISTANCE);
        return candidate;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (playerImage != null) {
            g.drawImage(playerImage, 0, 0, this);
        }
    }
}
