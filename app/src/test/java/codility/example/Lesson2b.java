package codility.example;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Lesson2b {

    @Test
    public void name() {
        Assert.assertEquals(7, solution(new int[]{9, 3, 9, 3, 9, 7, 9}));
        Assert.assertEquals(9, solution(new int[]{9}));
    }

    public int solution(int[] A) {
        HashMap<Integer, Integer> valueToCount = new HashMap<>(A.length);
        IntStream.range(0, A.length)
                .forEach(i -> {
                    int key = A[i];
                    Integer lastCount = valueToCount.get(key) == null ? 0 : valueToCount.get(key);
                    valueToCount.put(key, lastCount + 1);
                });

        return valueToCount.entrySet().stream()
                .filter(valueToCountEntry -> valueToCountEntry.getValue() % 2 != 0)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
    }
}
