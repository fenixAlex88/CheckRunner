# Check Runner

## Description

The project is an application for creating receipts taking into account discounts and a discount program, as well as calculating total costs

## Getting Started

### Dependencies

•  Java 21

•  Gradle 8.5


### Installing

You need to run maven to compile their package files

```bash
gradle build
```

Launch the application from the command line

```bash
java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java id-quantity discountCard=xxxx balanceDebitCard=xxxx saveToFile=xxxx datasource.url=XXXX datasource.username=XXXX datasource.password=XXXX
```
CheckRunner-1.0-SNAPSHOT.jar
Where:
1) id - the product identifier
2) quantity - the quantity of the product
3) discountCard=xxxx - the name and number of the discount card
4) balanceDebitCard=xxxx - the balance on the debit card
5) saveToFile - includes a relative (from the project root directory) path + file name with extensions
6) datasource.url -
   the URL of the database
7) datasource.username - the name of the user to connect to the database
8) datasource.password - password for connecting to the database

For example:
```bash
java -jar build/libs/CheckRunner-1.0-SNAPSHOT.jar 2-1 3-1 2-5 discountCard=1111 balanceDebitCard=100 saveToFile=./result.csv datasource.url=jdbc:postgresql://localhost:5432/check datasource.username=postgres datasource.password=postgres
```
### Description

After launching the application, arguments are read from the command line. They are parsed, validated and stored in the singleton class for future use. They create instances of services that access the database for data through repositories. Products are obtained from the database one at a time to maintain up-to-date information about their quantity in stock. A receipt is generated and written to a CSV file according to the command line request, as well as output to the console. With the help of the factory, custom exceptions are created, the message from which is written to a file and output to the console. JUnit tests are also written.

### Errors

#### BAD REQUEST 
If the input data is incorrect (not
Arguments filled in correctly, errors
quantity, lack of products) 
or If no arguments are passed:
saveToFile, datasource.url, datasource.username or datasource.password
#### NOT ENOUGH MONEY 
If there are insufficient funds (balance
less than the amount on the check)
#### INTERNAL SERVER ERROR 
In any other situations

### Authors

Pekar Aleksey
fenix_al@tut.by

