package model;

import domain.Account;

import java.util.List;

public record Result(Account account, List<String> violations) {
}
