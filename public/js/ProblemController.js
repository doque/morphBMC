morphBMC.controller("ProblemController", ['$scope', '$http', function($scope, $http) {
	// at some point this won't be fetched but filled by resource
	$http.get("/api/problems").success(function(data) {
		$scope.problem = data.problem;
		window.PROBLEM_ID = data.problem.id;
	});

}]);