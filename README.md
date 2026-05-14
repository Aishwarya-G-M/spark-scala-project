### Local development setup
- Find Spark version and documentation
- Select sbt, Scala and JDK version
- Create Intellij project
- Add dependencies

### First Spark Application
- Download example data (from Kaggle)
- Instantiate a standalone SparkSession
- Read csv file with options
- Show DataFrame - the first action we will be trying
- Add JVM option `--add-exports java.base/sun.nio.ch=ALL-UNNAMED`

### Schema
- Inspect the schema of a DataFrame using `.printSchema()`
- Find documentation of all data types (mapping between SQL and scala data types)
- CSV source option `inferSchema`

### The Dataset API
- Dataset is the main abstraction introduced in Spark SQL
- Spark SQL is an abstraction over Spark core's RDDs
- The Dataset API defines a DSL (domain-specific language, declarative, not using Scala functions)
- We can use this API to talk to spark to translate the code into spark internals that it can execute
- `type DataFrame = Dataset[Row]` : DataFrame is a type definition of being a Dataset
- `Row` is a generic object(untyped view)

### Referencing columns
- Mostly when using the API, we work with Columns `cols("a") +5`
- Ways of referencing columns: String, apply, col, $(implicits)
- Not necessarily bound to DataFrame

### Column functions
- The `Column` class
- Functions on columns: `===` , `cast`, `<`, `+`
- Reading the reference
- `sql.functions` : `col`, `lit`, `concat`

### Expressions
- We have explored the `Column` class which defines transformations on the columns
- We have the `org.apache.spark.sql.functions` which also defines a lot of functions which we can use to transform columns
- There is another way to transform columns (not recommended) : expressions - because it does not catch some errors in compile time ex: incorrect column names
- We can also write SQL expressions as strings, which will be interpreted at runtime (no compile safety)

### Rename columns, varargs, withColumn, filter
- Rename all columns to be of camel case format
- Add a column containing the diff between `open` and `close`
- Filter to days when the `close` price was more that 10% higher than the open price

### Concepts 
- What is Spark ? Why does it take long to execute for example a standalone spark app like in our case as opposed to using a library like pandas
- Why do we need to bind to a port ?
- Spark is a distributed processing engine : Our code can run locally, or on dozens or even hundreds of machines (GBs of data or Billions of rows)
- Usually used as processing engine on data lakes (file-based large-scale data stores); it is not a database
- Master-slave (parent-child) architecture : Driver does planning of work and assigns tasks to workers (declarative, SQL-like API)
- Too much overhead for using with small csv files like in our example

### Sort , Group, Aggregate
- Select, transform , rename columns (transforming DataFrame on each row)
- Next, working with multiple rows in a DataFrame
  - Sort on one or multiple columns (asc/desc)
  - GroupBy one or multiple columns and applying an aggregation on groups
- Available aggregation functions can be found in `sql-functions` (for some exists a shorthand `count`, `sum`)
- Assignment : Average and highest closing prices per year, sorted with the highest prices first

### Window Functions
- With `groupBy` we could only retrieve the grouping columns and the aggregation column (`year`,`maxClose`)
- What if we wanted to see the entire record of the days with the highest closing prices (e.g `open`,`high`, values)? 
- Window functions as construct to apply a partitioned view and allowing arbitrary transformations on the grouped data
- Assignment: Find rows of the highest closing prices on each year, sorted with the highest closing prices first

### Partitioning
### AST, logical plan and the Catalyst
- Spark SQL provides us with a fully declarative and Structured API
- Meaning it defines operators which we can use to tell Spark how the result can be derived from the input (`withColumn`,`groupBy`,`count`)
- Spark SQL is also called the structured API as it deals with structured data as Datasets have a schema
- Therefore, Sparks knows what is in the columns of a DataFrame
- By calling the API, we chain these operators, while Spark assembles abstract internal representation: The AST (Abstract Syntax Tree)
- Due to this internal representation we need not worry about what external API is being use to build the tree, i.e Scala, Python, R, SQL etc.
- Eventually all these external modes will built an AST representation.
- Spark uses the Catalyst (optimizer) to switch orders or pushing down filters etc to optimize the query for us.
- AST is therefore a computational plan. From AST we can tell how a current DataFrame is derived from its parent DataFrame, this is called a lineage.

### Lazy Evaluation
- There are transformations and actions
- A transformation simply adds a step to the AST without doing anything more
- An action in turn is called on the result Dataset and actually triggers the calculation of the result by executing the AST
- That is called lazy evaluation as we only evaluate the result once we want to see it
- The transformations will devise a logical plan on the driver program and only when action is called will the logical plan be executed on the cluster.

### Why do we need tests ?
- Do you know whether the result from our previous assignment was correct?
- We can gain certainty by inspecting the csv dataset and check against our result but this will get cumbersome at some point
- What if the code or the data changes ? Do it again ? How many times ? - Manual testing -> engineer’s nightmare
- High-quality software = Composition of small well-functioning and tested units
- What are unit tests ?
- Automated way of assuring that our units do what they are supposed to do (testing their interface)

### Unit Tests
- Add a test dependency : [scala-test](https://www.scalatest.org/)
- Write our first unit test
- What is testable code ?
- We would like to call a unit which contains a small self-contained set of functionality
- Therefore we have to provide the full context of the unit (class, parameters)
- Therefore, we want to keep it as small as possible
- Refactor our code to be easily testable

### Dataframe Test
- We will test our transformations and therefore our resulting DataFrame is correct
- Create a DataFrame with test data(date, open, close)
- Not all columns are required by the interface of the unit (dynamically typed)
- Using matchers to assert result; Here, we can choose which columns to test for (exact match or subset)
- Add JVM option `—add-exports java.base/sun.util.calendar=ALL-UNNAMED`