package com.anylambda.entity

import java.util.{Calendar, Date}

/**
  * Created by leeyh0216 on 17. 4. 23.
  */
class URINode {
  var nodeName: String = _
  var uri: String = _
  var httpMethod: String = _
  var function: String = _
  var lastModified: Date = Calendar.getInstance().getTime
}
