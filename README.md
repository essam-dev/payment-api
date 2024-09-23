# Exercise OnePay - Version 2.1

## Objective

Create a Spring Boot application exposing a RESTful API to manage _payments_.
This API must allow creating, reading, and modifying _payments_ in an H2 database.

A _payment_ confirms the purchase order of one or more _items_, and consists of:

- An object `Transaction` with the following required attributes:
    - Total amount of the transaction, in Euros.
    - Payment type, one of the following: `CREDIT_CARD`, `GIFT_CARD`, `PAYPAL`.
    - Payment status, one of the following: `NEW`, `AUTHORIZED`, `CAPTURED`, `CANCELED`.
    - One or more `Item` objects, each having the following required attributes:
        - Item name.
        - Price.
        - Quantity.

### Business rules

- A newly created `Transaction` must be in the status `NEW`.
- It is only possible to change the transaction status to `CAPTURED` when the transaction is currently in status `AUTHORIZED`.
- It is not possible to change the status of a `CAPTURED` or `CANCELED` transaction.
- It is not possible to change the purchase order when the transaction is modified.

## Validation

To verify the correctness of the API, build a test suite representing the following scenario:

- Creation of a `Transaction` with payment type `CREDIT_CARD`, for an order of **5** T-shirts costing **19.99** Euros each.
- Modification of the transaction to the status `AUTHORIZED`.
- Modification of the transaction to the status `CAPTURED`.

- Creation of a `Transaction` with payment type `PAYPAL`, for an order of **1** bike costing **208.00** Euros and **1** pair of shoes costing **30.00** Euros.
- Modification of the transaction to the status `CANCELED`.

- Retrieval of all transactions.

## Submission

The API must be built in English.

The code must be delivered as a project accessible via a public Git repository (Github, Gitlab, or similar). If this is not possible, please contact us in advance.

Feel free to ask us any questions or clarifications about the exercise.
