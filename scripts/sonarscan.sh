#!/bin/bash
source /etc/profile.d/maven.sh
cd ..
mvn clean
mvn test
mvn -Dsonar.login=squ_85aa3c508a62bd1e51dc8a10498e9f64c05987a7 -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco-ut/jacoco.xml sonar:sonar -Pcoverage