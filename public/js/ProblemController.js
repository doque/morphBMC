morphBMC.controller("ProblemController", ['$scope', '$http', '$location', function($scope, $http, $location) {

	
	$scope.problem = {};

	console.log($scope.problem)

	$scope.changeStage = function(stage) {

		$scope.problem.stage = '/' + stage;

		// POST to server, broadcast from there
		if ($scope.problem.userId == window.USER_ID) {
			console.log("setting stage to ", stage)
			$http.post("/api/problems/"+window.PROBLEM_ID, {
				stage: stage
			});
		}
	}

	// todo listen to state changed

	// fetch initial problem info
	$http.get("/api/problems/"+window.PROBLEM_ID).success(function(data) {
		$scope.problem = {
			id: data.problem.id,
			name: data.problem.name,
			owner: data.problem.owner,
			userId: data.problem.userId,
			// todo: either location path or current stage of problem
			stage: $location.path()
			//stage: '/' + data.problem.stage.toLowerCase()
		};
	});

}]);