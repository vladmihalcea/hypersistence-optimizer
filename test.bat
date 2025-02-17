@echo off

call mvn -pl hypersistence-optimizer-test-case,hypersistence-optimizer-config-example,hypersistence-optimizer-spring-hibernate-example,hypersistence-optimizer-spring-hibernate4-example,hypersistence-optimizer-spring-hibernate3-example,hypersistence-optimizer-spring-jpa-example,hypersistence-optimizer-spring-jpa-hibernate4-example,hypersistence-optimizer-spring-jpa-hibernate3-example,hypersistence-optimizer-micronaut-example clean install %* || exit /b  
set JAVA_HOME=%JAVA_HOME_17%
call mvn -pl hypersistence-optimizer-spring-boot3-example,hypersistence-optimizer-spring-boot-example,hypersistence-optimizer-spring-boot-jta-example,hypersistence-optimizer-glassfish-hibernate-example,hypersistence-optimizer-glassfish-hibernate4-example,hypersistence-optimizer-quarkus-spring-example,hypersistence-optimizer-quarkus-example,hypersistence-optimizer-micronaut2-example clean install %* || exit /b

goto:eof

