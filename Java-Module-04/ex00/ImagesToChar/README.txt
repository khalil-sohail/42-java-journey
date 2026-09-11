# ImagesToChar

## Compile

Run the following command from the project root:

javac -d target src/java/fr/_42/printer/logic/Logic.java src/java/fr/_42/printer/app/Program.java

## Run

Run the program with:

java -cp target fr._42.printer.app.Program <white_char> <black_char> <image_path>

## Example

java -cp target fr._42.printer.app.Program '|' '' '/full/path/to/image.bmp'

## Arguments

<white_char>
Character used to display white pixels.

<black_char>
Character used to display black pixels.

<image_path>
Full path to the BMP image to display.
