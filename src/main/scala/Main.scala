package de.agm.sparkscalaproject

import org.apache.spark.sql.functions.{col, lit}
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
  }
}
