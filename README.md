# IntroductionToOSS_Project
세종대학교 21학번 오픈소스SW개론(Introduction to OSS) 팀 프로젝트 레포지토리입니다.

- [팀장]소프트웨어학과 21 황재동 `Map, Audio` 
- 소프트웨어학과 21 안강준 `Player, Item`
- 소프트웨어학과 21 양현석 `UI, Scene`
- 소프트웨어학과 21 최영준 `Enemies`

## 컴파일 방법
src/Main 패키지에서 Main class 클릭 후 컴파일 진행

---

## 📦 클래스 및 메서드 설명

### 👤 Player 관련 클래스
플레이어 객체를 제어하는 핵심 클래스입니다.

- `move()`  
  → 플레이어 이동 처리

- `hit()`  
  → 플레이어와 적의 충돌 처리

- `updateInvincible()`  
  → 무적 지속 시간 갱신

- `getHpRatio()`  
  → 체력 비율 반환 (UI 표시용)

- `increasePlayerHP()`  
  → 체력 1 증가

- `decreasePlayerHP()`  
  → 체력 1 감소

- `draw(Graphics g)`  
  → 이미지 출력 및 깜빡임 효과 처리

- `update()`  
  → 이동 속도 및 방향 계산

- `getBounds()`  
  → 충돌 판정용 경계(Rectangle) 반환



### 🎮 PlayerPanel 클래스
게임 루프와 화면 처리를 담당하는 메인 패널입니다.

- `setEnemyPanel()`  
  → 적 패널 참조 연결

- `startGameThread()`  
  → 게임 스레드 시작

- `run()`  
  → 게임 루프 실행 (FPS 기반)

- `updateGame()`  
  → 전체 게임 로직 업데이트

- `paintComponent(Graphics g)`  
  → 그래픽 객체로 화면 출력

- `checkCollisions()`  
  → 플레이어와 적의 충돌 처리

- `setShotCount(int count)`  
  → 총알 개수 설정

- `getShotCount()`  
  → 총알 개수 반환



### 💥 Bullet 클래스
플레이어의 총알 정보를 관리합니다.

- `isActive()`  
  → 총알 활성 상태 확인

- `getX()`, `getY()`  
  → 총알 위치 반환



### 🎁 Item 클래스
아이템의 드랍 및 효과를 담당합니다.

- `PROB_BOMB`, `PROB_UPGRADE`, `PROB_HEALTH`  
  → 아이템 드랍 확률 상수

- `randomDrop()`  
  → 랜덤 아이템 생성

- `applyEffect(Player player)`  
  → 아이템 효과 적용

- `getBounds()`  
  → 충돌 판정용 경계 반환

- `isActive()`  
  → 활성 상태 확인

- `update()`  
  → 아이템 이동 및 상태 갱신  
  
---

## 🚨 Enemy 관련 클래스

### 🧨 Enemy 클래스
- `init()`  
  → 적 객체 초기 설정

- `draw(Graphics g)`  
  → 패널에 이미지 출력

- `update()`  
  → 속도·시간 기반 위치 갱신

- `CollisionResolution()`  
  → 패널 및 총알과의 충돌 처리

- `isActive()`  
  → 삭제 필요 여부 반환

- `getBound()`  
  → 객체 크기 정보 반환

- `reduceHP()`  
  → 체력 감소, 이펙트 및 파괴 처리

- `run()`  
  → 피격 시 이펙트 스레드 실행



### 👾 EnemyPanel 클래스
- `run()`  
  → 적 이동 및 시간 경과에 따른 난이도 상승

- `setPlayerPanel()`  
  → 플레이어 패널 참조 설정

---

## 🎲 Map_Audio 관련 클래스

### TileMapGenerator
- `TileMapGenerator()`  
  → 생성자 초기화 담당

- `initializeTileRows()`  
  → 생성자에서 불러 타일 변경을 위한 배열 초기화

- `getTileImages()`  
  → 리소스에서 타일 이미지 입력

- `updateScroll()`  
  → 타일맵을 움직이게 해주는 주요 메서드

- `shiftRowsDownWithNewIndex()`  
  → 화면 밖으로 타일이 벗어나면 아래로 시프트

- `draw(Graphics2D g2)`  
  → 화면에 타일 그리기



### 🖼 MapPanel

- `MapPanel()`  
  → 생성자: 패널을 만들어 씬 매니저에 전달하는 역할

- `startGameThread()`  
  → 스레드 시작

- `run()`  
  → `tileMapGenerator`의 `updateScroll()` 호출

- `paintComponent(Graphics g)`  
  → 화면에 `tileMapGenerator`의 `draw()` 호출



### 🔊 SoundManager

- `play(int index, float volume)`  
  → 원하는 음원을 재생

- `loop(int index, float volume, boolean continuous, int loopCount)`  
  → 원하는 음원을 연속 재생

