package com.anylambda.pipes

import org.apache.spark.sql.{DataFrame, SparkSession}

class TestBeginnerPipe(spark: SparkSession) extends AbstractBeginnerPipe(spark) {
  import spark.sql
  import AbstractBeginnerPipe.BeginnerDataFrame

  override def execute(): DataFrame = {
    sql("""use hive""")
    sql("""show tables""") show(false)
    sql("""SELECT * FROM A""") table "hello"
    sql("""SELECT COUNT(*) FROM hello""")  table "b" cache
  }
}
