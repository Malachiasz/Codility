package codility.example;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class Lesson4 {

    private static final int TRUE = 1;
    private static final int FALSE = 0;

    @Test
    public void test() {
        Assert.assertEquals(1, solution(new int[] {4,1,3,2}));
    }

    // is it permutation?
    public int solution(int[] A) {
        Arrays.sort(A);
        for(int i =0; i< A.length; i++){
            if (A[i] != i+1) {
                return FALSE;
            }
        }
        return TRUE;
    }
}
