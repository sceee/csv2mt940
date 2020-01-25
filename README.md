# csv2mt940
Adapted for CSV format of Fidor - some information like account number or final balance must be entered by the user

This program can convert a CSV file containing transaction data exported from Fidor into the well-known MT940 (SWIFT) data format.

ATTENTION: Use at your own risk. 

I converted it to gradle and removed the GUI so that it can be used from command line. Values can be configured in a file config.ini file located next to the .jar.
An example config.ini is located in `sample-config.ini`

# Build
```java
.\gradlew shadowJar
```

# Execute
Please provide the closing balance as the first argument
```java
java -jar .\build\libs\shadow-0.0.1-all.jar "10.10"
```

## License
MIT
