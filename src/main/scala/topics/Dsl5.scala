package de.agm.sparkscalaproject
package topics

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}

object Dsl5 {
  def run(df: DataFrame) : Unit = {
    // 1. Rename all columns to be of camel case format
    df.withColumnRenamed("Open","open")
      .withColumnRenamed("Close","close") // we are able to append the .withColumnRenamed this way because
    //it returns a dataframe upon which another same action can be performed.
    // But since this can be tedious we could do :
    val renamedColumns = List(
      col("Date").as("date"),
      col("Open").as("open"),
      col("High").as("high"),
      col("Low").as("low"),
      col("Close").as("close"),
      col("Adj Close").as("adjClose"),
      col("Volume").as("volume"),
    )
    // 2. Add a column containing the diff between `open` and `close`
    val stockData = df.select(renamedColumns: _*).withColumn("diff", col("close") - col("open"))
    //df.select(df.columns.map(c => col(c).as(c.toLowerCase())): _*).show()
    //stockData.show()

    // 3. filter to days when the close price was more than 10% higher than the opening price on that day
    stockData.filter(col("close") > col("open")*1.1).show();
  }
}
