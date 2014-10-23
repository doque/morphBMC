morphBMC.controller("DefinitionController", ['$scope', '$http', 'SocketsService', function($scope, $http, SocketsService) {

	$scope.addAttribute = function(parameter) {

		if (!parameter.attribute) return;

		if (parameter.attribute.name.length === 0) {
			return;
		}

		$http.post("/api/problems/" + window.PROBLEM_ID + "/parameters/"+parameter.id+"/attributes", {
			"name": parameter.attribute.name
		}).success(function(data) {
			parameter.attributes.unshift(data.attribute);
			parameter.attribute.name = "";
		});
	};


	SocketsService.status();

	$scope.addParameter = function() {
		if (!$scope.parameter) return;
		// sanity check
		if ($scope.parameter.name.length === 0) {
			// no value given
			$scope.addingParameter = false;
			return;
		}
		
		$http.post("/api/problems/" + window.PROBLEM_ID + "/parameters", {
			"name": $scope.parameter.name
		}).success(function(data) {
			$scope.addingParameter = false;
			// reset input field model
			$scope.parameter.name = "";
			$scope.parameters.push(data.parameter);
		});
	};

	$scope.removeParameter = function(parameter, index) {
		//console.log(parameter, index)
		$http.delete("/api/problems/" + window.PROBLEM_ID + "/parameters/" + parameter.id).success(function() {
			$scope.parameters.splice(index, 1);
		}).error(function() {
			alert("this parameter has existing compatibilities");
		});
	};

	// set up environment on load
	$http.get("/api/problems/"+window.PROBLEM_ID).success(function(data) {

		$scope.parameters = data.problem.parameters;
	});
}]);

