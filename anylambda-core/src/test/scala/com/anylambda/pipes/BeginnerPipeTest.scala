package com.anylambda.pipes

import org.apache.spark.sql
import org.junit.Test
import org.junit.rules.TemporaryFolder

class BeginnerPipeTest {
  val tmpDir = new TemporaryFolder()
  tmpDir.create()
  tmpDir.getRoot.setExecutable(true)
  tmpDir.getRoot.setReadable(true)
  tmpDir.getRoot.setWritable(true)

  System.setProperty("hadoop.home.dir", "C:\\Users\\leeyh\\Documents\\projects\\win_hadoop")
  val sparkSession = new sql.SparkSession.Builder()
                              .master("local")
                              .enableHiveSupport()
                              .getOrCreate()
  val pipe = new TestBeginnerPipe(sparkSession)

  @Test
  def testSQL(): Unit ={
    import sparkSession.sqlContext.implicits._

    sparkSession.sql("CREATE DATABASE IF NOT EXISTS hive")
    val src = Seq(
      (1, "leeyh0216"),
      (2, "psyoblade"),
      (3, "chiyoung"),
      (4, "shryu"),
      (5, "ajg")
    ).toDF("idx", "name").write.mode("append").saveAsTable("hive.A")
    pipe.execute()
  }
}
