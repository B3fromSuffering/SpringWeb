call runcrud
if "%ERRORLEVEL%" == "0" goto openurl
echo.
echo GRADLEW BUILD has errors - breaking work
goto fail

:openurl
start firefox http://localhost:8080/crud/v1/task/getTasks
goto end

:fail
echo.
echo There were errors

:end
echo.
echo Work is finished.