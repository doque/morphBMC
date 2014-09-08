name := "morphBMC"

version := "1.0-SNAPSHOT"

resolvers += Resolver.sonatypeRepo("releases")

//resolvers += "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "ws.securesocial" %% "securesocial" % "2.1.4",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
)     

play.Project.playJavaSettings
