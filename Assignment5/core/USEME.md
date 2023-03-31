# CS5010 Assignment5: Image Manipulation and Enhancement V2.0

Member: Queenie, Cheng    
Date: March 25 2023   
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

2. Run packed `core.jar` file under root path on command promote with command lind option.
    ```bash
    java -jar core.jar -file {script}
    ```
   > ex: java -jar core.jar -file res/newscript.text

   This scripts will run on images files under res/ folder and output file will be produced to the
   `res/ime/` and `res/mime/` folder.

