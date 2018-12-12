package itergen;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Numbers {
    public static void main(String[] args) {
//        IntStream.of(1, 3, 5, 2, 4, 6)
//        IntStream.iterate(1, x -> x + 2)
        IntStream.iterate(1, (x) -> ThreadLocalRandom.current().nextInt(1, 10_000))
                .parallel()
                .unordered()
                .limit(1_000_000_000)
                .forEach/*Ordered*/(n -> { if (n == 1) System.out.println(n);});
    }
}
