package com.ncsoft.ncslambda.network

import java.util

import com.ncsoft.ncslambda.entity.URINode
import org.glassfish.jersey.uri.UriTemplate
import org.junit.{Assert, Test}

import scala.collection.mutable.ListBuffer
/**
  * Created by leeyh0216 on 17. 4. 23.
  */
class URIManagementModuleTest {
  val uriManagementModule = new URIManagementModule

  @Test
  def testHasDuplicateURI(): Unit = {
    val existURIList = List("/a","/a/{1}","a/{1}/b")
    val existNodeList = for{ uri <- existURIList
    } yield {
      val node = new URINode
      node.uri = uri
      node.httpMethod = "GET"
      node
    }

    val firstMatchNode = new URINode
    firstMatchNode.uri = "/a"
    firstMatchNode.httpMethod = "GET"
    Assert.assertTrue(uriManagementModule.hasDuplicateURI(firstMatchNode,existNodeList))

    val firstURIDismatchNode = new URINode
    firstURIDismatchNode.uri = "/b"
    firstURIDismatchNode.httpMethod = "GET"
    Assert.assertFalse(uriManagementModule.hasDuplicateURI(firstURIDismatchNode,existNodeList))

    val firstMethodDismatchNode = new URINode
    firstMethodDismatchNode.uri = "/a"
    firstMethodDismatchNode.httpMethod = "PUT"
    Assert.assertFalse(uriManagementModule.hasDuplicateURI(firstMethodDismatchNode,existNodeList))

    val secondURIExactMatchNode = new URINode
    secondURIExactMatchNode.uri = "/a/second"
    secondURIExactMatchNode.httpMethod = "GET"
    Assert.assertFalse(uriManagementModule.hasDuplicateURI(secondURIExactMatchNode,existNodeList))

    val secondURIAbstractMatchNode = new URINode
    secondURIAbstractMatchNode.uri = "/a/{test}"
    secondURIAbstractMatchNode.httpMethod = "GET"
    Assert.assertTrue(uriManagementModule.hasDuplicateURI(secondURIAbstractMatchNode,existNodeList))
  }
}
