# CS5010 Assignment6: Graphical Image Manipulation and Enhancement

Member: Queenie, Cheng    
Date: April 5 2023   
Professor: Amit Shesh

## Project idea

We developed a text-based Image Processing application with MVC design idea in assignment 4 and assignment 5.
Moving to assignment 6, we implemented a view (GUI) for this Image Processing application.

## Project Structure

![UML Diagram](UML.png)

## Overview

We developed this project based on a Model + View + Controller (MVC) concept. Before assignment 6, this is a text-based
program, where View is not supported.

In general, Controller takes input from users then ask Model to execute desired action(s); Model is where computation
takes place.

In our infrastructure, Controller takes input from user. Invokes desired action(s) provided by Model if those operations
are valid, return 'unknown' to user otherwise.

Start from assignment 6, a graphical user interface (GUI) is available. It allows users to interactively load, process
and save images. Additionally, the GUI shows the histograms for an image.

To show the image and histogram effectively, we eventually adopted Model–view–viewmodel architectural pattern.

With this design pattern, the View is allowed to get data from the Model directly without Controller's help. In
contrast, any actions involved modifying data or computation, the View is prohibited to ask the Model to do so but the
Controller.

## Format limitation

Extending this project from assignment 4, we are now able to support more formats other than ASCII PPM.
Overall, the supporting formats are:

- ASCII PPM
- BMP
- JPG
- JPEG
- PNG

For those formats are not listed above, we are not able to provide any operations,
even the most basic operations load and save.

If an attempting image is not supported, the application will throw an exception.

## Controller

``` bash
Assignment 4 Text
ime/control
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
```
``` bash
Assignment 5 Script
mime/control
    ├── MoreImageCommand.java
    ├── MoreImageController.java
    └── command
        ├── ColorTrans.java
        ├── Dither.java
        ├── Filter.java
        ├── LoadInputStream.java
        └── SaveOutStream.java
```
``` bash
Assignment 6 GUI
gime/control
    ├── Features.java
    ├── IGraphicController
    └── GraphicImageController
```

