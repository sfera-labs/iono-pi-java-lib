# Iono Pi - Java library and utility program
Java library and utility program for Iono Pi (www.sferalabs.cc/iono-pi), a professional input/output expansion for Raspberry Pi.

This library is a JNI wrapper of the [Iono Pi native library](https://github.com/sfera-labs/iono-pi-c-lib) which must be installed before. 

The [Main.java](./src/main/java/cc/sferalabs/libs/iono_pi/Main.java) class is an example of how to use the library; it provides a command-line utility to access Iono Pi's functionalities. 

## Usage

### Development

Include Iono Pi in your Java project using [Maven](http://maven.apache.org/):

```
<project>
    ...
    <repositories>
        ...
        <repository>
            <id>sfera-repo</id>
            <url>http://sfera.sferalabs.cc/mvn-repo</url>
        </repository>
        ...
    </repositories>

    <dependencies>
        ...
        <dependency>
            <groupId>cc.sferalabs.libs</groupId>
            <artifactId>iono-pi</artifactId>
            <version>1.0.0</version>
        </dependency>
        ...
    </dependencies>
    ...
</project>
```
    
### Utility program

Install the Iono Pi native library and setup your Raspberry Pi as described [here](https://github.com/sfera-labs/iono-pi-c-lib).

Install Java 8:

    $ sudo apt-get install oracle-java8-jdk
    
Clone this repo using git:

    $ git clone https://github.com/sfera-labs/iono-pi-java-lib.git
    
Build the jar using [Maven](http://maven.apache.org/):

    $ mvn clean install
    
The resulting jar will be in the `target` directory: `iono-pi-<version>.jar`
    
Run the utility program (replace <version> with the actual version number):

    $ sudo java -jar target/iono-pi-<version>.jar

    
## IonoPi library documentation

TODO
