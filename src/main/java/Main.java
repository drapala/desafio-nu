import domain.Account;
import domain.Transaction;
import model.Result;
import service.Authorizer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        testSuccessfulTransaction();

        testInsufficientLimit();

        testInactiveAccount();

        testFirstTransactionAboveThreshold();

        testHighFrequencySmallInterval();

        testDoubledTransaction();
    }

    private static void testSuccessfulTransaction() {
        Transaction transaction = new Transaction(new BigDecimal("50.00"), "Burger King", System.currentTimeMillis());
        Account account = new Account(true, new BigDecimal("100.00"), new ArrayList<>());

        Result result = Authorizer.authorize(transaction, account);

        printResult("Successful domain.Transaction", result);
    }

    private static void testInsufficientLimit() {
        Transaction transaction = new Transaction(new BigDecimal("150.00"), "Burger King", System.currentTimeMillis());
        Account account = new Account(true, new BigDecimal("100.00"), new ArrayList<>());

        Result result = Authorizer.authorize(transaction, account);

        printResult("Insufficient Limit", result);
    }

    private static void testInactiveAccount() {
        Transaction transaction = new Transaction(new BigDecimal("50.00"), "Burger King", System.currentTimeMillis());
        Account account = new Account(false, new BigDecimal("100.00"), new ArrayList<>());

        Result result = Authorizer.authorize(transaction, account);

        printResult("Inactive domain.Account", result);
    }

    private static void testFirstTransactionAboveThreshold() {
        Transaction transaction = new Transaction(new BigDecimal("95.00"), "Burger King", System.currentTimeMillis());
        Account account = new Account(true, new BigDecimal("100.00"), new ArrayList<>());

        Result result = Authorizer.authorize(transaction, account);

        printResult("First domain.Transaction Above Threshold", result);
    }

    private static void testHighFrequencySmallInterval() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(new BigDecimal("10.00"), "Burger King", System.currentTimeMillis()));
        transactions.add(new Transaction(new BigDecimal("20.00"), "Burger King", System.currentTimeMillis() - 60_000));
        transactions.add(new Transaction(new BigDecimal("30.00"), "Burger King", System.currentTimeMillis() - 90_000));
        transactions.add(new Transaction(new BigDecimal("40.00"), "Burger King", System.currentTimeMillis() - 120_000));

        Account account = new Account(true, new BigDecimal("100.00"), transactions);

        Transaction newTransaction = new Transaction(new BigDecimal("50.00"), "Burger King", System.currentTimeMillis());
        Result result = Authorizer.authorize(newTransaction, account);

        printResult("High Frequency Small Interval", result);
    }

    private static void testDoubledTransaction() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(new BigDecimal("50.00"), "Burger King", System.currentTimeMillis() - 60_000));
        transactions.add(new Transaction(new BigDecimal("50.00"), "Burger King", System.currentTimeMillis() - 90_000));

        Account account = new Account(true, new BigDecimal("100.00"), transactions);

        Transaction newTransaction = new Transaction(new BigDecimal("50.00"), "Burger King", System.currentTimeMillis());
        Result result = Authorizer.authorize(newTransaction, account);

        printResult("Doubled domain.Transaction", result);
    }

    private static void printResult(String testCase, Result result) {
        System.out.println("Test Case: " + testCase);
        System.out.println("domain.Account: " + result.account());
        System.out.println("Violations: " + result.violations());
        System.out.println();
    }
}