package org.bytedeco.sbt.javacpp

import java.util.Locale

/**
 * Created by Lloyd on 2/22/16.
 */

object Platform {

  private val platformOverridePropertyKey: String = "sbt.javacpp.platform"

  /**
   * To override, set the "sbt.javacpp.platform" System Property. Multiple platforms can be passed as a space-separated string
   *
   * @example
   * {{{
   * sbt compile -Dsbt.javacpp.platform="android-arm android-x86"
   * }}}
   */
  val current: Seq[String] = sys.props.get(platformOverridePropertyKey) match {
    case Some(platform) if platform.trim().nonEmpty => platform.split(' ')
    case _ => Seq(
      s"${org.apache.commons.lang3.SystemUtils.OS_NAME.toLowerCase(Locale.US)}-" +
        s"${
          org.apache.commons.lang3.SystemUtils.OS_ARCH.toLowerCase(Locale.US) match {
            case "amd64" => "x86_64"
            case fine => fine
          }
        }")
  }

}