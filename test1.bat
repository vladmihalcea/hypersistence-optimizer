@echo off

call mvn -pl hypersistence-optimizer-test-case,hypersistence-optimizer-config-example,hypersistence-optimizer-spring-hibernate-example,hypersistence-optimizer-spring-hibernate4-example,hypersistence-optimizer-spring-hibernate3-example,hypersistence-optimizer-spring-jpa-example,hypersistence-optimizer-spring-jpa-hibernate4-example,hypersistence-optimizer-spring-jpa-hibernate3-example,hypersistence-optimizer-micronaut-example clean install %* || exit /b

goto:eof