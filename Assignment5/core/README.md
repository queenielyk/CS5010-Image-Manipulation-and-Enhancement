# CS5010 Assignment5: Image Manipulation and Enhancement V2.0

Member: Queenie, Cheng    
Date: March 25 2023   
Professor: Amit Shesh

## Project idea

Developing a text-based Image Process program with MVC design idea.

## Project Structure

![UML Diagram](UML.jpeg)

## Overview

We developed this project based on a Model + View + Controller (MVC) concept. However, as this is a
text-based program, so View is not applicable in this case.

In general, Controller takes input from users then ask Model to execute desired action(s); Model is
where computation takes place.

In our infrastructure, Controller takes input from user. Invokes desired action(s) provided by Model
if those operations are valid, return 'unknown' to user otherwise.

## Controller

``` bash
ime/controller
    ├── IController.java
    ├── ImageController.java
    ├── ImageCommand.java
    └── command
        ├── Brighten.java
        ├── Greyscale.java
        ├── Hflip.java
        ├── Load.java
        ├── RgbCombine.java
        ├── RgbSplit.java
        ├── Save.java
        └── Vflip.java
        └── Filter.java
        └── Dithering.java
```

### Design

Interface `IController` will only have two methods:

- `processCommand(String command)`  takes single line of string from Input and delegate to creat
  command object to execute.
- `run(MoreImageProcessor model)` pass the model to control and start running.

Implementation `MoreImageController` will extend on `ImageController` will have extra helper method:

- `processFileScript(Scanner fileScan)` to call processCommand line by line.
- `main()` to run the program.

The interface `MoreImageCommand` represent a command object on work with new
model `MoreImageProcessor`, which is sequence of operation on model.

We have several atomic commands like(`Save`,`Load`,`Greyscale` ..etc) and now we can use these
command to implements other command base on sequences of these atomic command.   
(For example, we can do RGB split by just use Greyscale three time.） This makes it easy for future
extensions.

``` bash
We add few more class to extend an reused the previous /ime package inorder to support 
more features.

mime/controller
    ├── MoreImageController.java
    ├── MoreImageCommand.java
    └── command
        ├── ColorTrans.java
        ├── Dither.java
        ├── Filter.java
        ├── LoadInputStream.java
        ├── SaveOutStream.java
``` 

After all these extension, we now support load image from Input-stream and Save to OutputStream
instead of only file path.

### Control flow

The major control flow will be done inside `processCommand()` implementation, which takes a String
command.

First, parse it into two part: commandType and List of Arguments by calling *scanner.next()* in a
loop.

Secondly, we make a switch to determine what kind of command object to creat and what kind of error
checking to apply.

Finally, If the command string fall into one case and get created successfully. We just call
the `cmd.execute()` to delegate the operations to the model.

### Fields

- `private MoreImageProcessor model`
- `private final Readable in`
- `private final Appendable out`

Beside the `MoreImageProcessor` model to operate on. We also store the Input as *Readable* and
Output
as *Appendable*.
This makes the controller capable to take not just *System.in/ out* but also files or simple string.

## Model

``` bash
ime/model
    ├── MoreImageProcessor.java
    └── MoreImageProcessorImpl.java
```

### Data Structure

We store an image as a 3D-Array: `int[Height][Width][RGB]`

Each image has it's width and height.
We initialized the 3D-Array by specifying the width and height to mimic a real image.
Image is composed by a group of components.
Each component is a combination of Red, Green, and Blue (RGB),
therefore we putted them together as an `int[]`.

```bash
Example
An image Dimension: 2x4
[
    [[R, G, B],[R, G, B],[R, G, B],[R, G, B]],
    [[R, G, B],[R, G, B],[R, G, B],[R, G, B]]
]
```

### Computation

`MoreImageProcessor` is an interface to indicate what kind of actions a processor should have:

- Load
- Dithering
- Save
- Combine
- Greyscale
    - red-component
    - green-component
    - blue-component
    - value-component
    - intensity-component
    - luma-component
    - sepia
- Flip
    - Horizontal
    - Vertical

- Filter
    - Blur
    - Sharpen

### Format limitation

Extending this project from assignment 4, we are now able to support more formats other than ASCII
PPM.
Overall, the supporting formats are:

- ASCII PPM
- BMP
- JPG
- JPEG
- PNG

For those formats are not listed above, we are not able to provide any operations,
even the most basic operations load and save.

If an attempting image is not supported, the processor will throw exception.

## Instruction

1. Run `MoreImageController.java` to start the program.
    - Enter command with parameters separated by spaces.**(ex. ">run res/script.text")**
    - Enter `exit`/`q`/`quit` to exit the program
   ```bash
   List of command IME supported:
   - load filepath ImgName
   - save filepath destImgName
   - brighten int sourceImg destImg
   - vertical-flip sourceImg destImg
   - horizontal-flip sourceImg destImg
   - greyscale component sourceImg destImg
   - rgb-split srcImg R-Img G-Img B-Img
   - rgb-combine destImg R-Img G-Img B-Img 

   New MIME added features:
   - greyscale sourceImg destImg (uses default luma-component)
   - dither sourceImg destImg
   - sepia sourceImg destImg
   - blur sourceImg destImg
   - sharpen sourceImg destImg
   ```

2. Run packed `core.jar` file on command promote with command lind option.
    - -file res/script.text
      **(ex. ">java -jar core.jar res/script.text")**
    - Program will run the script and shows

> This scripts will run on images files under res/folder and output file will produce to the same
> folder.

## Citation

Copyright of following images used is owned by Cheng Shi and authorized to use for this assignment.
> - res/cat.ppm
> - res/building.ppm

## Change Log

### From Assignment 4 to Assignment 5

#### 1. Image representation: Linked List -> 3D-Array

We used Linked List to represent an image in assignment 4;
however this is not convenient for us to apply filter matrix.
Therefore, we used 3D-Array instead of Linked List in assignment 5.
Even there is a change in our data structure, assignment 4 is still supporting on it's own.

### 2. Support more image operations

This latest version supports more image operations, such as:

- Blur
- Sharpen
- Color transformation
    - Greyscale
    - Sepia
- Dithering

### 3. Support more image format

While assignment 4 only support ASCII PPM image,
Assignment 5 supports:

- ASCII PPM
- BMP
- JPG
- JPEG
- PNG

Besides, regardless the format of the image being loaded, it's always an option to save the image to
another format.
> e.g. load xxx.ppm -> save xxx.bmp

To apply these new operations, please refer to [Instruction](#Instruction).