package de.agm.sparkscalaproject
package topics

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{row_number, year}

object ExploringWindowFunctions {
  def highestClosingPricesPerYear(df: DataFrame) : DataFrame = {
    import df.sparkSession.implicits._

    val window = Window.partitionBy(year($"date").as("year")).orderBy($"close".desc)
    df
      .withColumn("rank",row_number().over(window))
      .filter($"rank" === 1)
      .drop("rank")
      .sort($"close".desc)
  }
}
