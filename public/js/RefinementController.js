morphBMC.controller("DefinitionController", ['$scope', '$http', 'SocketService', function($scope, $http, SocketsService) {

	/**
	 * send a POST request to add a new attribute
	 * @param {[type]} parameter which parameter the attribute belongs to
	 */
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

	/**
	 * send a post request to add a new parameter
	 */
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

	/**
	 * sends a DELETE request to remove a parameter
	 * and kicks it from the $scope
	 */
	$scope.removeParameter = function(parameter) {
		$http.delete("/api/problems/" + window.PROBLEM_ID + "/parameters/" + parameter.id).success(function() {
			// remove from scope
			angular.forEach($scope.parameters, function(p, index) {
				if (p.id === parameter.id) {
					$scope.parameters.splice(index, 1);
					return;
				}
			});

		});
	};

	// set up environment on load
	$http.get("/api/problems/"+window.PROBLEM_ID).success(function(data) {
		$scope.parameters = data.problem.parameters;
	});
}]);

