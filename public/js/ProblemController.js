morphBMC.controller("ProblemController", ['$scope', '$http', '$location', function($scope, $http, $location) {

	// fetch initial problem info
	$http.get("/api/problems/"+window.PROBLEM_ID).success(function(data) {
		$scope.problem = data.problem;
		$scope.userId = window.USER_ID;
	});

	$scope.currentPath = function() {
		return $location.path();
	}

}]);