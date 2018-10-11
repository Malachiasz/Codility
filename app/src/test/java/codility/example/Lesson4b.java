package codility.example;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

public class Lesson4b {

    int IMPOSSIBLE = -1;

    /**
     *   A[0] = 5
     *   A[1] = 6
     *   A[2] = 3
     *   A[3] = 2
     *   A[4] = 0
     *   A[5] = 3
     *   A[6] = 5
     *   A[7] = 1
     */

    @Test
    public void test() {
        Assert.assertEquals(-1, solution(5, new int[]{1,3,1,4,2,3,5,4}));
    }

    public int solution(int X, int[] A) {
        SortedMap<Integer, Integer> positionToTime = new TreeMap<>();
        for (int i = 0; i < A.length; i++) {
            int time = A[i];
            int value = positionToTime.get(time) == null ? Integer.MAX_VALUE : positionToTime.get(time);
            positionToTime.put(time, Math.min(value, i));
        }

        int theSlowestTime = 0;

        int[] originalArray = A.clone();
        Arrays.sort(A);
        for (int i=0; i<A.length; i++){

        }
        // write your code in Java SE 8
        return 0;
    }
}
