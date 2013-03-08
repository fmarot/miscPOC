var casper = require('casper').create({
	clientScripts:  [
	'./misc/jquery.js'
	]
});
var imageWidth = casper.cli.get(0);


//casper.start('http://127.0.0.1:8080/templates/app.html?chosenTemplate=docteurToto', function() {
casper.start('app.html', function() {

	// Here's how to access the 'page' from underlying phantomJS implem
	this.page.paperSize = {
		format: 'A4'
	}
	
	/** Method needed to see console log utput in page context */
	this.page.onConsoleMessage = function (msg, line, source) {
		console.log('console> ' + msg);
	};
	
	casper.evaluate(function(imageWidthValue) {
		var targetInput = $('#imagesSizeInput');
		scope = angular.element(targetInput).scope();
		scope.imgWidth = imageWidthValue;
		scope.$apply();
		
		$('#myLabel').text('txt Lorem Ipsum modified by jquery');
	}, {imageWidthValue: imageWidth});
	
	casper.capture('casper-out.pdf');
});

casper.run();


