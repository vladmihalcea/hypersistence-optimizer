@echo off

set JAVA_HOME=%JAVA_HOME_17%
call mvn -pl hypersistence-optimizer-quarkus-spring-example,hypersistence-optimizer-quarkus-example,hypersistence-optimizer-micronaut2-example clean install %* || exit /b

goto:eof