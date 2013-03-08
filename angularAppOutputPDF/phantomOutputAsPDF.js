var system = require('system');
var myImageSizeIWant = system.args[1];

var page = require('webpage').create();

page.paperSize = {
	format: 'A4'
}

/** Method needed to see console log utput in page context */
page.onConsoleMessage = function (msg, line, source) {
     console.log('console> ' + msg);
};

page.open('app.html', function () {
	
	console.log("1 " + myImageSizeIWant);
	page.includeJs("http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js", function() {

		page.evaluate(function(imgSize) {
			$('#myLabel').text('txt Lorem Ipsum modified by jquery');
			var targetInput = $('#imagesSizeInput');
			
			scope = angular.element(targetInput).scope();
			scope.imgWidth = imgSize;
			scope.$apply();

			console.log("Taille image = " + $('#firstImage').width());
		}, myImageSizeIWant);
		page.render('phantom-out.pdf');
		
		phantom.exit();
	});
	
});



