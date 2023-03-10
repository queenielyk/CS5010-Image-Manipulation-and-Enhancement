# CS5010 Assignment4: Image Manipulation and Enhancement
Member: Queenie, Cheng    
Date: March 3 2023   
Professor: Amit Shesh

## Project idea
Develope a text-based Image Process program with MVC design idea.

## Project Strcuture

### Overview
We developed this project based on a Model + View + Controller (MVC) concept. However, as this is a text-based program, so View is not applicable in this case.

In general, Controller takes input from users then ask Model to execute desired action(s); Model is where computation takes place.

In our infrastructure, Controller takes input from user. Invokes desired action(s) proviede by Model if those operations are valid, return 'unknown' to user otherwise.

#### Controller
``` bash
IME/controller
    ├── IController.java
    ├── ImageCommand.java
    ├── ImageController.java
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

#### Model

##### Data Structure
Image is composed by a group of components.
Each component is a combination of Red, Green, and Blue (RGB).
Hence, we store a image as a Linked List: each compontent is a Node of Linked List who contains the reference of it's next component.

e.g. Node(RGB = 0,0,0) -> Node(RGB = 0,0,1) -> Node(RGB = 0,0,2) -> NULL

From our codes, `ImageComp` is the interface to indicate what a Node should be and actions available:
- Returning it's RGB value
- Setting it's next component
- Getting it's next component

`ImageCompImp` is a class to implement interface `ImageComp`.

##### Computation
`ImageProcessor` is an interface to indicate what kind of actions a processor should have:
- Load
- Greyscale : red-component / green-component / blue-component / value-component / intensity-component / luma-component
- Flip
  - Horizontal
  - Vertical
- Combine
- Save

In reality, there are various image format, e.g. PNG, JPG, BMP, etc.
In this project, our goal is to process images in ASCII PPM format, hence we implement `ImageProcessor` as `PpmProcessor`.

``` bash
IME/model
    ├── ImageComp.java
    ├── ImageCompImp.java
    ├── ImageProcessor.java
    └── PpmProcessor.java
```

## Instruciton
1.  Run ImageController.java to start the program   
2.  Enter command with parameters sepearte with spaces  **(ex. "Enter Command:run res/script.text")**
>This scripts will run on images files under res/folder and output will procudes to the same folder 

## Citation
Copyright of following images used is owned by Cheng Shi and authrized to use for this assignment.
- res/cat.ppm  
- res/building.ppm