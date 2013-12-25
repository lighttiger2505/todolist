import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "todolist"
  val appVersion      = "1.0"

  val appDependencies = Seq(
    "postgresql" % "postgresql" % "8.4-702.jdbc4"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings()
}
