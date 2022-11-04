#!/bin/bash
source /etc/profile.d/maven.sh
cd ..
mvn clean
mvn test
mvn -Dsonar.login=squ_a3558e8ed5b4ef578a375e1fe320fb4d1323852d -Dsonar.exclusions=src/main/java/com/gocaspi/taskfly/user/UserService.java,/src/main/java/com/gocaspi/taskfly/User/User.java,src/main/java/com/gocaspi/taskfly/TaskflyApplication.java,src/main/java/com/gocaspi/taskfly/User/UserController.java,src/main/java/com/gocaspi/taskfly/advice/ApiExceptionHandler.java -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco-ut/jacoco.xml sonar:sonar -Pcoverage