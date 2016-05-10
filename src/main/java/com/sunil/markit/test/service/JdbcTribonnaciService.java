package com.sunil.markit.test.service;

import com.sunil.markit.test.dao.JdbcTribonnaciDao;
import com.sunil.markit.test.entities.Tribonnaci;
import com.sunil.markit.test.enums.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.sunil.markit.test.enums.Type.ITERATIVE;
import static com.sunil.markit.test.enums.Type.RECURSIVE;

@Service
public class JdbcTribonnaciService implements TribonnaciService {

    private Map<Integer, Integer> recursiveCallCount = new ConcurrentHashMap<>();
    private Map<Integer, Integer> iterativeCallCount = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    @Autowired
    private JdbcTribonnaciDao jdbcTribonnaciDao;
    @Value("${executor.initialDelay}")
    private long initialDelay;
    @Value("${executor.period}")
    private long period;

    @PostConstruct
    private void initialiseMonitoringInformation() {
        populateCallCountInfoByType(recursiveCallCount, RECURSIVE);
        populateCallCountInfoByType(iterativeCallCount, ITERATIVE);
        startDbSaveScheduler();
    }

    @Override
    public BigInteger getTribonnaciRecursive(final int n) {
        Assert.isTrue(n >= 0, "Input value [" + n + "] is less than zero");
        return getTribonnaciRecursive(n, true);
    }

    private BigInteger getTribonnaciRecursive(final int n, boolean isFirstTime) {
        if (isFirstTime) {
            updateCallCount(n, recursiveCallCount);
        }

        if (n < 3) {
            return (n < 2) ? BigInteger.ZERO : BigInteger.ONE;
        }
        return getTribonnaciRecursive((n - 3), false)
                .add(getTribonnaciRecursive((n - 2), false)
                        .add(getTribonnaciRecursive((n - 1), false)));
    }


    @Override
    public BigInteger getTribonnaciIterative(final int n) {
        Assert.isTrue(n >= 0, "Input value [" + n + "] is less than zero");
        updateCallCount(n, iterativeCallCount);

        if (n < 3) {
            return (n < 2) ? BigInteger.ZERO : BigInteger.ONE;
        }
        BigInteger value1 = BigInteger.ZERO;
        BigInteger value2 = BigInteger.ZERO;
        BigInteger value3 = BigInteger.ONE;
        BigInteger result = BigInteger.ZERO;

        for (int idx = 3; idx <= n; idx++) {
            result = value1.add(value2.add(value3));
            value1 = value2;
            value2 = value3;
            value3 = result;
        }
        return result;
    }

    @Override
    public Map<Integer, Integer> getCallCount(final Type methodType) {
        if (methodType.equals(RECURSIVE)) {
            return recursiveCallCount;
        } else {
            return iterativeCallCount;
        }
    }

    private void updateCallCount(final int input, Map<Integer, Integer> callCount) {
        synchronized (callCount) {
            if (callCount.containsKey(input)) {
                callCount.put(input, callCount.get(input) + 1);
            } else {
                callCount.put(input, 1);
            }
        }
    }

    private void populateCallCountInfoByType(Map<Integer, Integer> callCount, final Type type) {
        for (Tribonnaci tribonnaci : jdbcTribonnaciDao.getTribonnaciByType(type)) {
            callCount.put(tribonnaci.getNumber(), tribonnaci.getCounter());
        }
    }

    private void startDbSaveScheduler() {
        scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    public void run() {
                        jdbcTribonnaciDao.saveTribonnaci(recursiveCallCount, RECURSIVE);
                    }
                },
                initialDelay,
                period,
                TimeUnit.MINUTES);

        scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    public void run() {
                        jdbcTribonnaciDao.saveTribonnaci(iterativeCallCount, ITERATIVE);
                    }
                },
                initialDelay,
                period,
                TimeUnit.MINUTES);
    }

    @PreDestroy
    public void clear() {
        scheduledExecutorService.shutdown();
    }
}
