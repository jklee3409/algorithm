import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * 배틀싸피 팀 대항전 로컬 시뮬레이터.
 *
 * - 127.0.0.1:8747 에서 배틀싸피 메인 프로그램처럼 대기하고, 봇(수정 없는 소켓 코드)을 접속시켜 대전한다.
 * - 봇은 각자 별도 URLClassLoader 로 로딩 → static 상태가 서로 격리된 채 한 JVM 에서 6개가 동시에 돈다.
 * - 규칙은 사용자 매뉴얼 기준이며, 매뉴얼에 없는 부분은 docs/DESIGN.md 3장의 가정을 따른다.
 *
 * 사용 예)
 *   java BattleSim league --bots base=dirA,A=dirB,B=dirC,C=dirD --scenarios all --games 2 --out results.csv
 *   java BattleSim single --a dirA --b dirB --map maps/practice.txt --turretHp 100 --order alt --verbose
 */
public class BattleSim {
    static PrintStream OUT;
    static final int[] DR = {-1, 1, 0, 0};
    static final int[] DC = {0, 0, -1, 1};
    static final String DIRS = "UDLR";
    static final String[] WORDS = {
        "BATTLE", "SSAFY", "SAMSUNG", "PYTHON", "KOTLIN", "VICTORY", "STRATEGY", "COMMANDER", "TACTICS", "ARMOR",
        "CANNON", "RADAR", "BRIDGE", "FOREST", "DESERT", "RIVER", "MOUNTAIN", "CASTLE", "KNIGHT", "DRAGON",
        "PHOENIX", "THUNDER", "LIGHTNING", "HORIZON", "JOURNEY", "COURAGE", "HONOR", "GLORY", "FREEDOM", "JUSTICE",
        "WISDOM", "HARMONY", "BALANCE", "ENERGY", "SPIRIT", "FUTURE", "HISTORY", "SCIENCE", "NATURE", "UNIVERSE",
        "GALAXY", "PLANET", "ROCKET", "SATELLITE", "NETWORK", "SERVER", "CLIENT", "SOCKET", "PACKET", "BINARY",
        "MATRIX", "VECTOR", "GRAPH", "QUEUE", "STACK", "HEAP", "TREE", "SEARCH", "SORTING", "RECURSION",
        "DYNAMIC", "GREEDY", "BACKTRACK", "PERMUTATION", "COMBINATION", "PROBABILITY", "STATISTICS", "LOGIC",
        "PUZZLE", "RIDDLE", "MYSTERY", "SECRET", "TREASURE", "ISLAND", "OCEAN", "HARBOR", "SUMMER", "WINTER",
        "AUTUMN", "SPRING", "MORNING", "EVENING", "MIDNIGHT", "SUNRISE", "SUNSET", "RAINBOW", "WEATHER", "CLOUD",
        "SILVER", "GOLDEN", "PURPLE", "ORANGE", "YELLOW", "GREEN", "BLUE", "WHITE", "BLACK", "DIAMOND",
        "CHAMPION", "WINNER", "PLAYER", "MEMBER", "LEADER", "PARTNER", "FRIEND", "FAMILY", "SCHOOL", "STUDENT",
        "TEACHER", "LIBRARY", "COMPUTER", "KEYBOARD", "MONITOR", "PRINTER", "WINDOW", "GARDEN", "KITCHEN", "BICYCLE"
    };

    // ------------------------------------------------------------------ 설정
    static final class Config {
        String mapName = "practice";
        int turretHp = 100;
        boolean altOrder = true;     // true: A1 B1 A2 B2 A3 B3 / false: A1 A2 A3 B1 B2 B3
        char zoneAs = 'G';           // 맵의 Z(포탑 주변 색칠 구역)를 무엇으로 볼지
        int normalAmmo = 999, megaStart = 0;
        int timeoutMs = 3000;
        int zoneRule = 2;            // 포탑 주변(체비셰프 거리 ≤ zoneRule) 진입 시 옐로카드, 3장 누적 시 제거 (0=끔, 가정)
        int maxTurn = 100;
        long seed = 1;
        int rotate = 0;              // 0~3 (90도 단위)
        boolean verbose = false;
        String tag() { return mapName + "/hp" + turretHp + "/" + (altOrder ? "alt" : "seq") + "/z" + zoneAs + "/r" + rotate; }
    }

    // ------------------------------------------------------------------ 게임 상태
    static final class Tank {
        int team, idx, r, c, hp = 100, normal, mega, turns, invalid, stays, decodeTry, decodeOk, shots, sandSteps, yellow;
        char dir;
        boolean alive = true;
        String plain;
        Socket sock;
        InputStream in;
        OutputStream out;
        Thread thread;
        String botName;
        long maxMs;
        String label() { return (team == 0 ? "A" : "B") + idx; }
    }

    static final class Result {
        String winner = "?", reason = "", tag;
        int rounds, dmgA, dmgB, invalidA, invalidB, killsByA, killsByB, megaA, megaB, decodeA, decodeB;
        int turretA, turretB, staysA, staysB, sandA, sandB, yellowA, yellowB;
        long maxMsA, maxMsB;
        String botA, botB;
    }

