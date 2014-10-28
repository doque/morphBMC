app.controller("RefinementController", ['$scope', '$http', 'SocketService', function($scope, $http, SocketsService) {

	// set up environment on load
	$http.get("/api/problems/"+window.PROBLEM_ID+"/parameters?all=yes").success(function(data) {
		$scope.parameters = data.parameters;
	});
}]);

