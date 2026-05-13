package de.agm.sparkscalaproject
package topics

import org.apache.spark.sql.{DataFrame, functions}
import org.apache.spark.sql.functions.{split, year}

object FuncsActingOnMultipleRowsPractice {
  // the dataframe you receive here is column modified dataframe where column names are camel-cased
  def run(columnRenamedStockData : DataFrame):DataFrame = {
        import columnRenamedStockData.sparkSession.implicits._

    columnRenamedStockData
      .groupBy(year($"date").as("year"))
      .agg(functions.max($"close").as("maxClose"), functions.avg($"close").as("avgClose"))
      .sort($"maxClose".desc)
      .show()

    // shorthands for aggregations
    columnRenamedStockData
      .groupBy(year($"date").as("year"))
      .max("close", "high")
      .show()

    columnRenamedStockData
  }

}
