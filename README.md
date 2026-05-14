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
