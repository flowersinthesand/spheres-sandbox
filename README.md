Spheres (sandbox)
===============

Spheres is a realtime web application framework that runs on the JVM.

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
