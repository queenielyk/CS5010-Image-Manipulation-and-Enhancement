# CS5010 Assignment7: Mosaic

Member: Queenie, Cheng    
Date: April 24 2023   
Professor: Amit Shesh

## Project Idea

Given the code provided, we implemented the image `mosaic` features to the APP for all
GUI/Text/Script.

## Implementation Details

### Model

In the provided code, the `IImageModel` was design to represent an image instances. And an image
stores width and height and List of `IPixels`.   
Each Pixel stores only RGB values but no coordinates. And they overwrote equals() and hashcode() so
any pixel with same RGB values is same is their design, which makes it hard to implements mosaic
base without coordinate info.

We had to write our own indexOf() method, so we can get the coordinate of each pixel.
Then we generate random clusters and match the image pixels with the cluster and average them to get
the resulting image.

### Controller

Even though controller design was kind of overcomplicated probably due to trying to reuse the
Text/Script based logic on GUI controller. And one weird design is the all command queued when
entered and only get executed after quit.  
But thankful to the command design pattern, The controller logic of provided was enough. None of the
control flow have to be changed but just add a `Mosaic` ImageCommand class and add a switch case
in `checkCommandType`.

### View

Their GUI View was well-structured and lay out was designed well enough, so we just add a new
JPanel. We just bind mosaic operations as an ActionListener to the button.   

Since some operation might take long time. So we preload the loading icon to the ImageLabel. 
Then we fire up a new thread to execute the image operations. Once the
new thread got processed, invokes the change to the main thread.
The GUI can still be responsive during the process.