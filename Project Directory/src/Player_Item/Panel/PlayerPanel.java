package Player_Item.Panel;

import Player_Item.Model.Player;
import main.KeyInputSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerPanel extends JPanel implements ActionListener {
    private final Player player;            // 플레이어
    private final KeyInputSystem input;     // 키 입력 시스템

    // 총알 관련

    PlayerPanel(int width, int height) {
        setOpaque(false);
        setPreferredSize(new Dimension(width, height));

        // 플레이어 생성
        player = new Player("player.png", width/2, height-100);

        // 키보드 입력 설정
        input = new KeyInputSystem();
        setFocusable(true);
        addKeyListener(input);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. 플레이어 그리기
        player.draw(g);

        // 2. 총알 그리기
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 1) 플레이어 이동
        int dx = input.isLeft  ? -1 : input.isRight ? 1 : 0;
        int dy = input.isUp    ? -1 : input.isDown  ? 1 : 0;
        player.move(dx, dy, getWidth(), getHeight());

        // 총알 발사 처리

        // 총알 업데이트 및 비활성 총알 제거

        // 아이템 접촉 시 아이템 제거 및 효과 적용

        repaint();
    }
}