    static final class Game {
        Config cfg;
        int H, W;
        char[][] terr;
        Tank[][] at;
        int[][] turretPos = new int[2][];
        int[] turretHp = new int[2];
        int[] turretMax = new int[2];
        List<Tank> tanks = new ArrayList<>();
        int[] initCount = new int[2];
        int shift;
        Random rnd;
        Result res = new Result();
        boolean over = false;
        StringBuilder trace = new StringBuilder();

        boolean inb(int r, int c) { return r >= 0 && c >= 0 && r < H && c < W; }

        boolean isTurret(int r, int c) {
            for (int t = 0; t < 2; t++) if (turretPos[t] != null && turretPos[t][0] == r && turretPos[t][1] == c && turretHp[t] > 0) return true;
            return false;
        }

        int turretTeamAt(int r, int c) {
            for (int t = 0; t < 2; t++) if (turretPos[t] != null && turretPos[t][0] == r && turretPos[t][1] == c && turretHp[t] > 0) return t;
            return -1;
        }

        boolean adjF(int r, int c) {
            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (inb(nr, nc) && terr[nr][nc] == 'F') return true;
            }
            return false;
        }

        String encrypt(String p) {
            StringBuilder sb = new StringBuilder();
            for (char ch : p.toCharArray()) sb.append((char) ('A' + (ch - 'A' + shift) % 26));
            return sb.toString();
        }

        // 해당 탱크 시점의 입력 데이터
        String stateFor(Tank me) {
            int myTeam = me.team, en = 1 - myTeam;
            String[][] cell = new String[H][W];
            for (int r = 0; r < H; r++) for (int c = 0; c < W; c++) cell[r][c] = String.valueOf(terr[r][c]);
            for (int t = 0; t < 2; t++) if (turretHp[t] > 0) cell[turretPos[t][0]][turretPos[t][1]] = (t == myTeam) ? "H" : "X";
            List<Tank> allies = new ArrayList<>(), foes = new ArrayList<>();
            for (Tank k : tanks) {
                if (!k.alive) continue;
                if (k == me) cell[k.r][k.c] = "M";
                else if (k.team == myTeam) { cell[k.r][k.c] = "M" + k.idx; allies.add(k); }
                else { cell[k.r][k.c] = "E" + k.idx; foes.add(k); }
            }
            List<String> codeLines = new ArrayList<>();
            me.plain = null;
            if (adjF(me.r, me.c)) {
                me.plain = WORDS[rnd.nextInt(WORDS.length)];
                codeLines.add(encrypt(me.plain));
            }
            int nAllies = 1 + allies.size() + (turretHp[myTeam] > 0 ? 1 : 0);
            int nEnemies = foes.size() + (turretHp[en] > 0 ? 1 : 0);
            StringBuilder sb = new StringBuilder();
            sb.append(H).append(' ').append(W).append(' ').append(nAllies).append(' ').append(nEnemies).append(' ').append(codeLines.size()).append('\n');
            for (int r = 0; r < H; r++) {
                for (int c = 0; c < W; c++) { if (c > 0) sb.append(' '); sb.append(cell[r][c]); }
                sb.append('\n');
            }
            sb.append("M ").append(me.hp).append(' ').append(me.dir).append(' ').append(me.normal).append(' ').append(me.mega).append('\n');
            for (Tank k : allies) sb.append("M").append(k.idx).append(' ').append(k.hp).append('\n');
            if (turretHp[myTeam] > 0) sb.append("H ").append(turretHp[myTeam]).append('\n');
            for (Tank k : foes) sb.append("E").append(k.idx).append(' ').append(k.hp).append('\n');
            if (turretHp[en] > 0) sb.append("X ").append(turretHp[en]).append('\n');
            for (String s : codeLines) sb.append(s).append('\n');
            String s = sb.toString();
            if (s.endsWith("\n")) s = s.substring(0, s.length() - 1);
            return s;
        }

        void damageUnitAt(Tank shooter, int r, int c, int dmg) {
            int tt = turretTeamAt(r, c);
            if (tt >= 0) {
                turretHp[tt] -= dmg;
                if (verbose()) trace.append("    turret ").append(tt == 0 ? "A" : "B").append(" -").append(dmg).append(" → ").append(turretHp[tt]).append('\n');
                if (turretHp[tt] <= 0) {
                    turretHp[tt] = 0;
                    finish(tt == 0 ? "B" : "A", "turret destroyed by " + shooter.label());
                }
                return;
            }
            Tank v = at[r][c];
            if (v == null) return;
            v.hp -= dmg;
            if (verbose()) trace.append("    ").append(v.label()).append(" -").append(dmg).append(" → ").append(v.hp).append('\n');
            if (v.hp <= 0) kill(v, shooter);
        }

