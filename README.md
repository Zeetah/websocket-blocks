websocket-blocks
================

Small Spring 4 Websocket example app with JavaScript frontend application.

## Dependencies
- Java 7
- Grunt http://gruntjs.com/
- Node http://nodejs.org/
- Ruby https://www.ruby-lang.org/en/downloads/
- Compass http://compass-style.org/

## Getting started

Start by cloning the whole project:
```
git clone https://github.com/Zeetah/websocket-blocks.git
```

Navigate to frontend folder and run commands:
```
npm install
bower install
```

In backend folder run:
```
// stars backend server
mvn clean jetty:run
```

At the same time in frontend folder run:
```
// starts frontend server and watch tasks
grunt server
```

