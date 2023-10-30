# hzcli

This is a simple command line tool to access a Hazelcast cluster and execute some commands
It uses Spring Shell underneath.

Run the application with on of the following command:

This starts as Hazelcast client
```
mvn spring-boot:run
```

This starts as Hazelcast lite member
```
mvn spring-boot:run "-Dspring-boot.run.profiles=lite" 
```

## Map Commands
* **m.entries** : This command shows the entries of a map
* **m.keytypes** : This command shows the types of keys in a map.  The original idea is from [Introducing CLC: The New Hazelcast Command-Line Experience](https://hazelcast.com/blog/introducing-clc-the-new-hazelcast-command-line-experience/)
* **m.size** : This command shows the size of a map

## Notes
The original idea is from [Digging into Hibernate's Query Cache](https://blog.frankel.ch/digging-hibernate-query-cache/)

Source code
https://github.com/ajavageek/query-cache