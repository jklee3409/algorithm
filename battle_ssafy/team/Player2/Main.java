import java.net.*;
import java.io.*;
import java.util.*;

public class Main {
    /////////////////////////////////
    // 메인 프로그램 통신 변수 정의
    /////////////////////////////////
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8747;
    private static String ARGS = "";
    private static Socket socket = null;

    ///////////////////////////////
    // 입력 데이터 변수 정의
    ///////////////////////////////
    private static String[][] mapData; // 맵 정보. 예) mapData[0][1] - [0, 1]의 지형/지물
    private static Map<String, String[]> myAllies = new HashMap<>(); // 아군 정보. 예) myAllies['M'] - 플레이어 본인의 정보
    private static Map<String, String[]> enemies = new HashMap<>(); // 적군 정보. 예) enemies['X'] - 적 포탑의 정보
    private static String[] codes; // 주어진 암호문. 예) codes[0] - 첫 번째 암호문
    
    public static void main(String[] args) {
        ARGS = args.length > 0 ? args[0] : "";

        ///////////////////////////////
        // 닉네임 설정 및 최초 연결
        ///////////////////////////////
        String NICKNAME = "대전3_팀원2";
        String gameData = init(NICKNAME);

        ///////////////////////////////
        // 알고리즘 메인 부분 구현 시작
        ///////////////////////////////

        Bot.start(NICKNAME);

        // 최초 데이터 파싱 (연결 실패로 null이면 파싱하지 않음)
        if (gameData != null && gameData.length() > 0) {
            Bot.safeParse(gameData);
        }

        // 반복문: 메인 프로그램 <-> 클라이언트(이 코드) 간 순차로 데이터 송수신(동기 처리)
        while (gameData != null && gameData.length() > 0) {

            // 파싱한 데이터를 화면에 출력하여 확인 (설정으로 끌 수 있음)
            Bot.printInput(gameData);

            // 탱크를 제어할 명령어를 output의 값으로 지정(type: String)
            // Bot.decide 내부의 모든 예외는 잡혀서 안전한 명령("S")으로 대체된다.
            String output = Bot.decide(gameData);

            // 메인 프로그램에서 명령을 처리할 수 있도록 명령어를 submit()의 인자로 전달
            gameData = submit(output);

            // submit()의 리턴으로 받은 갱신된 데이터를 다시 파싱
            if (gameData != null && gameData.length() > 0) {
                Bot.safeParse(gameData);
            }
        }

        // 게임 종료 요약 로그 기록
        Bot.finish();

        ///////////////////////////////
        // 알고리즘 메인 부분 구현 끝
        ///////////////////////////////

        // 반복문을 빠져나왔을 때 메인 프로그램과의 연결을 완전히 해제하기 위해 close() 호출
        close();
    }

    ////////////////////////////////////
    // 알고리즘 함수/메서드 부분 구현 시작
    ////////////////////////////////////

    // =====================================================================
    // [0] 설정 - 실전 피드백 후 여기 값을 바꿔가며 재튜닝
    // =====================================================================
    static final class Cfg {
        // 전략 선택: "A" = Blitz(공격형), "B" = Fortress(수비형), "C" = Adaptive(적응형)
        static final String STRATEGY = "C";

        // 콘솔에 기본 코드처럼 입력 데이터 전체를 출력할지 (-Dbattle.console=off 로 끌 수 있음)
        static final boolean CONSOLE_PRINT_DATA = !"off".equalsIgnoreCase(System.getProperty("battle.console", "on"));
        // 파일 로깅 (-Dbattle.log=off 로 끌 수 있음)
        static final boolean LOG_TO_FILE = !"off".equalsIgnoreCase(System.getProperty("battle.log", "on"));
        // 로그 폴더 (실행 위치 기준 상대 경로)
        static final String LOG_DIR = System.getProperty("battle.logdir", "battle_logs");
        // 매 턴 원본 입력을 로그에 남길지 (리플레이/재분석용)
        static final boolean LOG_RAW_INPUT = true;
        // 로그에 남길 후보 행동 수
        static final int LOG_TOP_CANDIDATES = 5;
        // 매뉴얼에 없는 지형 기호를 이동 가능으로 볼지 (기본: 이동 불가, 포탄 통과)
        static final boolean UNKNOWN_STANDABLE = false;
        // 확정된 암호 이동값을 파일에 저장해 다음 게임에서 먼저 시도할지
        static final boolean CIPHER_CACHE = !"off".equalsIgnoreCase(System.getProperty("battle.cipherCache", "on"));
        // 할 일이 없을 때 보낼 명령
        static final String IDLE_CMD = "S";
        // 가중치 덮어쓰기 (예: "defenders3=1;megaTargetAtt=3;exp=1.2") - 시뮬레이션 튜닝/실전 재튜닝용
        static final String TUNE = "";
        // 팀 내 역할 (3개 코드가 각자 다른 역할을 맡는다)
        //   AUTO      : 아군 위치로 역할 자동 배정 (3대 모두 같은 코드일 때)
        //   GUARD1/2  : 아군 포탑 봉쇄 칸 1번/2번 담당 (여유 있으면 메가 파밍)
        //   STRIKER   : 메가 확보 후 적 포탑 타격
        //   BAIT      : 아군 사선이 덮고 있는 위치로 적을 끌어들이는 유인조
        //   OVERWATCH : 아군/포탑을 노리는 칸을 사선으로 덮는 엄호조 (후미 공격)
        //   HUNTER    : 메가 확보 후 적 탱크 사냥 (2대 격파 승리)
        //   HUNTERD   : 사냥하되 아군 포탑에 가까운 적을 우선
        //   FLEX      : 포탑이 위협받으면 2번 봉쇄 칸 수비, 평소엔 사냥
        //   SENTRY    : 포탑 구역 밖 좁은 각도에서 포탑 사격 지점들을 한 번에 감시 (빠른 보급 후 복귀)
        //   BACKDOOR1/2 : 메가 확보 후 적 사선을 피해 적 포탑의 1번/2번 방향으로 침투 (상황 따라 적 탱크 공격)
        static final String ROLE = "BACKDOOR1";
        // 대기(S)가 연속 N번이면 제자리 무해 사격으로 대체 (옐로카드 '진행 방해' 회피용, 0=끔)
        static final int ANTI_IDLE_TURNS = 3;
        // 포탑 주변 금지 구역 반경 (체비셰프 거리, 2 = 포탑 포함 3x3 색칠 구역). 실전에서 아군 포탑으로 가다가
        // 페널티를 받는 현상 → 구역 진입이 '진행 방해'로 판정되는 것으로 보고 양쪽 포탑 구역 모두 진입 금지 (0=끔)
        static final int TURRET_ZONE = 2;
    }

    // =====================================================================
    // [1] 게임 상수 (매뉴얼 기준)
    // =====================================================================
    static final int[] DR = {-1, 1, 0, 0};          // U D L R
    static final int[] DC = {0, 0, -1, 1};
    static final String[] DN = {"U", "D", "L", "R"};
    static final int RANGE = 3;                       // 포탄 사거리
    static final int DMG_N = 30;                      // 일반 포탄 피해
    static final int DMG_M = 70;                      // 메가 포탄 피해
    static final int SAND_DMG = 10;                   // 모래 진입 피해
    static final int MAX_TURN = 100;
    static final int TANK_MAX_HP = 100;
    static final double INF = 1e15;

    static final int T_STAY = 0, T_MOVE = 1, T_FIRE = 2, T_DECODE = 3;

    // =====================================================================
    // [2] 데이터 구조
    // =====================================================================
    static final class Unit {
        final String label;   // M, M1.., H, E1.., X
        final char kind;      // 'M' 나, 'A' 아군 탱크, 'H' 아군 포탑, 'E' 적 탱크, 'X' 적 포탑
        int r, c, hp;

        Unit(String label, char kind, int r, int c, int hp) {
            this.label = label; this.kind = kind; this.r = r; this.c = c; this.hp = hp;
        }
        Unit copy() { return new Unit(label, kind, r, c, hp); }
        boolean isTank() { return kind == 'M' || kind == 'A' || kind == 'E'; }
        boolean mine() { return kind == 'M' || kind == 'A' || kind == 'H'; }
        public String toString() { return label + "(" + r + "," + c + ")" + hp; }
    }

    static final class Board {
        int H, W;
        char[][] terr;            // 지형 (유닛이 서 있는 칸은 기억된 지형 또는 G)
        Unit[][] at;              // 유닛 점유
        Unit me;
        char dir = 'R';
        int normal, mega;
        final List<Unit> allies = new ArrayList<>();   // 아군 탱크 (나 제외)
        final List<Unit> enemies = new ArrayList<>();  // 적 탱크
        Unit myTurret, enemyTurret;
        List<String> codes = new ArrayList<>();

        boolean inb(int r, int c) { return r >= 0 && c >= 0 && r < H && c < W; }

        Board copy() {
            Board n = new Board();
            n.H = H; n.W = W;
            n.terr = new char[H][];
            for (int i = 0; i < H; i++) n.terr[i] = terr[i].clone();
            n.at = new Unit[H][W];
            n.dir = dir; n.normal = normal; n.mega = mega; n.codes = codes;
            if (me != null) { n.me = me.copy(); n.at[n.me.r][n.me.c] = n.me; }
            for (Unit u : allies) { Unit k = u.copy(); n.allies.add(k); n.at[k.r][k.c] = k; }
            for (Unit u : enemies) { Unit k = u.copy(); n.enemies.add(k); n.at[k.r][k.c] = k; }
            if (myTurret != null) { n.myTurret = myTurret.copy(); n.at[n.myTurret.r][n.myTurret.c] = n.myTurret; }
            if (enemyTurret != null) { n.enemyTurret = enemyTurret.copy(); n.at[n.enemyTurret.r][n.enemyTurret.c] = n.enemyTurret; }
            return n;
        }

        void remove(Unit u) {
            if (at[u.r][u.c] == u) at[u.r][u.c] = null;
            allies.remove(u);
            enemies.remove(u);
            if (myTurret == u) myTurret = null;
            if (enemyTurret == u) enemyTurret = null;
            if (me == u) me = null;
        }

        List<Unit> myTanks() {
            List<Unit> l = new ArrayList<>();
            if (me != null) l.add(me);
            l.addAll(allies);
            return l;
        }

        Unit findAlly(String label) {
            for (Unit u : allies) if (u.label.equals(label)) return u;
            return null;
        }

        Unit findEnemy(String label) {
            for (Unit u : enemies) if (u.label.equals(label)) return u;
            return null;
        }
    }

    // 후보 행동
    static final class Cand {
        int type, d = -1;
        boolean mega;
        String text, cmd;
        int shift = -1;
        double prob;
        double score;
        StringBuilder why = new StringBuilder();
        // 평가 결과
        int nr, nc, sandLoss, hr = -1, hc = -1, nLos;
        char hitKind = 0;
        String hitLabel;
        boolean kill;
        double expIn, hExp;

        Cand(int type, int d) { this.type = type; this.d = d; }

        String label() { return cmd; }
    }

    // 역할 계획
    static final class Plan {
        String role = "ATTACK";
        double[][] pot;            // 목표까지의 비용 거리맵 (작을수록 좋음)
        List<double[]> goals = new ArrayList<>();
        Costs costs;
        int[] guard;               // 수비 역할일 때 담당 봉쇄 칸
        int megaTarget = 0;
        String note = "";
    }

    // 경로 비용 파라미터
    static final class Costs {
        double sand = 2.0;   // 모래 추가 비용 (체력 -10)
        double tree = 1.0;   // 나무 추가 비용 (사격 1턴 + α), 음수면 통과 불가
        double ally = 1.5;   // 아군 탱크 칸 추가 비용 (비켜줄 것으로 가정), 음수면 통과 불가
        double enemy = 6.0;  // 적 탱크 칸 추가 비용, 음수면 통과 불가
        double[][] extra;    // 칸별 추가 비용 (예: 적 사선 회피 침투 경로)
    }

    // 전략별 가중치
    static final class Weights {
        String name = "C-Adaptive";
        double prog = 6;            // 목표 접근 1비용당 점수
        double exp = 1.0;           // 예상 피격 피해 1당 패널티
        double hExp = 1.2;          // 아군 포탑 예상 피해 1당 패널티
        double dmg = 1.0;           // 적 탱크에 준 피해 1당 점수
        double kill = 60;           // 적 탱크 격파 보너스
        double xDmg = 3.0;          // 적 포탑에 준 피해 1당 점수
        double megaUse = 22;        // 메가 포탄 1개 소모 비용
        double sand = 0.5;          // 모래 피해 1당 패널티 (10피해=5점 < 최적 1걸음 6점 → 최적 경로일 때만 모래 진입)
        double osc = 4;             // 직전 칸으로 되돌아가는 진동 패널티
        double duel = 20;           // 결투 승/패 예측 보너스/패널티
        double huntBonus = 8;       // 이기는 결투를 걸 수 있는 위치 보너스
        double huntExpScale = 0.35; // 이기는 결투일 때 노출 패널티 축소 비율
        double death = 120;         // 예상 피해가 내 체력 이상일 때 추가 패널티
        double gameLoss = 5000;     // 내 행동 후 포탑 파괴/팀 패배가 예상될 때 패널티
        double treeShot = 1.0;      // 나무 사격 기본 비용
        double idle = 0.3;          // 대기 패널티 (동점이면 생산적인 행동 우선)
        double adv = 0;             // 후반 거리 판정용 전진 가중치 (단계별로 조정)
        double decodeScale = 1.0;   // 해독 가치 배율
        double goalDanger = 2.5;    // 적 사선 안에 있는 목표 칸 추가 비용
        double threatTarget = 1.3;  // 나/포탑을 노리는 적을 쏠 때 피해 가중
        boolean hunt = true;        // 사냥(메가 우위 시 사선 진입) 허용
        boolean farmAtt = true;     // 공격 역할의 초반 메가 파밍
        boolean farmDef = true;     // 수비 역할의 여유 시간 메가 파밍
        boolean dynamicDef = true;  // 위협 기반으로 수비 위치 이탈 허용
        boolean hunterRole = false; // 공격 역할이 포탑 대신 적 탱크를 추적 (시뮬레이션용 상대 D)
        int defenders3 = 2;         // 탱크 3대일 때 수비 인원
        int defenders2 = 1;         // 탱크 2대일 때 수비 인원
        int megaTargetAtt = 2;      // 공격 역할 메가 목표
        int megaTargetDef = 3;      // 수비 역할 메가 목표
        int farmDeadline = 30;      // 공격 역할 파밍 허용 턴
        int farmMaxDist = 8;        // 파밍하러 갈 최대 거리 (공격 역할)
        int farmMaxDistDef = 8;     // 파밍하러 갈 최대 거리 (수비 역할, ETA 조건이 안전 보장)
        int pinnedTurns = 0;        // 공격 정체+포탑 위협 시 지원 복귀 턴 수 (0=끔, 시뮬레이션상 끄는 편이 승률 +4%p)
        int defSlack = 3;           // 수비 복귀 여유 턴
        double sandCost = 5.0, treeCost = 1.0;   // 경로 탐색 시 모래/나무 추가 비용 (턴 환산)

