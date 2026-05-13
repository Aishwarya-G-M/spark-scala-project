package de.agm.sparkscalaproject

import de.agm.sparkscalaproject.topics.Dsl5
import org.apache.spark.sql.functions.{col, current_timestamp, expr, lit}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, SparkSession, functions}

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("spark-scala-project")
      .master("local[*]")
      .config("spark.driver.bindAddress","127.0.0.1")
      .getOrCreate()

    val df: DataFrame= spark.read
      .option("header",value = true)
      .option("inferSchema",value = true)
      .csv("data/AAPL.csv")

    df.show()
    df.printSchema()

    // ways to select columns from our dataframes
    //df.select("Date","Open","Close").show()
    // using object column
    //val column = df("Date")
    // using a function
    //col("Date")
    // import spark implicits
    //import spark.implicits._
    //$"Date"

    //df("Close") -> uses the apply
    //df.select(col("Date"), $"Open", df("Close")).show()
    // or
    //df.select(column, $"Open", df("Close")).show()

    val column2 = df("Open")
    val newColumn = (column2 + 2.0).as("OpenIncreasedBy2")
    // column2 + (2.0)
    val columnString = column2.cast(StringType)

    val newColumnString = functions.concat(columnString, lit("Hello world"))

//    df.select(column2,newColumn,columnString)
//      .filter(newColumn > 2.0)
//      .filter(newColumn > column2)
//      .filter(newColumn === column2)
//      .show()

    df.select(column2, newColumn, columnString,newColumnString).show(truncate = false)

    //It is discouraged to use string expressions because typos are not caught during compile time
    // example if below we had currents_timestamp instead of the actual current_timestamp this is not caught as compile error.
    val timestampFromExpression = expr("cast(current_timestamp() as string) as timestampExpression")
    //alternative way is to use current_timestamp() function directly
    //current_timestamp()

    val timestampFromFucntion = current_timestamp().cast(StringType).as("timestampFunction")

    df.select(timestampFromExpression, timestampFromFucntion).show()

    // which functions are available for SQL expressions - they are called sql built ins


    df.selectExpr("cast(Date as string)", "Open + 1.0", "current_timestamp()").show()


    // Run sql queries through spark sql as well
    // we first need to register our scala dataframe as a table
    // Recommendation is to use scala api
    df.createTempView("df")
    spark.sql("Select * from df").show()

    Dsl5.run(df)
  }
}
