# IntroductionToOSS_Project
세종대학교 21학번 오픈소스SW개론(Introduction to OSS) 팀 프로젝트 레포지토리입니다.

- [팀장]소프트웨어학과 21 황재동 `Map, Audio` 
- 소프트웨어학과 21 안강준 `Player, Item`
- 소프트웨어학과 21 양현석 `UI, Scene`
- 소프트웨어학과 21 최영준 `Enemies`

## 컴파일 방법
src/Main 패키지에서 Main class 클릭 후 컴파일 진행
📦 클래스 및 주요 메서드 설명

👤 Player 클래스

플레이어 객체를 관리하는 핵심 클래스입니다.
	•	move()
→ 플레이어 이동 처리
	•	hit()
→ 적과 충돌 시 체력 감소 및 무적 처리
	•	updateInvincible()
→ 무적 지속 시간 체크
	•	getHpRatio()
→ 체력 UI 비율 반환
	•	increasePlayerHP()
→ 체력 +1
	•	decreasePlayerHP()
→ 체력 -1
	•	draw(Graphics g)
→ 깜빡임 효과 포함 플레이어 이미지 출력
	•	update()
→ 이동 속도와 방향 반영하여 위치 갱신
	•	getBounds()
→ 충돌 감지를 위한 경계 정보 반환

⸻

🎮 PlayerPanel 클래스

게임 로직과 화면을 포함하는 메인 패널입니다.
	•	setEnemyPanel()
→ 적 패널과 연결
	•	startGameThread()
→ 게임 루프 스레드 시작
	•	run()
→ 타이머 기반 게임 루프 실행
	•	updateGame()
→ 전체 게임 상태 업데이트
	•	paintComponent(Graphics g)
→ 화면에 모든 객체 그리기
	•	checkCollisions()
→ 플레이어-적 충돌 판정
	•	setShotCount(int count)
→ 플레이어 총알 개수 설정
	•	getShotCount()
→ 총알 개수 반환

⸻

💥 Bullet 클래스

총알의 위치 및 상태를 관리합니다.
	•	isActive()
→ 현재 활성 상태 여부 반환
	•	getX() / getY()
→ 현재 위치 좌표 반환

⸻

🎁 Item 클래스

아이템의 생성과 효과 적용을 담당합니다.
	•	PROB_BOMB, PROB_UPGRADE, PROB_HEALTH
→ 각 아이템의 드랍 확률 상수
	•	randomDrop()
→ 확률에 따라 아이템 생성
	•	applyEffect(Player player)
→ 플레이어에게 아이템 효과 적용
	•	getBounds()
→ 아이템 충돌 감지를 위한 경계 반환
	•	isActive()
→ 활성 상태 여부 확인
	•	update()
→ 아이템 위치 및 상태 갱신
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
