@echo off
chcp 65001 >nul
cd /d "%~dp0"
echo === Gestionnaire de reservation de salles ===
java -version
if errorlevel 1 (echo Java JDK 21 doit etre installe. & pause & exit /b 1)
where mvn >nul 2>&1
if errorlevel 1 (echo Maven n'est pas dans le PATH. Ouvrir le projet avec IntelliJ ou installer Maven. & pause & exit /b 1)
echo Accueil : http://localhost:8080
echo Swagger : http://localhost:8080/swagger-ui.html
mvn spring-boot:run
pause
