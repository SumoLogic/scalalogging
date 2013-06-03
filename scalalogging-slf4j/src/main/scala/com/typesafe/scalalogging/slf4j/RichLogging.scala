package com.typesafe.scalalogging.slf4j

import org.slf4j.LoggerFactory

trait AbstractRichLogging {
  protected def logger: RichLogger
}

/**
 * Adds the lazy val `logger` of type [[$RichLogger]] to the class into which this trait is mixed.
 *
 * If you need a non-lazy [[$RichLogger]], which would probably be a special case,
 * use [[com.typesafe.scalalogging.slf4j.StrictRichLogging]].
 *
 * @define Logger com.typesafe.scalalogging.slf4j.RichLogger
 */
trait RichLogging extends AbstractRichLogging {

  protected lazy val logger: RichLogger =
    RichLogger(LoggerFactory getLogger getClass.getName)
}

/**
 * Adds the non-lazy val `logger` of type [[$RichLogger]] to the class into which this trait is mixed.
 *
 * If you need a lazy [[$RichLogger]], which would probably be preferrable,
 * use [[com.typesafe.scalalogging.slf4j.RichLogging]].
 *
 * @define Logger com.typesafe.scalalogging.slf4j.RichLogger
 */
trait StrictRichLogging extends AbstractRichLogging {

  protected val logger: RichLogger =
    RichLogger(LoggerFactory getLogger getClass.getName)
}