        void kill(Tank v, Tank by) {
            v.hp = 0;
            v.alive = false;
            at[v.r][v.c] = null;
            if (by != null && by.team != v.team) { if (by.team == 0) res.killsByA++; else res.killsByB++; }
            int alive = 0;
            for (Tank k : tanks) if (k.team == v.team && k.alive) alive++;
            int dead = initCount[v.team] - alive;
            boolean lose = initCount[v.team] >= 3 ? dead >= 2 : alive == 0;
            if (lose) finish(v.team == 0 ? "B" : "A", "tanks destroyed (" + (v.team == 0 ? "A" : "B") + " lost " + dead + ")");
        }

        void finish(String winner, String reason) {
            if (over) return;
            over = true;
            res.winner = winner;
            res.reason = reason;
        }

        boolean verbose() { return cfg.verbose; }

        void apply(Tank t, String cmd) {
            String[] tk = cmd.trim().split("\\s+");
            int i = 0;
            if (tk.length == 0 || tk[0].isEmpty()) { t.invalid++; return; }
            if (tk[0].length() == 1 && DIRS.indexOf(tk[0].charAt(0)) >= 0 && tk.length >= 2) {
                t.dir = tk[0].charAt(0);
                i = 1;
            }
            String act = tk[i];
            int d = DIRS.indexOf(t.dir);
            switch (act) {
                case "A": {
                    int nr = t.r + DR[d], nc = t.c + DC[d];
                    if (!inb(nr, nc) || at[nr][nc] != null || isTurret(nr, nc) || (terr[nr][nc] != 'G' && terr[nr][nc] != 'S')) {
                        t.invalid++;
                        if (cfg.verbose) trace.append("    INVALID MOVE ").append(t.label()).append(" -> (").append(nr).append(",").append(nc).append(") ").append(inb(nr, nc) ? String.valueOf(terr[nr][nc]) + (at[nr][nc] != null ? "/" + at[nr][nc].label() : "") : "OOB").append('\n');
                        return;
                    }
                    at[t.r][t.c] = null;
                    t.r = nr; t.c = nc;
                    at[nr][nc] = t;
                    if (terr[nr][nc] == 'S') {
                        t.sandSteps++;
                        t.hp -= 10;
                        if (t.hp <= 0) { kill(t, null); return; }
                    }
                    if (cfg.zoneRule > 0) {
                        for (int tt = 0; tt < 2; tt++) {
                            int cd = Math.max(Math.abs(turretPos[tt][0] - nr), Math.abs(turretPos[tt][1] - nc));
                            if (cd <= cfg.zoneRule) {
                                t.yellow++;
                                if (verbose()) trace.append("    YELLOW ").append(t.label()).append(" #").append(t.yellow).append('\n');
                                if (t.yellow >= 3) kill(t, null);
                                break;
                            }
                        }
                    }
                    return;
                }
                case "F": {
                    boolean mega = tk.length > i + 1 && tk[i + 1].equals("M");
                    if (mega) { if (t.mega <= 0) { t.invalid++; return; } t.mega--; }
                    else { if (t.normal <= 0) { t.invalid++; return; } t.normal--; }
                    t.shots++;
                    int dmg = mega ? 70 : 30;
                    for (int k = 1; k <= 3; k++) {
                        int nr = t.r + DR[d] * k, nc = t.c + DC[d] * k;
                        if (!inb(nr, nc)) break;
                        if (at[nr][nc] != null || isTurret(nr, nc)) { damageUnitAt(t, nr, nc, dmg); break; }
                        char tr = terr[nr][nc];
                        if (tr == 'T') { terr[nr][nc] = 'G'; break; }
                        if (tr == 'R' || tr == 'F') break;
                    }
                    return;
                }
                case "S":
                    t.stays++;
                    return;
                case "G": {
                    t.decodeTry++;
                    StringBuilder text = new StringBuilder();
                    for (int k = i + 1; k < tk.length; k++) { if (text.length() > 0) text.append(' '); text.append(tk[k]); }
                    if (t.plain != null && adjF(t.r, t.c) && text.toString().equals(t.plain)) { t.mega++; t.decodeOk++; }
                    else if (!adjF(t.r, t.c)) t.invalid++;
                    return;
                }
                default:
                    t.invalid++;
            }
        }

        void judge() {
            int[] dmg = new int[2];
            int[] dist = new int[2];
            for (Tank k : tanks) dmg[k.team] += 100 - Math.max(0, k.hp);
            for (int t = 0; t < 2; t++) dmg[t] += turretMax[t] - turretHp[t];
            for (Tank k : tanks) if (k.alive) {
                int[] tp = turretPos[1 - k.team];
                dist[k.team] += Math.abs(k.r - tp[0]) + Math.abs(k.c - tp[1]);
            }
            if (dmg[0] != dmg[1]) finish(dmg[0] < dmg[1] ? "A" : "B", "turn limit: damage " + dmg[0] + " vs " + dmg[1]);
            else if (dist[0] != dist[1]) finish(dist[0] < dist[1] ? "A" : "B", "turn limit: distance " + dist[0] + " vs " + dist[1]);
            else finish("B", "turn limit: full tie → second player");
        }

