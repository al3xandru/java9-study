package com.mypopescu.tut.j9.rs;


import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

/**
 * @author Alex Popescu
 * @since 1.0, 2018-02-17
 */
public class TransformProcessor<T, R> extends SubmissionPublisher<R> implements Processor<T, R> {
    private Function<T, R> function;
    private Subscription subscription;

    public TransformProcessor(Function<T, R> func) {
        super();
        this.function = func;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        submit(function.apply(item));
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        close();
    }
}