        Weights copy() {
            Weights w = new Weights();
            w.name = name; w.prog = prog; w.exp = exp; w.hExp = hExp; w.dmg = dmg; w.kill = kill; w.xDmg = xDmg;
            w.megaUse = megaUse; w.sand = sand; w.osc = osc; w.duel = duel; w.huntBonus = huntBonus;
            w.huntExpScale = huntExpScale; w.death = death; w.gameLoss = gameLoss; w.treeShot = treeShot; w.idle = idle; w.adv = adv;
            w.decodeScale = decodeScale; w.goalDanger = goalDanger; w.threatTarget = threatTarget;
            w.hunt = hunt; w.farmAtt = farmAtt; w.farmDef = farmDef; w.dynamicDef = dynamicDef; w.hunterRole = hunterRole;
            w.defenders3 = defenders3; w.defenders2 = defenders2; w.megaTargetAtt = megaTargetAtt;
            w.megaTargetDef = megaTargetDef; w.farmDeadline = farmDeadline; w.farmMaxDist = farmMaxDist;
            w.farmMaxDistDef = farmMaxDistDef; w.pinnedTurns = pinnedTurns;
            w.defSlack = defSlack; w.sandCost = sandCost; w.treeCost = treeCost;
            return w;
        }

        static Weights forStrategy(String s) {
            Weights w = new Weights();
            if ("A".equals(s)) {
                // 설계안 A - Blitz: 3대 모두 적 포탑으로 돌진, 위험 회피 낮음, 파밍 없음
                w.name = "A-Blitz";
                w.prog = 8; w.exp = 0.3; w.hExp = 0.3; w.duel = 0; w.hunt = false;
                w.farmAtt = false; w.farmDef = false; w.dynamicDef = false;
                w.defenders3 = 0; w.defenders2 = 0; w.megaTargetAtt = 0; w.megaTargetDef = 0;
                w.goalDanger = 0; w.death = 40; w.treeCost = 0.5;
            } else if ("B".equals(s)) {
                // 설계안 B - Fortress: 2대 포탑 봉쇄 고정, 1대는 메가 4개 파밍 후 신중한 공격
                w.name = "B-Fortress";
                w.exp = 1.6; w.hExp = 2.0; w.dynamicDef = false; w.farmDef = false; w.farmAtt = true;
                w.defenders3 = 2; w.defenders2 = 1; w.megaTargetAtt = 4; w.farmDeadline = 45;
                w.farmMaxDist = 12; w.goalDanger = 5;
            } else if ("D".equals(s)) {
                // 시뮬레이션 상대 D - Hunter: 메가 2개 확보 후 적 탱크 사냥 (2대 격파 승리 노림)
                w.name = "D-Hunter";
                w.exp = 0.6; w.hExp = 0.5; w.huntExpScale = 0.2; w.hunterRole = true;
                w.defenders3 = 0; w.defenders2 = 0; w.farmDef = false; w.dynamicDef = false;
                w.megaTargetAtt = 2; w.farmDeadline = 25; w.goalDanger = 1;
            } else {
                // 설계안 C - Adaptive: 위협 기반 역할 재배정 + 결투/사냥 + 후반 판정 대응
                w.name = "C-Adaptive";
            }
            applyRole(w, Cfg.ROLE);
            applyTune(w, Cfg.TUNE);
            return w;
        }

        // 역할별 성향 보정
        static void applyRole(Weights w, String role) {
            if (role == null || "AUTO".equals(role)) return;
            w.name = w.name + "/" + role;
            if ("SENTRY".equals(role)) {
                w.megaTargetDef = 2; w.farmMaxDistDef = 10; w.hExp = 1.5;
            } else if (role.startsWith("BACKDOOR")) {
                w.megaTargetAtt = 3; w.farmMaxDist = 10; w.farmDeadline = 35; w.goalDanger = 3.0;
            } else if (role.startsWith("GUARD")) {
                w.megaTargetDef = 3;
            } else if ("STRIKER".equals(role)) {
                w.megaTargetAtt = 2;
            } else if ("BAIT".equals(role)) {
                w.exp = 0.9; w.hunt = true; w.huntExpScale = 0.3; w.huntBonus = 12;
                w.megaTargetAtt = 2; w.goalDanger = 1.0;
            } else if ("OVERWATCH".equals(role)) {
                w.exp = 1.2; w.megaTargetAtt = 2; w.goalDanger = 3.0;
            } else if ("HUNTER".equals(role) || "HUNTERD".equals(role) || "FLEX".equals(role)) {
                w.exp = 0.6; w.hExp = 0.8; w.huntExpScale = 0.2; w.megaTargetAtt = 2;
                w.farmDeadline = 25; w.goalDanger = 1;
            }
        }

        // "이름=값;이름=값" 형식으로 필드 덮어쓰기 (잘못된 항목은 무시)
        static void applyTune(Weights w, String tune) {
            if (tune == null || tune.trim().isEmpty()) return;
            for (String kv : tune.split(";")) {
                String[] p = kv.split("=");
                if (p.length != 2) continue;
                try {
                    java.lang.reflect.Field f = Weights.class.getDeclaredField(p[0].trim());
                    f.setAccessible(true);
                    String v = p[1].trim();
                    if (f.getType() == double.class) f.setDouble(w, Double.parseDouble(v));
                    else if (f.getType() == int.class) f.setInt(w, Integer.parseInt(v));
                    else if (f.getType() == boolean.class) f.setBoolean(w, Boolean.parseBoolean(v));
                } catch (Throwable t) { /* 무시 */ }
            }
            w.name = w.name + "[" + tune + "]";
        }
    }

    // =====================================================================
    // [3] 진입점
    // =====================================================================
    static final class Bot {
        static boolean parseOk = false;
        static String lastParseError = null;

        static void start(String nick) {
            Mem.startMs = System.currentTimeMillis();
            Log.open(nick, Weights.forStrategy(Cfg.STRATEGY).name);
            Cipher.loadCache();
        }

        static void safeParse(String gameData) {
            try {
                parseData(gameData);
                parseOk = true;
                lastParseError = null;
            } catch (Throwable t) {
                parseOk = false;
                lastParseError = t.toString();
            }
        }

        static void printInput(String gameData) {
            if (!Cfg.CONSOLE_PRINT_DATA) return;
            try { printData(gameData); } catch (Throwable t) { /* 출력 실패는 무시 */ }
        }

        static String decide(String raw) {
            long t0 = System.nanoTime();
            String cmd = Cfg.IDLE_CMD;
            try {
                if (!parseOk) {
                    Log.line("[WARN] 입력 파싱 실패 → 대기: " + lastParseError);
                    Log.line("RAW: " + oneLine(raw));
                    Mem.turn++;
                    return cmd;
                }
                cmd = Brain.think(raw, t0);
            } catch (Throwable e) {
                Log.error("decide", e);
                cmd = Cfg.IDLE_CMD;
            }
            if (cmd == null || cmd.trim().isEmpty()) cmd = Cfg.IDLE_CMD;
            return cmd;
        }

        static void finish() {
            try { Brain.summary(); } catch (Throwable e) { /* 무시 */ }
            Log.close();
        }
    }

    // =====================================================================
    // [4] 메모리 (턴 간 유지되는 정보 + 통계)
    // =====================================================================
    static final class Mem {
        static int turn = 0;
        static int myIndex = 0;
        static String myLabel = "M?";
        static char[][] lastTerr;
        static boolean[][] blocked;     // 이동 실패로 학습한 진입 불가 칸
        static boolean[][] shellBlock;  // 사격 결과로 학습한 포탄 차단 칸
        static Board prev;              // 직전 턴 (행동 전) 보드
        static Cand prevAct;            // 직전 턴 행동
        static String prevRole = "";
        static double attackBest = INF;  // 공격 역할일 때 지금까지 가장 가까웠던 목표 거리
        static int attackStall = 0;      // 목표 거리 개선이 없던 턴 수
        static boolean supportMode = false;
        static boolean flexGuarding = false;
        static final ArrayList<int[]> posHist = new ArrayList<>();
        static final Map<String, Integer> enemyMegaEst = new HashMap<>();
        static final Map<String, int[]> enemyLastPos = new HashMap<>();
        static final Map<String, Integer> enemyLastHp = new HashMap<>();
        static final Map<String, Integer> allyLastHp = new HashMap<>();
        static final Set<String> enemySeen = new TreeSet<>();
        static final Set<String> allySeen = new TreeSet<>();
        static final Set<String> unknownSyms = new TreeSet<>();
        static int myTurretMax = -1, enemyTurretMax = -1;
        static int enemyTanksInit = 0, myTanksInit = 0;
        static long startMs;
        // 통계
        static int shots, megaShots, shotHits, kills, treesShot, decodeTry, decodeOk, megaGained;
        static int moveFail, idleTurns, dmgEnemyFire, dmgSand, dmgUnexplained, errors;
        static double maxThinkMs, sumThinkMs;
        static int idleStreak = 0, idleFires = 0;
        static final Map<String, Integer> roleCount = new TreeMap<>();
        static final List<String> events = new ArrayList<>();

        static int megaEst(String label) {
            Integer v = enemyMegaEst.get(label);
            return v == null ? 0 : v;
        }

        static void noteUnknown(String sym) {
            if (unknownSyms.add(sym)) events.add("UNKNOWN_SYMBOL '" + sym + "' (이동 불가/포탄 통과로 처리)");
        }

        // 피해 판정용: 우리 팀 누적 피해 (탱크 + 포탑)
        static int myTeamDamage(Board b) {
            int d = 0;
            for (String l : allySeen) {
                Unit u = l.equals(myLabel) ? b.me : b.findAlly(l);
                d += (u == null) ? TANK_MAX_HP : Math.max(0, TANK_MAX_HP - u.hp);
            }
            if (myTurretMax > 0) d += myTurretMax - (b.myTurret == null ? 0 : b.myTurret.hp);
            return d;
        }

        static int enemyTeamDamage(Board b) {
            int d = 0;
            for (String l : enemySeen) {
                Unit u = b.findEnemy(l);
                d += (u == null) ? TANK_MAX_HP : Math.max(0, TANK_MAX_HP - u.hp);
            }
            if (enemyTurretMax > 0) d += enemyTurretMax - (b.enemyTurret == null ? 0 : b.enemyTurret.hp);
            return d;
        }

