#!/bin/bash
source /etc/profile.d/maven.sh
cd ..
mvn clean
mvn test
mvn -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.exclusions=src/main/java/com/gocaspi/taskfly/advice/ApiExceptionHandler.java -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco-ut/jacoco.xml sonar:sonar -Pcoverage