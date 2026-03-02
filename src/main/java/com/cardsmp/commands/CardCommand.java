@echo off
echo ========================================
echo    CardSMP Plugin Builder
echo ========================================

REM Create folder structure
mkdir src\main\java\com\cardsmp\cards
mkdir src\main\java\com\cardsmp\config
mkdir src\main\java\com\cardsmp\data
mkdir src\main\java\com\cardsmp\abilities
mkdir src\main\java\com\cardsmp\listeners
mkdir src\main\java\com\cardsmp\commands
mkdir src\main\resources

echo Folder structure created!

REM Download pom.xml
curl -L -o pom.xml https://pastebin.com/raw/YOUR_POM_XML_LINK 2>nul
if not exist pom.xml (
    echo Creating pom.xml...
    (
        echo ^<?xml version="1.0" encoding="UTF-8"?^>
        echo ^<project xmlns="http://maven.apache.org/POM/4.0.0"^>
        echo     ^<modelVersion^>4.0.0^</modelVersion^>
        echo     ^<groupId^>com.cardsmp^</groupId^>
        echo     ^<artifactId^>CardSMP^</artifactId^>
        echo     ^<version^>1.0.0^</version^>
        echo     ^<packaging^>jar^</packaging^>
        echo     ^<properties^>
        echo         ^<maven.compiler.source^>21^</maven.compiler.source^>
        echo         ^<maven.compiler.target^>21^</maven.compiler.target^>
        echo     ^</properties^>
        echo     ^<repositories^>
        echo         ^<repository^>
        echo             ^<id^>papermc^</id^>
        echo             ^<url^>https://repo.papermc.io/repository/maven-public/^</url^>
        echo         ^</repository^>
        echo     ^</repositories^>
        echo     ^<dependencies^>
        echo         ^<dependency^>
        echo             ^<groupId^>io.papermc.paper^</groupId^>
        echo             ^<artifactId^>paper-api^</artifactId^>
        echo             ^<version^>1.21.4^</version^>
        echo             ^<scope^>provided^</scope^>
        echo         ^</dependency^>
        echo     ^</dependencies^>
        echo     ^<build^>
        echo         ^<finalName^>CardSMP-^${project.version}^</finalName^>
        echo         ^<plugins^>
        echo             ^<plugin^>
        echo                 ^<groupId^>org.apache.maven.plugins^</groupId^>
        echo                 ^<artifactId^>maven-compiler-plugin^</artifactId^>
        echo                 ^<version^>3.11.0^</version^>
        echo                 ^<configuration^>
        echo                     ^<source^>21^</source^>
        echo                     ^<target^>21^</target^>
        echo                 ^</configuration^>
        echo             ^</plugin^>
        echo         ^</plugins^>
        echo     ^</build^>
        echo ^</project^>
    ) > pom.xml
)

echo.
echo ========================================
echo    Setup Complete!
echo ========================================
echo Ab Maven install karke build karein:
echo    mvn clean package
echo.
pause