### Design
#### Text/Script 
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
(For example, we can do RGB split by just use Greyscale three time.

#### GUI
`Features` will provide list of call back function to views.   
`IGraphicController` will extend `Features` with one extra method:
- `runGUI(IView,MoreImageProcessor)` which will take in the view and controller and start running
represent a completely different control logic.

### Control flow
#### Text/Script
The major control flow will be done inside `processCommand()` implementation, which takes a String
command.

First, parse it into two part: commandType and List of Arguments by calling *scanner.next()* in a
loop.

Secondly, we make a switch to determine what kind of command object to creat and what kind of error
checking to apply.

Finally, If the command string fall into one case and get created successfully. We just call
the `cmd.execute()` to delegate the operations to the model.

#### GUI
`GraphicImageController` will just act as a bunch of features to view in order for view to bind to 
action. 
One of the method will be called when view actions was triggered by user. 
Then controller will use parse the input and build command and execute on the model
and use method provide by view to update view accordingly to complete different task.

### Fields

- `private MoreImageProcessor model`
- `private final Readable in`
- `private final Appendable out`

Beside the `MoreImageProcessor` model to operate on. We also store the Input as *Readable* and
Output
as *Appendable*.
This makes the controller capable to take not just *System.in/ out* but also files or simple string.

## Read/Write Image

This program support various image format, however they need a different way to read/write. We isolated this handling to
keep the dependency of Model.

```bash
mime/model
    ├── AbsrtuctImageHandler.java
    ├── ImageHandler.java
    ├── ImageIOHandler.java
    ├── PpmHandler.java
    ├── BufferImageConverter.java
    └── BufferImageConverterImpl.java
```

`BufferImageConverter.java` is an interface indicates a converter method that converts an image from the Model into a
BufferImage.
`BufferImageConverterImpl.java` implemented `BufferImageConverter.java`.
This is an object can be used when saving an image with ImageIO into OutputStream or showing image on our GUI.

`ImageHandler.java` is an interface that indicates what kind of actions a handler should have:

- Read image
- Get image
- Get info
- Save image

`AbstractImageHandler.java` is an abstract class to implement methods that indicated by interface `ImageHandler.java`
and each concrete class behaves the same.

`ImageIOHandler.java` and `PpmHandler.java` extends `AbstractImageHandler.java` and implements methods that the behavior
is depends on itself.
Additionally, `ImageIOHandler.java` has an `BufferImageConverter` object in order to convert the image from the Model
into a BufferImage for ImageIO to save the image into a file.

Once the controller recognized a command is related to read/write image, it will determine which handler it should
invoke based on the format of that image.
> ppm → PpmHandler
>
> bmp / jpg / jpeg / png → ImageIOHandler

## Model

**The model design since Assignment 5 is not compatible to the model design in Assignment 4. For more details,
read [Change Log.](#1-image-representation-linked-list--3d-array)**

``` bash
ime/model
    └── ImageProcessor.java

mime/model
    ├── MoreImageProcessor.java
    └── MoreImageProcessorImpl.java

gime/model
    ├── ReadOnlyImageProcessor.java
    └── ReadOnlyImageProcessorImpl.java
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

**Interface design of assignment 6 is different to assignment 5. For more details,
read [Change Log.](#2-interface-segregation-move-read-only-methods-from-moreimageprocessor-to-readonlyimageprocessor)**

`ImageProcessor` is an interface indicates actions that an image processor should have:

- Load `@Deprecated`
- Save `@Deprecated`
- Combine
- ~~Greyscale~~ Color Trans
    - red-component
    - green-component
    - blue-component
    - value-component
    - intensity-component
    - luma-component
- Flip
    - Horizontal
    - Vertical

`ReadOnlyImageProcessor` is an interface indicates read-only actions to be an object adopter of `MoreImageProcessor`.
The read-only actions are:

- Get
    - Info
    - Image
    - Image Name List

`MoreImageProcessor` is an interface extends both `ImageProcessor` and `ReadOnlyImageProcessor`.It also indicates what
kind of new actions a processor should have:

- Load
- Dithering
- ~~Greyscale~~ Color Trans
    - greyscale
    - sepia
- Filter
    - Blur
    - Sharpen

## View (GUI)

```bash
src/gime/view/
    ├── CustomJButton.java
    ├── CustomJComboBox.java
    ├── Histogram.java
    ├── IView.java
    ├── JFrameView.java
    └── resources
        ├── add-new-50.png
        ├── arrows.png
        ├── brightness.png
        ├── horizontal-flip.png
        ├── intersection.png
        ├── save-50.png
        └── vertical-flip.png
```

### Design

We implemented the View by `javax.swing`.

In our GUI design, we have a topbar for dropdowns and buttons, an image panel to show selected image, and a histogram
panel to show the histogram of the selected image.

#### Topbar

There are three dropdowns and seven buttons at the topbar. The functionalities (from left to right) are:

1. Effects (Dropdown)
    - Color Transform - Red Component
    - Color Transform - Green Component
    - Color Transform - Blue Component
    - Color Transform - Value
    - Color Transform - Intensity
    - Color Transform - Luma
    - Color Transform - Greyscale
    - Color Transform - Sepia
    - Blur
    - Sharpen
    - Dither
2. Loaded image file name (Dropdown)
3. Effect applied in sequence (Dropdown)
4. Adjust Brightness (Icon Button)
5. RGB-Split (Icon Button)
6. RGB-Combine (Icon Button)
7. Horizontal Flip (Icon Button)
8. Vertical Flip (Icon Button)
9. Load Image (Icon Button)
10. Save Image (Icon Button)

**The application will throw an error dialog when pressing any buttons except `Load Image` if no image is loaded into the application.**

#### Image Panel
Automatically refresh the showing image if the user select an image is not the current one.
Besides, if user selected the image name has not performed the same process as the previous, the application show the raw image in default.

#### Histogram Panel
Automatically refresh the histogram of the showing image if the user select an image is not the current one.
For color images, there are four lines ({Component} - {Line color}):
- Red - Red
- Green - Green
- Blue - Blue
- Intensity - Black

For greyscale images, there is one line only.


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

2. Run `src/gime/GIMERunner.java` to start the program with GUI.
    1. If no image is loaded, all Buttons and Dropdowns throw a dialog to show error message, except
       Button `load image`.
    2. Users are allowed to load multiple images one-by-one.
    3. For every new loaded image, we named it with it's filename but removed all existing `-`.
    4. The application automatically named each image by appending `-{effect name}` to the current image name.
    5. Users are able to switch between images.
    6. The effect indicated at the left dropdown allows clicking multiple times.

3. Run packed `core.jar` file under root path on command promote with command line option.
    ```bash
   # Invoke the application to open and execute a script file then shut down.
    java -jar Program.jar -file {path-of-script-file}
   
   # Invoke the application to open in an interactive text mode.
   # Users are allowed to type the script and execute it one line at a time.
    java -jar Program.jar -text
    
   # Invoke the application to open the GUI.
   # This is the default opening action if users double-click on the jar file.
    java -jar Program.jar
    ```
   > ex: java -jar core.jar -file res/newscript.text
   >
   > ex: java -jar core.jar -text
   >
   > ex: java -jar core.jar

   This scripts will run on images files under res/ folder and output file will be produced to the
   `res/ime/` and `res/mime/` folder.

## Citation

Copyright of following images used is owned by Cheng Shi and authorized to use for this assignment.

```bash
core/res
   ├── cat.ppm
   └── format
       ├── cat.bmp
       ├── cat.jpeg
       ├── cat.jpg
       ├── cat.png
       ├── cat.ppm
       └── something.png
```

## Change Log

### From Assignment 5 to Assignment 6

#### 1. GUI
A GUI is supported to allow a user to interactively load, process and save images.
To know more about the GUI, please refer to [View (GUI)](#view-gui).

#### 2. Interface Segregation: Move Read-Only methods from `MoreImageProcessor` to `ReadOnlyImageProcessor`

In assignment 5, the `MoreImageProcessor` provides methods `getInfo` and `getImage` for the Controller to save images
into OutputStream.
In assignment 6, the View needs a read-only Model to show the image effectively without the permission to perform image
processing.
Hence, we adopted Interface Segregation.
This is done by:

```java
interface ReadOnlyImageProcessor {
  int[] getInfo();

  int[][][] getImage();
}

interface MoreImageProcessor extends ReadOnlyImageProcessor {
}

class MoreImageProcessorImpl implements MoreImageProcessor {
}
```

#### 3. `BufferImageConverterImpl` implements `BufferImageConverter`

In assignment 5, the image conversion (int[][][] → BufferImage) was written inside a method together with the saving
methodology in `ImageIOHandler`.
But, BufferImage is also required by the View to show the image. Therefore, we created a new
interface `BufferImageConverter` which is implemented by `BufferImageConverterImpl` and moved the conversion out as a
method itself.
With this design, the View is able to have a `BufferImageConverter` object to convert the image getting from the Model
and show it without repeating the codes to do conversion.

### From Assignment 4 to Assignment 5

#### 1. Image representation: Linked List → 3D-Array

We used Linked List to represent an image in assignment 4;
however this is not convenient for us to apply filter matrix.
Therefore, we used 3D-Array instead of Linked List in assignment 5.
Even there is a change in our data structure, assignment 4 is still supporting on it's own.

#### 2. Support more image operations

This latest version supports more image operations, such as:

- Blur
- Sharpen
- Color transformation
    - Greyscale
    - Sepia
- Dithering

#### 3. Support more image format

While assignment 4 only support ASCII PPM image,
Assignment 5 supports:

- ASCII PPM
- BMP
- JPG
- JPEG
- PNG

Besides, regardless the format of the image being loaded, it's always an option to save the image to
another format.
> e.g. load xxx.ppm → save xxx.bmp

To apply these new operations, please refer to [Instruction](#Instruction).
