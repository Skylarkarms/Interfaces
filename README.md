# Interfaces

Additional functional interfaces for the Java Environment.
Includes pre-initialized lambda instances to help re-usage
and default state inferences.
Also includes 'ToString'-type static utilities and a StackTrace-formatter as part of the ToStringFunction.

This artifact is divided into two sub-packages:

- .interfaces: Which contain the main functional interfaces.
    - [BinaryFunction](https://html-preview.github.io/?url=https://github.com/Skylarkarms/Interfaces/blob/main/javadoc/interfaces/BinaryFunction.html)
    - [BinaryPredicate](https://html-preview.github.io/?url=https://github.com/Skylarkarms/Interfaces/blob/main/javadoc/interfaces/BinaryPredicate.html)
    - [BooleanOperator](https://html-preview.github.io/?url=https://github.com/Skylarkarms/Interfaces/blob/main/javadoc/interfaces/BooleanOperator.html)
    - [OnSwapped](https://html-preview.github.io/?url=https://github.com/Skylarkarms/Interfaces/blob/main/javadoc/interfaces/OnSwapped.html)
    - [Producer](https://html-preview.github.io/?url=https://github.com/Skylarkarms/Interfaces/blob/main/javadoc/interfaces/Producer.html)
    - [StringSupplier](https://html-preview.github.io/?url=https://github.com/Skylarkarms/Interfaces/blob/main/javadoc/interfaces/StringSupplier.html)
    - [StringUnaryOperator](https://html-preview.github.io/?url=https://github.com/Skylarkarms/Interfaces/blob/main/javadoc/interfaces/StringUnaryOperator.html)
    - [ToStringFunction](https://html-preview.github.io/?url=https://github.com/Skylarkarms/Interfaces/blob/main/javadoc/interfaces/ToStringFunction.html)
- .utils: Which contain lambda instances that do not necessarily belong to the interfaces provided by this artifact.
    - [Consumers](https://html-preview.github.io/?url=https://github.com/Skylarkarms/Interfaces/blob/main/javadoc/utils/Consumers.html)
    - [Functions](https://html-preview.github.io/?url=https://github.com/Skylarkarms/Interfaces/blob/main/javadoc/utils/Functions.html)

### Implementation
int your `build.gradle` file
```groovy
repositories {
   mavenCentral()
}

dependencies {
   implementation 'io.github.skylarkarms:interfaces:1.0.2'
}
```

or in your `POM.xml`
```xml
<dependencies>
   <dependency>
      <groupId>io.github.skylarkarms</groupId>
      <artifactId>interfaces</artifactId>
      <version>1.0.2</version>
   </dependency>
</dependencies>
```
