************************************** Application Requirements **************************************
In this exercise you will implement a simple service to calculate Tribonnaci numbers. The service will be called by multiple clients simultaneously.

1) Basic Service

Implement the following Java interface:

import java.math.BigInteger;

public interface TribonnaciService {

/**

* Calculates tribonnaci numbers recursively

* @param n

* @return The nth tribonnaci number

*/

public BigInteger getTribonnaciRecursive(int n);

/**

* Calculates tribonnaci numbers iteratively

* @param n

* @return The nth tribonnaci number

*/

public BigInteger getTribonnaciIterative(int n); }

2) Method call logging

Add monitoring to your service so that you can track the number of times each method is called for each value n. Expose this monitoring information by implementing the following additional method in your service:

public static enum Type { RECURSIVE, ITERATIVE

}

/**

* Get the number of times that the corresponding getTribonnaci

* method has been called for each parameter n

* @param methodType Whether to return the count for the RECURSIVE

* or ITERATIVE method implementation

* @return A map of (n => number of times the getTribonnaci

* was called for the value n)

*/

public Map<Integer, Integer> getCallCount(Type methodType);

3) Persistence

Add a persistence layer to your service for the method call logging information you captured in (2).

a) Design a database schema to store the information

b) On construction, your service should load the current state from the database

c) Every 15 minutes, your service should save the current state to the database

We would prefer you to use Spring JDBCTemplate or plain JDBC, but if you are not familiar with either of these, you may also use any other well-known framework.

******************************************************************************************************