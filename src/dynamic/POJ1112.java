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
        while (true) {
            if(!success)
                break;
            //找到第一对互不相识的
            int leftSample=-1;
            int rightSample=-1;
            find:while (true) {
                Integer candidate = pool.poll();
                for (Integer choose : pool) {
                    if (!ref[candidate][choose] || !ref[choose][candidate]) {
                        leftSample=candidate;
                        rightSample=choose;
                        break find;
                    }
                }
                common.addLast(candidate);
            }
            if(leftSample==-1)
                break;
            if(pool.size()==0)
                break;
            Pair pair=new Pair();
            pair.left.addLast(leftSample);
            pair.right.addLast(rightSample);
            pairs.add(pair);
            for (Integer candidate : pool) {

            }
        }
    }



}
class Pair {
    LinkedList left=new LinkedList();
    LinkedList right=new LinkedList();
}
