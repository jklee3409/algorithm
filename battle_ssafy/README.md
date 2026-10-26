# 배틀싸피 팀 대항전 봇 (3:3)

첨부된 사용자 매뉴얼 v1.1.10 / 운영 안내 PDF / 기본 코드 `Main.java` 만을 기준으로 재설계했다.

## 최종 팀 구성 (시뮬레이션 7,000여 판 중 최고 승률 조합, 71.7%)

| 순서 | 파일 | 역할 |
|---|---|---|
| Player1 | `team/Player1/Main.java` | **SENTRY** – 포탑 구역 밖 좁은 각도 수비, 빠른 보급 후 복귀 |
| Player2 | `team/Player2/Main.java` | **BACKDOOR1** – 메가 3개 확보 후 침투, 거리 3에서 포탑 포격 (유리하면 적 탱크 우선) |
| Player3 | `team/Player3/Main.java` | **HUNTERD** – 메가 2개 확보 후 아군 포탑 근처 적부터 사냥 |

세 파일은 같은 엔진에서 `ROLE` 과 `NICKNAME` 만 다르다. 재생성: `sim/package_team.sh SENTRY BACKDOOR1 HUNTERD 닉1 닉2 닉3`

## 폴더 구성

| 경로 | 내용 |
|---|---|
| `team/Player1~3/Main.java` | **제출용 팀 코드 3개** (역할별, 코드 순서 Player1 > Player2 > Player3) |
| `Main.java` | 공통 엔진 원본 (`Cfg.ROLE`, `Cfg.STRATEGY` 로 역할/전략 선택) |
| `designs/A_blitz, B_fortress, C_adaptive` | 1차 설계안 3종 (3대 모두 같은 코드일 때) |
| `original/Main.java` | 첨부된 기본 코드 (비교용, 수정 없음) |
| `docs/DESIGN.md` | 규칙 분석, 가정, 설계안, 팀 전략, 시뮬레이션 결과 |
| `docs/STRATEGY_MEMO.txt` | 제출용 전략 메모(명단/코드 순서/전략) 초안 |
| `sim/` | 로컬 시뮬레이터 (배틀싸피 서버 흉내) + 리그/분석 스크립트 + 결과 CSV |

## 제출 전 체크리스트
1. 각 `team/PlayerN/Main.java` 의 `NICKNAME` 을 본인 `지역반_이름` 으로 수정 (12자 이내)
2. 파일명 규칙: `Player1_대전3_이종규.java` 처럼 제출 (Java는 클래스명이 `Main` 이므로 실행 환경에서 `Main.java` 로 사용)
3. 코드 순서(Player1 > 2 > 3)를 메모에 기재한 순서와 동일하게 배치 — 역할이 슬롯에 맞춰 설계됨
4. 소켓 통신부(`init/submit/receive/close`)와 `parseData/printData` 는 원본과 바이트 단위로 동일

## 로그 활용 (실전 후 피드백 → 재튜닝)
실행 위치 기준 `battle_logs/` 폴더에 탱크별로 생성된다.

| 파일 | 내용 |
|---|---|
| `battle_<시각>_<닉네임>_<ROLE>_xxxx.log` | 턴별 원본 입력(RAW), 내 상태/역할, 아군/적 위치, 이벤트, 후보 행동 상위 5개 점수와 이유, 최종 선택, 연산 시간 + 게임 요약 |
| `..._xxxx.csv` | 턴별 지표 (위치, 체력, 탄약, 역할, 명령, 노출 피해, 팀 피해량, 이벤트) — 엑셀로 그래프화 |
| `cipher_shift.txt` | 확정된 카이사르 이동값 캐시 (다음 게임에서 먼저 시도) |

로그에서 먼저 볼 이벤트:
- `UNEXPLAINED_DAMAGE` : 적 사선 밖인데 체력 감소 → 페널티/미지 규칙 단서
- `MOVE_FAIL` : 이동 실패 (해당 칸을 진입불가로 학습) → 매뉴얼에 없는 지형 규칙 단서
- `UNKNOWN_SYMBOL` : 매뉴얼에 없는 맵 기호 (예: 포탑 주변 색칠 구역)
- `DECODE_FAIL / DECODE_OK` : 암호 해독 결과, `TREE_SURVIVED` : 나무 내구도 > 1
- 게임 화면의 옐로카드 시점 ↔ 로그의 같은 턴 `CHOICE` 를 대조하면 페널티 조건을 특정할 수 있다

재튜닝은 `Main.java` 의 `Cfg` (역할, 무해사격 주기, 로그 옵션)와 `Weights` (가중치) 값을 바꾸고
`sim/` 으로 검증한 뒤 `sim/package_team.sh` 로 3개 코드를 다시 생성한다.

## 시뮬레이터 사용법 (Linux/macOS, JDK 17+)
```bash
sim/build.sh /tmp/bs                                   # 설계안/상대 봇 컴파일
sim/build_variant.sh /tmp/bs G1 C "" GUARD1            # 역할/가중치 변형 컴파일
cd sim && ./run_league.sh /tmp/bs results/my --bots "mine=/tmp/bs/bots/G1+/tmp/bs/bots/S+/tmp/bs/bots/O,opp=/tmp/bs/bots/D" \
   --mapList practice,gen:mixed:4 --hps 10,100 --orders alt --games 1
python3 analyze.py results/my.csv
java -cp /tmp/bs/sim BattleSim single --a /tmp/bs/bots/C --b /tmp/bs/bots/D --map practice --verbose   # 한 판 상세 재생
```
`run_league.sh` 는 `unshare -rn` 으로 샤드마다 독립 네트워크 네임스페이스를 만들어 8747 포트를 병렬로 쓴다.
