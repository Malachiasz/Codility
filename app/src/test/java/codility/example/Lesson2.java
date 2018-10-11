package codility.example;

import org.junit.Assert;

import org.junit.Test;

import java.util.stream.IntStream;

public class Lesson2 {

    @Test
    public void name() {
        Assert.assertArrayEquals(new int[]{6, 3, 8, 9, 7}, solution(new int[]{3, 8, 9, 7, 6}, 1));
        Assert.assertArrayEquals(new int[]{9, 7, 6, 3, 8}, solution(new int[]{3, 8, 9, 7, 6}, 3));
        Assert.assertArrayEquals(new int[]{1000}, solution(new int[]{1000}, 5));

    }

    public int[] solution(int[] A, int K) {
        int[] reuslt = new int[A.length];
        IntStream.range(0, A.length)
                .forEach(i -> {
                    int position = getPosition(i, K, A.length);
                    System.out.format("Position %d replaced by [%s] /n", i, A[position]);
                    reuslt[i] = A[position];
                });
        return reuslt;
    }

    public int getPosition(int position, int numberOfRotation, int arrayLength) {
        int shiftedPosition = position - numberOfRotation % arrayLength;
        if (shiftedPosition >= 0) {
            return shiftedPosition;
        } else {
            return shiftedPosition + arrayLength;
        }
    }
}
