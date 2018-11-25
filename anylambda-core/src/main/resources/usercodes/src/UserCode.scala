package com.leeyh0216.lambda
import org.apache.spark.sql
import org.apache.spark.sql.SparkSession

case class User(name: String, age: Int)

object UserCode {
  def main(args: Array[String]): Unit ={
    val spark = new sql.SparkSession.Builder().config("spark.driver.memory", "4g").master("local").appName("hello").getOrCreate()

    spark.sql("""select 1""").show(3,false)
  }
}