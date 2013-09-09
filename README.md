Spheres (sandbox)
===============

Spheres is a realtime web application framework that runs on the JVM.

## Main interface
### For the Application developer
* [Spheres](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/Spheres.java)
* [App](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/App.java)
* [Session](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/portal/Session.java) by the Portal

### For the Bridge provider
* [Bridge](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/Bridge.java)
* [Transport](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/Transport.java)
 * [HttpExchange](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/HttpExchange.java)
 * [WebSocket](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/WebSocket.java)

### For the Protocol provider
* [Protocol](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/Protocol.java)
* [Socket](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/Socket.java)
 * [HttpSocket](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/HttpSocket.java)
 * [WsSocket](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/WsSocket.java)
* [SessionBase](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/SessionBase.java)

### For the Messenger provider
* [Messenger](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/Messenger.java)
* [MessageHolder](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/MessageHolder.java)

For details, generate and see the javadoc.
```
git clone https://github.com/flowersinthesand/spheres-sandbox.git
cd spheres-sandbox
mvn javadoc:javadoc
```
Then, open a browser and connect to `target/apidocs/index.html`.

## Application

A sample application to demonstrate what the Spheres is based on Vert.x, Portal and Hazelcast.

### How to run
You need only Java 7, Git and Maven. Execute the following commands:
```
git clone https://github.com/flowersinthesand/spheres-sandbox.git
cd spheres-sandbox
mvn package vertx:runMod
```
Then, open a browser and connect to `http://localhost:8080`. You can change port number by appending `-Dport=${number}` to `mvn` command. If you run multiple applications, you can see that those applications are clustered.

### Bootstrap class
See, [ChatVerticle.java](https://github.com/flowersinthesand/spheres-sandbox/blob/master/src/main/java/com/github/flowersinthesand/spheres/samples/chat/ChatVerticle.java)
