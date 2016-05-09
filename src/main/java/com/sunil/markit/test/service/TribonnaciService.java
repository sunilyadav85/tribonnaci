package com.sunil.markit.test.service;

import com.sunil.markit.test.enums.Type;

import java.math.BigInteger;
import java.util.Map;

public interface TribonnaciService {

    BigInteger getTribonnaciRecursive(int n);

    BigInteger getTribonnaciIterative(int n);

    Map<Integer, Integer> getCallCount(Type methodType);

}
