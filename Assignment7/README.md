# Image Processing
Team members - Vamshika Lekkala, Srinivasa Pavan Kumar Danaboina

We have designed our Image Processing Project using the Model View Controller (MVC) Architecture and Command Design Pattern.

The Flow of our Program is as follows:

1) The Program starts execution from the ImageProcessingApp.java (contains main())
2) The main() calls the goController()
3) The Controller calls the View to display to the user
4) The controller takes an input (image command) from the user
5) This input is converted into a command class object based on the respective command
6) Go back to step 3. Repeat until the user enters "q" to quit
7) The respective command object is created and executed
8) The executeCommand() for each command class calls the respective image model method to return an imageModel.
9) Each image returned by the command class are added to a hashmap which stores the image name as the key and its image model as the value.
10) The images that are saved with save commands can be found in folder.
For instance, 
If the command is "brighten 10 koala koala-brighter" it will create an object of BRIGHTEN which will in turn call the Brighten method of the ImageModel class using the 'koala' image. 
The brighten method will return a new brightened image and save it in the hashmap in the controller.
In this manner the Controller stores all the imageModel objects created (based on the commands provided by the user). 

Control package:
ImageProcessingApp.java - contains the main method where program execution starts.
IUserCommands.java - parents interface for IReadWrite and IImageCommand.
IController.java - Interface represents the controller. 
Controller.java - It creates command class objects for each valid command. It maintains the hashmap of images created so far. 
ImageNotFoundException.java - returns an exception if the image name given is not found.
InvalidFileException.java - returns an exception if the given file type is invalid.
InvalidInputException.java -  returns an exception when the user gives an invalid exception.
IImageCommand.java - This interface is used to instantiate various controllers based on the command given by the user. The classes that implement this interface call their respective methods in the model class to execute the logic.
ImageCommand.java - extends IImageComand.java
Implementations of IImageCommand.java -
1. Brighten - brighten controller calls the brighten method and returns a new image after the operation.
2. Greyscale - Greyscale controller calls the Greyscale method and returns a new image after the operation.
3. Combine - Combine controller calls the Combine method and returns a new image after the operation.
4. HorizontalFlip - HorizontalFlip controller calls the HorizontalFlip method and returns a new image after the operation.
5. VerticalFlip - VerticalFlip controller calls the VerticalFlip method and returns a new image after the operation.
6. Filter - Filter controller calls the Filter method and returns a new image after the operation for blur or sharpen.
7. Transformation - Transformation controller calls the Transformation method and returns a new image after the operation for sepia or new greyscale.
8. Dither - Dither controller calls the Dither method and returns a new image after the operation.
IReadWrite - used to read and write data from/to a file. 
ReadWrite - implements IReadWrite. reads and writes from/to a file. 
reads a command file
ReadWriteImage - implements read and write operations for an image.
loads and saves JPG, JPEG, PNG and BMP.
ReadWritePPM -implements read and write operations for an image.
loads and saves PPM.
IGUIController - Interface represents the GUIController.
GUIController - Instantiates the GUIView and parses the user inputs.


Model Package:
IImageModel.java - interface that represents an 8 bit image has all the operations that you do on an image.
ImageModel.java - implements IImageModel.java. It has image properties like width, height, max value and a list of pixels. Each processed image is a new image object.
IPixel.java - represents a pixel and returns its properties i.e. red, green and blue.
Pixel.java - implements IPixel. represented as (r,g,b)
ImageFilter.java - it is an enum that has all the respective filters or kernels such as Blur filter and Sharpen filter
ImageColorTransformation.java - it is an enum that has the matrix to perform color transformation like Greyscale Luma and Sepia.

View package:
IImageView.java - view of the application that displays a message to the user.
ImageView.java - Class that is executed to form the view for the user.
IImageGUIView - Interface represents the View. It constructs the GUI.
ImageGUIView - Builds the GUI for the user. Takes input from the user. The corresponding action listener event is passed to the controller
LineChart - Constructs the histogram in GUI. 

Tests:
ImagCommandTest.java - tests the ImageCommand.java
ControllerModelTest.java - tests the end to end flow from controller to model 
ControllerTest.java - mocks the model to verify if the input in the controller is same as the input passed to the model
ImageModelTest.java - tests methods of java that manipulates images
PixelTest - tests Pixel class
ReadWriteImageTest - tests ReadWriteImage class
ReadWritePPMTest - tests ReadWritePPM class

Design Changes:
We added a new class named LogicController. We seperated the command parsing and delegating logic from the main controller so that different views can use the same logic.
We added a new interface and class named IGUIController and GUIController respectively. They are responsible for taking input from GUI and sending it to Model. 
We added a new interface and class named IGUIView and GUIView respectively. They are responsible for showing the functionalities available in the application and showing the outputs for the inputs given. We added a new class named LineChart that executes the logic for creating a histogram. 

How to use our program:
Run the below commands from res folder
1. Run 'java -jar PDPAssignment_05.jar -file scriptforjar.txt'

2. Run java -jar Program.jar -text
   Enter commands in the console.

3. Run java -jar Program.jar
   Use GUI to input commands

Our screenshot of the application - screenshot.png

In res folder:
Our JAR file - PDPAssignment_05.jar
Our example script file is - scriptforjar.txt
Our example image - dog.png

Citation:
Citations for dog.png - A dog image accessed 29 March 2023, <https://www.pngwing.com/en/free-png-dkhtg>.

dog.jpg,dog.bmp and dog.ppm - created by us by converting dog.png and you are free to use it.

ourExample.ppm, test.ppm, test123.png, test.png, test.jpg, test.bmp - created by us and you are free to use it.