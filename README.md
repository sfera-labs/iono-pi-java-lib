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
            <version>1.2.0</version>
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

    $ cd iono-pi-java-lib
    $ mvn clean install
    
The resulting jar will be in the `target` directory: `iono-pi-<version>.jar`
    
Run the utility program (replace `<version>` with the actual version number):

    $ sudo java -jar target/iono-pi-<version>.jar

    
## IonoPi library documentation

The entry point to the API is the `IonoPi` abstract class.

Before anything else, you need to call once:

```Java
IonoPi.init();
```

to initialize the library and configure the Raspberry Pi's GPIO pins.

When Iono Pi's functionalities are no longer needed:

```Java
IonoPi.shutdown();
```

And here are the available functionalities:

### LED

Set the LED on/off:

```Java
boolean on = true; // or false
IonoPi.LED.set(on);
```

Check if it's on:

```Java
boolean on = IonoPi.LED.isOn();
```

### Outputs

The outputs are accessible via the `IonoPi.Output` enumeration.

The relay outputs:

```Java
IonoPi.Output.O1
IonoPi.Output.O2
IonoPi.Output.O3
IonoPi.Output.O4
```

and the open collectors:

```Java
IonoPi.Output.OC1
IonoPi.Output.OC2
IonoPi.Output.OC3
```

Set closed/open:

```Java
boolean closed = true; // or false
IonoPi.Output.O1.set(closed);
```

Check:

```Java
boolean closed = IonoPi.Output.OC3.isClosed();
```

### Digital Inputs

```Java
IonoPi.DigitalInput.DI1
IonoPi.DigitalInput.DI2
IonoPi.DigitalInput.DI3
IonoPi.DigitalInput.DI4
IonoPi.DigitalInput.DI5
IonoPi.DigitalInput.DI6
```

Read state:

```Java
boolean high = IonoPi.DigitalInput.DI4.isHigh();
```

Set interrupt listener:

```Java
IonoPi.DigitalInput.DI6.setListener(new DigitalInputListener() {

    @Override
    public void onChange(DigitalInput input, boolean high) {
        System.out.println(high ? "high" : "low");
    }
});
```

Remove listener:

```Java
IonoPi.DigitalInput.DI6.setListener(null);
```

### Digital I/Os (TTL lines)

```Java
IonoPi.DigitalIO.TTL1
IonoPi.DigitalIO.TTL2
IonoPi.DigitalIO.TTL3
IonoPi.DigitalIO.TTL4
```

Set high/low:

```Java
boolean high = true; // or false
IonoPi.DigitalIO.TTL2.set(high);
```

Check:

```Java
boolean high = IonoPi.DigitalIO.TTL2.isHigh();
```

### Analog Inputs

```Java
IonoPi.AnalogInput.AI1
IonoPi.AnalogInput.AI2
IonoPi.AnalogInput.AI3
IonoPi.AnalogInput.AI4
```

Read value:

```Java
float v = IonoPi.AnalogInput.AI1.read();
```

Read value converted to voltage (V):

```Java
float v = IonoPi.AnalogInput.AI1.readVoltage();
```

### 1-Wire

#### 1-Wire Bus

Retrieve all devices on the bus:

```Java
List<OneWireBusDevice> devices = IonoPi.OneWire.getBusDevices();
for (OneWireBusDevice d : devices) {
    System.out.println("ID: " + d.getId());
}
```

Read temperature measured by device (with max 3 attempts):

```Java
OneWireBusDevice d;
int milliC = d.readTemperature(3);
System.out.println("T: " + milliC / 1000.0 + " °C");
```

#### 1-Wire MaxDetect

Read temperature and relative humidity from the probe connected to TTL2 (with max 3 attempts):

```Java
int[] t_rh = IonoPi.OneWire.maxDetectRead(IonoPi.DigitalIO.TTL2, 3);
if (t_rh != null) {
    System.out.println("T: " + t_rh[0] / 10.0 + "°C");
    System.out.println("RH: " + t_rh[1] / 10.0 + "%");
}
```

### Wiegand

```Java
IonoPi.Wiegand.W1 // TTL1 = Wiegand Data 0, TTL2 = Wiegand Data 1
IonoPi.Wiegand.W2 // TTL3 = Wiegand Data 0, TTL4 = Wiegand Data 1
```

Monitor interface 1 in a new thread and stop after 30 seconds or if 42 is received:

```Java
Thread th = new Thread() {
    public void run() {
        try {
            IonoPi.Wiegand.W1.monitor(new WiegandListener() {

                @Override
                public boolean onData(Wiegand wInterface, int count, long data) {
                    System.out.println("Bits: " + count + " - Data: " + data);
                    if (data == 42) {
                        return false;
                    }
                    return true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
};

th.start();

try {
    Thread.sleep(30000);
} catch (InterruptedException e) {
}

IonoPi.Wiegand.W1.stop();
th.join();
```