        void fillResult() {
            for (Tank k : tanks) {
                if (k.team == 0) {
                    res.dmgA += 100 - Math.max(0, k.hp); res.invalidA += k.invalid; res.megaA += k.decodeOk; res.decodeA += k.decodeTry;
                    res.maxMsA = Math.max(res.maxMsA, k.maxMs); res.staysA += k.stays; res.sandA += k.sandSteps; res.yellowA += k.yellow;
                } else {
                    res.dmgB += 100 - Math.max(0, k.hp); res.invalidB += k.invalid; res.megaB += k.decodeOk; res.decodeB += k.decodeTry;
                    res.maxMsB = Math.max(res.maxMsB, k.maxMs); res.staysB += k.stays; res.sandB += k.sandSteps; res.yellowB += k.yellow;
                }
            }
            res.dmgA += turretMax[0] - turretHp[0];
            res.dmgB += turretMax[1] - turretHp[1];
            res.turretA = turretHp[0];
            res.turretB = turretHp[1];
        }

        String render() {
            StringBuilder sb = new StringBuilder();
            for (int r = 0; r < H; r++) {
                for (int c = 0; c < W; c++) {
                    String s = String.valueOf(terr[r][c]);
                    int tt = turretTeamAt(r, c);
                    if (tt >= 0) s = tt == 0 ? "H" : "X";
                    if (at[r][c] != null) s = at[r][c].label();
                    sb.append(String.format("%-3s", s));
                }
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    // ------------------------------------------------------------------ 맵
    static final class MapDef {
        String name;
        char[][] terr;
        int[][] turret = new int[2][];
        Map<String, int[]> spawns = new TreeMap<>();
    }

    static MapDef loadMap(Path p) throws IOException {
        List<String[]> rows = new ArrayList<>();
        for (String line : Files.readAllLines(p, StandardCharsets.UTF_8)) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            rows.add(line.split("\\s+"));
        }
        MapDef m = new MapDef();
        m.name = p.getFileName().toString().replace(".txt", "");
        int H = rows.size(), W = rows.get(0).length;
        m.terr = new char[H][W];
        for (int r = 0; r < H; r++) for (int c = 0; c < W; c++) {
            String t = rows.get(r)[c];
            char ch = 'G';
            if (t.equals("H")) m.turret[0] = new int[]{r, c};
            else if (t.equals("X")) m.turret[1] = new int[]{r, c};
            else if (t.length() == 2 && (t.charAt(0) == 'A' || t.charAt(0) == 'B')) m.spawns.put(t, new int[]{r, c});
            else ch = t.charAt(0);
            m.terr[r][c] = ch;
        }
        return m;
    }

    static MapDef rotate(MapDef m, int times) {
        MapDef cur = m;
        for (int k = 0; k < (times % 4 + 4) % 4; k++) {
            int H = cur.terr.length, W = cur.terr[0].length;
            MapDef n = new MapDef();
            n.name = cur.name;
            n.terr = new char[W][H];
            for (int r = 0; r < H; r++) for (int c = 0; c < W; c++) n.terr[c][H - 1 - r] = cur.terr[r][c];
            for (int t = 0; t < 2; t++) n.turret[t] = new int[]{cur.turret[t][1], H - 1 - cur.turret[t][0]};
            for (Map.Entry<String, int[]> e : cur.spawns.entrySet()) n.spawns.put(e.getKey(), new int[]{e.getValue()[1], H - 1 - e.getValue()[0]});
            cur = n;
        }
        return cur;
    }

    // 점대칭 랜덤 맵 생성 (type: sand / forest / open / mixed)
    static MapDef generate(String type, long seed) {
        Random rnd = new Random(seed * 7919L + type.hashCode());
        int N = 16;
        for (int attempt = 0; attempt < 200; attempt++) {
            MapDef m = new MapDef();
            m.name = "gen-" + type + "-" + seed;
            char[][] g = new char[N][N];
            for (char[] row : g) Arrays.fill(row, 'G');
            m.turret[0] = new int[]{15, 0};
            m.turret[1] = new int[]{0, 15};
            int[][] aSp = {{5, 0}, {6, 0}, {11, 0}};
            if (rnd.nextBoolean()) aSp = new int[][]{{9, 0}, {12, 1}, {14, 3}};
            if (rnd.nextInt(3) == 0) aSp = new int[][]{{4, 0}, {8, 0}, {12, 0}};
            for (int i = 0; i < 3; i++) {
                m.spawns.put("A" + (i + 1), aSp[i]);
                m.spawns.put("B" + (i + 1), new int[]{N - 1 - aSp[i][0], N - 1 - aSp[i][1]});
            }
            boolean[][] keep = new boolean[N][N];
            for (int[] p : new int[][]{m.turret[0], m.turret[1]})
                for (int dr = -2; dr <= 2; dr++) for (int dc = -2; dc <= 2; dc++) {
                    int r = p[0] + dr, c = p[1] + dc;
                    if (r >= 0 && c >= 0 && r < N && c < N) keep[r][c] = true;
                }
            for (int[] p : m.spawns.values()) keep[p[0]][p[1]] = true;
            // 보급시설: 하단 가장자리 2개 + 대칭
            int f1 = 4 + rnd.nextInt(3), f2 = 8 + rnd.nextInt(3);
            int[][] fs = {{15, f1}, {15, f2}};
            if (rnd.nextInt(3) == 0) fs = new int[][]{{13, f1}, {9 + rnd.nextInt(3), 2 + rnd.nextInt(2)}};
            for (int[] f : fs) { g[f[0]][f[1]] = 'F'; g[N - 1 - f[0]][N - 1 - f[1]] = 'F'; keep[f[0]][f[1]] = true; keep[N - 1 - f[0]][N - 1 - f[1]] = true; }
            int nR, nT, nW, nS;
            switch (type) {
                case "sand": nR = 5; nT = 4; nW = 3; nS = 14; break;
                case "forest": nR = 3; nT = 16; nW = 2; nS = 2; break;
                case "open": nR = 4; nT = 3; nW = 2; nS = 2; break;
                default: nR = 5; nT = 7; nW = 3; nS = 5;
            }
            placeClusters(g, keep, rnd, 'R', nR, 1, 3);
            placeClusters(g, keep, rnd, 'T', nT, 1, 4);
            placeClusters(g, keep, rnd, 'W', nW, 2, 4);
            if (type.equals("sand")) {
                // 주요 경로(중앙 대각 띠)를 모래로 덮기
                for (int r = 0; r < N; r++) for (int c = 0; c < N; c++) {
                    int diag = Math.abs((r + c) - 15);
                    if (diag <= 1 && g[r][c] == 'G' && !keep[r][c] && rnd.nextInt(10) < 7) { g[r][c] = 'S'; g[N - 1 - r][N - 1 - c] = 'S'; }
                }
            }
            placeClusters(g, keep, rnd, 'S', nS, 1, 4);
            if (type.equals("open")) {
                // 배후 공간을 넓게 비우기
                for (int r = 0; r < N; r++) for (int c = 0; c < N; c++)
                    if ((r >= 10 && c <= 5) && g[r][c] != 'F') { g[r][c] = 'G'; g[N - 1 - r][N - 1 - c] = 'G'; }
            }
            m.terr = g;
            if (connected(m)) return m;
        }
        throw new IllegalStateException("map gen failed");
    }

    static void placeClusters(char[][] g, boolean[][] keep, Random rnd, char ch, int count, int minLen, int maxLen) {
        int N = g.length;
        for (int i = 0; i < count; i++) {
            int r = rnd.nextInt(N), c = rnd.nextInt(N);
            int len = minLen + rnd.nextInt(maxLen - minLen + 1);
            boolean horiz = rnd.nextBoolean();
            boolean blob = rnd.nextInt(3) == 0;
            for (int k = 0; k < len; k++) {
                int rr = r + (horiz ? 0 : k), cc = c + (horiz ? k : 0);
                for (int b = 0; b < (blob ? 2 : 1); b++) {
                    int r2 = rr + (horiz ? b : 0), c2 = cc + (horiz ? 0 : b);
                    if (r2 < 0 || c2 < 0 || r2 >= N || c2 >= N) continue;
                    if (keep[r2][c2] || keep[N - 1 - r2][N - 1 - c2] || g[r2][c2] == 'F') continue;
                    g[r2][c2] = ch;
                    g[N - 1 - r2][N - 1 - c2] = ch;
                }
            }
        }
    }

    // 모든 시작 지점에서 상대 포탑 사격 지점까지 (나무 통과 허용) 도달 가능한지
    static boolean connected(MapDef m) {
        int N = m.terr.length;
        for (Map.Entry<String, int[]> e : m.spawns.entrySet()) {
            int team = e.getKey().charAt(0) == 'A' ? 0 : 1;
            int[] tp = m.turret[1 - team];
            boolean[][] seen = new boolean[N][N];
            ArrayDeque<int[]> q = new ArrayDeque<>();
            q.add(e.getValue());
            seen[e.getValue()[0]][e.getValue()[1]] = true;
            boolean ok = false;
            while (!q.isEmpty() && !ok) {
                int[] p = q.poll();
                if ((p[0] == tp[0] && Math.abs(p[1] - tp[1]) <= 3) || (p[1] == tp[1] && Math.abs(p[0] - tp[0]) <= 3)) ok = true;
                for (int d = 0; d < 4; d++) {
                    int nr = p[0] + DR[d], nc = p[1] + DC[d];
                    if (nr < 0 || nc < 0 || nr >= N || nc >= N || seen[nr][nc]) continue;
                    char t = m.terr[nr][nc];
                    if (t == 'W' || t == 'R' || t == 'F') continue;
                    if ((nr == m.turret[0][0] && nc == m.turret[0][1]) || (nr == m.turret[1][0] && nc == m.turret[1][1])) continue;
                    seen[nr][nc] = true;
                    q.add(new int[]{nr, nc});
                }
            }
            if (!ok) return false;
        }
        return true;
    }

    // ------------------------------------------------------------------ 봇 실행
    static ServerSocket server;

    static Thread launchBot(String dir, String name, Map<String, Throwable> crashes) throws Exception {
        URLClassLoader cl = new URLClassLoader(new URL[]{new File(dir).toURI().toURL()}, ClassLoader.getPlatformClassLoader());
        Class<?> k = cl.loadClass("Main");
        Method m = k.getMethod("main", String[].class);
        Thread t = new Thread(() -> {
            try {
                m.invoke(null, (Object) new String[0]);
            } catch (Throwable e) {
                Throwable c = e.getCause() != null ? e.getCause() : e;
                synchronized (crashes) { crashes.put(name, c); }
            }
        }, "bot-" + name);
        t.setDaemon(true);
        t.start();
        return t;
    }

    static String readCmd(Tank t, int timeoutMs) throws IOException {
        t.sock.setSoTimeout(timeoutMs);
        byte[] buf = new byte[4096];
        int n = t.in.read(buf);
        if (n <= 0) throw new EOFException("closed");
        return new String(buf, 0, n, StandardCharsets.UTF_8);
    }

    static void send(Tank t, String s) throws IOException {
        t.out.write(s.getBytes(StandardCharsets.UTF_8));
        t.out.flush();
    }

    static Result play(MapDef base, Config cfg, String[] botDirsA, String[] botDirsB, String nameA, String nameB) throws Exception {
        MapDef m = rotate(base, cfg.rotate);
        Game g = new Game();
        g.cfg = cfg;
        g.rnd = new Random(cfg.seed);
        g.shift = 1 + g.rnd.nextInt(25);
        g.H = m.terr.length; g.W = m.terr[0].length;
        g.terr = new char[g.H][g.W];
        for (int r = 0; r < g.H; r++) for (int c = 0; c < g.W; c++) {
            char ch = m.terr[r][c];
            g.terr[r][c] = ch == 'Z' ? cfg.zoneAs : ch;
        }
        g.at = new Tank[g.H][g.W];
        for (int t = 0; t < 2; t++) {
            g.turretPos[t] = m.turret[t];
            g.turretHp[t] = cfg.turretHp;
            g.turretMax[t] = cfg.turretHp;
            g.terr[m.turret[t][0]][m.turret[t][1]] = 'G';
        }
        g.res.tag = cfg.tag();
        g.res.botA = nameA;
        g.res.botB = nameB;
        int n = botDirsA.length;
        Map<String, Throwable> crashes = new HashMap<>();
        List<Tank> connectOrder = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int team = 0; team < 2; team++) {
                Tank t = new Tank();
                t.team = team; t.idx = i + 1;
                int[] sp = m.spawns.get((team == 0 ? "A" : "B") + (i + 1));
                t.r = sp[0]; t.c = sp[1];
                t.dir = team == 0 ? 'R' : 'L';
                t.normal = cfg.normalAmmo; t.mega = cfg.megaStart;
                t.botName = team == 0 ? nameA : nameB;
                g.at[t.r][t.c] = t;
                g.tanks.add(t);
                connectOrder.add(t);
                g.initCount[team]++;
            }
        }
        try {
            for (Tank t : connectOrder) {
                t.thread = launchBot(t.team == 0 ? botDirsA[t.idx - 1] : botDirsB[t.idx - 1], t.label(), crashes);
                server.setSoTimeout(15000);
                t.sock = server.accept();
                t.sock.setTcpNoDelay(true);
                t.in = t.sock.getInputStream();
                t.out = t.sock.getOutputStream();
                String init = readCmd(t, 15000);
                if (!init.startsWith("INIT")) throw new IllegalStateException("bad init: " + init);
            }
            List<Tank> order = new ArrayList<>();
            if (cfg.altOrder) { for (int i = 0; i < n; i++) for (int team = 0; team < 2; team++) order.add(find(g, team, i + 1)); }
            else { for (int team = 0; team < 2; team++) for (int i = 0; i < n; i++) order.add(find(g, team, i + 1)); }

            int round = 0;
            while (!g.over) {
                round++;
                boolean anyActed = false;
                for (Tank t : order) {
                    if (g.over) break;
                    if (!t.alive || t.turns >= cfg.maxTurn) continue;
                    anyActed = true;
                    String state = g.stateFor(t);
                    long t0 = System.nanoTime();
                    String cmd;
                    try {
                        send(t, state);
                        cmd = readCmd(t, cfg.timeoutMs);
                    } catch (SocketTimeoutException e) {
                        g.finish(t.team == 0 ? "B" : "A", "timeout " + t.label() + (crashes.containsKey(t.label()) ? " crash=" + crashes.get(t.label()) : ""));
                        break;
                    } catch (IOException e) {
                        g.finish(t.team == 0 ? "B" : "A", "disconnect " + t.label() + (crashes.containsKey(t.label()) ? " crash=" + crashes.get(t.label()) : ""));
                        break;
                    }
                    long ms = (System.nanoTime() - t0) / 1_000_000;
                    t.maxMs = Math.max(t.maxMs, ms);
                    t.turns++;
                    if (cfg.verbose) g.trace.append(String.format("R%02d %s@(%d,%d) hp%d m%d : %s%n", round, t.label(), t.r, t.c, t.hp, t.mega, cmd.trim()));
                    g.apply(t, cmd);
                }
                if (!g.over && !anyActed) g.judge();
                g.res.rounds = round;
            }
            if (cfg.verbose) {
                OUT.println(g.trace);
                OUT.println(g.render());
            }
        } finally {
            for (Tank t : g.tanks) { try { if (t.sock != null) t.sock.close(); } catch (IOException ignored) { } }
            for (Tank t : g.tanks) { if (t.thread != null) t.thread.join(3000); }
        }
        g.fillResult();
        return g.res;
    }

    static Tank find(Game g, int team, int idx) {
        for (Tank t : g.tanks) if (t.team == team && t.idx == idx) return t;
        return null;
    }

    // ------------------------------------------------------------------ 실행부
    static Map<String, String> parseArgs(String[] a, int from) {
        Map<String, String> m = new HashMap<>();
        for (int i = from; i < a.length; i++) {
            if (a[i].startsWith("--")) {
                String k = a[i].substring(2);
                String v = (i + 1 < a.length && !a[i + 1].startsWith("--")) ? a[++i] : "true";
                m.put(k, v);
            }
        }
        return m;
    }

    static MapDef mapByName(String name, Path mapDir) throws IOException {
        if (name.startsWith("gen:")) {
            String[] p = name.split(":");
            return generate(p[1], Long.parseLong(p[2]));
        }
        return loadMap(mapDir.resolve(name + ".txt"));
    }

    public static void main(String[] args) throws Exception {
        OUT = System.out;
        System.setOut(new PrintStream(new OutputStream() { public void write(int b) { } public void write(byte[] b, int o, int l) { } }));
        System.setProperty("battle.console", "off");
        if (System.getProperty("battle.log") == null) System.setProperty("battle.log", "off");
        System.setProperty("battle.cipherCache", "off");
        String mode = args.length > 0 ? args[0] : "single";
        Map<String, String> o = parseArgs(args, 1);
        Path mapDir = Paths.get(o.getOrDefault("maps", "maps"));
        server = new ServerSocket();
        server.setReuseAddress(true);
        server.bind(new InetSocketAddress("127.0.0.1", 8747));
        try {
            if (mode.equals("single")) single(o, mapDir);
            else if (mode.equals("league")) league(o, mapDir);
            else if (mode.equals("showmap")) {
                MapDef m = mapByName(o.get("map"), mapDir);
                for (char[] row : m.terr) OUT.println(new String(row).replace("", " ").trim());
                OUT.println("spawns=" + spawnStr(m));
            }
        } finally {
            server.close();
        }
        System.exit(0);
    }

    static String spawnStr(MapDef m) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, int[]> e : m.spawns.entrySet()) sb.append(e.getKey()).append(Arrays.toString(e.getValue())).append(' ');
        return sb.toString();
    }

    static Config cfgFrom(Map<String, String> o) {
        Config c = new Config();
        c.turretHp = Integer.parseInt(o.getOrDefault("turretHp", "100"));
        c.altOrder = !o.getOrDefault("order", "alt").equals("seq");
        c.zoneAs = o.getOrDefault("zone", "G").charAt(0);
        c.seed = Long.parseLong(o.getOrDefault("seed", "1"));
        c.rotate = Integer.parseInt(o.getOrDefault("rotate", "0"));
        c.timeoutMs = Integer.parseInt(o.getOrDefault("timeout", "3000"));
        c.verbose = o.containsKey("verbose");
        return c;
    }

    static void single(Map<String, String> o, Path mapDir) throws Exception {
        Config c = cfgFrom(o);
        String map = o.getOrDefault("map", "practice");
        c.mapName = map;
        int n = Integer.parseInt(o.getOrDefault("n", "3"));
        String[] a = new String[n], b = new String[n];
        Arrays.fill(a, o.get("a"));
        Arrays.fill(b, o.get("b"));
        Result r = play(mapByName(map, mapDir), c, a, b, o.getOrDefault("nameA", "A"), o.getOrDefault("nameB", "B"));
        OUT.println(header());
        OUT.println(row(r));
    }

    static String header() {
        return "tag,botA,botB,winner,winnerBot,reason,rounds,dmgA,dmgB,turretA,turretB,killsByA,killsByB,invalidA,invalidB,"
                + "megaA,megaB,decodeA,decodeB,staysA,staysB,sandA,sandB,maxMsA,maxMsB,yellowA,yellowB";
    }

    static String row(Result r) {
        String wb = r.winner.equals("A") ? r.botA : r.winner.equals("B") ? r.botB : "?";
        return String.join(",", r.tag, r.botA, r.botB, r.winner, wb, "\"" + r.reason.replace('"', '\'') + "\"",
                "" + r.rounds, "" + r.dmgA, "" + r.dmgB, "" + r.turretA, "" + r.turretB, "" + r.killsByA, "" + r.killsByB,
                "" + r.invalidA, "" + r.invalidB, "" + r.megaA, "" + r.megaB, "" + r.decodeA, "" + r.decodeB,
                "" + r.staysA, "" + r.staysB, "" + r.sandA, "" + r.sandB, "" + r.maxMsA, "" + r.maxMsB, "" + r.yellowA, "" + r.yellowB);
    }

    // "dir" 이면 3슬롯 동일 코드, "dir1+dir2+dir3" 이면 Player1/2/3 슬롯별 코드
    static String[] teamDirs(String v) {
        String[] a = new String[3];
        if (v.contains("+")) {
            String[] p = v.split("\\+");
            for (int i = 0; i < 3; i++) a[i] = p[Math.min(i, p.length - 1)];
        } else Arrays.fill(a, v);
        return a;
    }

    // 리그: 모든 봇 쌍 × 시나리오 × 진영 교대
    static void league(Map<String, String> o, Path mapDir) throws Exception {
        LinkedHashMap<String, String> bots = new LinkedHashMap<>();
        for (String kv : o.get("bots").split(",")) { String[] p = kv.split("="); bots.put(p[0], p[1]); }
        List<String> maps = Arrays.asList(o.getOrDefault("mapList", "practice").split(","));
        List<Integer> hps = new ArrayList<>();
        for (String s : o.getOrDefault("hps", "100").split(",")) hps.add(Integer.parseInt(s));
        List<Boolean> orders = new ArrayList<>();
        for (String s : o.getOrDefault("orders", "alt").split(",")) orders.add(!s.equals("seq"));
        List<Character> zones = new ArrayList<>();
        for (String s : o.getOrDefault("zones", "G").split(",")) zones.add(s.charAt(0));
        int games = Integer.parseInt(o.getOrDefault("games", "1"));
        long seed0 = Long.parseLong(o.getOrDefault("seed", "1"));
        String focus = o.get("focus");           // 지정 시 해당 봇이 포함된 대전만
        Set<String> cands = new HashSet<>();     // 지정 시 후보 vs 고정 상대 대전만 (후보끼리/상대끼리 제외)
        if (o.containsKey("cands")) cands.addAll(Arrays.asList(o.get("cands").split(",")));
        int shard = Integer.parseInt(o.getOrDefault("shard", "0")), shards = Integer.parseInt(o.getOrDefault("shards", "1"));
        PrintStream csv = new PrintStream(new FileOutputStream(o.getOrDefault("out", "results.csv")), true, "UTF-8");
        csv.println(header());
        List<String> names = new ArrayList<>(bots.keySet());
        int job = 0;
        Map<String, int[]> wins = new TreeMap<>();
        for (String map : maps) for (int hp : hps) for (boolean alt : orders) for (char z : zones) for (int gi = 0; gi < games; gi++) {
            MapDef md = mapByName(map, mapDir);
            for (int i = 0; i < names.size(); i++) for (int j = 0; j < names.size(); j++) {
                if (i == j) continue;
                String na = names.get(i), nb = names.get(j);
                if (focus != null && !na.equals(focus) && !nb.equals(focus)) continue;
                if (!cands.isEmpty() && cands.contains(na) == cands.contains(nb)) continue;
                if ((job++ % shards) != shard) continue;
                Config c = new Config();
                c.mapName = map; c.turretHp = hp; c.altOrder = alt; c.zoneAs = z;
                c.seed = seed0 * 1000003L + gi * 7919L + map.hashCode() * 31L + hp;
                c.rotate = (int) (Math.abs(c.seed) % 4);
                String[] a = teamDirs(bots.get(na)), b = teamDirs(bots.get(nb));
                Result r;
                try {
                    r = play(md, c, a, b, na, nb);
                } catch (Exception e) {
                    r = new Result();
                    r.tag = c.tag(); r.botA = na; r.botB = nb; r.winner = "?"; r.reason = "sim error " + e;
                }
                csv.println(row(r));
                String wb = r.winner.equals("A") ? na : r.winner.equals("B") ? nb : null;
                String key = na + " vs " + nb;
                int[] w = wins.computeIfAbsent(key, k -> new int[3]);
                if (wb == null) w[2]++; else if (wb.equals(na)) w[0]++; else w[1]++;
                OUT.println(String.format("[%s] %-22s A=%-6s B=%-6s → %s (%s) r=%d dmg %d/%d inv %d/%d mega %d/%d ms %d/%d",
                        c.tag(), key, na, nb, wb, r.reason, r.rounds, r.dmgA, r.dmgB, r.invalidA, r.invalidB, r.megaA, r.megaB, r.maxMsA, r.maxMsB));
            }
        }
        csv.close();
    }
}
