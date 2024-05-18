package org.acme.getting.started;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import jakarta.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ReactiveGreetingService {

    @Inject
    ManagedExecutor managedExecutor;

    private static final Logger log = Logger.getLogger(ReactiveGreetingService.class);

    private BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);

    public Uni<String> greeting(String name) {
        return Uni.createFrom().item(name)
                .onItem().transform(n -> String.format("hello %s", name));
    }

    public Multi<String> greetings(int count, String name) {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .onItem().transform(n -> String.format("hello %s - %d", name, n))
                .select().first(count);

    }

    public Uni<String> hello() {
        managedExecutor.submit(this::addItemInQueue);
        try {
            log.info("Waiting for hello");
            return Uni.createFrom().item(queue.poll(5, TimeUnit.SECONDS)).onItem().transform(Function.identity()).onFailure().transform(e -> new IllegalArgumentException("falis"));
        } catch (InterruptedException e) {
            log.error("Error occurred for waiting ", e);
            throw new RuntimeException(e);
        }
    }

    private void addItemInQueue() {
        try {
            Thread.sleep(2_000);
            queue.offer("hello");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
