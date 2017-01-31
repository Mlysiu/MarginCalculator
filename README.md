# MarginCalculator
Proposed schema is in ```resources``` directory inside main.

* Launch this project with ````sbt "run <months> <loanAmount> <deductible>"```` command

* Test it with ```sbt test``` command

### Example run command
```sbt "run 50 50000 10000"```


### User interface
As this application keeps the schema inside a .json file it is allowing for a wide spread of the UI designs.

For example data can be provided into the form similar to table or any other kinds of UI grid.


### Data storage
A NoSQL database (like Redis or MongoDB) is perfectly fine to keep these schemas.

There are no benefits of dividing the schema so that it could fit a relational table. We can embrace the fact that it is written in .json and use it as value to some artificially created key (e.g. bankname_timestamp).