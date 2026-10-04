# 42 Java Journey

My solutions for the ten Java modules in the 42 curriculum, from language fundamentals to database access, testing, Spring, and socket programming.

## Modules

| Module | Main topics |
| ------ | ----------- |
| [Java Module 00](Java-Module-00/) | Basic syntax, console input, control flow, arrays, and strings. |
| [Java Module 01](Java-Module-01/) | Object-oriented programming, interfaces, custom lists, and exceptions in a transaction manager. |
| [Java Module 02](Java-Module-02/) | File I/O, collections, file signatures, text similarity, and filesystem operations. |
| [Java Module 03](Java-Module-03/) | Threads, synchronization, parallel array summation, and concurrent downloads. |
| [Java Module 04](Java-Module-04/) | Packages, resources, JAR packaging, and external libraries in an image-to-text printer. |
| [Java Module 05](Java-Module-05/) | PostgreSQL, JDBC repositories, connection pooling, and pagination for chat data. |
| [Java Module 06](Java-Module-06/) | JUnit parameterized tests, database tests, and mocking with Mockito. |
| [Java Module 07](Java-Module-07/) | Reflection, HTML generation through annotation processing, and a small annotation-based ORM. |
| [Java Module 08](Java-Module-08/) | Spring dependency injection, XML and Java configuration, JdbcTemplate, and service testing. |
| [Java Module 09](Java-Module-09/) | Client/server sockets, concurrent chat rooms, authentication, message persistence, and JSON communication. |

## Repository Structure

```text
42-java-journey/
├── Java-Module-00/
├── Java-Module-01/
├── Java-Module-02/
├── Java-Module-03/
├── Java-Module-04/
├── Java-Module-05/
├── Java-Module-06/
├── Java-Module-07/
├── Java-Module-08/
└── Java-Module-09/
```

Each module contains numbered exercise directories (`ex00`, `ex01`, etc.). Later exercises contain separate Maven projects with their own source trees and resources.

## Tools and Build Notes

- Modules 00–03 use standalone Java files compiled with `javac`.
- Module 04 uses `javac` and `jar`, with JCommander and JCDP in its final exercise. Build instructions are in each `ImagesToChar/README.txt`.
- Modules 05–09 use Maven. Most POMs target Java 25; the Module 07 ORM targets Java 17. Check the individual project's `pom.xml` for its configuration.
- Database exercises use PostgreSQL, JDBC, and HikariCP. Tests use JUnit, Mockito, HSQLDB, or H2, depending on the exercise.
- Later modules use Spring Context and JDBC, Spring Security Crypto for password hashing, and Jackson for JSON.

Build and run exercises from their individual project directories. Database-backed exercises need their own connection settings and schema setup; there is no repository-wide build.
