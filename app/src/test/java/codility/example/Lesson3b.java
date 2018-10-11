package codility.example;

import junit.framework.Assert;

import org.junit.Test;

public class Lesson3b {

    @Test
    public void test() {
        Assert.assertEquals(3, solution(10, 85, 30));
        Assert.assertEquals(142730189, solution(3, 999111321, 7));
    }

    public int solution(int X, int Y, int D) {
        return (int) Math.ceil((Y - X) / (double)D);
    }
}
