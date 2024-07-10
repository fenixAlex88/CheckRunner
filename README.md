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
java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java id-quantity discountCard=xxxx balanceDebitCard=xxxx pathToFile=XXXX saveToFile=xxxx
```

Where:
1) id - the product identifier
2) quantity - the quantity of the product
3) discountCard=xxxx - the name and number of the discount card
4) balanceDebitCard=xxxx - the balance on the debit card
5) pathToFile - includes a relative (from the project root directory) path + file name with extensions (always present in the specified format)
6) saveToFile - includes a relative (from the project root directory) path + file name with extensions

For example:
```bash
java -cp target/classes ru.clevertec.check.CheckRunner 3-3 2-1 5-1 4-2 2-2 discountCard=2222 balanceDebitCard=1000 pathToFile=./products.csv saveToFile=./error_result.csv
```
### Description

After launch, the application reads the specified file with a list of products and generates a sales receipt. If an error occurs, inform the console and the file with the sales receipt.

### Errors

#### BAD REQUEST 
If the input data is incorrect (not
Arguments filled in correctly, errors
quantity, lack of products) 
or If no arguments are passed:
pathToFile and/or saveToFile
#### NOT ENOUGH MONEY 
If there are insufficient funds (balance
less than the amount on the check)
#### INTERNAL SERVER ERROR 
In any other situations

### Authors

Pekar Aleksey
fenix_al@tut.by

