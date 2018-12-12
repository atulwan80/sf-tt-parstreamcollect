package averages;

import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

class Average {
    private long count = 0;
    private double sum = 0;

    public Average() {}

    public void include(double n) {
        count++;
        sum += n;
    }

    public void merge(Average other) {
        count += other.count;
        sum += other.sum;
    }

    public OptionalDouble get() {
        if (count > 0) {
            return OptionalDouble.of(sum / count);
        } else {
            return OptionalDouble.empty();
        }
    }
}

public class FindMean {
    public static void main(String[] args) {
        long start = System.nanoTime();
        DoubleStream.generate(() -> ThreadLocalRandom.current().nextDouble(-Math.PI, Math.PI))
                .parallel()
//                .unordered()
                .limit(1_000_000_000L)
                .map(x -> Math.sin(x))
                .collect(
                        () -> new Average(),
                        (soFar, value) -> soFar.include(value),
                        (finalBucket, intermediate) -> finalBucket.merge(intermediate))
                .get()
                .ifPresent(v -> System.out.println("The average is " + v));
        long elapsed = System.nanoTime() - start;
        System.out.printf("Time was %7.3f\n", elapsed/1_000_000_000.0);

    }
}

