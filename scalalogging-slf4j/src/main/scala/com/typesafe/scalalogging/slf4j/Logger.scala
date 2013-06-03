/*
 * Copyright 2012 Typesafe Inc. <http://www.typesafe.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.typesafe.scalalogging.slf4j

import language.experimental.macros
import org.slf4j.{ Logger => Underlying, Marker }

object Logger {

  /**
   * Create a [[com.typesafe.scalalogging.slf4j.Logger]] wrapping the given underlying `org.slf4j.Logger`.
   */
  def apply(underlying: Underlying): Logger =
    new Logger(underlying)
}

/**
 * Convenient and performant wrapper around the given underlying `org.slf4j.Logger`.
 *
 * Convenient, because you can use string formatting, string interpolation or whatever you want
 * without thinking too much about performance.
 * Performant, because by using macros the log methods are expanded inline to the check-enabled idiom.
 */
class Logger(val underlying: Underlying) {

  def addLoggingContext(message: String, callerPosition: String, callerLocation: String): String = message

  // Error

  final def error(message: String): Unit = macro LoggerMacros.errorMessage

  final def error(message: String, params: AnyRef*): Unit = macro LoggerMacros.errorMessageParams

  final def error(message: String, t: Throwable): Unit = macro LoggerMacros.errorMessageThrowable

  final def error(marker: Marker, message: String): Unit = macro LoggerMacros.errorMarkerMessage

  final def error(marker: Marker, message: String, params: AnyRef*): Unit = macro LoggerMacros.errorMarkerMessageParams

  final def error(marker: Marker, message: String, t: Throwable): Unit = macro LoggerMacros.errorMarkerMessageThrowable

  // Warn

  final def warn(message: String): Unit = macro LoggerMacros.warnMessage

  final def warn(message: String, params: AnyRef*): Unit = macro LoggerMacros.warnMessageParams

  final def warn(message: String, t: Throwable): Unit = macro LoggerMacros.warnMessageThrowable

  final def warn(marker: Marker, message: String): Unit = macro LoggerMacros.warnMarkerMessage

  final def warn(marker: Marker, message: String, params: AnyRef*): Unit = macro LoggerMacros.warnMarkerMessageParams

  final def warn(marker: Marker, message: String, t: Throwable): Unit = macro LoggerMacros.warnMarkerMessageThrowable

  // Info

  final def info(message: String): Unit = macro LoggerMacros.infoMessage

  final def info(message: String, params: AnyRef*): Unit = macro LoggerMacros.infoMessageParams

  final def info(message: String, t: Throwable): Unit = macro LoggerMacros.infoMessageThrowable

  final def info(marker: Marker, message: String): Unit = macro LoggerMacros.infoMarkerMessage

  final def info(marker: Marker, message: String, params: AnyRef*): Unit = macro LoggerMacros.infoMarkerMessageParams

  final def info(marker: Marker, message: String, t: Throwable): Unit = macro LoggerMacros.infoMarkerMessageThrowable

  // Debug

  final def debug(message: String): Unit = macro LoggerMacros.debugMessage

  final def debug(message: String, params: AnyRef*): Unit = macro LoggerMacros.debugMessageParams

  final def debug(message: String, t: Throwable): Unit = macro LoggerMacros.debugMessageThrowable

  final def debug(marker: Marker, message: String): Unit = macro LoggerMacros.debugMarkerMessage

  final def debug(marker: Marker, message: String, params: AnyRef*): Unit = macro LoggerMacros.debugMarkerMessageParams

  final def debug(marker: Marker, message: String, t: Throwable): Unit = macro LoggerMacros.debugMarkerMessageThrowable

  // Trace

  final def trace(message: String): Unit = macro LoggerMacros.traceMessage

  final def trace(message: String, params: AnyRef*): Unit = macro LoggerMacros.traceMessageParams

  final def trace(message: String, t: Throwable): Unit = macro LoggerMacros.traceMessageThrowable

  final def trace(marker: Marker, message: String): Unit = macro LoggerMacros.traceMarkerMessage

  final def trace(marker: Marker, message: String, params: AnyRef*): Unit = macro LoggerMacros.traceMarkerMessageParams

  final def trace(marker: Marker, message: String, t: Throwable): Unit = macro LoggerMacros.traceMarkerMessageThrowable
}

object RichLogger {
  def apply(underlying: Underlying): RichLogger =
    new RichLogger(underlying)
}

class RichLogger(underlying: Underlying) extends Logger(underlying) {
  override def addLoggingContext(message: String, callerPosition: String, callerLocation: String): String = {
    s"[pos=$callerPosition $callerLocation] $message"
  }
}
