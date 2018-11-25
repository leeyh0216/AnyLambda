package com.anylambda.pipes

import org.apache.spark.sql.{DataFrame, SparkSession}

object AbstractBeginnerPipe {
  implicit class BeginnerDataFrame(val df: DataFrame) {
    def table(tblName: String): DataFrame = { df.createOrReplaceTempView(tblName); df }
    def parallel(numPartition: Int): DataFrame = { df.repartition(numPartition); df }
    def cache: DataFrame = df.cache()
  }
}

abstract class AbstractBeginnerPipe(spark: SparkSession) {

  def execute(): DataFrame

}