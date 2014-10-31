app.controller("ProblemController", ['$scope', '$http', '$location', '$timeout', function($scope, $http, $location, $timeout) {

	$scope.problem = {};

	/**
	 * changes the stage of a problem
	 */
	$scope.changeStage = function(stage) {

		$scope.problem.stage = '/' + stage;

		// POST to server, broadcast from there
		if ($scope.problem.userId == window.USER_ID) {
			$http.post("/api/problems/" + window.PROBLEM_ID + "/stage", {
				stage: stage
			});
		}
	}

	/**
	 * edits the name of a problem
	 */
	$scope.editName = function() {
		var name = $scope.problem.name;
		if (name.length === 0) {
			$scope.editingName = false;
			return;
		}
		$http.post("/api/problems/"+window.PROBLEM_ID+"/name", {
			name: $scope.problem.name
		}).success(function() {
			$scope.editingName = false;
		})
	}

	/**
	 * saves a problem statement
	 */
	$scope.saveStatement = function(e) {

		var button = $(e.target);

		$http.post("/api/problems/" + window.PROBLEM_ID + "/statement", {
			statement: $scope.problem.statement
		}).success(function() {
			button.removeClass("btn-primary").addClass("btn-success").text("Saved!");
			$timeout(function() {
				button.removeClass("btn-success").addClass("btn-primary").text("Save");
			}, 5000);
		});
	};

	// todo listen to state changed

	// fetch initial problem info
	$http.get("/api/problems/"+window.PROBLEM_ID).success(function(data) {
		$scope.problem = {
			id: data.problem.id,
			name: data.problem.name,
			owner: data.problem.owner,
			userId: data.problem.userId,
			statement: data.problem.statement,

			// todo: either location path or current stage of problem
			stage: $location.path()
			//stage: '/' + data.problem.stage.toLowerCase()
		};
	});

}]);