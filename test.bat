@echo off

set JAVA_HOME=%JAVA_HOME_8%
@echo on
echo Using Java version - [%JAVA_HOME%]
@echo off

mvn clean install

goto:eof