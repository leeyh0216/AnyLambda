package com.anylambda.util

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File, InputStream}
import java.net.URL
import java.util
import java.util.jar.JarFile

import scala.collection.mutable.ListBuffer
import scala.reflect.internal.util.ScalaClassLoader.URLClassLoader
import scala.util.control.NonFatal

/**
  * Created by leeyh0216 on 17. 4. 27.
  */
class DynamicClassLoader(urls : Seq[URL], clsLoader : ClassLoader) extends URLClassLoader(urls,clsLoader) with Logging{

  val fileExistChk = ( u: URL ) => {
    val file = new File(u.toString.replace("file:",""))
    logger.info("exist {} : {}",file.getAbsolutePath,file.exists())
    file.exists()
  }

  val jarFileList = urls.filter(fileExistChk).map( u => new JarFile(u.toString.replace("file:","")))
  println(jarFileList.size)
  @throws(classOf[ClassNotFoundException])
  def findClassFromJarList(clsName : String) : Class[_] = {
    var className = clsName.replaceAll("\\.","/")
    val result = jarFileList.map{ f =>
//      val entries = f.entries()
//      while(entries.hasMoreElements){
//        val e = entries.nextElement()
//        logger.info(e.getName)
//      }
      //logger.info("{} entry",f.getEntry(String.format("%s.class",className)))
      (f,f.getJarEntry(String.format("%s.class",className)))
    }.filter( e => e._2!=null )
    if(result.size==0)
      throw new ClassNotFoundException(String.format("%s 클래스를 찾을 수 없습니다.",clsName))
    else{
      val jarFile = result(0)._1
      val jarEntry = result(0)._2

      var inputStream : InputStream = null
      var byteArrayOutputStream : ByteArrayOutputStream = null
      try {
        inputStream = jarFile.getInputStream(jarEntry)
        byteArrayOutputStream = new ByteArrayOutputStream()
        var nextValue : Int = inputStream.read()
        while(nextValue != -1){
          byteArrayOutputStream.write(nextValue)
          nextValue = inputStream.read()
        }

        val clsByte = byteArrayOutputStream.toByteArray
        logger.info("{} Class Find in Runtime Jar",clsName)
        defineClass(clsName,clsByte,0,clsByte.length,null)
      }
      catch{
        case NonFatal(e) => throw new ClassNotFoundException(String.format("%s 클래스를 찾을 수 없습니다.",clsName))
      }
      finally{
        if(byteArrayOutputStream != null)
          byteArrayOutputStream.close()
        if(inputStream != null)
          inputStream.close()
      }
    }
  }
  override def addURL(url: URL): Unit = super.addURL(url)

  override def findClass(name: String): Class[_] = {
    //logger.info(s"find class : $name")
    try {
      super.findClass(name)

    }
    catch{
      case NonFatal(e) => findClassFromJarList(name)
    }
  }
}
