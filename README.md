# Projeto de Autorização de Transações

Este projeto implementa um sistema de autorização de transações financeiras, garantindo que todas as operações sejam validadas conforme regras de negócio pré-definidas. É projetado para ser robusto, seguro e fácil de integrar com sistemas existentes.

## Descrição

O sistema de autorização de transações verifica várias condições antes de permitir uma transação, incluindo verificação de saldo disponível, frequência de transações e outros critérios de segurança. Este repositório contém toda a lógica de backend necessária para a autorização de transações financeiras.

## Funcionalidades

- Verificação de conta ativa.
- Checagem de limite de transações.
- Prevenção de transações duplicadas em curto intervalo de tempo.
- Verificação de limite de saldo antes de autorizar uma transação.

## Tecnologias Utilizadas

- Java 21
- Maven para gerenciamento de dependências

## Instalação

### Pré-requisitos

- Java JDK 21 ou superior
- Maven 3.6.3 ou superior

### Configuração

Clone o repositório para sua máquina local:

```bash
git clone https://github.com/drapala/desafio-nu.git
cd desafio-nu
```

Para construir o projeto, execute:

```bash
mvn clean install
```

## Uso

Para rodar o sistema de autorização, execute:

```bash
java -jar target/desafio-nu-1.0.0.jar
```

### Exemplos de Uso

Para autorizar uma transação, você deve enviar uma requisição ao sistema com os detalhes da transação. Aqui está um exemplo simplificado de como você pode fazer isso em código:

```java
Transaction transaction = new Transaction(new BigDecimal("100.00"), "Burger King", System.currentTimeMillis());
Account account = new Account(true, new BigDecimal("500.00"), new ArrayList<>());
Result result = Authorizer.authorize(transaction, account);
System.out.println(result);
```
Se tudo ocorrer corretamente, você deve ter o seguinte output:
```python
Test Case: Successful domain.Transaction
domain.Account: account = {
  active: true,
  availableLimit: 50.00,
  history: [
    {amount: 50.00, merchant: "Burger King", time: 1713450995999}
  ]
}
Violations: []

Test Case: Insufficient Limit
domain.Account: account = {
  active: true,
  availableLimit: 100.00,
  history: [

  ]
}
Violations: [first-transaction-above-threshold, insufficient-limit]

Test Case: Inactive domain.Account
domain.Account: account = {
  active: false,
  availableLimit: 100.00,
  history: [

  ]
}
Violations: [account-not-active]

Test Case: First domain.Transaction Above Threshold
domain.Account: account = {
  active: true,
  availableLimit: 100.00,
  history: [

  ]
}
Violations: [first-transaction-above-threshold]

Test Case: High Frequency Small Interval
domain.Account: account = {
  active: true,
  availableLimit: 100.00,
  history: [
    {amount: 10.00, merchant: "Burger King", time: 1713450996021},
    {amount: 20.00, merchant: "Burger King", time: 1713450936021},
    {amount: 30.00, merchant: "Burger King", time: 1713450906021},
    {amount: 40.00, merchant: "Burger King", time: 1713450876021}
  ]
}
Violations: [high-frequency-small-interval]

Test Case: Doubled domain.Transaction
domain.Account: account = {
  active: true,
  availableLimit: 100.00,
  history: [
    {amount: 50.00, merchant: "Burger King", time: 1713450936021},
    {amount: 50.00, merchant: "Burger King", time: 1713450906021}
  ]
}
Violations: [doubled-transaction]


Process finished with exit code 0
```
