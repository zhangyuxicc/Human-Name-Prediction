# human-name-prediction
Design:
First I collect the names and locations from the samples.
Seven locations with highest probability of name occurrence are selected as the predict location.
Only location with probability higher than 10% will be shown.
When a new file is provided, I use tesseract to generate hocr and text file to get the real name locations. 
Each time the new information will be recorded to reinforce the following predictions.
