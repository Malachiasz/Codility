package codility.example;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class Lesson3 {

    @Test
    public void name() {
        Assert.assertEquals(4, solution(new int[]{2, 3, 1, 5}));
        Assert.assertEquals(2, solution(new int[]{1, 3}));
        Assert.assertEquals(2, solution(new int[]{1}));
        Assert.assertEquals(1, solution(new int[]{}));
    }

    public int solution(int[] A) {
        Arrays.sort(A);
        for (int i = 0; i < A.length; i++) {
            if (A[i] != i + 1) {
                return i + 1;
            }
        }
        return A.length + 1;
    }
}
