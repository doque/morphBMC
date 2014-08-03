name := "morphBMC"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     
resolvers := Seq("typesafe" at "http://repo.typesafe.com/typesafe/releases/")

play.Project.playJavaSettings
