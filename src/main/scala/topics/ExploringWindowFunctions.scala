package de.agm.sparkscalaproject
package topics

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{row_number, year}

object ExploringWindowFunctions {
  def run(sortedAggDataFrame: DataFrame) : Unit = {
    import sortedAggDataFrame.sparkSession.implicits._

    val window = Window.partitionBy(year($"date").as("year")).orderBy($"close".desc)
    sortedAggDataFrame
      .withColumn("rank",row_number().over(window))
      .filter($"rank" === 1)
      .sort($"close".desc)
      .show()
  }
}
