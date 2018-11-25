package com.anylambda.compiler.core

case class CompileResult(scalaFiles: List[String], dependencyLibraries: List[String], elapsed: Long, createdJarFile: String)
