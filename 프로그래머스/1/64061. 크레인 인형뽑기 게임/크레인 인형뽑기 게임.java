import java.util.*;

class Solution {

        public static int solution(int[][] board, int[] moves) {
            Stack<Integer> stack = new Stack<>();
            int removedDollsCount = 0;

            for (int move : moves) {
                int column = move - 1;
                int pickedDoll = pickDoll(board, column);

                if (pickedDoll != 0) {
                    if (!stack.isEmpty() && stack.peek() == pickedDoll) {
                        stack.pop();
                        removedDollsCount += 2; // 2개가 터짐
                    } else {
                        stack.push(pickedDoll);
                    }
                }
            }
            return removedDollsCount;
        }

        private static int pickDoll(int[][] board, int column) {
            for (int row = 0; row < board.length; row++) {
                if (board[row][column] != 0) {
                    int doll = board[row][column];
                    board[row][column] = 0; // 인형 꺼내기
                    return doll;
                }
            }
            return 0; // 꺼낼 인형이 없는 경우
        }
    }