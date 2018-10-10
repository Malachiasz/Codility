package java8.streams;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// Examples from https://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/
public class ExampleOne {


    @Test
    public void test() {
        List<String> myList = Arrays.asList("a1", "a2", "b1", "c2", "c1");
        myList
                .stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);

    }


    @Test
    public void test2() {
        List<String> myList = Arrays.asList("a1", "a2", "a3");
        myList
                .stream()
                .findFirst()
                .ifPresent(System.out::println);

    }


    @Test
    public void test3() {
        Stream.of("a1", "b1", "c1")
                .findFirst()
                .ifPresent(System.out::println);

    }

    @Test
    public void test4() {
        IntStream.of(1, 2, 4, 6)
                .map(operand -> operand * 2)
                .map(operand -> operand / 2)
                .reduce((left, right) -> left + right)
                .ifPresent(System.out::println);
    }

    @Test
    public void test5() {
        IntStream.range(0, 12)
                .average()
                .ifPresent(System.out::println);
    }

    @Test
    public void test6() {
        Arrays.stream(new int[]{1, 2, 3})
                .average()
                .ifPresent(System.out::println);
    }

    @Test
    public void test7() {
        Stream.of("a1", "a2", "a3")
                .map(s -> s.substring(1))
                .mapToInt(Integer::parseInt)
                .average()
                .ifPresent(System.out::println);
    }

    @Test
    public void test8() {
        IntStream.range(1, 4)
                .mapToObj(value -> "a" + value)
                .forEach(System.out::println);
    }

    @Test
    public void test9() {
        Stream.of("d2", "a2", "b1", "b2", "c")
                .filter(s -> {
                    System.out.println(s);
                    return true;
                })
                .forEach(System.out::println);
    }

    @Test
    public void test10() {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch" + s);
                    return s.startsWith("A");
                });
    }

    @Test
    public void test11() {
        Stream.of("d2", "a2", "b1", "b3", "c", "a4")
                .filter(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("a");
                })
                .sorted((o1, o2) -> {
                    System.out.printf("sorted: %s %s \n", o1, o2);
                    return o1.compareTo(o2);
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach" + s));
    }

    @Test
    public void test12() {
        Stream<String> stream = Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> s.startsWith("a"));

        stream.anyMatch(s -> true);
        stream.noneMatch(s -> true);
    }

    @Test
    public void test13() {
        Supplier<Stream<String>> streamSupplier = () -> Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> s.startsWith("a"));

        streamSupplier.get().anyMatch(s -> true);
        streamSupplier.get().noneMatch(s -> true);
    }

    class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    List<Person> persons =
            Arrays.asList(
                    new Person("Max", 18),
                    new Person("Peter", 23),
                    new Person("Pamela", 23),
                    new Person("David", 12));

    @Test
    public void test14() {
        Set<Person> filtered =
                persons
                        .stream()
                        .filter(p -> p.name.startsWith("P"))
                        .collect(Collectors.toSet());

        System.out.println(filtered);    // [Peter, Pamela]
    }

    @Test
    public void test15() {
        Map<Integer, List<Person>> personsByAge = persons
                .stream()
                .collect(Collectors.groupingBy(p -> p.age));

        personsByAge.forEach((age, people) -> System.out.format("age %s: %s \n", age, people));
    }

    @Test
    public void test16() {
        Double averageAge = persons
                .stream()
                .collect(Collectors.averagingInt(value -> value.age));
        System.out.println(averageAge);
    }

    @Test
    public void test17() {
        IntSummaryStatistics ageSummary = persons
                .stream()
                .collect(Collectors.summarizingInt(value -> value.age));

        System.out.println(ageSummary);
    }

    @Test
    public void test18() {
        String phrase = persons
                .stream()
                .filter(person -> person.age >= 18)
                .map(p -> p.name)
                .collect(Collectors.joining(" & ","In Germany ", " are of legal age."));

        System.out.println(phrase);
    }

    @Test
    public void test19() {
        Map<Integer, String> map = persons
                .stream()
                .collect(Collectors.toMap(
                        p-> p.age, p -> p.name, (name1, name2) -> name1 + ";" + name2)
                );

        System.out.println(map);
    }

    @Test
    public void test20() {
        Collector<Person, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> new StringJoiner(" | "),
                        (stringJoiner, person) -> stringJoiner.add(person.name.toLowerCase()),
                        StringJoiner::merge,
                        StringJoiner::toString
                );

        String names = persons
                .stream()
                .collect(personNameCollector);

        System.out.println(names);
    }


    class Foo {
        String name;
        List<Bar> bars = new ArrayList<>();

        Foo(String name) {
            this.name = name;
        }
    }

    class Bar {
        String name;

        Bar(String name) {
            this.name = name;
        }
    }

    @Test
    public void test21() {
        List<Foo> foos = new ArrayList<>();

        IntStream
                .range(1,4)
                .forEach(i -> foos.add(new Foo("Foo" + i)));

        foos.forEach(foo ->
                IntStream
                        .range(1, 4)
                        .forEach(i -> foo.bars.add(new Bar("Bar" + i + " <- " + foo.name))));

        foos.stream()
        .flatMap(foo -> foo.bars.stream())
        .forEach(bar -> System.out.println(bar.name));
    }

    @Test
    public void test22() {
        IntStream.range(1, 4)
                .mapToObj(i -> new Foo("Foo" + i))
                .peek(foo -> IntStream.range(1,4)
                    .mapToObj(value -> new Bar("Bar" + value + " <- " + foo.name))
                    .forEach(foo.bars::add))
                .flatMap(foo -> foo.bars.stream())
                .forEach(b -> System.out.println(b.name));

    }

    @Test
    public void test23() {
        persons
                .stream()
                .reduce((person, person2) -> person.age > person2.age ? person : person2)
                .ifPresent(System.out::println);
    }

    @Test
    public void test24() {
        Person result = persons
                .stream()
                .reduce(new Person("", 0),
                        (person, person2) -> {
                            person.age += person2.age;
                            person.name += person2.name;
                            return person;
                        });

        System.out.format("%s, %s", result.name, result.age);
    }

    @Test
    public void test25() {
        Integer ageSum = persons
                .parallelStream()
                .reduce(0, (sum, person) -> {
                    System.out.format("accumulator: sum=%s; person=%s\n", sum, person);
                    return sum += person.age;
                }, (sum1, sum2) -> {
                    System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                    return sum1 + sum2;
                });

        System.out.println(ageSum);
    }

    @Test
    public void test26() {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println(commonPool.getParallelism());    // 3
    }

    @Test
    public void test27() {
        Arrays.asList("a1", "a2", "b1", "b2", "c1")
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter %s [%s]\n", s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map %s [%s]\n", s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .forEach(r -> System.out.format("forEach: %s [%s]\n", r, Thread.currentThread().getName()));
    }
}
