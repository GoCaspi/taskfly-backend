#!/bin/bash
source /etc/profile.d/maven.sh
mvn clean
mvn test
mvn -Dsonar.login="$1" -Dsonar.exclusions=src/main/java/com/gocaspi/taskfly/user/UserService.java,/src/main/java/com/gocaspi/taskfly/task/TaskService.java,src/main/java/com/gocaspi/taskfly/TaskflyApplication.java,src/main/java/com/gocaspi/taskfly/advice/ApiExceptionHandler.java -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco-ut/jacoco.xml sonar:sonar -Pcoverage