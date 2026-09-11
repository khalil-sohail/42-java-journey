# ImagesToChar

## Requirements

Java JDK must be installed.

The following libraries must be placed in the `lib/` directory:

* jcommander-1.82.jar
* JCDP-4.0.1.jar

## Compile

Run the following commands from the project root:

```
rm -rf target && mkdir target
javac -cp "lib/*" -d target $(find src/java -name "*.java")
```

## Copy Resources

Copy the image resources into the target directory:

```
cp -r src/resources target/
```

## Include External Libraries

Extract JCommander and JCDP classes into the target directory:

```
cd target
jar xf ../lib/jcommander-1.82.jar
jar xf ../lib/JCDP-4.0.1.jar
cd ..
```

The target directory will now contain the application classes,
resources, and classes from the external libraries.

## Build JAR

Create the executable JAR:

jar cfm target/images-to-chars-printer.jar src/manifest.txt  -C target fr  -C target com  -C target resources

## Run

Run the application with:

```
java -jar target/images-to-chars-printer.jar --white=<COLOR> --black=<COLOR>
```

## Example

```
java -jar target/images-to-chars-printer.jar --white=RED --black=GREEN
```

## Arguments

--white=<COLOR>
Background color used for white pixels in the source image.

--black=<COLOR>
Background color used for black pixels in the source image.

## Example Colors

BLACK
RED
GREEN
YELLOW
BLUE
MAGENTA
CYAN
WHITE
