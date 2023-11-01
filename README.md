# hzcli

This is a simple command line tool to access a Hazelcast cluster and execute some commands
It uses Spring Shell underneath.

If you don't have a running Hazelcast cluster, you can create a test environment  by executing
```
docker-compose up
```

# Profiles
You can select your profile which is **lite** or **default** using **application.properties**.   
* **Lite** profile starts a lite member   
* **Default** profile start a Hazelcast client

# Maven
Run the application with the following command. 
```
mvn spring-boot:run
```

## Map Commands
* **m.entries** : This command shows the entries of a map
* **m.keytypes** : This command shows the types of keys in a map.  The original idea is from [Introducing CLC: The New Hazelcast Command-Line Experience](https://hazelcast.com/blog/introducing-clc-the-new-hazelcast-command-line-experience/)
* **m.size** : This command shows the size of a map

## SQL Commands
* **columns** : This command selects information_schema.columns
* **mappings** : This command selects information_schema.mappings
* **tables** : This command selects information_schema.tables
* 
## Notes
The original idea is from [Digging into Hibernate's Query Cache](https://blog.frankel.ch/digging-hibernate-query-cache/)

Source code
https://github.com/ajavageek/query-cache