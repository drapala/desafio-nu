package domain;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Account {
    private boolean active;
    private BigDecimal availableLimit;
    private List<Transaction> history;

    public Account(boolean active, BigDecimal availableLimit, List<Transaction> history) {
        this.active = active;
        this.availableLimit = availableLimit;
        this.history = new ArrayList<>(history);
    }

    public boolean isActive() {
        return active;
    }

    public BigDecimal getAvailableLimit() {
        return availableLimit;
    }

    public List<Transaction> getHistory() {
        return new ArrayList<>(history);
    }

    private List<Transaction> getTransactionsSince(Instant cutoff) {
        return history.stream()
                .filter(t -> t.getTime() >= cutoff.toEpochMilli())
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsInLast2Minutes(String merchant, long transactionTime) {
        Instant cutoff = Instant.ofEpochMilli(transactionTime).minus(Duration.ofMinutes(2));
        return getTransactionsSince(cutoff).stream()
                .filter(t -> t.getMerchant().equals(merchant))
                .collect(Collectors.toList());
    }

    public List<Transaction> getSimilarTransactionsInLast2Minutes(Transaction transaction) {
        Instant cutoff = Instant.ofEpochMilli(transaction.getTime()).minus(Duration.ofMinutes(2));
        return getTransactionsSince(cutoff).stream()
                .filter(t -> t.getMerchant().equals(transaction.getMerchant()) &&
                        t.getAmount().compareTo(transaction.getAmount()) == 0)
                .collect(Collectors.toList());
    }

    public Account addTransaction(Transaction transaction) {
        List<Transaction> updatedHistory = new ArrayList<>(history);
        updatedHistory.add(transaction);
        BigDecimal newLimit = this.availableLimit.subtract(transaction.getAmount());
        return new Account(this.active, newLimit, updatedHistory);
    }

    @Override
    public String toString() {
        String historyEntries = history.stream()
                .map(t -> String.format(
                        "    {amount: %s, merchant: \"%s\", time: %d},\n",
                        t.getAmount(), t.getMerchant(), t.getTime()
                ))
                .reduce("", (partialString, element) -> partialString + element);

        if (!historyEntries.isEmpty()) {
            historyEntries = historyEntries.substring(0, historyEntries.length() - 2);
        }

        return String.format(
                "account = {\n" +
                        "  active: %s,\n" +
                        "  availableLimit: %s,\n" +
                        "  history: [\n%s\n" +
                        "  ]\n" +
                        "}",
                active, availableLimit, historyEntries
        );
    }
}
