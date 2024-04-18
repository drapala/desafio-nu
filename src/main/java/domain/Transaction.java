package domain;

import java.math.BigDecimal;

public class Transaction {
    private BigDecimal amount;
    private String merchant;
    private long time;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, String merchant, long time) {
        this.amount = amount;
        this.merchant = merchant;
        this.time = time;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMerchant() {
        return merchant;
    }

    public long getTime() {
        return time;
    }
}
