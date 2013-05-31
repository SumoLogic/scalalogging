package com.typesafe.scalalogging.slf4j

import org.specs2.mutable.Specification

object Slf4jSimpleLoggingSpec extends Specification with SimpleLogging {
  private val Message = "String message!"

  "SimpleLogging" should {
    "log error" in {
      error(Message)
    }
    "log warn" in {
      warn(Message)
    }
    "log info" in {
      warn(Message)
    }
    "log debug" in {
      debug(Message)
    }
    "log trace" in {
      trace(Message)
    }
  }
}