        // 직전 행동 결과 확인, 적 추적, 학습
        static void observe(Board b) {
            events.clear();
            if (turn == 1) {
                Set<Integer> used = new HashSet<>();
                for (Unit a : b.allies) {
                    int k = digit(a.label);
                    if (k > 0) used.add(k);
                }
                int teamSize = b.allies.size() + 1;
                for (int k = 1; k <= Math.max(3, teamSize); k++) {
                    if (!used.contains(k)) { myIndex = k; break; }
                }
                myLabel = "M" + myIndex;
                myTanksInit = teamSize;
                enemyTanksInit = b.enemies.size();
                events.add("START index=" + myIndex + " team=" + teamSize + " vs " + enemyTanksInit
                        + " map=" + b.H + "x" + b.W);
            }
            if (b.enemies.size() > enemyTanksInit) enemyTanksInit = b.enemies.size();
            allySeen.add(myLabel);
            for (Unit a : b.allies) allySeen.add(a.label);
            for (Unit e : b.enemies) enemySeen.add(e.label);
            if (b.myTurret != null) myTurretMax = Math.max(myTurretMax, b.myTurret.hp);
            if (b.enemyTurret != null) enemyTurretMax = Math.max(enemyTurretMax, b.enemyTurret.hp);

            // 적 추적: 보급시설 옆에서 멈춰 있으면 해독 중으로 보고 메가 보유 추정
            for (Unit e : b.enemies) {
                int[] lp = enemyLastPos.get(e.label);
                if (lp != null && lp[0] == e.r && lp[1] == e.c && Geo.adjF(b, e.r, e.c)) {
                    int v = Math.min(5, megaEst(e.label) + 1);
                    enemyMegaEst.put(e.label, v);
                    events.add(e.label + " 보급시설 옆 정지 → 메가 추정 " + v);
                }
                enemyLastPos.put(e.label, new int[]{e.r, e.c});
                Integer lh = enemyLastHp.get(e.label);
                if (lh != null && lh > e.hp) events.add(e.label + " 체력 " + lh + "→" + e.hp);
                enemyLastHp.put(e.label, e.hp);
            }
            for (Iterator<Map.Entry<String, Integer>> it = enemyLastHp.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Integer> en = it.next();
                if (b.findEnemy(en.getKey()) == null) { events.add("적 " + en.getKey() + " 격파됨"); it.remove(); }
            }
            for (Unit a : b.allies) {
                Integer lh = allyLastHp.get(a.label);
                if (lh != null && lh > a.hp) events.add("아군 " + a.label + " 체력 " + lh + "→" + a.hp);
                allyLastHp.put(a.label, a.hp);
            }
            for (Iterator<Map.Entry<String, Integer>> it = allyLastHp.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Integer> en = it.next();
                if (b.findAlly(en.getKey()) == null) { events.add("아군 " + en.getKey() + " 격파됨"); it.remove(); }
            }
            if (prev != null && prev.myTurret != null && b.myTurret != null && b.myTurret.hp < prev.myTurret.hp)
                events.add("아군 포탑 피격 " + prev.myTurret.hp + "→" + b.myTurret.hp);

            if (b.me != null) posHist.add(new int[]{b.me.r, b.me.c});
            if (posHist.size() > 12) posHist.remove(0);

            if (prev == null || prevAct == null || prev.me == null || b.me == null) return;

            // 1) 이동 결과
            boolean moved = b.me.r != prev.me.r || b.me.c != prev.me.c;
            if (prevAct.type == T_MOVE) {
                if (!moved) {
                    moveFail++;
                    if (prevAct.nr >= 0 && b.inb(prevAct.nr, prevAct.nc) && b.at[prevAct.nr][prevAct.nc] == null) {
                        blocked[prevAct.nr][prevAct.nc] = true;
                        events.add("MOVE_FAIL " + prevAct.cmd + " → (" + prevAct.nr + "," + prevAct.nc
                                + ") 지형'" + b.terr[prevAct.nr][prevAct.nc] + "' 진입불가로 학습");
                    } else {
                        events.add("MOVE_FAIL " + prevAct.cmd);
                    }
                } else if (b.me.r != prevAct.nr || b.me.c != prevAct.nc) {
                    events.add("MOVE_MISMATCH 예상(" + prevAct.nr + "," + prevAct.nc + ") 실제(" + b.me.r + "," + b.me.c + ")");
                }
            } else if (moved) {
                events.add("UNEXPECTED_MOVE (" + prev.me.r + "," + prev.me.c + ")→(" + b.me.r + "," + b.me.c + ")");
            }

            // 2) 체력 변화 원인 추정
            int dHp = prev.me.hp - b.me.hp;
            int expectSand = (prevAct.type == T_MOVE && moved && prevAct.sandLoss > 0) ? SAND_DMG : 0;
            if (dHp > 0) {
                int sand = Math.min(dHp, expectSand);
                dmgSand += sand;
                int rest = dHp - sand;
                if (sand > 0) events.add("모래 피해 -" + sand);
                if (rest > 0) {
                    boolean losNow = false;
                    for (Unit e : b.enemies) if (Geo.los(b, e.r, e.c, b.me.r, b.me.c)) losNow = true;
                    if (prevAct.nLos > 0 || losNow) {
                        dmgEnemyFire += rest;
                        events.add("피격 -" + rest + " (예상 노출 " + fmt(prevAct.expIn) + ")");
                    } else {
                        dmgUnexplained += rest;
                        events.add("UNEXPLAINED_DAMAGE -" + rest + " (사선 밖 피해: 페널티/미지 규칙 가능성)");
                    }
                }
            } else if (dHp < 0) {
                events.add("체력 증가 +" + (-dHp));
            }

            // 3) 해독 결과
            if (prevAct.type == T_DECODE) {
                if (b.mega > prev.mega) {
                    decodeOk++; megaGained += b.mega - prev.mega;
                    Cipher.onResult(prevAct.shift, true);
                    events.add("DECODE_OK shift=" + prevAct.shift + " 메가 " + prev.mega + "→" + b.mega);
                } else {
                    Cipher.onResult(prevAct.shift, false);
                    events.add("DECODE_FAIL shift=" + prevAct.shift + " 평문=" + prevAct.text);
                }
            } else if (b.mega > prev.mega) {
                megaGained += b.mega - prev.mega;
                events.add("메가 증가(해독 외) " + prev.mega + "→" + b.mega);
            }

            // 4) 사격 결과
            if (prevAct.type == T_FIRE) {
                if (prevAct.hitKind == 'T' && b.inb(prevAct.hr, prevAct.hc)) {
                    if (b.terr[prevAct.hr][prevAct.hc] == 'T') events.add("TREE_SURVIVED (" + prevAct.hr + "," + prevAct.hc + ") 나무 내구도>1?");
                } else if (prevAct.hitKind == 'X') {
                    if (b.enemyTurret != null && prev.enemyTurret != null && b.enemyTurret.hp >= prev.enemyTurret.hp) {
                        events.add("TURRET_SHOT_NO_EFFECT 적 포탑 체력 변화 없음");
                    } else shotHits++;
                } else if (prevAct.hitKind == 'E') {
                    Unit now = b.findEnemy(prevAct.hitLabel);
                    Unit before = prev.findEnemy(prevAct.hitLabel);
                    if (now == null) { shotHits++; kills++; events.add("KILL " + prevAct.hitLabel); }
                    else if (before != null && now.hp >= before.hp) events.add("SHOT_NO_EFFECT " + prevAct.hitLabel);
                    else shotHits++;
                }
            }
        }
    }

    // =====================================================================
    // [5] 입력 → 보드 변환
    // =====================================================================
    static Board buildBoard() {
        Board b = new Board();
        int H = mapData == null ? 0 : mapData.length;
        int W = (H > 0 && mapData[0] != null) ? mapData[0].length : 0;
        b.H = H; b.W = W;
        b.terr = new char[H][W];
        b.at = new Unit[H][W];
        boolean memOk = Mem.lastTerr != null && Mem.lastTerr.length == H && (H == 0 || Mem.lastTerr[0].length == W);
        if (!memOk) {
            Mem.lastTerr = new char[H][W];
            Mem.blocked = new boolean[H][W];
            Mem.shellBlock = new boolean[H][W];
        }
        List<Unit> allyTanks = new ArrayList<>(), enemyTanks = new ArrayList<>();
        for (int r = 0; r < H; r++) {
            for (int c = 0; c < W; c++) {
                String t = mapData[r][c];
                t = (t == null) ? "" : t.trim();
                char under = Mem.lastTerr[r][c] != 0 ? Mem.lastTerr[r][c] : 'G';
                if (t.length() == 1 && "GSWTRF".indexOf(t.charAt(0)) >= 0) {
                    b.terr[r][c] = t.charAt(0);
                    Mem.lastTerr[r][c] = t.charAt(0);
                } else if (t.equals("M")) {
                    b.terr[r][c] = under;
                    b.me = new Unit("M", 'M', r, c, TANK_MAX_HP);
                    b.at[r][c] = b.me;
                    Mem.blocked[r][c] = false;
                } else if (t.equals("H")) {
                    b.terr[r][c] = under;
                    b.myTurret = new Unit("H", 'H', r, c, 0);
                    b.at[r][c] = b.myTurret;
                } else if (t.equals("X")) {
                    b.terr[r][c] = under;
                    b.enemyTurret = new Unit("X", 'X', r, c, 0);
                    b.at[r][c] = b.enemyTurret;
                } else if (t.length() >= 2 && t.charAt(0) == 'M') {
                    b.terr[r][c] = under;
                    Unit u = new Unit(t, 'A', r, c, TANK_MAX_HP);
                    allyTanks.add(u);
                    b.at[r][c] = u;
                    Mem.blocked[r][c] = false;
                } else if (t.length() >= 2 && t.charAt(0) == 'E') {
                    b.terr[r][c] = under;
                    Unit u = new Unit(t, 'E', r, c, TANK_MAX_HP);
                    enemyTanks.add(u);
                    b.at[r][c] = u;
                    Mem.blocked[r][c] = false;
                } else {
                    b.terr[r][c] = '?';
                    Mem.noteUnknown(t.isEmpty() ? "(빈칸)" : t);
                }
            }
        }
        b.allies.addAll(allyTanks);
        b.enemies.addAll(enemyTanks);

        String[] mv = myAllies.get("M");
        if (b.me != null && mv != null) {
            b.me.hp = toInt(mv, 0, TANK_MAX_HP);
            b.dir = (mv.length > 1 && mv[1] != null && mv[1].trim().length() > 0) ? mv[1].trim().charAt(0) : 'R';
            b.normal = toInt(mv, 2, 0);
            b.mega = toInt(mv, 3, 0);
        }
        for (Unit u : b.allies) u.hp = toInt(myAllies.get(u.label), 0, TANK_MAX_HP);
        if (b.myTurret != null) b.myTurret.hp = toInt(myAllies.get("H"), 0, 100);
        for (Unit u : b.enemies) u.hp = toInt(Main.enemies.get(u.label), 0, TANK_MAX_HP);
        if (b.enemyTurret != null) b.enemyTurret.hp = toInt(Main.enemies.get("X"), 0, 100);
        // 체력 0 이하로 표기된 유닛은 제거
        for (Unit u : new ArrayList<>(b.allies)) if (u.hp <= 0) b.remove(u);
        for (Unit u : new ArrayList<>(b.enemies)) if (u.hp <= 0) b.remove(u);
        if (Main.codes != null) {
            for (String s : Main.codes) if (s != null && s.trim().length() > 0) b.codes.add(s.trim());
        }
        return b;
    }

    // =====================================================================
    // [6] 기하 / 사선 / 경로 탐색
    // =====================================================================
    static final class Geo {
        static boolean standableTerr(char t) {
            return t == 'G' || t == 'S' || (t == '?' && Cfg.UNKNOWN_STANDABLE);
        }

        static boolean standable(Board b, int r, int c) {
            return b.inb(r, c) && b.at[r][c] == null && standableTerr(b.terr[r][c]) && !Mem.blocked[r][c] && zoneOk(b, r, c);
        }

        // 포탑까지의 체비셰프 거리 (포탑이 없으면 큰 값)
        static int zoneDepth(Board b, int r, int c) {
            int best = 99;
            Unit[] ts = {b.myTurret, b.enemyTurret};
            for (Unit t : ts) {
                if (t == null) continue;
                int d = Math.max(Math.abs(t.r - r), Math.abs(t.c - c));
                if (d >= 1) best = Math.min(best, d);
            }
            return best;
        }

        static boolean inZone(Board b, int r, int c) {
            return Cfg.TURRET_ZONE > 0 && zoneDepth(b, r, c) <= Cfg.TURRET_ZONE;
        }

        // 구역 칸은 진입 금지. 단, 이미 구역 안이면 포탑에서 멀어지는 방향만 허용 (탈출)
        static boolean zoneOk(Board b, int r, int c) {
            if (!inZone(b, r, c)) return true;
            if (b.me == null || !inZone(b, b.me.r, b.me.c)) return false;
            return zoneDepth(b, r, c) > zoneDepth(b, b.me.r, b.me.c);
        }

        // 지형만 보았을 때 포탄이 통과하는가 (유닛 제외)
        static boolean shellPass(Board b, int r, int c) {
            char t = b.terr[r][c];
            if (t == 'T' || t == 'R' || t == 'F') return false;
            return !Mem.shellBlock[r][c];
        }

        // 포탄 궤적: {r, c, type} type 0=허공, 1=유닛, 2=나무, 3=단단한 장애물
        static int[] cast(Board b, int r, int c, int d) {
            for (int k = 1; k <= RANGE; k++) {
                int nr = r + DR[d] * k, nc = c + DC[d] * k;
                if (!b.inb(nr, nc)) return new int[]{-1, -1, 0};
                if (b.at[nr][nc] != null) return new int[]{nr, nc, 1};
                char t = b.terr[nr][nc];
                if (t == 'T') return new int[]{nr, nc, 2};
                if (!shellPass(b, nr, nc)) return new int[]{nr, nc, 3};
            }
            return new int[]{-1, -1, 0};
        }

        // (r1,c1)에서 쏜 포탄이 (r2,c2)에 닿는가 (중간 칸만 검사)
        static boolean los(Board b, int r1, int c1, int r2, int c2) {
            if (r1 != r2 && c1 != c2) return false;
            int dist = Math.abs(r1 - r2) + Math.abs(c1 - c2);
            if (dist < 1 || dist > RANGE) return false;
            int sr = Integer.signum(r2 - r1), sc = Integer.signum(c2 - c1);
            for (int k = 1; k < dist; k++) {
                int r = r1 + sr * k, c = c1 + sc * k;
                if (b.at[r][c] != null) return false;
                if (!shellPass(b, r, c)) return false;
            }
            return true;
        }

        static boolean adjF(Board b, int r, int c) {
            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (b.inb(nr, nc) && b.terr[nr][nc] == 'F') return true;
            }
            return false;
        }

        static int manhattan(int r1, int c1, int r2, int c2) {
            return Math.abs(r1 - r2) + Math.abs(c1 - c2);
        }

        static double terrCost(char t, Costs k) {
            if (t == 'G') return 1;
            if (t == 'S') return 1 + k.sand;
            if (t == 'T') return k.tree < 0 ? INF : 2 + k.tree;
            if (t == '?' && Cfg.UNKNOWN_STANDABLE) return 1;
            return INF;
        }

        // (r,c) 칸에 들어가는 비용
        static double enterCost(Board b, int r, int c, Costs k) {
            if (!b.inb(r, c)) return INF;
            Unit u = b.at[r][c];
            double base = terrCost(b.terr[r][c], k);
            if (u != null) {
                if (u.kind == 'M') return base;
                if (u.kind == 'A') return k.ally < 0 ? INF : base + k.ally;
                if (u.kind == 'E') return k.enemy < 0 ? INF : base + k.enemy;
                return INF;
            }
            if (Mem.blocked[r][c]) return INF;
            if (inZone(b, r, c)) return INF;
            if (k.extra != null) base += k.extra[r][c];
            return base;
        }

