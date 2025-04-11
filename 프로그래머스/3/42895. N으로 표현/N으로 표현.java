import java.io.*;
import java.util.*;

class Solution {
    public int solution(int N, int number) {
        if (N == number) return 1;

        List<HashSet<Integer>> total = new ArrayList<>();
        for (int i = 0; i <= 8; i++) total.add((new HashSet<Integer>()));

        total.get(1).add(N);

        for (int i = 2; i <= 8; i++) {
            HashSet<Integer> list = total.get(i);

            list.add(Integer.parseInt(String.valueOf(N).repeat(i)));

            for (int j = 1; j < i; j++) {
                HashSet<Integer> sub1 = total.get(j);
                HashSet<Integer> sub2 = total.get(i - j);

                for (int a : sub1) {
                    for (int b : sub2) {
                        list.add(a + b);
                        list.add(a - b);
                        list.add(a * b);
                        if (b != 0) list.add(a / b);
                    }
                }
            }

            if (list.contains(number)) return i;
        }

        return -1;
    }
}