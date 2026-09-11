# ImagesToChar

## Compile

Run the following commands from the project root:

rm -rf target
javac -d target src/java/fr/_42/printer/logic/Logic.java src/java/fr/_42/printer/app/Program.java

## Copy Resources

Copy the resource files into the target directory:

cp -r src/resources target/

## Build JAR

Create the executable JAR using the manifest file:

jar cfm target/images-to-chars-printer.jar src/manifest.txt -C target fr -C target resources

## Run

Run the application with:

java -jar target/images-to-chars-printer.jar <white_char> <black_char>

## Example

java -jar target/images-to-chars-printer.jar . 0

## Arguments

<white_char>
Character used to display white pixels.

<black_char>
Character used to display black pixels.

The image is loaded from the resources directory contained inside the JAR archive.
