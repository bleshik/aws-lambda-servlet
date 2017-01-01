name := "AWS Lambda Jersey Adapter"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.1.0"

libraryDependencies += "org.glassfish.jersey.containers" % "jersey-container-servlet" % "2.23.2"

libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.1.0" exclude("commons-logging", "commons-logging")

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"

libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.21"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.21" % "test"
