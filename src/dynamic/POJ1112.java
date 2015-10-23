package dynamic;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by pengjinfei on 2015/10/23.
 * Description:Team Them Up!
 */
public class POJ1112 {
    //存储未分组的人
    public static LinkedList<Integer> pool=new LinkedList<>();
    public static LinkedList<Integer> common = new LinkedList<>();
    //输入数据
    public static boolean[][] ref;
    //总人数
    public static int N;
    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        String lines=bufferedReader.readLine();
        N = Integer.parseInt(lines);
        ref = new boolean[N][N];
        pool=new LinkedList();
        for (int i = 0; i < N; i++) {
            pool.addLast(i);
            lines=bufferedReader.readLine();
            String[] strings = lines.split(" ");
            int j=0;
            int k;
            while((k=Integer.parseInt(strings[j]))!=0){
                ref[i][k-1]=true;
                j++;
            }
        }
        LinkedList<Pair> pairs=new LinkedList<>();
        boolean success=true;
        whole:while (true) {
            if(!success)
                break;
            //找到第一对互不相识的
            int leftSample=-1;
            int rightSample=-1;
            find:while (true) {
                Integer candidate = pool.poll();
                int count=pool.size();
                for (int i=0;i<count;i++) {
                    Integer choose=pool.poll();
                    if (!ref[candidate][choose] || !ref[choose][candidate]) {
                        leftSample=candidate;
                        rightSample=choose;
                        pool.remove((Object)rightSample);
                        break find;
                    }
                }
                common.addLast(candidate);
                pool.remove((Object) candidate);
            }
            if(leftSample==-1)
                break;
            if(pool.size()==0)
                break;
            Pair pair=new Pair();
            pair.left.addLast(leftSample);
            pair.right.addLast(rightSample);
            pairs.add(pair);
            for (int i=0;i<pool.size();i++) {
                Integer candidate=pool.poll();
                Integer leftKown = isKown(candidate, pair.left);
                Integer rightKown = isKown(candidate, pair.right);
                if (leftKown!=0&& rightKown!=0) {
                    success=false;
                    break whole;
                }
                if (leftKown ==0&& rightKown==0) {
                    pool.addLast(candidate);
                    continue ;
                }
                i=-1;
                if (leftKown == 0) {
                    pair.left.addLast(candidate);
                    pair.right.addLast(rightKown);
                    pool.remove((Object)rightKown);
                } else {
                    pair.left.addLast(leftKown);
                    pair.right.addLast(candidate);
                    pool.remove((Object)leftKown);
                }
            }
        }
    }

    private static Integer isKown(Integer candidate, LinkedList<Integer> left) {
        while (left.size() != 0) {
            Integer choose=left.poll();
            if (!ref[candidate][choose] || !ref[choose][candidate]) {

                return choose;
            }
        }
        return 0;
    }


}
class Pair {
    LinkedList left=new LinkedList();
    LinkedList right=new LinkedList();
}
