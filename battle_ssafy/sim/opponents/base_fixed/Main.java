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
        String NICKNAME = "대전3_이종규";
        String gameData = init(NICKNAME);
        
        ///////////////////////////////
        // 알고리즘 메인 부분 구현 시작
        ///////////////////////////////
        
        int[][] DIRS = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        String[] MOVE_CMDS = {"R A", "D A", "L A", "U A"};
        String[] FIRE_CMDS = {"R F", "D F", "L F", "U F"};
        String[] MEGA_CMDS = {"R F M", "D F M", "L F M", "U F M"};
        String START_SYMBOL = "M";
        String TARGET_SYMBOL = "X";
        String WALL_SYMBOL = "R";
        String WATER_SYMBOL = "W";
        
        Queue<String> actions = new LinkedList<>();

        // 최초 데이터 파싱
        parseData(gameData);
        
        // 출발지점, 목표지점의 위치 확인
        int[][] positions = findPositions(mapData, START_SYMBOL, TARGET_SYMBOL);
        int[] start = positions[0];
        int[] target = positions[1];
        
        if (start == null || target == null) {
            System.out.println("[ERROR] Start or target not found in map");
            close();
            return;
        }
        
        // 최초 경로 탐색
        actions = bfs(mapData, start, target, WALL_SYMBOL, WATER_SYMBOL, DIRS, MOVE_CMDS, FIRE_CMDS, MEGA_CMDS);

        // 반복문: 메인 프로그램 <-> 클라이언트(이 코드) 간 순차로 데이터 송수신(동기 처리)
        while (gameData != null && gameData.length() > 0) {
        	
            // 파싱한 데이터를 화면에 출력하여 확인
            printData(gameData);

            // 이전 경로 탐색 결과가 존재하지 않을 경우 다시 탐색
            if (actions.isEmpty()) {
            	positions = findPositions(mapData, START_SYMBOL, TARGET_SYMBOL);
                start = positions[0];
                target = positions[1];
                actions = (start != null && target != null) ? bfs(mapData, start, target, WALL_SYMBOL, WATER_SYMBOL, DIRS, MOVE_CMDS, FIRE_CMDS, MEGA_CMDS) : new LinkedList<>();
            }

            // 탱크를 제어할 명령어를 output의 값으로 지정(type: String)
            String output = actions.isEmpty() ? "A" : actions.poll();

            // 메인 프로그램에서 명령을 처리할 수 있도록 명령어를 submit()의 인자로 전달
            gameData = submit(output);

            // submit()의 리턴으로 받은 갱신된 데이터를 다시 파싱
            if (gameData != null && gameData.length() > 0) {
                parseData(gameData);
            }
        }

        ///////////////////////////////
        // 알고리즘 메인 부분 구현 끝
        ///////////////////////////////
        
        // 반복문을 빠져나왔을 때 메인 프로그램과의 연결을 완전히 해제하기 위해 close() 호출
        close();
    }

    ////////////////////////////////////
    // 알고리즘 함수/메서드 부분 구현 시작
    ////////////////////////////////////
    
    // 특정 기호의 위치 찾기
	private static int[][] findPositions(String[][] grid, String startMark, String targetMark) {
	    int rows = grid.length;
	    int cols = grid[0].length;
	    
	    int[] start = null;
	    int[] target = null;
	
	    for (int row = 0; row < rows; row++) {
	        for (int col = 0; col < cols; col++) {
	            
	        	if (grid[row][col].equals(startMark)) start = new int[]{row, col};
	            else if (grid[row][col].equals(targetMark)) target = new int[]{row, col};
	        }
	    }
	
	    return new int[][]{start, target};
	}

    // 경로 탐색 알고리즘
    private static Queue<String> bfs(
            String[][] grid,
            int[] start,
            int[] target,
            String wall,
            String water,
            int[][] dirs,
            String[] moveCmds,
            String[] fireCmds,
            String[] megaCmds) {
        
        int rows = grid.length;
        int cols = grid[0].length;
        
        Queue<BFSNode> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(new BFSNode(start[0], start[1], new LinkedList<>()));
        visited.add(start[0] + "," + start[1]);

        while (!queue.isEmpty()) {
            BFSNode current = queue.poll();
            int r = current.row;
            int c = current.col;
            
            Queue<String> actions = current.actions;
            
            
            for (int d = 0; d < dirs.length; d++) {
                int nr = r;
                int nc = c;
                
                int count = 0;
                while (count < 3) {
                	nr += dirs[d][0];
                	nc += dirs[d][1];
                	
                	if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) break;
                    if (grid[nr][nc] == null || grid[nr][nc].equals(wall) || grid[nr][nc].equals(water)) break;
                	
                	if (nr == target[0] && nc == target[1]) {
                		int[] booms = getBoom();               	
                        Queue<String> result = new LinkedList<>(actions);
                        if (booms[1] > 0) result.offer(megaCmds[d]);
                        else result.offer(fireCmds[d]);
                        return result;
                	}
                	
                	count++;
                }    
              
            }

            // 일반 이동
            for (int d = 0; d < dirs.length; d++) {
                int nr = r + dirs[d][0];
                int nc = c + dirs[d][1];
                String key = nr + "," + nc;
                
                if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) continue;
                if (grid[nr][nc] == null || grid[nr][nc].equals(wall) || grid[nr][nc].equals(water)) continue;
                if (visited.contains(key)) continue;          
                
	            if (grid[nr][nc].equals("T")) {
	            	// [sim 수정] 원본은 나무 칸을 방문 처리하지 않아 인접 나무에서 BFS가 무한 증식(시간 초과)
	            	visited.add(key);
	            	Queue<String> newActions = new LinkedList<>(actions);
	            	newActions.offer(fireCmds[d]);
	            	newActions.offer(moveCmds[d]);
	            	queue.offer(new BFSNode(nr, nc, newActions));
	            	continue;
	            }
	                
                visited.add(key);
        	    Queue<String> newActions = new LinkedList<>(actions);
        	    newActions.offer(moveCmds[d]);
        	    queue.offer(new BFSNode(nr, nc, newActions));
            }
        }

        return new LinkedList<>();
    }
    
    private static int[] getBoom() {
    	int[] result = new int[2];
    	
    	for (String key : myAllies.keySet()) {
            String[] value = myAllies.get(key);
            if (key.equals("M")) {
                result[0] = Integer.parseInt(value[2]);
                result[1] = Integer.parseInt(value[3]);
            } 
        }
    	
    	return result;
    }

    // BFS 헬퍼 클래스
    private static class BFSNode {
        int row, col;
        Queue<String> actions;
        
        BFSNode(int row, int col, Queue<String> actions) {
            this.row = row;
            this.col = col;
            this.actions = actions;
        }
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
