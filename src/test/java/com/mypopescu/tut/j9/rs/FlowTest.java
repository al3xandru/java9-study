package com.mypopescu.tut.j9.rs;


import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


/**
 * Examples provided/inspired by <a href="http://www.baeldung.com/java-9-reactive-streams">Java 9 Reactive Stream</a>
 *
 * @author Alex Popescu
 * @since 1.0, 2018-02-17
 */
public class FlowTest {
    @Test
    void whenSubscribedThenShouldConsumeAll() {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        EndSubscriber<String> subscriber = new EndSubscriber<>(-1);
        publisher.subscribe(subscriber);


        assertThat(publisher.getNumberOfSubscribers()).isEqualTo(1);
        assertThat(subscriber.isSubscribed())
            .as("SubmissionPublisher.subscribe was invoked")
            .isTrue();

        List<String> items = List.of("1", "x", "2", "x", "3", "x");
        items.forEach(publisher::submit);

        await().atMost(1000, TimeUnit.MILLISECONDS)
               .until(() -> {
                   assertThat(subscriber.consumedElements).containsExactlyElementsOf(items);
                   return true;
               });

        publisher.close();
        await().atMost(1000, TimeUnit.MILLISECONDS)
               .until(() -> {
                   assertThat(subscriber.isCompleted())
                           .as("SubmissionPublisher.close() was invoked and Subscriber should have got onClose")
                           .isTrue();
                   return subscriber.isCompleted();
               });
    }

    @Test
    void whenSubscribedAndTransformElementsThenShouldConsumeAll() {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        TransformProcessor<String, Integer> transformProcessor = new TransformProcessor<>(Integer::parseInt);
        EndSubscriber<Integer> subscriber = new EndSubscriber<>(-1);

        List<String> input = List.of("1", "2", "3");
        List<Integer> expected = List.of(1, 2, 3);

        publisher.subscribe(transformProcessor);
        transformProcessor.subscribe(subscriber);

        input.forEach(publisher::submit);
        publisher.close();

        await().atMost(1000, TimeUnit.MILLISECONDS)
               .until(() -> {
                   assertThat(subscriber.consumedElements).containsExactlyElementsOf(expected);
                   return true;
               });
    }

    @Test
    void cappedSubscriber() {
        SubmissionPublisher<String> pub = new SubmissionPublisher<>();
        EndSubscriber<String> sub = new EndSubscriber<>(2);

        pub.subscribe(sub);
        List.of("1", "2", "3").forEach(pub::submit);

        await().atMost(1000, TimeUnit.MILLISECONDS)
               .until(() -> {
                   assertThat(sub.consumedElements.size()).isEqualTo(2);
                   assertThat(sub.consumedElements).containsExactly("1", "2");
                   return true;
               });
    }

}
