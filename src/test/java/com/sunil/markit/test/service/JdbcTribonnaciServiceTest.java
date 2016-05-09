package com.sunil.markit.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static com.sunil.markit.test.enums.Type.ITERATIVE;
import static com.sunil.markit.test.enums.Type.RECURSIVE;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ContextConfiguration(locations = {"classpath:/system-test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcTribonnaciServiceTest {

    @Autowired
    JdbcTribonnaciService classToTest;

    @Test
    public void testInitialRecursiveCallWhenInputIsLessThan3() {
        //Given
        int[] valuesToTest = {0, 1, 2};

        //When
        BigInteger[] result = new BigInteger[3];
        for (int idx = 0; idx < valuesToTest.length; idx++) {
            result[idx] = classToTest.getTribonnaciRecursive(valuesToTest[idx]);
        }

        //Then
        assertThat(result[0], equalTo(BigInteger.ZERO));
        assertThat(result[1], equalTo(BigInteger.ZERO));
        assertThat(result[2], equalTo(BigInteger.ONE));
    }

    @Test
    public void testRecursiveCallWhenInputIsGreaterThan3() {
        //Given
        int[] valuesToTest = {3, 4, 5, 6};

        //When
        BigInteger[] result = new BigInteger[4];
        for (int idx = 0; idx < valuesToTest.length; idx++) {
            result[idx] = classToTest.getTribonnaciRecursive(valuesToTest[idx]);
        }

        //Then
        assertThat(result[0], equalTo(BigInteger.ONE));
        assertThat(result[1], equalTo(BigInteger.valueOf(2)));
        assertThat(result[2], equalTo(BigInteger.valueOf(4)));
        assertThat(result[3], equalTo(BigInteger.valueOf(7)));
    }

    @Test
    public void shouldThrowExceptionForRecursiveCallWhenInputValueIsNegative() {
        //Given
        int valueToTest = -1;

        //When
        try {
            classToTest.getTribonnaciRecursive(valueToTest);
        }
        //Then
        catch (IllegalArgumentException exp) {
            assertThat(exp.getMessage(), is("Input value [-1] is less than zero"));
        }
    }

    @Test
    public void testInitialIterativeCallWhenInputIsLessThan3() {
        //Given
        int[] valuesToTest = {0, 1, 2};

        //When
        BigInteger[] result = new BigInteger[3];
        for (int idx = 0; idx < valuesToTest.length; idx++) {
            result[idx] = classToTest.getTribonnaciIterative(valuesToTest[idx]);
        }

        //Then
        assertThat(result[0], equalTo(BigInteger.ZERO));
        assertThat(result[1], equalTo(BigInteger.ZERO));
        assertThat(result[2], equalTo(BigInteger.ONE));
    }

    @Test
    public void testIterativeCallWhenInputIsGreaterThan3() {
        //Given
        int[] valuesToTest = {3, 4, 5, 6};

        //When
        BigInteger[] result = new BigInteger[4];
        for (int idx = 0; idx < valuesToTest.length; idx++) {
            result[idx] = classToTest.getTribonnaciIterative(valuesToTest[idx]);
        }

        //Then
        assertThat(result[0], equalTo(BigInteger.ONE));
        assertThat(result[1], equalTo(BigInteger.valueOf(2)));
        assertThat(result[2], equalTo(BigInteger.valueOf(4)));
        assertThat(result[3], equalTo(BigInteger.valueOf(7)));
    }

    @Test
    public void shouldThrowExceptionForIterativeCallWhenInputValueIsNegative() {
        //Given
        int valueToTest = -1;

        //When
        try {
            classToTest.getTribonnaciIterative(valueToTest);
        }
        //Then
        catch (IllegalArgumentException exp) {
            assertThat(exp.getMessage(), is("Input value [-1] is less than zero"));
        }
    }

    @Test
    public void shouldReturnRecursiveCallCount() {
        //Given

        //When
        Map<Integer, Integer> recursiveCallCount = classToTest.getCallCount(RECURSIVE);


        //Then
        Map<Integer, Integer> expectedRecursiveResult = getExpectedRecursiveResult();
        assertThat(recursiveCallCount, is(notNullValue()));
        assertThat(recursiveCallCount.size(), equalTo(expectedRecursiveResult.size()));

        for (Map.Entry<Integer, Integer> entry : recursiveCallCount.entrySet()) {
            assertThat(recursiveCallCount.get(entry.getKey()), equalTo(expectedRecursiveResult.get(entry.getKey())));
        }
    }

    @Test
    public void shouldReturnIterativeCallCount() {
        //Given

        //When
        Map<Integer, Integer> iterativeCallCount = classToTest.getCallCount(ITERATIVE);


        //Then
        Map<Integer, Integer> expectedIterativeResult = getExpectedIterativeResult();
        assertThat(iterativeCallCount, is(notNullValue()));
        assertThat(iterativeCallCount.size(), equalTo(expectedIterativeResult.size()));

        for (Map.Entry<Integer, Integer> entry : iterativeCallCount.entrySet()) {
            assertThat(iterativeCallCount.get(entry.getKey()), equalTo(expectedIterativeResult.get(entry.getKey())));
        }

    }

    @Test
    public void shouldReturnRecursiveCallCountPostCallCountUpdate() {
        //Given
        int[] valuesToTest = {3, 4, 5, 6};
        for (int idx = 0; idx < valuesToTest.length; idx++) {
            classToTest.getTribonnaciRecursive(valuesToTest[idx]);
        }

        //When
        Map<Integer, Integer> recursiveCallCount = classToTest.getCallCount(RECURSIVE);


        //Then
        Map<Integer, Integer> expectedRecursiveResultPostSave = getExpectedRecursiveResultPostCallCountUpdate();
        assertThat(recursiveCallCount, is(notNullValue()));
        assertThat(recursiveCallCount.size(), equalTo(expectedRecursiveResultPostSave.size()));

        for (Map.Entry<Integer, Integer> entry : recursiveCallCount.entrySet()) {
            assertThat(recursiveCallCount.get(entry.getKey()), equalTo(expectedRecursiveResultPostSave.get(entry.getKey())));
        }
    }

    private Map<Integer, Integer> getExpectedRecursiveResultPostCallCountUpdate() {
        Map<Integer, Integer> result = new HashMap<>();
        result.put(0, 5);
        result.put(1, 8);
        result.put(2, 10);
        result.put(3, 8);
        result.put(4, 1);
        result.put(5, 1);
        result.put(6, 35);
        result.put(84, 5);
        result.put(24, 13);
        result.put(78, 23);
        result.put(94, 74);
        result.put(35, 83);
        return result;
    }

    private Map<Integer, Integer> getExpectedRecursiveResult() {
        Map<Integer, Integer> result = new HashMap<>();
        result.put(0, 5);
        result.put(1, 8);
        result.put(2, 10);
        result.put(3, 7);
        result.put(6, 34);
        result.put(84, 5);
        result.put(24, 13);
        result.put(78, 23);
        result.put(94, 74);
        result.put(35, 83);
        return result;
    }

    private Map<Integer, Integer> getExpectedIterativeResult() {
        Map<Integer, Integer> result = new HashMap<>();
        result.put(0, 5);
        result.put(1, 3);
        result.put(2, 2);
        result.put(3, 7);
        result.put(4, 8);
        result.put(24, 23);
        result.put(14, 45);
        result.put(17, 24);
        result.put(37, 33);
        return result;
    }
}

