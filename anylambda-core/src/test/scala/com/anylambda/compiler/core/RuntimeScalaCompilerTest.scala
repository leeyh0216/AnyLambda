package com.anylambda.compiler.core

import java.io.File

import com.anylambda.compiler.core
import org.junit.Test
import org.junit.rules.TemporaryFolder

class RuntimeScalaCompilerTest {

  @Test
  def testCompileClasses(): Unit ={
    val rootDir = new File("src/test/resources/spark_based_project")
    val srcDir = new File(rootDir, "src")
    val dependenciesDir = new File(rootDir, "libs")
    val dependenciesDirList = List(dependenciesDir.getAbsolutePath)


    val tmpDir = new TemporaryFolder()
    tmpDir.create()
    val rsc = new RuntimeScalaCompiler.Builder()
                    .setSourceDir(srcDir.getAbsolutePath)
                    .setDependenciesDirs(dependenciesDirList)
                    .setResultDir(tmpDir.getRoot.getAbsolutePath)
                    .build()
    val compileResult = rsc.compile()
  }
}
