package dynamic;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by pengjinfei on 2015/10/23.
 * Description:Team Them Up!
 * AC:2015/10/23 19:12
 */
public class POJ1112 {
    //存储未分组的人
    public static LinkedList<Integer> pool = new LinkedList<Integer>();
    public static LinkedList<Integer> common = new LinkedList<Integer>();
    //输入数据
    public static boolean[][] ref;
    //总人数
    public static int N;
    public static boolean[][] pick;
    public static int[][] mem;
    public static int[] items;
    public static boolean[] choose;

    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String lines = bufferedReader.readLine();
        N = Integer.parseInt(lines);
        ref = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            pool.addLast(i);
            lines = bufferedReader.readLine();
            String[] strings = lines.split(" ");
            int j = 0;
            int k;
            while ((k = Integer.parseInt(strings[j])) != 0) {
                ref[i][k - 1] = true;
                j++;
            }
        }
        LinkedList<Pair> pairs = new LinkedList<Pair>();
        boolean success = true;
        whole:
        while (true) {
            if (!success)
                break;
            //找到第一对互不相识的
            int leftSample = -1;
            int rightSample = -1;
            find:
            while (pool.size() != 0) {
                Integer candidate = pool.poll();
                for (Integer choose : pool) {
                    if (!ref[candidate][choose] || !ref[choose][candidate]) {
                        leftSample = candidate;
                        rightSample = choose;
                        pool.remove((Object) choose);
                        break find;
                    }
                }
                common.addLast(candidate);
            }
            if (leftSample == -1)
                break;
            Pair pair = new Pair();
            pair.left.addLast(leftSample);
            pair.right.addLast(rightSample);
            pairs.add(pair);
            if (pool.size() == 0)
                break;
            for (int i = 0; i < pool.size(); i++) {
                //TODO 可以只与pair的尾部相比较
                Integer candidate = pool.poll();
                boolean leftKown = isKown(candidate, pair.left);
                boolean rightKown = isKown(candidate, pair.right);
                if (!leftKown && !rightKown) {
                    success = false;
                    break whole;
                }
                if (leftKown && rightKown) {
                    pool.addLast(candidate);
                    continue;
                }
                i = -1;
                if (leftKown) {
                    pair.left.addLast(candidate);
                } else {
                    pair.right.addLast(candidate);
                }
            }
            if (pair.left.size() != 0 && pair.left.size() < pair.right.size()) {
                LinkedList temp = pair.left;
                pair.left = pair.right;
                pair.right = temp;
            }
        }
        //对结果集pairs和common进行计算，查找最优解
        if (!success) {
            System.out.println("No solution");
            return;
        }
        int groups = pairs.size();
        if (groups == 0) {
            System.out.print(N - N / 2);
            for (int i = 0; i < N - N / 2; i++) {
                System.out.print(" "+(i+1));
            }
            System.out.println();
            System.out.print(N / 2);
            for (int i = N-N/2; i < N ; i++) {
                System.out.print(" "+(i+1));
            }
            return;
        }
        choose = new boolean[groups];
        if (groups != 1) {
            items = new int[groups];
            int total = 0;
            for (int i = 0; i < groups; i++) {
                Pair pair = pairs.get(i);
                items[i] = pair.left.size() - pair.right.size();
                total += items[i];
            }
            total = total / 2;
            pick = new boolean[groups + 1][total + 1];
            mem = new int[groups + 1][total + 1];
            pick(groups, total);
            for (int i = groups, value = total; i >= 1; i--) {
                if (pick[i][value]) {
                    choose[i - 1] = true;
                    value -= items[i - 1];
                } else
                    choose[i - 1] = false;
            }
        }
        //TODO 去除排序
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };
        PriorityQueue<Integer> leftQueue = new PriorityQueue<Integer>(10, comparator);
        PriorityQueue<Integer> rightQueue = new PriorityQueue<Integer>(10, comparator);
        for (boolean b : choose) {
            Pair pair = pairs.poll();
            if (b) {
                leftQueue.addAll(pair.right);
                rightQueue.addAll(pair.left);
            } else {
                leftQueue.addAll(pair.left);
                rightQueue.addAll(pair.right);
            }
        }
        if (common.size() != 0) {
            int diff = leftQueue.size() - rightQueue.size();
            int commonsize = common.size();
            if (diff >= commonsize) {
                rightQueue.addAll(common);
            } else {
                int toadd = (commonsize + diff) / 2;
                for (int i = 0; i < toadd; i++) {
                    rightQueue.add(common.poll());
                }
                leftQueue.addAll(common);
            }
        }
        System.out.print(leftQueue.size());
        while (leftQueue.size() != 0) {
            Integer num = leftQueue.remove();
            System.out.print(" " + (num + 1));
        }
        System.out.println();
        System.out.print(rightQueue.size());
        while (rightQueue.size() != 0) {
            Integer num = rightQueue.remove();
            System.out.print(" " + (num + 1));
        }
    }

    private static int pick(int n, int total) {
        if (mem[n][total] != 0)
            return mem[n][total];
        if (total == 0) {
            pick[n][total] = false;
            mem[n][total] = 0;
            return 0;
        }
        int weight = items[n - 1];
        if (n == 1) {
            if (weight <= total) {
                pick[1][total] = true;
                return weight;
            } else {
                pick[1][total] = false;
                return 0;
            }
        }
        int notn = pick(n - 1, total);
        if (weight > total) {
            pick[n][total] = false;
            mem[n][total] = notn;
            return notn;
        }
        int choosen = weight + pick(n - 1, total - weight);
        if (choosen > notn) {
            pick[n][total] = true;
            mem[n][total] = choosen;
            return choosen;
        } else {
            pick[n][total] = false;
            mem[n][total] = notn;
            return notn;
        }
    }

    private static boolean isKown(Integer candidate, LinkedList<Integer> left) {
        for (Integer choose : left) {
            if (!ref[candidate][choose] || !ref[choose][candidate]) {
                return false;
            }
        }
        return true;
    }


}

class Pair {
    LinkedList left = new LinkedList();
    LinkedList right = new LinkedList();
}
