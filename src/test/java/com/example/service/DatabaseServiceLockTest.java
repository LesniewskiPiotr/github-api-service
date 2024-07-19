package com.example.service;

import com.example.model.RequestCount;
import com.example.repository.RequestCountRepository;
import com.example.service.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseServiceLockTest {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private RequestCountRepository requestCountRepository;

    @BeforeEach
    public void setUp() {
        requestCountRepository.deleteAll();
    }

    @Test
    public void testLockingMechanism() throws InterruptedException, ExecutionException, TimeoutException {
        String login = "lockTestUser";

        RequestCount initialCount = new RequestCount();
        initialCount.setLogin(login);
        initialCount.setCount(0);
        requestCountRepository.save(initialCount);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        long[] startTimes = new long[2];
        long[] endTimes = new long[2];

        Future<Void> future1 = executorService.submit(() -> {
            latch.await();
            startTimes[0] = System.currentTimeMillis();
            databaseService.incrementRequestCount(login);
            endTimes[0] = System.currentTimeMillis();
            return null;
        });

        Future<Void> future2 = executorService.submit(() -> {
            latch.countDown();
            Thread.sleep(100);
            startTimes[1] = System.currentTimeMillis();
            databaseService.incrementRequestCount(login);
            endTimes[1] = System.currentTimeMillis();
            return null;
        });

        future1.get(5, TimeUnit.SECONDS);
        future2.get(5, TimeUnit.SECONDS);
        executorService.shutdown();

        Optional<RequestCount> finalCount = requestCountRepository.findById(login);
        assertTrue(finalCount.isPresent());
        assertEquals(2, finalCount.get().getCount());

        assertTrue(endTimes[0] < startTimes[1], "The first thread should finish before the second thread starts.");
    }
}
