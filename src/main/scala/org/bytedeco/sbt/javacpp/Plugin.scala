package org.bytedeco.sbt.javacpp

import scala.language.postfixOps
import sbt._
import sbt.Keys._

import scala.util.Try

object Plugin extends AutoPlugin {

  override def projectSettings: Seq[Setting[_]] = {
    import autoImport._
    Seq(
      autoCompilerPlugins := true,
      javaCppPlatform := Platform.current,
      javaCppVersion := Versions.javaCppVersion,
      javaCppPresetLibs := Seq.empty,
      libraryDependencies += {
        "org.bytedeco" % "javacpp" % javaCppVersion.value jar
      },
      javaCppPresetDependencies
    )
  }

  object Versions {
    val javaCppVersion = "1.4.3"
  }

  object autoImport {
    val javaCppPlatform = SettingKey[Seq[String]]("javaCppPlatform", """The platform that you want to compile for (defaults to the platform of the current computer). You can also set this via the "sbt.javacpp.platform" System Property """)
    val javaCppVersion = SettingKey[String]("javaCppVersion", s"Version of Java CPP that you want to use, defaults to ${Versions.javaCppVersion}")
    val javaCppPresetLibs = SettingKey[Seq[(String, String)]]("javaCppPresetLibs", "List of additional JavaCPP presets that you would wish to bind lazily, defaults to an empty list")
  }

  override def requires: Plugins = plugins.JvmPlugin

  override def trigger: PluginTrigger = allRequirements

  private def javaCppPresetDependencies: Def.Setting[Seq[ModuleID]] = {
    import autoImport._
    libraryDependencies ++= {
      val cppPresetVersion = majorMinorOnly(javaCppVersion.value)
      javaCppPresetLibs.value.flatMap {
        case (libName, libVersion) =>
          val generic = "org.bytedeco.javacpp-presets" % libName % s"$libVersion-$cppPresetVersion" classifier ""
          val platformSpecific = javaCppPlatform.value.map { platform =>
            "org.bytedeco.javacpp-presets" % libName % s"$libVersion-$cppPresetVersion" classifier platform
          }
          generic +: platformSpecific
      }
    }
  }

  /**
   * Before javacpp 1.4
   * Given a version string, simply drops the patch level and returns the major-minor version only
   *
   * Starting from javacpp 1.4
   * The version number of the presets are equal to the javacpp version.
   *
   * @param version eg. "1.4.2"
   */
  private def majorMinorOnly(version: String): String =
    version match {
      case VersionSplit(a :: b :: _) if a < 2 & b < 4 => s"$a.$b"
      case VersionSplit(_) => version
      case _ => throw new IllegalArgumentException("Version format not recognized")
    }

  private object VersionSplit {
    def unapply(arg: String): Option[List[Int]] =
      Try(arg.split('.').map(_.toInt).toList).toOption
  }

}