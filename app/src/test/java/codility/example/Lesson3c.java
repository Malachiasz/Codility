package codility.example;

import junit.framework.Assert;

import org.junit.Test;

public class Lesson3c {


    @Test
    public void test() {
        Assert.assertEquals(1, solution(new int[]{3, 1, 2, 4, 3}));
        Assert.assertEquals(1, solve(new int[]{3, 1, 2, 4, 3}));
    }

    // FROM INTERNET
    public int solve(final int[] values) {
        long leftSum = 0;
        long rightSum = 0;
        for (int i = 0; i < values.length; i++) {
            rightSum += values[i];
        }

        long minDifference = Integer.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            leftSum += values[i];
            rightSum -= values[i];
            final long difference = Math.abs(leftSum - rightSum);
            minDifference = Math.min(minDifference, difference);
        }

        return (int) minDifference;
    }


    public int solution(int[] A) {
        int smallestDifference = Integer.MAX_VALUE;
        for(int i = 1; i< A.length; i++) {
            int difference = difference(A, i);
            smallestDifference = Math.min(difference, smallestDifference);
            System.out.println(smallestDifference);
        }
        return smallestDifference;
    }

    public int difference(int[] A, int P) {
        int sumOfElementsLeft = 0;
        for(int i=0; i<P; i++){
            sumOfElementsLeft += A[i];
        }

        int sumOfElementsRight = 0;
        for(int i=P; i<A.length; i++){
            sumOfElementsRight += A[i];
        }

        return Math.abs(sumOfElementsLeft - sumOfElementsRight);
    }
}
