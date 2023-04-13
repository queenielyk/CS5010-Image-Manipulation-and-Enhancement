# CS5010 Assignment6: Graphical Image Manipulation and Enhancement

Member: Queenie, Cheng    
Date: April 5 2023   
Professor: Amit Shesh

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

