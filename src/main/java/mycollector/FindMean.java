package mycollector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;

class Average {
    private long count = 0;
    private double sum = 0;

    public Average() {}

    public void include(double n) {
        count++;
        sum += n;
    }

    public Average merge(Average other) {
        Average res = new Average();
        res.count = count;
        res.sum = sum;
        res.count += other.count;
        res.sum += other.sum;
        return res;
    }

    public OptionalDouble get() {
        if (count > 0) {
            return OptionalDouble.of(sum / count);
        } else {
            return OptionalDouble.empty();
        }
    }
}

class CollectToAverage implements Collector<Double, Average, OptionalDouble> {

    @Override
    public Supplier<Average> supplier() {
        return () -> new Average();
    }

    @Override
    public BiConsumer<Average, Double> accumulator() {
        return (a, i) -> a.include(i);
    }

    @Override
    public BinaryOperator<Average> combiner() {
        return (a1, a2) -> a1.merge(a2);
    }

    @Override
    public Function<Average, OptionalDouble> finisher() {
        return a -> a.get();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>(Arrays.asList(Characteristics.CONCURRENT, Characteristics.UNORDERED));
    }
}

public class FindMean {
    public static void main(String[] args) {
        long start = System.nanoTime();
        DoubleStream.generate(() -> ThreadLocalRandom.current().nextDouble(-Math.PI, Math.PI))
//                .parallel()
//                .unordered()
                .limit(1_000_000_000L)
                .mapToObj(x -> Math.sin(x))
                .collect(new CollectToAverage())
                .ifPresent(v -> System.out.println("The average is " + v));
        long elapsed = System.nanoTime() - start;
        System.out.printf("Time was %7.3f\n", elapsed/1_000_000_000.0);
    }
}