        // 다중 목표 역방향 다익스트라: dist[r][c] = (r,c)에서 가장 가까운 목표까지 비용 + 목표 오프셋
        static double[][] dist(Board b, List<double[]> goals, Costs k) {
            double[][] d = new double[b.H][b.W];
            for (double[] row : d) Arrays.fill(row, INF);
            PriorityQueue<double[]> pq = new PriorityQueue<>(64, (x, y) -> Double.compare(x[0], y[0]));
            for (double[] g : goals) {
                int r = (int) g[0], c = (int) g[1];
                if (!b.inb(r, c) || enterCost(b, r, c, k) >= INF) continue;
                if (g[2] < d[r][c]) { d[r][c] = g[2]; pq.add(new double[]{g[2], r, c}); }
            }
            while (!pq.isEmpty()) {
                double[] cur = pq.poll();
                int r = (int) cur[1], c = (int) cur[2];
                if (cur[0] > d[r][c]) continue;
                double ec = enterCost(b, r, c, k);
                if (ec >= INF) continue;
                for (int dd = 0; dd < 4; dd++) {
                    int nr = r + DR[dd], nc = c + DC[dd];
                    if (!b.inb(nr, nc) || enterCost(b, nr, nc, k) >= INF) continue;
                    double nd = cur[0] + ec;
                    if (nd < d[nr][nc]) { d[nr][nc] = nd; pq.add(new double[]{nd, nr, nc}); }
                }
            }
            return d;
        }

        // target을 쏠 수 있는 칸들 {r, c, 사이 나무 수}
        static List<double[]> attackCells(Board b, Unit target, double treeTurn) {
            List<double[]> res = new ArrayList<>();
            if (target == null) return res;
            for (int d = 0; d < 4; d++) {
                int trees = 0;
                for (int k = 1; k <= RANGE; k++) {
                    int r = target.r + DR[d] * k, c = target.c + DC[d] * k;
                    if (!b.inb(r, c)) break;
                    char t = b.terr[r][c];
                    if (t == 'R' || t == 'F' || Mem.shellBlock[r][c]) break;
                    if (t == 'T') { trees++; continue; }
                    Unit u = b.at[r][c];
                    if (u != null && !u.isTank()) break;
                    if (standableTerr(t) && !Mem.blocked[r][c] && !inZone(b, r, c))
                        res.add(new double[]{r, c, trees * treeTurn + (RANGE - k) * 0.4});   // 최대 사거리 선호
                }
            }
            return res;
        }

        // 아군 포탑 봉쇄 칸: 열린 방향마다 포탑에서 가장 가까운 설 수 있는 칸 {r, c, dir}
        static List<int[]> guardCells(Board b) {
            List<int[]> res = new ArrayList<>();
            Unit h = b.myTurret;
            if (h == null) return res;
            for (int d = 0; d < 4; d++) {
                for (int k = 1; k <= RANGE; k++) {
                    int r = h.r + DR[d] * k, c = h.c + DC[d] * k;
                    if (!b.inb(r, c)) break;
                    char t = b.terr[r][c];
                    if (t == 'R' || t == 'F' || t == 'T' || Mem.shellBlock[r][c]) break;
                    if (standableTerr(t) && !Mem.blocked[r][c] && !inZone(b, r, c)) { res.add(new int[]{r, c, d}); break; }
                }
            }
            return res;
        }

        // 보급시설과 인접한 설 수 있는 칸
        static List<double[]> farmCells(Board b, double dangerW) {
            List<double[]> res = new ArrayList<>();
            for (int r = 0; r < b.H; r++) for (int c = 0; c < b.W; c++) {
                if (b.terr[r][c] != 'F') continue;
                for (int d = 0; d < 4; d++) {
                    int nr = r + DR[d], nc = c + DC[d];
                    if (!b.inb(nr, nc) || !standableTerr(b.terr[nr][nc]) || Mem.blocked[nr][nc] || inZone(b, nr, nc)) continue;
                    Unit u = b.at[nr][nc];
                    if (u != null && !u.isTank()) continue;
                    if (u != null && u.kind == 'E') continue;
                    double off = dangerW * losCount(b, nr, nc);
                    if (b.myTurret != null) off += 0.05 * manhattan(nr, nc, b.myTurret.r, b.myTurret.c);
                    res.add(new double[]{nr, nc, off});
                }
            }
            return res;
        }

        // 현재 (r,c)를 사선에 둔 적 탱크 수
        static int losCount(Board b, int r, int c) {
            int n = 0;
            for (Unit e : b.enemies) if (los(b, e.r, e.c, r, c)) n++;
            return n;
        }

        static List<double[]> single(int r, int c) {
            List<double[]> l = new ArrayList<>();
            l.add(new double[]{r, c, 0});
            return l;
        }

