# Check Runner

## Description

The project is an application for creating receipts taking into account discounts and a discount program, as well as calculating total costs

## Getting Started

### Dependencies

•  Java 21

•  Maven 3.8


### Installing

You need to run maven to compile their package files

```bash
mvn clean compile 
```

Launch the application from the command line

```bash
java -cp target/classes ru.clevertec.check.CheckRunner id-quantity discountCard=xxxx balanceDebitCard=xxxx
```

Where:
1) id - the product identifier
2) quantity - the quantity of the product
3) discountCard=xxxx - the name and number of the discount card
4) balanceDebitCard=xxxx - the balance on the debit card

For example:
```bash
java -cp target/classes ru.clevertec.check.CheckRunner 3-3 2-1 5-1 4-2 2-2 discountCard=2222 balanceDebitCard=1000
```

### Description

Once launched, the application reads information from the launch line and generates a sales receipt. If an error occurs, notify the console and the sales receipt file.
You can view the result of the application by opening the file ./result.csv

### Errors

#### BAD REQUEST
If the input data is incorrect (not
Arguments filled in correctly, errors
quantity, lack of products)
#### NOT ENOUGH MONEY
If there are insufficient funds (balance
less than the amount on the check)
#### INTERNAL SERVER ERROR
In any other situations

### Authors


Pekar Aleksey
fenix_al@tut.by

