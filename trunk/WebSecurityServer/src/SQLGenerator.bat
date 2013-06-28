set WS=%~dp0

 
java -jar ..\WebContent\WEB-INF\lib\mybatis-generator-core-1.3.2.jar -configfile ..\src\MysqlGeneratorConfig.xml -overwrite

pause