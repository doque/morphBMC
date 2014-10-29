app.controller("DefinitionController", ['$scope', '$http', 'SocketService', function($scope, $http, SocketsService) {

	/**
	 * send a POST request to add a new attribute
	 * @param {[type]} parameter which parameter the attribute belongs to
	 */
	$scope.addAttribute = function(parameter, attribute) {

		if (!attribute || !attribute.name) return;

		$http.post("/api/problems/" + window.PROBLEM_ID + "/parameters/"+parameter.id+"/attributes", attribute).success(function(data) {
			parameter.attributes.unshift(data.attribute);
			attribute.name = "";
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

	// set up environment on load
	$http.get("/api/problems/"+window.PROBLEM_ID+"/parameters").success(function(data) {
		$scope.parameters = data.parameters;
	});
}]);

