'use strict';


function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
}

function mainController($scope, $http, $route, $routeParams) {

	$scope.displayFooter = true;
	$scope.chosenTemplate = getURLParameter('chosenTemplate');
	$scope.imgWidth = 200;
}


