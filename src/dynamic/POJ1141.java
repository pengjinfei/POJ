package dynamic;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by pengjinfei on 2015/10/26.
 * Description:AC 2015/10/27 00:16
 */
public class POJ1141 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String in = scanner.nextLine();
        int N = in.length();
        if (N == 0) {
            System.out.println();
            return;
        }
        String[][] out = new String[N + 2][];
        for (int i = 0; i <N+2; i++) {
            out[i] = new String[N + 2];
            Arrays.fill(out[i], "");
        }
        for (int i = 0; i < N; i++) {
            if (in.charAt(i) == '(' || in.charAt(i) == ')')
                out[i+1][i+1] = "()";
            else
                out[i+1][i+1] = "[]";
        }
        for (int len = 2; len <= N; len++) {
            for (int i = 1; i < N - len + 2; i++) {
                char begin = in.charAt(i-1);
                char end = in.charAt(i + len - 2);
                if (end == '(') {
                    out[i][i + len - 1] = out[i][i + len - 2] + "()";
                } else if (end == '[') {
                    out[i][i + len - 1] = out[i][i + len - 2] + "[]";
                } else if (begin == ')') {
                    out[i][i + len - 1] = "()" + out[i + 1][i + len - 1];
                } else if (begin == ']') {
                    out[i][i + len - 1] = "[]" + out[i + 1][i + len - 1];
                } else {
                    char left_find;
                    if (begin == '(')
                        left_find = ')';
                    else
                        left_find = ']';
                    int fine = Integer.MAX_VALUE;
                    for (int j = i + 1; j < i + len; j++) {
                        if (in.charAt(j-1) == left_find) {
                            String temps = begin + out[i + 1][j - 1] + left_find + out[j + 1][i + len - 1];
                            if (fine > temps.length()) {
                                fine = temps.length();
                                out[i][i + len - 1] = temps;
                            }
                        }
                    }
                    char right_find;
                    if (end == ')')
                        right_find = '(';
                    else
                        right_find = '[';
                    for (int j = i; j < i + len - 1; j++) {
                        if (in.charAt(j-1) == right_find) {
                            String temps = out[i][j - 1] + right_find + out[j + 1][i + len - 2] + end;
                            if (fine > temps.length()) {
                                fine = temps.length();
                                out[i][i + len - 1] = temps;
                            }
                        }
                    }
                    if (out[i][i + len - 1] == "") {
                        String c1 = out[i][i + len - 2] + right_find + end;
                        String c2 = begin + "" + left_find + out[i + 1][i + len - 1];
                        out[i][i + len - 1] = c1.length() < c2.length() ? c1 : c2;
                    }
                }
            }

        }
        System.out.println(out[1][N]);
    }
}
