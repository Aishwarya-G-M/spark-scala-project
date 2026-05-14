package de.agm.sparkscalaproject

import de.agm.sparkscalaproject.topics.ExploringWindowFunctions
import org.apache.spark.sql.types.{DateType, DoubleType, StructField, StructType}
import org.apache.spark.sql.{Encoder, Encoders, Row, SparkSession}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers.contain
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.sql.Date

class Test1 extends AnyFunSuite {
  test("add (5 + 2) returns 7") {
    val result = Main.add(5,2)

    assert(result == 7)
  }

  private val spark = SparkSession
    .builder()
    .appName("FirstTest")
    .master("local[*]")
    .getOrCreate()

  private val schema = StructType(
    Seq(
      StructField("date",DateType,nullable = true),
      StructField("open",DoubleType,nullable = true),
      StructField("close",DoubleType,nullable = true)
    ))

  test("returns highest closing prices for year"){
    val testRows = Seq(
      Row(Date.valueOf("2022-01-12"),1.0,2.0),
      Row(Date.valueOf("2023-03-01"),1.0,2.0),
      Row(Date.valueOf("2023-01-12"),1.0,3.0)
    )
    val expected = Seq(
      Row(Date.valueOf("2023-01-12"),1.0,3.0),
      Row(Date.valueOf("2022-01-12"),1.0,2.0)
    )

    implicit val encoder : Encoder[Row] = Encoders.row(schema)
    val testDF = spark.createDataset(testRows)
    val resultList = ExploringWindowFunctions.highestClosingPricesPerYear(testDF)
      .collect()

    //resultList should contain allElementsOf(expected)
    resultList should contain theSameElementsAs (expected)
  }
}
