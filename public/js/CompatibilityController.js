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
			"attributes": attributes,
			"rating": rating
		}).success(function(data) {
			console.log(data);
		});
	};

	/**
	 * calculates the offsets of parameter arributes
	 * needed for rowspan calculation on compatibility table
	 * e.g. if first parameter has 2 attributes and second has
	 * 4, the offset array will be [2,4]
	 * during iteration of <tr> elements, everytime $index is in offset,
	 * a rowspan td is rendered
	 * @return {array} 
	 */
	$scope.getOffsets = function() {
		var offsets = [];
		angular.forEach($scope.parameters, function(p, i) {
			this.push(i * p.attributes.length)
		}, offsets);

		console.log(offsets)
		return offsets;	
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
		var attributes = [];
		angular.forEach($scope.parameters, function(p) {
			if (p.id === id) {
				attributes = attributes.concat(p.attributes);
			}
		});
		return attributes;
	};

	/**
	 * returns the parameter that a single attribute belongs to

	 */
	$scope.getParameterByAttribute = function(id) {
		// walk through parameters
		var param = null;
		angular.forEach($scope.parameters, function(p) {
			// walk through all attributes of parameters
			angular.forEach(p.attributes, function(a) {
				if (a.id == id) {
					param = p;
				}
			});
		});
		return param;
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

