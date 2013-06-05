set WS=%~dp0

 
java -jar D:\workspace\WebServer\WebContent\WEB-INF\lib\mybatis-generator-core-1.3.2.jar -configfile D:\workspace\WebServer\src\MysqlGeneratorConfig.xml -overwrite

pause