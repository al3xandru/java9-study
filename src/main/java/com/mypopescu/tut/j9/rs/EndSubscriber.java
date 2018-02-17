package com.mypopescu.tut.j9.rs;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Alex Popescu
 * @since 1.0, 2018-02-17
 */
public class EndSubscriber<T> implements Subscriber<T> {
    private final AtomicInteger maxConsumed;
    private Subscription subscription;
    private boolean subscribed;
    private boolean completed;
    public List<T> consumedElements = new LinkedList<>();

    public EndSubscriber(int maxConsumed) {
        if (maxConsumed < 1) {
            this.maxConsumed = new AtomicInteger(Integer.MAX_VALUE);
        }
        else {
            this.maxConsumed = new AtomicInteger(maxConsumed);
        }
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscribed = true;
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        maxConsumed.decrementAndGet();
        System.out.printf("onText(%s): %s%n", item.getClass(), item);
        consumedElements.add(item);
        if (maxConsumed.get() > 0) {
            subscription.request(1);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        this.completed = true;
        System.out.println("done");
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public boolean isCompleted() {
        return completed;
    }
}
