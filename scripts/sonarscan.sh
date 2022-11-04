#!/bin/bash
source /etc/profile.d/maven.sh
cd ..
mvn clean
mvn test
mvn -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.exclusions=src/main/java/com/gocaspi/taskfly/task/TaskService.java,src/test/java/com/gocaspi/taskfly/task/TaskControllerTest.java,src/main/java/com/gocaspi/taskfly/User/UserController.java,src/main/java/com/gocaspi/taskfly/User/User.java,src/main/java/com/gocaspi/taskfly/user/userService.java,src/main/java/com/gocaspi/taskfly/taskflyApplication.java,src/main/java/com/gocaspi/taskfly/TaskflyApplication.java,src/main/java/com/gocaspi/taskfly/advice/ApiExceptionHandler.java -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco-ut/jacoco.xml sonar:sonar -Pcoverage