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

import org.slf4j.Marker
import scala.reflect.macros.Context
import scala.language.existentials

private object LoggerMacros {

  type LoggerContext = Context { type PrefixType = Logger }

  class Builder[C <: Context](val c: C, level: String) {
    import c.universe._
    private val underlying = Select(c.prefix.tree, newTermName("underlying"))
    private val isEnabled = Select(underlying, newTermName(s"is${level.head.toUpper +: level.tail}Enabled"))
    private def logger = Select(underlying, newTermName(level))

    private def logArgs(args: List[c.universe.Tree]): c.Expr[Unit] = {
      val fallbackMessage = Apply(
        Select(
          Apply(
            Select(Ident(c.mirror.staticModule("scala.StringContext")), newTermName("apply")),
            List(
              Literal(Constant("Couldn't generate message, because ")),
              Literal(Constant(" thrown: '")),
              Literal(Constant("'"))
            )
          ),
          newTermName("s")
        ),
        List(
          Apply(Select(Apply(Select(Ident(newTermName("e")), newTermName("getClass")), List()), newTermName("getName")), List()),
          Apply(Select(Ident(newTermName("e")), newTermName("getMessage")), List())
        )
      )

      val fallback = Apply(logger, List(fallbackMessage))

      val safeLog = Try(
        Apply(logger, args),
        List(CaseDef(
          Bind(
            newTermName("e"),
            Typed(Ident(nme.WILDCARD), Select(Ident(c.mirror.staticModule("scala.package")), newTypeName("Exception")))
          ),
          EmptyTree,
          fallback
        )),

        EmptyTree
      )
      c.Expr(If(isEnabled, safeLog, EmptyTree))
    }

    def logMessage(message: c.Expr[String]) = {
      logArgs(List(message.tree))
    }

    def logMessageParams(message: c.Expr[String],
      params: Seq[c.Expr[AnyRef]],
      marker: Option[c.Expr[Marker]]) = {
      val paramsWildcard = Typed(
        Apply(
          Ident(newTermName("List")),
          (params map (_.tree)).toList
        ),
        Ident(tpnme.WILDCARD_STAR)
      )
      logArgs(marker.foldRight(message.tree +: List(paramsWildcard))(_.tree +: _))
    }

    def logMessageThrowable(message: c.Expr[String], t: c.Expr[Throwable]) = {
      logArgs(List(message.tree, t.tree))
    }

    def logMarkerMessage(marker: c.Expr[Marker], message: c.Expr[String]) = {
      logArgs(List(marker.tree, message.tree))
    }

    def logMarkerMessageThrowable(marker: c.Expr[Marker], message: c.Expr[String], t: c.Expr[Throwable]) = {
      logArgs(List(marker.tree, message.tree, t.tree))
    }
  }

  // Error
  def errorMessage(c: LoggerContext)(message: c.Expr[String]) =
    new Builder[c.type](c, "error").logMessage(message)

  def errorMessageParams(c: LoggerContext)(message: c.Expr[String], params: c.Expr[AnyRef]*) =
    new Builder[c.type](c, "error").logMessageParams(message, params, None)

  def errorMessageThrowable(c: LoggerContext)(message: c.Expr[String], t: c.Expr[Throwable]) =
    new Builder[c.type](c, "error").logMessageThrowable(message, t)

