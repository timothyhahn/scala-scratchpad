name := "scratchpad"
 
version := "1.0"
 
scalaVersion := "2.10.3"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Typesafe snapshots" at "http://repo.typesafe.com/typesafe/snapshots/" 

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.3"

libraryDependencies += "com.typesafe.akka" %% "akka-transactor" % "2.2.3"

libraryDependencies += "org.scala-stm" %% "scala-stm" % "0.7"

