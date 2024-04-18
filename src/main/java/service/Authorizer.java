package service;

import domain.Account;
import domain.Transaction;
import model.Result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Authorizer {
    public static Result authorize(Transaction transaction, Account account) {
        List<String> violations = new ArrayList<>();
        Account updatedAccount = account;

        if (!updatedAccount.isActive()) {
            violations.add("account-not-active");
        }

        if (updatedAccount.getHistory().isEmpty() && transaction.getAmount().compareTo(updatedAccount.getAvailableLimit().multiply(new BigDecimal("0.9"))) > 0) {
            violations.add("first-transaction-above-threshold");
        }

        if (transaction.getAmount().compareTo(updatedAccount.getAvailableLimit()) > 0) {
            violations.add("insufficient-limit");
        }

        List<Transaction> last2MinTransactions = updatedAccount.getTransactionsInLast2Minutes(transaction.getMerchant(), transaction.getTime());
        if (last2MinTransactions.size() >= 3) {
            violations.add("high-frequency-small-interval");
        }

        List<Transaction> similarTransactions = updatedAccount.getSimilarTransactionsInLast2Minutes(transaction);
        if (similarTransactions.size() > 0) {
            violations.add("doubled-transaction");
        }

        if (violations.isEmpty()) {
            updatedAccount = updatedAccount.addTransaction(transaction);
        }

        return new Result(updatedAccount, violations);
    }
}