  def errorMarkerMessage(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String]) =
    new Builder[c.type](c, "error").logMarkerMessage(marker, message)

  def errorMarkerMessageParams(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String], params: c.Expr[AnyRef]*) =
    new Builder[c.type](c, "error").logMessageParams(message, params, Some(marker))

  def errorMarkerMessageThrowable(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String], t: c.Expr[Throwable]) =
    new Builder[c.type](c, "error").logMarkerMessageThrowable(marker, message, t)

  // Warn
  def warnMessage(c: LoggerContext)(message: c.Expr[String]) =
    new Builder[c.type](c, "warn").logMessage(message)

  def warnMessageParams(c: LoggerContext)(message: c.Expr[String], params: c.Expr[AnyRef]*) =
    new Builder[c.type](c, "warn").logMessageParams(message, params, None)

  def warnMessageThrowable(c: LoggerContext)(message: c.Expr[String], t: c.Expr[Throwable]) =
    new Builder[c.type](c, "warn").logMessageThrowable(message, t)

  def warnMarkerMessage(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String]) =
    new Builder[c.type](c, "warn").logMarkerMessage(marker, message)

  def warnMarkerMessageParams(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String], params: c.Expr[AnyRef]*) =
    new Builder[c.type](c, "warn").logMessageParams(message, params, Some(marker))

  def warnMarkerMessageThrowable(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String], t: c.Expr[Throwable]) =
    new Builder[c.type](c, "warn").logMarkerMessageThrowable(marker, message, t)

  // Info
  def infoMessage(c: LoggerContext)(message: c.Expr[String]) =
    new Builder[c.type](c, "info").logMessage(message)

  def infoMessageParams(c: LoggerContext)(message: c.Expr[String], params: c.Expr[AnyRef]*) =
    new Builder[c.type](c, "info").logMessageParams(message, params, None)

  def infoMessageThrowable(c: LoggerContext)(message: c.Expr[String], t: c.Expr[Throwable]) =
    new Builder[c.type](c, "info").logMessageThrowable(message, t)

  def infoMarkerMessage(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String]) =
    new Builder[c.type](c, "info").logMarkerMessage(marker, message)

  def infoMarkerMessageParams(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String], params: c.Expr[AnyRef]*) =
    new Builder[c.type](c, "info").logMessageParams(message, params, Some(marker))

  def infoMarkerMessageThrowable(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String], t: c.Expr[Throwable]) =
    new Builder[c.type](c, "info").logMarkerMessageThrowable(marker, message, t)

  // Debug
  def debugMessage(c: LoggerContext)(message: c.Expr[String]) =
    new Builder[c.type](c, "debug").logMessage(message)

  def debugMessageParams(c: LoggerContext)(message: c.Expr[String], params: c.Expr[AnyRef]*) =
    new Builder[c.type](c, "debug").logMessageParams(message, params, None)

  def debugMessageThrowable(c: LoggerContext)(message: c.Expr[String], t: c.Expr[Throwable]) =
    new Builder[c.type](c, "debug").logMessageThrowable(message, t)

  def debugMarkerMessage(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String]) =
    new Builder[c.type](c, "debug").logMarkerMessage(marker, message)

  def debugMarkerMessageParams(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String], params: c.Expr[AnyRef]*) =
    new Builder[c.type](c, "debug").logMessageParams(message, params, Some(marker))

  def debugMarkerMessageThrowable(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String], t: c.Expr[Throwable]) =
    new Builder[c.type](c, "debug").logMarkerMessageThrowable(marker, message, t)

  // Trace
  def traceMessage(c: LoggerContext)(message: c.Expr[String]) =
    new Builder[c.type](c, "trace").logMessage(message)

  def traceMessageParams(c: LoggerContext)(message: c.Expr[String], params: c.Expr[AnyRef]*) =
    new Builder[c.type](c, "trace").logMessageParams(message, params, None)

  def traceMessageThrowable(c: LoggerContext)(message: c.Expr[String], t: c.Expr[Throwable]) =
    new Builder[c.type](c, "trace").logMessageThrowable(message, t)

  def traceMarkerMessage(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String]) =
    new Builder[c.type](c, "trace").logMarkerMessage(marker, message)

  def traceMarkerMessageParams(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String], params: c.Expr[AnyRef]*) =
    new Builder[c.type](c, "trace").logMessageParams(message, params, Some(marker))

  def traceMarkerMessageThrowable(c: LoggerContext)(marker: c.Expr[Marker], message: c.Expr[String], t: c.Expr[Throwable]) =
    new Builder[c.type](c, "trace").logMarkerMessageThrowable(marker, message, t)
}
