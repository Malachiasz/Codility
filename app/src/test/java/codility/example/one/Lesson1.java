package codility.example.one;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;

public class Lesson1 {

    @Test
    public void test() {
        Assert.assertEquals(2, solution(9));
        Assert.assertEquals(4, solution(529));
    }


    public int solution(int N) {
        int biggestResult = 0;
        int result = 0;
        char[] chars = Integer.toBinaryString(N).toCharArray();
        for (char aChar : chars) {
            if (aChar == '1') {
                if (biggestResult < result) {
                    biggestResult = result;
                }
                result = 0;
            } else {
                result++;
            }
        }
        return biggestResult;
    }
}