        // 적 탱크가 아군 포탑을 쏠 수 있는 칸까지 걸리는 최소 턴 (위협도)
        static double enemyEtaToTurret(Board b) {
            if (b.myTurret == null || b.enemies.isEmpty()) return INF;
            Costs k = new Costs();
            k.sand = 0.3; k.tree = 0; k.ally = 2; k.enemy = 0;
            double[][] d = dist(b, attackCells(b, b.myTurret, 1.0), k);
            double best = INF;
            for (Unit e : b.enemies) best = Math.min(best, d[e.r][e.c]);
            return best;
        }
    }

    // =====================================================================
    // [7] 카이사르 암호 해독 (빈도 분석 + 실패한 이동값 배제 + 성공값 확정)
    // =====================================================================
    static final class Cipher {
        static final double[] FREQ = {8.17, 1.49, 2.78, 4.25, 12.70, 2.23, 2.02, 6.09, 6.97, 0.15, 0.77, 4.03, 2.41,
                6.75, 7.51, 1.93, 0.10, 5.99, 6.33, 9.06, 2.76, 0.98, 2.36, 0.15, 1.97, 0.07};
        static final String[] BIGRAMS = {"TH", "HE", "IN", "ER", "AN", "RE", "ON", "AT", "EN", "ND", "TI", "ES", "OR",
                "TE", "OF", "ED", "IS", "IT", "AL", "AR", "ST", "TO", "NT", "NG", "SE", "HA", "AS", "OU", "IO", "LE", "VE",
                "CO", "ME", "DE", "HI", "RI", "RO", "IC", "NE", "EA", "RA", "CE", "LI", "CH", "LL", "BE", "MA", "SI", "OM", "UR"};
        static final String[] WORDS = {"BATTLE", "SSAFY", "SAMSUNG", "SOFTWARE", "ACADEMY", "YOUTH", "TANK", "MISSILE",
                "MEGA", "ATTACK", "DEFENSE", "TURRET", "SUPPLY", "ALGORITHM", "CODE", "JAVA", "PYTHON", "HELLO", "WORLD",
                "GAME", "VICTORY", "KOREA", "SEOUL", "DAEJEON", "GWANGJU", "GUMI", "BUSAN", "PROGRAM", "COMPUTER",
                "SECRET", "CAESAR", "CIPHER", "TEAM", "PLAYER", "SHIELD", "FIRE", "POWER"};
        static final double[] LOGF = new double[26];
        static {
            for (int i = 0; i < 26; i++) LOGF[i] = Math.log(FREQ[i] / 100.0);
        }
        static final List<String> samples = new ArrayList<>();
        static final Set<Integer> failed = new TreeSet<>();
        static int confirmed = -1, prior = -1;

        static String decode(String s, int k) {
            StringBuilder sb = new StringBuilder();
            for (char ch : s.toCharArray()) {
                if (ch >= 'A' && ch <= 'Z') sb.append((char) ('A' + ((ch - 'A' - k) % 26 + 26) % 26));
                else if (ch >= 'a' && ch <= 'z') sb.append((char) ('a' + ((ch - 'a' - k) % 26 + 26) % 26));
                else sb.append(ch);
            }
            return sb.toString();
        }

        static double score(String p) {
            String u = p.toUpperCase();
            double sc = 0;
            for (int i = 0; i < u.length(); i++) {
                char ch = u.charAt(i);
                if (ch >= 'A' && ch <= 'Z') sc += LOGF[ch - 'A'];
            }
            for (int i = 0; i + 1 < u.length(); i++) {
                String bg = u.substring(i, i + 2);
                for (String g : BIGRAMS) if (g.equals(bg)) { sc += 0.6; break; }
            }
            for (String w : WORDS) if (u.contains(w)) sc += 1.5 * w.length();
            return sc;
        }

        static void addSample(String s) {
            if (s == null || s.isEmpty()) return;
            String u = s.toUpperCase();
            if (!samples.contains(u)) {
                samples.add(u);
                if (samples.size() > 40) samples.remove(0);
            }
        }

        // {이동값, 성공 확률 추정}
        static double[] choose() {
            if (confirmed >= 0) return new double[]{confirmed, 0.97};
            if (prior >= 0 && !failed.contains(prior)) return new double[]{prior, 0.75};
            double[] tot = new double[26];
            double max = -INF;
            for (int k = 0; k < 26; k++) {
                if (failed.contains(k)) { tot[k] = -INF; continue; }
                for (String s : samples) tot[k] += score(decode(s, k));
                if (k == 0) tot[k] -= 3.0; // 이동값 0(평문 그대로)은 가능성 낮게
                max = Math.max(max, tot[k]);
            }
            if (max <= -INF / 2) return new double[]{-1, 0};
            double sum = 0;
            int best = -1;
            for (int k = 0; k < 26; k++) {
                if (tot[k] <= -INF / 2) continue;
                sum += Math.exp(tot[k] - max);
                if (best < 0 || tot[k] > tot[best]) best = k;
            }
            double p = Math.exp(tot[best] - max) / sum;
            return new double[]{best, Math.min(0.9, p)};
        }

        static void onResult(int k, boolean ok) {
            if (k < 0) return;
            if (ok) {
                confirmed = k;
                saveCache();
            } else {
                failed.add(k);
                if (confirmed == k) confirmed = -1;
                if (prior == k) prior = -1;
            }
        }

        static File cacheFile() { return new File(Cfg.LOG_DIR, "cipher_shift.txt"); }

        static void loadCache() {
            if (!Cfg.CIPHER_CACHE) return;
            try {
                File f = cacheFile();
                if (!f.exists()) return;
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
                String line = br.readLine();
                br.close();
                if (line != null) {
                    int v = Integer.parseInt(line.trim());
                    if (v >= 0 && v < 26) prior = v;
                }
            } catch (Throwable t) { /* 캐시는 없어도 됨 */ }
        }

        static void saveCache() {
            if (!Cfg.CIPHER_CACHE) return;
            try {
                File d = new File(Cfg.LOG_DIR);
                if (!d.exists()) d.mkdirs();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(cacheFile()), "UTF-8"));
                pw.println(confirmed);
                pw.close();
            } catch (Throwable t) { /* 무시 */ }
        }
    }

    // =====================================================================
    // [8] 두뇌: 역할 계획 → 후보 생성 → 1수 시뮬레이션 평가 → 선택
    // =====================================================================
    static final class Brain {

        static String think(String raw, long t0) {
            Mem.turn++;
            Board b = buildBoard();
            Mem.observe(b);
            if (b.me == null) {
                Log.turnHeader(b, null, null);
                Log.line("  내 탱크를 맵에서 찾지 못함 → 대기");
                Mem.prev = b; Mem.prevAct = null;
                return Cfg.IDLE_CMD;
            }
            for (String s : b.codes) Cipher.addSample(s);

            Weights w = phaseAdjust(b, Weights.forStrategy(Cfg.STRATEGY));
            Plan plan = makePlan(b, w);
            List<Cand> cands = candidates(b, plan, w);
            for (Cand c : cands) score(b, c, plan, w);

            Cand best = null;
            for (Cand c : cands) if (best == null || c.score > best.score) best = c;
            best = validate(b, best);
            // 대기가 연속되면 제자리 무해 사격으로 대체 (위치는 유지 → 봉쇄 유지)
            if (best.type == T_STAY && Cfg.ANTI_IDLE_TURNS > 0 && Mem.idleStreak >= Cfg.ANTI_IDLE_TURNS - 1) {
                Cand h = harmlessFire(b, best);
                if (h != null) { best = h; Mem.idleFires++; }
            }
            Mem.idleStreak = best.type == T_STAY ? Mem.idleStreak + 1 : 0;

            // 기록
            double ms = (System.nanoTime() - t0) / 1e6;
            Mem.maxThinkMs = Math.max(Mem.maxThinkMs, ms);
            Mem.sumThinkMs += ms;
            Mem.roleCount.put(plan.role, Mem.roleCount.containsKey(plan.role) ? Mem.roleCount.get(plan.role) + 1 : 1);
            if (best.type == T_FIRE && best.hitKind != 0) { Mem.shots++; if (best.mega) Mem.megaShots++; if (best.hitKind == 'T') Mem.treesShot++; }
            if (best.type == T_DECODE) Mem.decodeTry++;
            if (best.type == T_STAY) Mem.idleTurns++;

            Log.turn(raw, b, plan, w, cands, best, ms);
            Mem.prev = b;
            Mem.prevAct = best;
            Mem.prevRole = plan.role;
            return best.cmd;
        }

        // 게임 단계별 가중치 보정 (후반 판정 대응)
        static Weights phaseAdjust(Board b, Weights base) {
            Weights w = base.copy();
            int remain = MAX_TURN - Mem.turn;
            if (remain <= 15) {
                int my = Mem.myTeamDamage(b), en = Mem.enemyTeamDamage(b);
                if (my <= en) {         // 피해 판정에서 동점/우세 → 모래 피해는 곧 패배 요인이므로 금지
                    w.sandCost = 50; w.sand = 3;
                }
                if (my < en) {          // 앞서는 중: 안전 운영
                    w.exp *= 1.5; w.hunt = false; w.adv = 0.5;
                } else if (my == en) {  // 동점: 거리 합 판정 대비 전진
                    w.adv = 3;
                } else {                // 뒤지는 중: 공격적으로
                    w.exp *= 0.6; w.duel *= 0.5; w.adv = 1.5; w.death *= 0.5;
                }
                w.decodeScale *= 0.3;
            }
            return w;
        }

        static int labelKey(Unit u) {
            if (u.kind == 'M') return Mem.myIndex;
            return digit(u.label);
        }

        static Plan makePlan(Board b, Weights w) {
            if (!"AUTO".equals(Cfg.ROLE)) return Roles.plan(b, w, Cfg.ROLE);
            Plan p = new Plan();
            Costs k = new Costs();
            k.sand = w.sandCost; k.tree = w.treeCost;
            p.costs = k;

            List<Unit> mine = b.myTanks();
            Collections.sort(mine, (x, y) -> Integer.compare(labelKey(x), labelKey(y)));
            int meIdx = mine.indexOf(b.me);
            List<int[]> guards = Geo.guardCells(b);
            int n = mine.size();
            int want = n >= 3 ? w.defenders3 : (n == 2 ? w.defenders2 : 0);
            want = Math.min(want, guards.size());

            int myG = -1;
            double[][] myGuardMap = null;
            if (want > 0) {
                // 배정용 거리는 탱크를 무시한 지형 거리 → 아군 봇들이 같은 결론을 내도록
                Costs flat = new Costs();
                flat.sand = w.sandCost; flat.tree = w.treeCost; flat.ally = 0; flat.enemy = 0;
                double[][][] gmap = new double[guards.size()][][];
                for (int i = 0; i < guards.size(); i++)
                    gmap[i] = Geo.dist(b, Geo.single(guards.get(i)[0], guards.get(i)[1]), flat);
                int[] asg = assign(mine, gmap, want);
                myG = asg[meIdx];
                if (myG >= 0) {
                    p.guard = guards.get(myG);
                    p.goals = Geo.single(p.guard[0], p.guard[1]);
                    myGuardMap = Geo.dist(b, p.goals, k);
                }
            }

            double eta = Geo.enemyEtaToTurret(b);
            p.note = "적→포탑 ETA=" + fmt(eta) + " 수비배정=" + want + "/" + guards.size();
            int remain = MAX_TURN - Mem.turn;
            if (myG >= 0 && eta > remain + 1) {
                // 남은 턴 안에 적이 포탑 사격 위치에 못 옴 → 수비 해제, 거리 판정을 위해 전진
                myG = -1;
                p.guard = null;
                p.note += " (수비해제: 남은턴 " + remain + ")";
            }

            if (myG >= 0) {
                double myDist = myGuardMap[b.me.r][b.me.c];
                boolean free = w.dynamicDef && eta > myDist + w.defSlack + 2;
                if (free && w.farmDef && b.mega < w.megaTargetDef) {
                    List<double[]> fg = Geo.farmCells(b, w.goalDanger);
                    double[][] fm = Geo.dist(b, fg, k);
                    double df = fm[b.me.r][b.me.c];
                    double back = INF;   // 보급 칸에서 담당 봉쇄 칸까지 복귀 거리
                    for (double[] f : fg) back = Math.min(back, myGuardMap[(int) f[0]][(int) f[1]]);
                    if (df <= w.farmMaxDistDef && back < INF / 2 && eta > df + back + w.defSlack + 1) {
                        p.role = "FARM(DEF)";
                        p.goals = fg;
                        p.pot = fm;
                        p.megaTarget = w.megaTargetDef;
                        return p;
                    }
                }
                p.role = "DEFEND";
                p.pot = myGuardMap;
                p.megaTarget = w.megaTargetDef;
                return p;
            }

            if (w.farmAtt && b.mega < w.megaTargetAtt && Mem.turn <= w.farmDeadline) {
                List<double[]> fg = Geo.farmCells(b, w.goalDanger);
                double[][] fm = Geo.dist(b, fg, k);
                if (fm[b.me.r][b.me.c] <= w.farmMaxDist) {
                    p.role = "FARM(ATT)";
                    p.goals = fg;
                    p.pot = fm;
                    p.megaTarget = w.megaTargetAtt;
                    return p;
                }
            }

            p.role = "ATTACK";
            p.megaTarget = w.megaTargetAtt;
            Unit target = b.enemyTurret;
            if (w.hunterRole && !b.enemies.isEmpty()) {
                // 가장 가까운(동률이면 약한) 적 탱크를 추적
                Unit best = null;
                for (Unit e : b.enemies) {
                    int d1 = Geo.manhattan(b.me.r, b.me.c, e.r, e.c);
                    if (best == null || d1 < Geo.manhattan(b.me.r, b.me.c, best.r, best.c)
                            || (d1 == Geo.manhattan(b.me.r, b.me.c, best.r, best.c) && e.hp < best.hp)) best = e;
                }
                target = best;
                p.role = "HUNT";
            } else if (target == null && !b.enemies.isEmpty()) {
                target = b.enemies.get(0);
                for (Unit e : b.enemies) if (e.hp < target.hp) target = e;
                p.role = "HUNT";
            }
            List<double[]> goals = Geo.attackCells(b, target, 1.0);
            for (double[] g : goals) g[2] += w.goalDanger * Geo.losCount(b, (int) g[0], (int) g[1]);
            p.goals = goals;
            p.pot = Geo.dist(b, goals, k);

            // 공격이 막혀(전진 정체) 있는데 아군 포탑이 위협받으면 지원 복귀
            double cur = p.pot[b.me.r][b.me.c];
            if (cur < Mem.attackBest - 0.5) { Mem.attackBest = cur; Mem.attackStall = 0; }
            else Mem.attackStall++;
            boolean turretThreat = b.myTurret != null && (eta <= 6 || Geo.losCount(b, b.myTurret.r, b.myTurret.c) > 0);
            if (Mem.supportMode && (!turretThreat || b.myTurret == null)) { Mem.supportMode = false; Mem.attackStall = 0; Mem.attackBest = cur; }
            if (!Mem.supportMode && w.pinnedTurns > 0 && Mem.attackStall >= w.pinnedTurns && turretThreat && remain > 3)
                Mem.supportMode = true;
            if (Mem.supportMode && b.myTurret != null) {
                List<double[]> sg = new ArrayList<>();
                for (int[] g : guards) {
                    Unit u = b.at[g[0]][g[1]];
                    if (u == null || u.kind == 'M') sg.add(new double[]{g[0], g[1], 0});
                }
                if (sg.isEmpty()) {   // 봉쇄 칸이 모두 찼으면 포탑 사격 지점들을 감시할 수 있는 칸으로
                    for (double[] ac : Geo.attackCells(b, b.myTurret, 1.0)) sg.add(new double[]{ac[0], ac[1], 1 + ac[2]});
                }
                if (!sg.isEmpty()) {
                    p.role = "SUPPORT";
                    p.goals = sg;
                    p.pot = Geo.dist(b, sg, k);
                    p.note += " (공격정체 " + Mem.attackStall + "턴 → 지원)";
                }
            }
            return p;
        }

        // 탱크 → 봉쇄 칸 배정 (총 거리 최소, 동률이면 번호 순) ; 반환: 탱크별 봉쇄칸 인덱스 or -1
        static int[] assign(List<Unit> tanks, double[][][] gmap, int want) {
            int n = tanks.size();
            int[] cur = new int[n], best = new int[n];
            Arrays.fill(best, -1);
            double[] bestCost = {INF * 10};
            boolean[] used = new boolean[gmap.length];
            assignRec(0, tanks, gmap, want, 0, 0.0, cur, best, bestCost, used);
            return best;
        }

        static void assignRec(int i, List<Unit> tanks, double[][][] gmap, int want, int cnt, double cost,
                              int[] cur, int[] best, double[] bestCost, boolean[] used) {
            int n = tanks.size();
            if (i == n) {
                if (cnt == want && cost < bestCost[0] - 1e-9) { bestCost[0] = cost; System.arraycopy(cur, 0, best, 0, n); }
                return;
            }
            Unit t = tanks.get(i);
            for (int g = 0; g < gmap.length; g++) {
                if (used[g] || cnt >= want) continue;
                double dv = gmap[g][t.r][t.c];
                if (dv >= INF) dv = 1e6;
                used[g] = true; cur[i] = g;
                assignRec(i + 1, tanks, gmap, want, cnt + 1, cost + dv, cur, best, bestCost, used);
                used[g] = false;
            }
            if (want - cnt <= n - i - 1) {
                cur[i] = -1;
                assignRec(i + 1, tanks, gmap, want, cnt, cost, cur, best, bestCost, used);
            }
        }

        static List<Cand> candidates(Board b, Plan p, Weights w) {
            List<Cand> l = new ArrayList<>();
            Unit me = b.me;
            // 사격 (메가 → 일반 순서: 동점이면 앞의 것이 선택됨)
            for (int d = 0; d < 4; d++) {
                int[] hit = Geo.cast(b, me.r, me.c, d);
                if (hit[2] == 1) {
                    Unit u = b.at[hit[0]][hit[1]];
                    if (u.mine()) continue;   // 아군 오사격 금지
                    if (b.mega > 0) { Cand c = new Cand(T_FIRE, d); c.mega = true; c.cmd = DN[d] + " F M"; l.add(c); }
                    if (b.normal > 0) { Cand c = new Cand(T_FIRE, d); c.cmd = DN[d] + " F"; l.add(c); }
                } else if (hit[2] == 2 && b.normal > 0) {
                    Cand c = new Cand(T_FIRE, d); c.cmd = DN[d] + " F"; l.add(c);
                }
            }
            // 이동
            for (int d = 0; d < 4; d++) {
                int nr = me.r + DR[d], nc = me.c + DC[d];
                if (Geo.standable(b, nr, nc)) { Cand c = new Cand(T_MOVE, d); c.cmd = DN[d] + " A"; l.add(c); }
            }
            // 해독 (메가가 아직 쓸모 있을 때만)
            if (Geo.adjF(b, me.r, me.c) && !b.codes.isEmpty() && Cipher.failed.size() < 10
                    && megaValue(b.mega, p.megaTarget) * w.decodeScale > 1.0) {
                double[] ch = Cipher.choose();
                if (ch[0] >= 0 && ch[1] >= 0.15) {
                    String plain = Cipher.decode(b.codes.get(0), (int) ch[0]).toUpperCase().replace(' ', '_');
                    Cand c = new Cand(T_DECODE, -1);
                    c.shift = (int) ch[0]; c.prob = ch[1]; c.text = plain; c.cmd = "G " + plain;
                    l.add(c);
                }
            }
            // 대기
            Cand s = new Cand(T_STAY, -1);
            s.cmd = Cfg.IDLE_CMD;
            l.add(s);
            return l;
        }

        static int shotsToKill(int hp, int megas) {
            int n = 0;
            while (hp > 0) {
                if (megas > 0) { hp -= DMG_M; megas--; } else hp -= DMG_N;
                n++;
            }
            return n;
        }

        static double threatDmg(Unit e) { return Mem.megaEst(e.label) > 0 ? DMG_M : DMG_N; }

        // 적 e가 나를 쏠 확률 (볼 수 있는 표적 수로 분산, 포탑이 보이면 포탑을 노릴 가능성)
        static double targetProb(Board a, Unit e) {
            int nT = 0;
            for (Unit t : a.myTanks()) if (Geo.los(a, e.r, e.c, t.r, t.c)) nT++;
            boolean seesH = a.myTurret != null && Geo.los(a, e.r, e.c, a.myTurret.r, a.myTurret.c);
            double p = seesH ? 0.3 : 1.0;
            return p / Math.max(1, nT);
        }

        // 메가 1개 추가 확보의 가치 (역할별 목표 이상이면 거의 0 → 무한 해독 방지)
        static double megaValue(int have, int target) {
            if (have < target) return 30 * Math.pow(0.8, have);
            return have < target + 2 ? 2 : 0;
        }

        static void score(Board b, Cand c, Plan p, Weights w) {
            Board a = b.copy();
            Unit me = a.me;
            int r0 = me.r, c0 = me.c;
            double s = 0;
            StringBuilder why = c.why;
            int megaAfter = a.mega;
            double[][] pot = p.pot;

            if (c.type == T_MOVE) {
                a.at[r0][c0] = null;
                me.r += DR[c.d]; me.c += DC[c.d];
                a.at[me.r][me.c] = me;
                if (a.terr[me.r][me.c] == 'S') { c.sandLoss = SAND_DMG; me.hp -= SAND_DMG; }
            } else if (c.type == T_FIRE) {
                int[] hit = Geo.cast(a, r0, c0, c.d);
                c.hr = hit[0]; c.hc = hit[1];
                if (c.mega) { a.mega--; megaAfter--; s -= w.megaUse; why.append("메가소모 "); } else a.normal--;
                if (hit[2] == 1) {
                    Unit u = a.at[hit[0]][hit[1]];
                    c.hitKind = u.kind; c.hitLabel = u.label;
                    int dmg = c.mega ? DMG_M : DMG_N;
                    int before = u.hp;
                    int real = Math.min(dmg, before);
                    u.hp -= dmg;
                    if (u.kind == 'X') {
                        s += w.xDmg * real;
                        why.append("포탑피해").append(real).append(' ');
                        if (u.hp <= 0) { s += 1e7; why.append("포탑파괴! "); a.remove(u); }
                    } else if (u.kind == 'E') {
                        boolean threat = Geo.los(b, u.r, u.c, r0, c0)
                                || (b.myTurret != null && Geo.los(b, u.r, u.c, b.myTurret.r, b.myTurret.c));
                        double finish = 1.0 + 0.5 * (TANK_MAX_HP - before) / (double) TANK_MAX_HP;
                        double v = w.dmg * real * finish * (threat ? w.threatTarget : 1.0);
                        s += v;
                        why.append(u.label).append("피해").append(real).append(' ');
                        if (u.hp <= 0) {
                            s += w.kill; c.kill = true;
                            a.remove(u);
                            int dead = Mem.enemyTanksInit - a.enemies.size();
                            boolean win = Mem.enemyTanksInit >= 3 ? dead >= 2 : a.enemies.isEmpty();
                            if (win) { s += 1e6; why.append("결정적격파! "); } else why.append("격파 ");
                        } else if (w.duel > 0 && Geo.los(a, u.r, u.c, me.r, me.c)) {
                            int kk = shotsToKill(u.hp, megaAfter);
                            int jj = shotsToKill(me.hp, Mem.megaEst(u.label));
                            boolean winDuel = kk <= jj - 1;
                            s += winDuel ? w.duel : -w.duel;
                            why.append(winDuel ? "결투우세 " : "결투열세 ");
                        }
                    }
                } else if (hit[2] == 2) {
                    c.hitKind = 'T';
                    a.terr[hit[0]][hit[1]] = 'G';
                    s -= w.treeShot;
                    pot = Geo.dist(a, p.goals, p.costs);
                    why.append("나무제거 ");
                }
            } else if (c.type == T_DECODE) {
                double v = w.decodeScale * c.prob * megaValue(a.mega, p.megaTarget);
                s += v;
                why.append("해독(p=").append(fmt(c.prob)).append(")+").append(fmt(v)).append(' ');
            } else {
                s -= w.idle;
            }

            int nr = me.r, nc = me.c;
            c.nr = nr; c.nc = nc;

            // 목표 접근 (최적 경로 1걸음 = 1, 모래 같은 지형 추가비용은 별도 패널티로만 반영)
            double p0 = p.pot[r0][c0], p1 = pot[nr][nc];
            if (p0 < INF / 2 && p1 < INF / 2) {
                double extra = 0;
                if (c.type == T_MOVE) extra = Math.max(0, Geo.terrCost(b.terr[nr][nc], p.costs) - 1);
                double g = w.prog * (p0 - p1 - extra);
                s += g;
                if (Math.abs(g) > 1e-9) why.append("접근").append(fmt(g)).append(' ');
            } else if (p0 >= INF / 2 && p1 < INF / 2) {
                s += w.prog * 2;
            }

            // 후반 거리 판정 대비
            if (w.adv > 0 && a.enemyTurret != null) {
                double g = w.adv * (Geo.manhattan(r0, c0, a.enemyTurret.r, a.enemyTurret.c)
                        - Geo.manhattan(nr, nc, a.enemyTurret.r, a.enemyTurret.c));
                s += g;
                if (Math.abs(g) > 1e-9) why.append("전진").append(fmt(g)).append(' ');
            }

            // 내 노출 (행동 후 적 사선)
            double expIn = 0;
            int nLos = 0;
            Unit sole = null;
            for (Unit e : a.enemies) {
                if (Geo.los(a, e.r, e.c, nr, nc)) {
                    nLos++; sole = e;
                    expIn += threatDmg(e) * targetProb(a, e);
                }
            }
            double adj = expIn;
            if (nLos == 1 && w.hunt && c.type != T_FIRE) {
                int kk = shotsToKill(sole.hp, megaAfter);
                int jj = shotsToKill(me.hp, Mem.megaEst(sole.label));
                if (kk <= jj - 1) { adj *= w.huntExpScale; s += w.huntBonus; why.append("사냥가능 "); }
            }
            if (adj > 0) { s -= w.exp * adj; why.append("노출-").append(fmt(w.exp * adj)).append(' '); }
            if (expIn >= me.hp) {
                // 내가 죽으면 팀이 지는 상황(3대 중 이미 1대 손실, 또는 마지막 1대)이면 패배급 패널티
                int aliveMine = a.allies.size() + 1;
                int deadMine = Mem.myTanksInit - aliveMine;
                boolean fatal = Mem.myTanksInit >= 3 ? deadMine >= 1 : aliveMine <= 1;
                s -= fatal ? w.gameLoss * 0.5 : w.death;
                why.append(fatal ? "사망=패배위험 " : "치명위험 ");
            }
            c.expIn = expIn; c.nLos = nLos;

            // 아군 포탑 노출 (포탑이 파괴될 수 있으면 패배급 패널티)
            if (a.myTurret != null) {
                double he = 0;
                for (Unit e : a.enemies)
                    if (Geo.los(a, e.r, e.c, a.myTurret.r, a.myTurret.c)) he += threatDmg(e);
                if (he > 0) {
                    // 열린 사선은 매 라운드 계속 맞으므로 "포탑 파괴까지 남은 라운드"로 위험도를 평가
                    int roundsToDie = (int) Math.ceil(a.myTurret.hp / he);
                    double pen = w.hExp * he;
                    if (roundsToDie <= 1) pen += w.gameLoss;
                    else if (roundsToDie == 2) pen += w.gameLoss * 0.5;
                    else if (roundsToDie == 3) pen += w.gameLoss * 0.15;
                    if (roundsToDie <= 3) why.append("포탑위험(").append(roundsToDie).append("R) ");
                    s -= pen;
                    why.append("포탑노출-").append(fmt(pen)).append(' ');
                }
                c.hExp = he;
            }

            // 모래, 진동
            if (c.sandLoss > 0) { s -= w.sand * c.sandLoss; why.append("모래 "); }
            if (c.type == T_MOVE && Mem.posHist.size() >= 2) {
                int[] pp = Mem.posHist.get(Mem.posHist.size() - 2);
                if (pp[0] == nr && pp[1] == nc) { s -= w.osc; why.append("되돌림 "); }
            }
            c.score = s;
        }

        // 아무것도 맞히지 않는(허공/바위) 방향으로 일반탄 발사 - 아군/포탑/나무/적에는 절대 쏘지 않음
        static Cand harmlessFire(Board b, Cand stay) {
            if (b.normal <= 1) return null;
            int pick = -1;
            for (int d = 0; d < 4; d++) {
                int[] hit = Geo.cast(b, b.me.r, b.me.c, d);
                if (hit[2] == 3) { pick = d; break; }          // 바위/보급시설 우선
                if (hit[2] == 0 && pick < 0) pick = d;           // 허공
            }
            if (pick < 0) return null;
            Cand c = new Cand(T_FIRE, pick);
            c.cmd = DN[pick] + " F";
            c.nr = b.me.r; c.nc = b.me.c;
            c.score = stay.score; c.expIn = stay.expIn; c.nLos = stay.nLos; c.hExp = stay.hExp;
            c.why.append("무해사격(연속대기 대체) ").append(stay.why);
            return c;
        }

        // 최종 안전 검사: 무효 커맨드가 절대 나가지 않도록
        static Cand validate(Board b, Cand c) {
            boolean ok = true;
            if (c == null) ok = false;
            else if (c.type == T_MOVE) ok = Geo.standable(b, b.me.r + DR[c.d], b.me.c + DC[c.d]);
            else if (c.type == T_FIRE) {
                int[] hit = Geo.cast(b, b.me.r, b.me.c, c.d);
                ok = (c.mega ? b.mega > 0 : b.normal > 0) && hit[2] != 0
                        && !(hit[2] == 1 && b.at[hit[0]][hit[1]].mine());
            } else if (c.type == T_DECODE) ok = Geo.adjF(b, b.me.r, b.me.c) && !b.codes.isEmpty();
            if (ok) return c;
            Cand s = new Cand(T_STAY, -1);
            s.cmd = Cfg.IDLE_CMD;
            s.why.append("검증실패→대기");
            return s;
        }

        static void summary() {
            Log.line("");
            Log.line("==================== GAME SUMMARY ====================");
            Log.line("strategy=" + Weights.forStrategy(Cfg.STRATEGY).name + " myLabel=" + Mem.myLabel + " turns=" + Mem.turn
                    + " elapsed=" + (System.currentTimeMillis() - Mem.startMs) / 1000.0 + "s");
            Log.line("shots=" + Mem.shots + " (mega " + Mem.megaShots + ", tree " + Mem.treesShot + ") hits=" + Mem.shotHits
                    + " kills=" + Mem.kills);
            Log.line("decode try=" + Mem.decodeTry + " ok=" + Mem.decodeOk + " megaGained=" + Mem.megaGained
                    + " cipherShift=" + Cipher.confirmed + " failedShifts=" + Cipher.failed);
            Log.line("damage taken: enemyFire=" + Mem.dmgEnemyFire + " sand=" + Mem.dmgSand
                    + " unexplained=" + Mem.dmgUnexplained);
            Log.line("moveFail=" + Mem.moveFail + " idleTurns=" + Mem.idleTurns + " idleFires=" + Mem.idleFires
                    + " errors=" + Mem.errors + " role=" + Cfg.ROLE);
            Log.line("think ms: max=" + fmt(Mem.maxThinkMs) + " avg=" + fmt(Mem.turn > 0 ? Mem.sumThinkMs / Mem.turn : 0));
            Log.line("roles=" + Mem.roleCount + " unknownSymbols=" + Mem.unknownSyms);
            if (Mem.prev != null) {
                Log.line("last known team damage: mine=" + Mem.myTeamDamage(Mem.prev) + " enemy=" + Mem.enemyTeamDamage(Mem.prev));
            }
            Log.line("======================================================");
        }
    }

    // =====================================================================
    // [8-1] 역할 고정형 팀 플레이 (3개 코드가 서로 다른 역할)
    //   - 아군이 누구인지(라벨) 몰라도 동작하도록, 협동은 "아군 위치" 기반으로 계산
    //   - 크로스파이어: 아군/포탑을 쏠 수 있는 칸(위협 칸)을 내 사선으로 덮는다
    //   - 유인-매복: 적이 나를 쏘러 들어올 칸이 아군 사선에 덮여 있는 곳에 선다
    // =====================================================================
    static final class Roles {

        static Plan plan(Board b, Weights w, String role) {
            Costs k = new Costs();
            k.sand = w.sandCost; k.tree = w.treeCost;
            int remain = MAX_TURN - Mem.turn;
            double eta = Geo.enemyEtaToTurret(b);
            String note = "적→포탑 ETA=" + fmt(eta);

            if ("SENTRY".equals(role)) {
                Plan g = sentry(b, w, k, eta, remain);
                if (g != null) { g.note = note + " " + g.note; return g; }
                Plan a = attack(b, w, k, "ATTACK");
                a.note = note + " (수비 해제)";
                return a;
            }

            if (role.startsWith("GUARD")) {
                Plan g = guard(b, w, k, role.equals("GUARD1") ? 0 : 1, eta, remain);
                if (g != null) { g.note = note + " " + g.note; return g; }
                // 봉쇄할 칸이 없거나 남은 턴에 위협이 없으면 엄호/전진
                Plan o = overwatch(b, w, k, true);
                if (o != null && eta <= remain + 1) { o.note = note + " (봉쇄칸 없음→포탑 엄호)"; return o; }
                Plan a = attack(b, w, k, "ATTACK");
                a.note = note + " (수비 해제)";
                return a;
            }

            // 공통: 초반 메가 파밍 (FLEX 는 위협 판단 후)
            // FLEX: 포탑이 위협받는 동안은 2번 봉쇄 칸 수비, 아니면 사냥 (위협 해제는 여유를 두고 판단 → 진동 방지)
            if ("FLEX".equals(role)) {
                Plan g = guard(b, w, k, 1, eta, remain);
                if (g != null) {
                    // 봉쇄 칸까지 복귀 거리 + 여유 ≥ 적 도착 시간이면 수비 (자리를 비우지 않음)
                    double back = Geo.dist(b, Geo.single(g.guard != null ? g.guard[0] : b.me.r, g.guard != null ? g.guard[1] : b.me.c), k)[b.me.r][b.me.c];
                    double margin = w.defSlack + (Mem.flexGuarding ? 4 : 2);
                    boolean threat = eta <= back + margin
                            || (b.myTurret != null && Geo.losCount(b, b.myTurret.r, b.myTurret.c) > 0);
                    Mem.flexGuarding = threat;
                    if (threat) { g.role = "FLEX-" + g.role; g.note = note + " 복귀거리 " + fmt(back) + " " + g.note; return g; }
                } else {
                    Mem.flexGuarding = false;
                }
            }

            Plan f = farm(b, w, k, w.megaTargetAtt, w.farmMaxDist, Mem.turn <= w.farmDeadline);
            if (f != null) { f.note = note; return f; }

            Plan p = null;
            if (role.startsWith("BACKDOOR")) {
                p = backdoor(b, w, k, role.equals("BACKDOOR1") ? 0 : 1);
            } else if ("STRIKER".equals(role)) {
                p = attack(b, w, k, "STRIKE");
            } else if ("HUNTER".equals(role) || "FLEX".equals(role)) {
                p = hunt(b, w, k, false);
            } else if ("HUNTERD".equals(role)) {
                p = hunt(b, w, k, true);
            } else if ("OVERWATCH".equals(role)) {
                p = overwatch(b, w, k, false);
            } else if ("BAIT".equals(role)) {
                p = bait(b, w, k);
            }
            if (p == null) p = attack(b, w, k, "ATTACK");
            p.note = note + (p.note.isEmpty() ? "" : " " + p.note);
            return p;
        }

        // 봉쇄 칸 선택: pick 번째 열린 방향 (아군이 이미 서 있으면 다른 빈 봉쇄 칸)
        static Plan guard(Board b, Weights w, Costs k, int pick, double eta, int remain) {
            List<int[]> guards = Geo.guardCells(b);
            if (guards.isEmpty() || pick >= guards.size()) return null;
            if (eta > remain + 1) return null;   // 남은 턴 안에 위협 없음 → 수비 해제
            int[] g = guards.get(pick);
            Unit occ = b.at[g[0]][g[1]];
            if (occ != null && occ.kind == 'A') {
                for (int[] o : guards) {
                    Unit u = b.at[o[0]][o[1]];
                    if (u == null || u.kind == 'M') { g = o; break; }
                }
            }
            Plan p = new Plan();
            p.costs = k;
            p.guard = g;
            p.goals = Geo.single(g[0], g[1]);
            p.pot = Geo.dist(b, p.goals, k);
            p.megaTarget = w.megaTargetDef;
            p.role = "GUARD";
            double myDist = p.pot[b.me.r][b.me.c];
            // 적이 오기 전 여유가 있으면 메가 파밍 후 복귀
            if (w.farmDef && b.mega < w.megaTargetDef && eta > myDist + w.defSlack + 2) {
                List<double[]> fg = Geo.farmCells(b, w.goalDanger);
                double[][] fm = Geo.dist(b, fg, k);
                double df = fm[b.me.r][b.me.c];
                double back = INF;
                for (double[] f : fg) back = Math.min(back, p.pot[(int) f[0]][(int) f[1]]);
                if (df <= w.farmMaxDistDef && back < INF / 2 && eta > df + back + w.defSlack + 1) {
                    Plan fp = new Plan();
                    fp.costs = k; fp.goals = fg; fp.pot = fm; fp.megaTarget = w.megaTargetDef;
                    fp.role = "FARM(GUARD)";
                    fp.note = "복귀거리 " + fmt(back);
                    return fp;
                }
            }
            return p;
        }

        // 좁은 각도 수비: 포탑 구역 밖에서 포탑 사격 지점(구역 안/밖)을 가장 많이 사선에 두는 칸
        static Plan sentry(Board b, Weights w, Costs k, double eta, int remain) {
            Unit h = b.myTurret;
            if (h == null || eta > remain + 1) return null;
            double[][] tw = new double[b.H][b.W];
            boolean any = false;
            for (int d = 0; d < 4; d++) {
                for (int s = 1; s <= RANGE; s++) {
                    int r = h.r + DR[d] * s, c = h.c + DC[d] * s;
                    if (!b.inb(r, c) || !Geo.shellPass(b, r, c)) break;
                    if (Geo.standableTerr(b.terr[r][c])) { tw[r][c] += Geo.inZone(b, r, c) ? 0.25 : 1.0; any = true; }
                }
            }
            if (!any) return null;
            List<double[]> raw = new ArrayList<>();
            double maxV = 0;
            for (int r = 0; r < b.H; r++) for (int c = 0; c < b.W; c++) {
                boolean mine = b.me.r == r && b.me.c == c;
                if (!(mine || Geo.standable(b, r, c)) || Geo.inZone(b, r, c)) continue;
                double v = coverValue(b, r, c, tw);
                if (v <= 0) continue;
                raw.add(new double[]{r, c, v});
                maxV = Math.max(maxV, v);
            }
            if (raw.isEmpty()) return null;
            Plan p = new Plan();
            p.costs = k;
            for (double[] x : raw) {
                double off = (maxV - x[2]) * 2.0 + 0.15 * Geo.manhattan((int) x[0], (int) x[1], h.r, h.c)
                        + w.goalDanger * Geo.losCount(b, (int) x[0], (int) x[1]);
                p.goals.add(new double[]{x[0], x[1], off});
            }
            p.pot = Geo.dist(b, p.goals, k);
            p.megaTarget = w.megaTargetDef;
            p.role = "SENTRY";
            p.note = "감시값 최대 " + fmt(maxV);
            // 빠른 보급: 적이 오기 전에 다녀올 수 있으면 메가 확보
            double myDist = p.pot[b.me.r][b.me.c];
            if (w.farmDef && b.mega < w.megaTargetDef && eta > myDist + w.defSlack + 2) {
                List<double[]> fg = Geo.farmCells(b, w.goalDanger);
                double[][] fm = Geo.dist(b, fg, k);
                double df = fm[b.me.r][b.me.c];
                double back = INF;
                for (double[] f : fg) back = Math.min(back, p.pot[(int) f[0]][(int) f[1]]);
                if (df <= w.farmMaxDistDef && back < INF / 2 && eta > df + back + w.defSlack + 1) {
                    Plan fp = new Plan();
                    fp.costs = k; fp.goals = fg; fp.pot = fm; fp.megaTarget = w.megaTargetDef;
                    fp.role = "FARM(SENTRY)";
                    fp.note = "복귀거리 " + fmt(back);
                    return fp;
                }
            }
            return p;
        }

        // 백도어: 적 사선을 피하는 경로로 적 포탑의 pick 번째 방향 사격 지점으로 침투.
        //         가까운 적 탱크와의 결투가 유리하면(먼저 맞아도 이김) 그 탱크를 우선 공격
        static Plan backdoor(Board b, Weights w, Costs k, int pick) {
            Unit me = b.me;
            Unit prey = null;
            for (Unit e : b.enemies) {
                int d = Geo.manhattan(me.r, me.c, e.r, e.c);
                if (d > 5) continue;
                int kk = Brain.shotsToKill(e.hp, b.mega), jj = Brain.shotsToKill(me.hp, Mem.megaEst(e.label));
                if (kk <= jj - 1 && (prey == null || e.hp < prey.hp)) prey = e;
            }
            Costs sk = new Costs();
            sk.sand = k.sand; sk.tree = k.tree;
            sk.extra = new double[b.H][b.W];
            for (int r = 0; r < b.H; r++) for (int c = 0; c < b.W; c++) sk.extra[r][c] = 3.0 * Geo.losCount(b, r, c);
            Plan p = new Plan();
            p.costs = sk;
            p.megaTarget = w.megaTargetAtt;
            if (prey != null) {
                p.role = "BACKDOOR-HUNT";
                p.goals = Geo.attackCells(b, prey, 1.0);
                p.pot = Geo.dist(b, p.goals, sk);
                p.note = "유리한 결투 " + prey.label;
                if (p.pot[me.r][me.c] < INF / 2) return p;
            }
            Unit x = b.enemyTurret;
            if (x == null) return hunt(b, w, k, false);
            List<double[]> all = Geo.attackCells(b, x, 1.0);
            // 포탑 기준 방향별로 나눠 pick 번째 방향을 선호 (두 대가 서로 다른 방향에서 협공)
            List<Integer> dirs = new ArrayList<>();
            for (double[] g : all) {
                int d = dirOf(x, (int) g[0], (int) g[1]);
                if (!dirs.contains(d)) dirs.add(d);
            }
            Collections.sort(dirs);
            int want = dirs.isEmpty() ? -1 : dirs.get(pick % dirs.size());
            for (double[] g : all) {
                if (dirOf(x, (int) g[0], (int) g[1]) != want) g[2] += 4;
                g[2] += w.goalDanger * Geo.losCount(b, (int) g[0], (int) g[1]);
            }
            p.role = "BACKDOOR";
            p.goals = all;
            p.pot = Geo.dist(b, all, sk);
            p.note = "침투방향 " + (want >= 0 ? DN[want] : "-");
            return p;
        }

        static int dirOf(Unit t, int r, int c) {
            if (r < t.r) return 0;
            if (r > t.r) return 1;
            if (c < t.c) return 2;
            return 3;
        }

        static Plan farm(Board b, Weights w, Costs k, int target, int maxDist, boolean allowed) {
            if (!allowed || b.mega >= target) return null;
            List<double[]> fg = Geo.farmCells(b, w.goalDanger);
            if (fg.isEmpty()) return null;
            double[][] fm = Geo.dist(b, fg, k);
            if (fm[b.me.r][b.me.c] > maxDist) return null;
            Plan p = new Plan();
            p.costs = k; p.goals = fg; p.pot = fm; p.megaTarget = target;
            p.role = "FARM";
            return p;
        }

        static Plan attack(Board b, Weights w, Costs k, String name) {
            Plan p = new Plan();
            p.costs = k;
            p.role = name;
            p.megaTarget = w.megaTargetAtt;
            Unit target = b.enemyTurret;
            if (target == null) target = weakestEnemy(b);
            if (target == null) {   // 목표 없음 → 제자리
                p.goals = Geo.single(b.me.r, b.me.c);
                p.pot = Geo.dist(b, p.goals, k);
                return p;
            }
            List<double[]> goals = Geo.attackCells(b, target, 1.0);
            for (double[] g : goals) g[2] += w.goalDanger * Geo.losCount(b, (int) g[0], (int) g[1]);
            p.goals = goals;
            p.pot = Geo.dist(b, goals, k);
            return p;
        }

        static Unit weakestEnemy(Board b) {
            Unit t = null;
            for (Unit e : b.enemies) if (t == null || e.hp < t.hp) t = e;
            return t;
        }

        // defensive=true 이면 아군 포탑에 가까운 적을 우선 추적
        static Plan hunt(Board b, Weights w, Costs k, boolean defensive) {
            if (b.enemies.isEmpty()) return null;
            Unit t = null;
            int best = Integer.MAX_VALUE;
            for (Unit e : b.enemies) {
                int d = Geo.manhattan(b.me.r, b.me.c, e.r, e.c) + e.hp / 30;   // 가깝고 약한 적
                if (defensive && b.myTurret != null) d += Geo.manhattan(e.r, e.c, b.myTurret.r, b.myTurret.c);
                if (d < best) { best = d; t = e; }
            }
            Plan p = new Plan();
            p.costs = k;
            p.role = "HUNT";
            p.megaTarget = w.megaTargetAtt;
            p.goals = Geo.attackCells(b, t, 1.0);
            for (double[] g : p.goals) g[2] += w.goalDanger * Geo.losCount(b, (int) g[0], (int) g[1]);
            p.pot = Geo.dist(b, p.goals, k);
            p.note = "표적 " + t.label;
            return p;
        }

        // 적이 각 칸까지 오는 데 걸리는 대략적 턴 수
        static double[][] enemyReach(Board b) {
            List<double[]> src = new ArrayList<>();
            for (Unit e : b.enemies) src.add(new double[]{e.r, e.c, 0});
            Costs k = new Costs();
            k.sand = 0; k.tree = 1; k.ally = 0; k.enemy = 0;
            return Geo.dist(b, src, k);
        }

        // (r,c)에서 사선으로 덮을 수 있는 칸들의 가중치 합
        static double coverValue(Board b, int r, int c, double[][] tw) {
            double v = 0;
            for (int d = 0; d < 4; d++) {
                for (int s = 1; s <= RANGE; s++) {
                    int nr = r + DR[d] * s, nc = c + DC[d] * s;
                    if (!b.inb(nr, nc)) break;
                    v += tw[nr][nc];
                    Unit u = b.at[nr][nc];
                    if (u != null && u.kind != 'M') break;
                    if (!Geo.shellPass(b, nr, nc)) break;
                }
            }
            return v;
        }

        static boolean allyLos(Board b, int r, int c) {
            for (Unit a : b.allies) if (Geo.los(b, a.r, a.c, r, c)) return true;
            return false;
        }

        // 엄호조: 아군 탱크/포탑을 쏠 수 있는 위협 칸(적이 곧 도달 가능)을 최대한 많이 사선으로 덮는 위치
        static Plan overwatch(Board b, Weights w, Costs k, boolean turretOnly) {
            if (b.enemies.isEmpty()) return null;
            double[][] de = enemyReach(b);
            double[][] tw = new double[b.H][b.W];
            List<Unit> protect = new ArrayList<>();
            if (b.myTurret != null) protect.add(b.myTurret);
            if (!turretOnly) protect.addAll(b.allies);
            boolean any = false;
            for (Unit u : protect) {
                double wt = u.kind == 'H' ? 3.0 : 1.5;
                for (double[] y : Geo.attackCells(b, u, 0)) {
                    if (y[2] > 0) continue;
                    int yr = (int) y[0], yc = (int) y[1];
                    if (de[yr][yc] <= 4) { tw[yr][yc] += wt; any = true; }
                }
            }
            if (!any) return null;
            List<double[]> goals = new ArrayList<>();
            double maxV = 0;
            List<double[]> raw = new ArrayList<>();
            for (int r = 0; r < b.H; r++) for (int c = 0; c < b.W; c++) {
                boolean mine = b.me.r == r && b.me.c == c;
                if (!mine && !Geo.standable(b, r, c)) continue;
                double v = coverValue(b, r, c, tw);
                if (v <= 0) continue;
                raw.add(new double[]{r, c, v});
                maxV = Math.max(maxV, v);
            }
            if (raw.isEmpty()) return null;
            for (double[] x : raw) {
                double off = (maxV - x[2]) * 1.0 + w.goalDanger * Geo.losCount(b, (int) x[0], (int) x[1]);
                goals.add(new double[]{x[0], x[1], off});
            }
            Plan p = new Plan();
            p.costs = k;
            p.role = turretOnly ? "COVER(H)" : "OVERWATCH";
            p.megaTarget = w.megaTargetAtt;
            p.goals = goals;
            p.pot = Geo.dist(b, goals, k);
            p.note = "엄호값 최대 " + fmt(maxV);
            return p;
        }

        // 유인조: 지금은 적 사선 밖이지만, 적이 나를 쏘려고 들어올 칸이 아군 사선에 덮여 있는 위치
        static Plan bait(Board b, Weights w, Costs k) {
            if (b.enemies.isEmpty() || b.allies.isEmpty()) return null;
            double[][] de = enemyReach(b);
            List<double[]> raw = new ArrayList<>();
            double maxV = -INF;
            for (int r = 0; r < b.H; r++) for (int c = 0; c < b.W; c++) {
                boolean mine = b.me.r == r && b.me.c == c;
                if (!mine && !Geo.standable(b, r, c)) continue;
                if (Geo.losCount(b, r, c) > 0) continue;
                int covered = 0, open = 0;
                for (int d = 0; d < 4; d++) {
                    for (int s = 1; s <= RANGE; s++) {
                        int nr = r + DR[d] * s, nc = c + DC[d] * s;
                        if (!b.inb(nr, nc)) break;
                        Unit u = b.at[nr][nc];
                        if (u != null && u.kind != 'E') break;
                        if (!Geo.shellPass(b, nr, nc)) break;
                        if (Geo.standableTerr(b.terr[nr][nc]) && de[nr][nc] <= 2) {
                            if (allyLos(b, nr, nc)) covered++; else open++;
                        }
                    }
                }
                if (covered + open == 0) continue;
                double v = 2.0 * covered - 1.5 * open - 0.3 * Math.abs(de[r][c] - 4);
                raw.add(new double[]{r, c, v});
                maxV = Math.max(maxV, v);
            }
            if (raw.isEmpty() || maxV <= 0) return null;
            List<double[]> goals = new ArrayList<>();
            for (double[] x : raw) if (x[2] > 0) goals.add(new double[]{x[0], x[1], maxV - x[2]});
            Plan p = new Plan();
            p.costs = k;
            p.role = "BAIT";
            p.megaTarget = w.megaTargetAtt;
            p.goals = goals;
            p.pot = Geo.dist(b, goals, k);
            p.note = "유인값 최대 " + fmt(maxV);
            return p;
        }
    }

    // =====================================================================
    // [9] 로깅 (battle_logs/battle_시각_닉네임_xxxx.log / .csv)
    // =====================================================================
    static final class Log {
        static PrintWriter out, csv;
        static boolean on = false;

        static void open(String nick, String strategy) {
            if (!Cfg.LOG_TO_FILE) return;
            try {
                File dir = new File(Cfg.LOG_DIR);
                if (!dir.exists()) dir.mkdirs();
                String ts = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String safe = nick.replaceAll("[^0-9A-Za-z_-]", "");   // 파일명은 ASCII만 (한글 경로 깨짐 방지)
                if (safe.isEmpty()) safe = "bot";
                String base = "battle_" + ts + "_" + safe + "_" + Cfg.ROLE + "_" + Integer.toHexString(0x1000 + new Random().nextInt(0xEFFF));
                out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, base + ".log")), "UTF-8"));
                csv = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, base + ".csv")), "UTF-8"));
                csv.println("turn,ms,r,c,hp,dir,normal,mega,role,cmd,score,expIn,nLos,hExp,enemyTurretHp,myTurretHp,"
                        + "myTeamDmg,enemyTeamDmg,allies,enemies,events");
                on = true;
                line("# Battle SSAFY bot log | strategy=" + strategy + " | nick=" + nick + " | start=" + new Date());
                out.flush();
            } catch (Throwable t) {
                on = false;
            }
        }

        static void line(String s) {
            if (!on) return;
            try { out.println(s); } catch (Throwable t) { /* 무시 */ }
        }

        static void error(String where, Throwable e) {
            Mem.errors++;
            System.out.println("[BOT-ERROR] " + where + ": " + e);
            if (!on) return;
            try {
                out.println("[ERROR] " + where + ": " + e);
                for (StackTraceElement st : e.getStackTrace()) out.println("    at " + st);
                out.flush();
            } catch (Throwable t) { /* 무시 */ }
        }

        static void turnHeader(Board b, Plan p, Weights w) {
            if (!on) return;
            line("");
            line("==== TURN " + Mem.turn + " (+" + (System.currentTimeMillis() - Mem.startMs) + "ms) ====");
        }

        static String units(List<Unit> l) {
            StringBuilder sb = new StringBuilder();
            for (Unit u : l) sb.append(u).append(' ');
            return sb.toString().trim();
        }

        static void turn(String raw, Board b, Plan p, Weights w, List<Cand> cands, Cand best, double ms) {
            // 콘솔 한 줄 요약
            System.out.println("[BOT] T" + Mem.turn + " " + Mem.myLabel + "@(" + b.me.r + "," + b.me.c + ") hp" + b.me.hp
                    + " mega" + b.mega + " role=" + p.role + " -> " + best.cmd + "  [" + best.why.toString().trim() + "]");
            if (!on) return;
            try {
                turnHeader(b, p, w);
                if (Cfg.LOG_RAW_INPUT) line("RAW: " + oneLine(raw));
                line("ME " + Mem.myLabel + " @(" + b.me.r + "," + b.me.c + ") dir=" + b.dir + " hp=" + b.me.hp
                        + " normal=" + b.normal + " mega=" + b.mega + " | role=" + p.role
                        + (p.guard != null ? " guard=(" + p.guard[0] + "," + p.guard[1] + ")" : "")
                        + " | " + p.note + " | w=" + w.name + (w.adv > 0 ? " adv=" + fmt(w.adv) : ""));
                line("ALLY " + units(b.allies) + (b.myTurret != null ? " | H" + "(" + b.myTurret.r + "," + b.myTurret.c + ")" + b.myTurret.hp : " | H 없음"));
                StringBuilder es = new StringBuilder();
                for (Unit e : b.enemies) es.append(e).append(Mem.megaEst(e.label) > 0 ? "[mega~" + Mem.megaEst(e.label) + "]" : "").append(' ');
                line("ENEMY " + es.toString().trim() + (b.enemyTurret != null ? " | X(" + b.enemyTurret.r + "," + b.enemyTurret.c + ")" + b.enemyTurret.hp : " | X 없음"));
                if (!b.codes.isEmpty()) line("CODE " + b.codes + " samples=" + Cipher.samples.size() + " confirmed=" + Cipher.confirmed + " failed=" + Cipher.failed);
                if (!Mem.events.isEmpty()) line("EVENT " + String.join(" | ", Mem.events));
                List<Cand> sorted = new ArrayList<>(cands);
                Collections.sort(sorted, (x, y) -> Double.compare(y.score, x.score));
                StringBuilder cs = new StringBuilder();
                for (int i = 0; i < Math.min(Cfg.LOG_TOP_CANDIDATES, sorted.size()); i++) {
                    Cand c = sorted.get(i);
                    cs.append("[").append(c.cmd).append("] ").append(fmt(c.score)).append(" {").append(c.why.toString().trim()).append("}  ");
                }
                line("CAND " + cs.toString().trim());
                line("CHOICE " + best.cmd + " score=" + fmt(best.score) + " expIn=" + fmt(best.expIn) + " nLos=" + best.nLos
                        + " | " + fmt(ms) + "ms");

                StringBuilder ev = new StringBuilder();
                for (String e : Mem.events) ev.append(e.replace(',', ';').replace('"', '\'')).append(" / ");
                csv.println(Mem.turn + "," + fmt(ms) + "," + b.me.r + "," + b.me.c + "," + b.me.hp + "," + b.dir + ","
                        + b.normal + "," + b.mega + "," + p.role + "," + best.cmd + "," + fmt(best.score) + ","
                        + fmt(best.expIn) + "," + best.nLos + "," + fmt(best.hExp) + ","
                        + (b.enemyTurret != null ? b.enemyTurret.hp : -1) + "," + (b.myTurret != null ? b.myTurret.hp : -1) + ","
                        + Mem.myTeamDamage(b) + "," + Mem.enemyTeamDamage(b) + "," + b.allies.size() + "," + b.enemies.size()
                        + ",\"" + ev + "\"");
                out.flush();
                csv.flush();
            } catch (Throwable t) { /* 로깅 실패는 무시 */ }
        }

        static void close() {
            if (!on) return;
            try { out.flush(); out.close(); csv.flush(); csv.close(); } catch (Throwable t) { /* 무시 */ }
            on = false;
        }
    }

    // =====================================================================
    // [10] 유틸
    // =====================================================================
    static int toInt(String[] arr, int idx, int def) {
        if (arr == null || idx >= arr.length || arr[idx] == null) return def;
        try { return Integer.parseInt(arr[idx].trim()); } catch (Exception e) { return def; }
    }

    static int digit(String label) {
        for (int i = label.length() - 1; i >= 0; i--) {
            char ch = label.charAt(i);
            if (ch >= '0' && ch <= '9') return ch - '0';
        }
        return 0;
    }

    static String fmt(double v) {
        if (v >= INF / 2) return "INF";
        if (Math.abs(v - Math.round(v)) < 1e-6) return String.valueOf(Math.round(v));
        return String.format(Locale.US, "%.2f", v);
    }

    static String oneLine(String raw) {
        return raw == null ? "null" : raw.trim().replace("\r", "").replace("\n", " | ");
    }

	////////////////////////////////////
	// 알고리즘 함수/메서드 부분 구현 끝
	////////////////////////////////////

    ///////////////////////////////
    // 메인 프로그램 통신 메서드 정의
    ///////////////////////////////

    // 메인 프로그램 연결 및 초기화
    private static String init(String nickname) {
        try {
            System.out.println("[STATUS] Trying to connect to " + HOST + ":" + PORT + "...");
            socket = new Socket();
            socket.connect(new InetSocketAddress(HOST, PORT));
            System.out.println("[STATUS] Connected");
            String initCommand = "INIT " + nickname;

            return submit(initCommand);
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to connect. Please check if the main program is waiting for connection.");
            e.printStackTrace();
            return null;
        }
    }

    // 메인 프로그램으로 데이터(명령어) 전송
    private static String submit(String stringToSend) {
        try {
            OutputStream os = socket.getOutputStream();
            String sendData = ARGS + stringToSend + " ";
            os.write(sendData.getBytes("UTF-8"));
            os.flush();

            return receive();
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to send data. Please check if connection to the main program is valid.");
            e.printStackTrace();
        }
        return null;
    }

    // 메인 프로그램으로부터 데이터 수신
    private static String receive() {
        try {
            InputStream is = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int length = is.read(bytes);
            if (length <= 0) {
                System.out.println("[STATUS] No receive data from the main program.");
                close();
                return null;
            }

            String gameData = new String(bytes, 0, length, "UTF-8");
            if (gameData.length() > 0 && gameData.charAt(0) >= '1' && gameData.charAt(0) <= '9') {
                return gameData;
            }

            System.out.println("[STATUS] No receive data from the main program.");
            close();
            return null;
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to receive data. Please check if connection to the main program is valid.");
            e.printStackTrace();
        }
        return null;
    }

    // 연결 해제
    private static void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("[STATUS] Connection closed");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Network connection has been corrupted.");
            e.printStackTrace();
        }
    }

	///////////////////////////////
	// 입력 데이터 파싱
	///////////////////////////////

    // 입력 데이터를 파싱하여 각각의 배열/맵에 저장
    private static void parseData(String gameData) {
        // 입력 데이터를 행으로 나누기
        String[] gameDataRows = gameData.split("\n");
        int rowIndex = 0;

        // 첫 번째 행 데이터 읽기
        String[] header = gameDataRows[rowIndex].split(" ");
        int mapHeight = header.length >= 1 ? Integer.parseInt(header[0]) : 0; // 맵의 세로 크기
        int mapWidth = header.length >= 2 ? Integer.parseInt(header[1]) : 0;  // 맵의 가로 크기
        int numOfAllies = header.length >= 3 ? Integer.parseInt(header[2]) : 0;  // 아군의 수
        int numOfEnemies = header.length >= 4 ? Integer.parseInt(header[3]) : 0;  // 적군의 수
        int numOfCodes = header.length >= 5 ? Integer.parseInt(header[4]) : 0;  // 암호문의 수
        rowIndex++;

        // 기존의 맵 정보를 초기화하고 다시 읽어오기
        mapData = new String[mapHeight][mapWidth];
        for (int i = 0; i < mapHeight; i++) {
            String[] col = gameDataRows[rowIndex + i].split(" ");
            for (int j = 0; j < col.length; j++) {
                mapData[i][j] = col[j];
            }
        }
        rowIndex += mapHeight;

        // 기존의 아군 정보를 초기화하고 다시 읽어오기
        myAllies.clear();
        for (int i = rowIndex; i < rowIndex + numOfAllies; i++) {
            String[] ally = gameDataRows[i].split(" ");
            String allyName = ally.length >= 1 ? ally[0] : "-";
            String[] allyData = new String[ally.length - 1];
            System.arraycopy(ally, 1, allyData, 0, ally.length - 1);
            myAllies.put(allyName, allyData);
        }
        rowIndex += numOfAllies;

        // 기존의 적군 정보를 초기화하고 다시 읽어오기
        enemies.clear();
        for (int i = rowIndex; i < rowIndex + numOfEnemies; i++) {
            String[] enemy = gameDataRows[i].split(" ");
            String enemyName = enemy.length >= 1 ? enemy[0] : "-";
            String[] enemyData = new String[enemy.length - 1];
            System.arraycopy(enemy, 1, enemyData, 0, enemy.length - 1);
            enemies.put(enemyName, enemyData);
        }
        rowIndex += numOfEnemies;

        // 기존의 암호문 정보를 초기화하고 다시 읽어오기
        codes = new String[numOfCodes];
        for (int i = 0; i < numOfCodes; i++) {
            codes[i] = gameDataRows[rowIndex + i];
        }
    }

    // 파싱한 데이터를 화면에 출력
    private static void printData(String gameData) {
        System.out.printf("\n----------입력 데이터----------\n%s\n----------------------------\n", gameData);

        System.out.printf("\n[맵 정보] (%d x %d)\n", mapData.length, mapData[0].length);
        for (String[] row : mapData) {
            for (String cell : row) {
                System.out.printf("%s ", cell);
            }
            System.out.println();
        }

        System.out.printf("\n[아군 정보] (아군 수: %d)\n", myAllies.size());
        for (String key : myAllies.keySet()) {
            String[] value = myAllies.get(key);
            if (key.equals("M")) {
                System.out.printf("M (내 탱크) - 체력: %s, 방향: %s, 보유한 일반 포탄: %s, 보유한 메가 포탄: %s\n",
                        value[0], value[1], value[2], value[3]);
            } else if (key.equals("H")) {
                System.out.printf("H (아군 포탑) - 체력: %s\n", value[0]);
            } else {
                System.out.printf("%s (아군 탱크) - 체력: %s\n", key, value[0]);
            }
        }

        System.out.printf("\n[적군 정보] (적군 수: %d)\n", enemies.size());
        for (String key : enemies.keySet()) {
            String[] value = enemies.get(key);
            if (key.equals("X")) {
                System.out.printf("X (적군 포탑) - 체력: %s\n", value[0]);
            } else {
                System.out.printf("%s (적군 탱크) - 체력: %s\n", key, value[0]);
            }
        }

        System.out.printf("\n[암호문 정보] (암호문 수: %d)\n", codes.length);
        for (String code : codes) {
            System.out.println(code);
        }
    }
}