- `playOrLoop(...)`  
  → 사용자가 사용할 함수 입력에 따라 play or loop 호출

- `stop(int id)`  
  → id를 이용해 특정 음원 정지

- `stopAll()`  
  → 모든 음원 정지 (재생되고 있는 것 포함)

- `setVolume(Clip clip, float volume)`  
  → 음원을 재생하기 전 볼륨을 설정

---

## 🎲 UI 관련 클래스
### 🧩 BaseScene (Abstract)
- `setScene()`  
  → Scene 속성 설정

- `setGameObjectList()`  
  → Scene 내 객체 등록

- `setUISet()`  
  → UI 구성 요소 설정



### 🌐 MainScene (BaseScene 상속)
- `setScene()`  
  → 화면 크기 설정

- `setGameObjectList()`  
  → 맵, UIPlayerPanel 생성

- `setUISet()`  
  → 버튼 이벤트 등록 및 UI 구성



### 🕹 InGameScene (BaseScene 상속)
- `setScene()`  
  → 화면 크기 설정 및 InGameManager 리셋

- `setGameObjectList()`  
  → 맵, 플레이어, 적 등록

- `setUISet()`  
  → 일시정지 UI 및 리스너 등록



### 🚀 LoadingScene (BaseScene 상속)
- `setScene()`  
  → 화면 크기 및 배경 설정

- `setGameObjectList()`  
  → (없음 또는 필요 시 설정)

- `setUISet()`  
  → 팁 텍스트 및 애니메이션 UI 등록

- `initLoading()`  
  → 로딩 씬 폰트/텍스트 생성



### 💀 GameOverScene (BaseScene 상속)
- `setScene()`  
  → 화면 크기 설정

- `setGameObjectList()`  
  → (필요시 설정)

- `setUISet()`  
  → GameOver UI 구성 및 리스너 등록



### 🧭 SceneManager
- `changeScene()`  
  → 씬 전환 및 초기화 요청

- `innerChangeScene()`  
  → 패널 제거 후 새 씬 초기화 및 출력



### 🖼 UIPanel (Abstract)
- `loadFonts()`  
  → UI용 폰트 로딩

- `drawUI(Graphics g)`  
  → 공통 UI 출력 처리



### 🎯 UIPlayerPanel
- `loadImage()`  
  → Player 이미지 불러오기

- `startInterpolatedMove()`  
  → 부드러운 움직임 구현

- `scheduleNextRandomMove()`  
  → 다음 이동 예약 (3~4초 주기)

- `generateNewRandomPosition()`  
  → 랜덤 위치 계산



### 🎛 MainUIPanel (UIPanel 상속)
- `initUIComponents()`  
  → 타이틀, 버튼 등 초기 설정

- `applyImageToButton()`  
  → 버튼 이미지 크기 조정

- `getStartButton()`  
  → 외부에서 Start 버튼 접근

- `getExitButton()`  
  → 외부에서 Exit 버튼 접근



### ⏱ InGameUIPanel (UIPanel 상속)
- `setScore(int)`  
  → 점수 UI 적용

- `setPlayer()`  
  → 플레이어 등록

- `startTimerUpdate()`  
  → 타이머 시작

- `interpolateHealth()`  
  → 체력 UI 보간 처리

- `drawUI()`  
  → UI 요소 출력



### 🛑 InGamePausePanel (UIPanel 상속)
- `getMainMenuButton()`  
  → 메인 메뉴 버튼 반환

- `getRetryButton()`  
  → 재시작 버튼 반환



### 🧾 GameOverUIPanel (UIPanel 상속)
- `getMainMenuButton()`  
  → 메인 메뉴 버튼 반환

- `getRetryButton()`  
  → 재시작 버튼 반환

- `drawUI()`  
  → 점수 및 시간 출력



### 🧮 GameManager
- `getInstance()`  
  → 싱글톤 인스턴스 반환

- `setMainFrame()`  
  → 메인 프레임 등록

- `getMainFrame()`  
  → 메인 프레임 반환

- `updateScore()`  
  → 점수 갱신

- `getScore()`  
  → 점수 반환

- `getTimer()`  
  → 시간 반환

- `resetScoreAndTimer()`  
  → 점수와 시간 초기화



### 🔁 InGameManager
- `getInstance()`  
  → 싱글톤 인스턴스 반환

- `initialize()`  
  → 객체 생성 및 초기화

- `resetPause()`  
  → 일시정지 변수 초기화

- `reset()`  
  → 글로벌 변수 초기화

- `setPlayer()`  
  → 플레이어 등록

- `isPaused()`  
  → 일시정지 여부 확인

- `getPauseOverlayPanel()`  
  → 배경 패널 반환

- `setupKeyListener()`  
  → ESC 입력 리스너 설정

- `togglePause()`  
  → 일시정지 메뉴 토글

- `setPausePanelVisible()`  
  → 메뉴 UI 표시

- `updateScore()`  
  → 적 처치 시 점수 갱신

