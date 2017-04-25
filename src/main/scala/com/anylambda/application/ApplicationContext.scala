package com.anylambda.application

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory


/**
  * Created by leeyh0216 on 17. 4. 22.
  */
class ApplicationContext(val appName : String) {
  val applicationName = appName
}
