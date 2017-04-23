package com.ncsoft.ncslambda.network

import java.util

import com.ncsoft.ncslambda.entity.URINode
import com.ncsoft.ncslambda.util.Logging
import org.glassfish.jersey.uri.UriTemplate

/**
  * Created by leeyh0216 on 17. 4. 23.
  */
class URIManagementModule extends Logging{

  def hasDuplicateURI(node: URINode, existNodeList: List[URINode]): Boolean ={
    //이미 존재하는 Node 중 URI만 일치하는 Node 추출
    val filteredByURIList = existNodeList.filter{n : URINode =>
      val params = new util.HashMap[String,String]()
      val template = new UriTemplate(node.uri)
      template.`match`(n.uri,params)
    }

    //만일 URI가 매칭하는 Node가 존재할 경우 Method까지 일치하는지 확인한다.
    if(filteredByURIList.size!=0){
      val filteredByMethodList = filteredByURIList.filter{s : URINode =>
        s.httpMethod == node.httpMethod
      }
      //Method 일치 여부를 Return한다(Method가 동일할 경우 true, Method가 동일하지 않을 경우 false)
      if(filteredByMethodList.size == 0)
        false
      else
        true
    }
    else
      false
  }
}
