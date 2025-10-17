@echo off

call test1 || exit /b
call test2 || exit /b
call test3 || exit /b

goto:eof