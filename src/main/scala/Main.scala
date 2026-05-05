package de.agm.sparkscalaproject

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}

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
    df.select("Date","Open","Close").show()
    // using object column
    val column = df("Date")
    // using a function
    col("Date")
    // import spark implicits
    import spark.implicits._
    $"Date"

    //df("Close") -> uses the apply
    //df.select(col("Date"), $"Open", df("Close")).show()
    // or
    df.select(column, $"Open", df("Close")).show()
  }
}
