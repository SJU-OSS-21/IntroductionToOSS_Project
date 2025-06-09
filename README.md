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
  - move() method
    - 플레이어 이동 함수
  - hit() method
    - 플레이어와 적과의 충돌처리
  - updateInvincible() method
    - 매 프레임 무적 지속 시간 경과 체크
  - getHpRatio() method
    - 체력 UI바 만들 때 체력 비율 계산 
  - increasePlayerHP() method
    - 플레이어 체력 1 증가
  - decreasePlayerHP() method
    - 플레이어 체력 1 감소
  - draw() method
    - 패널에 이미지 그리기, 깜빡이는 효과 주기
  - update() method
    - 속도와 시간으로 위치 계산
  - getBounds() method
    - 객체 크기 반환
- PlayerPanel Class
  - setEnemyPanel() method
    - 적 패널 참조 저장
  - startGameThread() method
    - 게임 스레드 시작
  - run() method
    - 목표 fps로 게임 update
  - updateGame() method
    - 게임 로직 전체 업데이트
  - paintComponent() method
    - 화면 그리기
  - checkCollisions() method
    - 플레이어와 적 충돌 처리
  - setShotCount() method
    - 충알 개수 setter
  - getshotCount() method
    - 총알 개수 getter
- Bullet Class
  - isActive()  method
    - 활성 여부 확인
  - getX() method
    - x 위치 getter
  - getY() method
    - y 위치 getter
- Item Class
  - PROB_BOMB, PROB_UPGRADE, PROB_HEALTH
    - 각 아이템 드랍 확률
  - randomDrop() method
    - 아이템 랜덤 드랍
  - applyEffect()  method
    - 각 아이템의 효과 적용
  - getBounds()
    - 충돌 판정용 경계 반환
  - isActive() method
    - 활성 여부 반환
  - update()
    - 아이템 이동 업데이트
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
