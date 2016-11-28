# Spring Web API Test Stubber

A time-saving utility for auto-generating SpringMVC-based test stub code.

## 36sec Teaser Movie

- RestController

    https://youtu.be/J1rHVkxiTp8

## Getting Started

### 1. Build jar

>./gradlew jar

![Imgur](http://i.imgur.com/9kLBvPs.png)

### 2. Place jar in your project library directory

![Imgur](http://i.imgur.com/z8PQNCW.png)

### 3. Create Gradle Task

```groovy
// YOUR build.gradle

//...

repositories {
    mavenCentral()

    // directory where the spring-web-api-test-stubber.jar is stored.
    flatDir(name: "project-libs", dirs: "libs")
}

//...

dependencies {

    //...

    // For ApiStubGenerator - start
    compile files('libs/spring-web-api-test-stubber-1.0-SNAPSHOT.jar')
    compile group: 'com.squareup', name: 'javapoet', version: '1.8.0'
    compile group: 'junit', name: 'junit', version: '4.11'
    compile group: 'org.springframework.boot', name: 'spring-boot-starter-test', version: '1.4.2.RELEASE'
    // For ApiStubGenerator - end

    //...
}

//...

// For ApiStubGenerator - start
sourceSets {
    generated {
        java {
            srcDirs = ['src/test/java']
        }
    }
}

task generateApiTestStub(type: JavaCompile, group: 'build') {
    source = sourceSets.main.java.srcDirs
    classpath = configurations.compile
    options.compilerArgs = [
            "-proc:only",
            "-processor", "homo.efficio.spring.web.api.test.stubber.processor.RestControllerProcessor"
    ]
    destinationDir = sourceSets.generated.java.srcDirs.iterator().next()
}
// For ApiStubGenerator - end

//...
```

### 4. Generate the stub file(s)

>./gradlew generateApiTestStub

or just double click on the task

![Imgur](http://i.imgur.com/gWI8LoY.png?1)

The generated file(s) will be placed in `src/test/java/generated/YOUR/PACKAGE`

![Imgur](http://i.imgur.com/kUhUoY6.png?1)

### 5. Enjoy testing

Everything is ready!!

Just change the package, method names and write your own test codes!!

## Customization for your own project

This is written in Java 8 and the generated files are for Spring Boot 1.4.* by default.

But you can customize it easily because the source files are not difficult at all. ;).

## Inspired by

- Hannes Dorfmann Blog: http://hannesdorfmann.com/annotation-processing/annotationprocessing101
- JavaPoet: https://github.com/square/javapoet
- QueryDSL: http://www.querydsl.com/

## License

Apache 2.0

