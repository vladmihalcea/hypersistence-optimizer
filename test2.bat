@echo off

set JAVA_HOME=%JAVA_HOME_17%
call mvn -pl hypersistence-optimizer-spring-boot3-example,hypersistence-optimizer-spring-boot-example,hypersistence-optimizer-spring-boot-jta-example,hypersistence-optimizer-glassfish-hibernate-example,hypersistence-optimizer-glassfish-hibernate4-example clean install %* || exit /b

goto:eof