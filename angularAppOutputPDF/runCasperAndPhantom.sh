#/bin/sh

#requires casperjs and phantomjs to be both installed

# the number represents the size of the image that casper/phantom will pass as argument to the Angular app
casperjs casperOutputAsPDF.js 150
phantomjs phantomOutputAsPDF.js 1200