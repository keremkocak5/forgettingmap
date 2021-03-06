## A simple thread-safe forgetting map


### Solution Patterns

There were two concerns for this problem: 

(a) Performance: For efficiency, a HashMap and a LinkedList are combined in the [ForgettingMap](https://github.com/keremkocak5/forgettingmap/blob/master/src/main/java/com/kerem/ForgettingMap.java) class. A HashMap is efficient  for retrieving a key and can store 'key' and 'content' pairs. However, since a HashMap is not ordered, it is not possible to use it to track the sequence of the elements. That's why a LinkedList is used: it tracks the sequence of the elements.  
                 
(b) Thread Safety: Synchronized methods are used to ensure thread-safety, tough this may cause performance issues. Thread safety is tested at [ForgettingMapThreadTest](https://github.com/keremkocak5/forgettingmap/blob/master/src/test/java/com/kerem/ForgettingMapThreadTest.java) class: If the "synchronized" keywords are removed from [ForgettingMap](https://github.com/keremkocak5/forgettingmap/blob/master/src/main/java/com/kerem/ForgettingMap.java), a race condition would occur, and  [ForgettingMapThreadTest](https://github.com/keremkocak5/forgettingmap/blob/master/src/test/java/com/kerem/ForgettingMapThreadTest.java) would fail.    

### Unit testing

The command below can be used to run the unit tests.
```
mvn clean test
```

### Dependencies 
- Java 1.8.0_131 
- Junit 5.5.2
- maven 3.6.3
- maven-surefire-plugin 2.22.2