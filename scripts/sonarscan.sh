#!/bin/bash
source /etc/profile.d/maven.sh
mvn clean
mvn test
mvn -Dsonar.login="$1" -Dsonar.exclusions=src/main/java/com/gocaspi/taskfly/MongoValidationConfig.java,src/main/java/com/gocaspi/taskfly/taskcollection/TaskCollectionRepositoryImpl.java,src/main/java/com/gocaspi/taskfly/SecurityConfiguration.java,src/main/java/com/gocaspi/taskfly/advice/ApiExceptionHandler.java,Dsonar.exclusions=src/main/java/com/gocaspi/taskfly/TaskflyApplication.java -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco-ut/jacoco.xml sonar:sonar -Pcoverage