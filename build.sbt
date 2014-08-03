name := "morphBMC"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     
resolvers += Resolver.typesafeIvyRepo("releases")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.2")


play.Project.playJavaSettings
