morphBMC.controller("CompatibilityController", ['$scope', '$http', function($scope, $http) {

	/**
	 * saves a compatibility
	 * @param {array} attributes the pair of attribute ids
	 * @param rating the rating as int
	 */
	$scope.addCompatibility = function(attributes, rating) {
		// attributes contains two attribute ids
		if (attributes.length !== 2) {
			console.log("invalid attribute parameter, length is %d", attributes.length);
			return;
		}
		$http.post("/api/compatibility", {
		});
	};


	/**
	 * returns all attributes from all problems
	 */
	$scope.getAllAttributes = function() {
		var attributes = [];
		// merges all attributes from each parameter into one array
		angular.forEach($scope.parameters, function(parameter, i) {
			if (parameter.attributes.length) {
				attributes = attributes.concat(parameter.attributes);
			}
		});

		return attributes;
	};

	/**
	 * returns attributes for a specific problem
	 */
	$scope.getAttributesByParameter = function(id) {
		angular.forEach($scope.parameter, function(p) {
			if (p.id === id) {
				return p.attributes;
			}
		});
		return null;
	};

	/**
	 * returns the parameter that a single attribute belongs to

	 */
	$scope.getParameterByAttribute = function(id) {
		// walk through parameters
		angular.forEach($scope.parameter, function(p) {
			// walk through all attributes of parameters
			angular.forEach(p.attributes, function(a) {
				if (a.id === id) {
					return p;
				}
			});
			return null;
		});
	};


	// grab all existing parameters to build table
	$http.get("/api/problems").success(function(data) {
		// contains problem properties and parameters with their attributes,
		// if present yet
		$scope.parameters = data.problem.parameters;
		
	});
	// then load all present compatibilities
	//$http.get("/api/problems/" + window.PROBLEM_ID + "/compatibilities").success(function(data) {
	//	$scope.compatibilities = data.compatibilities;
	//});

}]);

