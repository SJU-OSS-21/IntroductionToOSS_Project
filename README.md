# IntroductionToOSS_Project
세종대학교 21학번 오픈소스SW개론(Introduction to OSS) 팀 프로젝트 레포지토리입니다.

- [팀장]소프트웨어학과 21 황재동 `Map, Audio` 
- 소프트웨어학과 21 안강준 `Player, Item`
- 소프트웨어학과 21 양현석 `UI, Scene`
- 소프트웨어학과 21 최영준 `Enemies`

## 컴파일 방법
src/Main 패키지에서 Main class 클릭 후 컴파일 진행

## 메서드 설명
### Player
- Player Class
  - move() method -> 플레이어 이동 함수
  - hit() method
    - 플레이어와 적과의 충돌처리
  - updateInvincible() method
  - getHpRatio() method
  - increasePlayerHP() method
  - decreasePlayerHP() method
  - draw() method
  - update() method
  - getBounds() method
- PlayerPanel Class
  - setEnemyPanel() method
  - startGameThread() method
  - run() method
  - updateGame() method
  - paintComponent() method
  - checkCollisions() method
  - setShotCount() method
  - getshotCount method
- Bullet Class
  - isActive()  method
  - getX() method
  - getY() method
- Item Class
  - PROB_BOMB, PROB_UPGRADE, PROB_HEALTH
  - randomDrop() method
  - applyEffect()  method
  - isActive() method
### Enemy
- Enemy Class
  - init() method
  - draw() method
  - update() method
  - CollsionResolution() method
  - isActive() method
  - getBoudn() method
  - reduceHP() method
  - run() method
- EnemyPanel Class
  - run() method
  - setPlayerPanel() method
### Map_Audio
###UI
