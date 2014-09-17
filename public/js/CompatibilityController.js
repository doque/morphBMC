morphBMC.controller("CompatibilityController", ['$scope', '$http', function($scope, $http) {

	$scope.adding = false;

	/**
	 * saves a compatibility
	 * @param {array} attributes the pair of attribute ids
	 * @param rating the rating as an object with fields ids and rating
	 */
	$scope.addCompatibility = function(compatibility) {
		console.log(compatibility);
		// attributes contains two attribute ids
		if (compatibility.attributes.length !== 2) {
			console.log("invalid attribute parameter, length is %d", attributes.length);
			return;
		}
		$http.post("/api/problems/" + window.PROBLEM_ID +"/compatibilities", compatibility).success(function(data) {
			console.log(data);
		});
	};

	/**
	 * calculates the offsets of parameter arributes
	 * needed for rowspan calculation on compatibility table
	 * e.g. if first parameter has 2 attributes and second has
	 * 4, the offset array will be [2, 6]
	 * during iteration of <tr> elements, only everytime $index is in offset,
	 * a td with large rowspan is rendered since that is enough for all attributes
	 * @return {array} 
	 */
	$scope.getOffsets = function() {
		var offsets = [0];
		angular.forEach($scope.parameters, function(p, i) {
			// offsets are calculated by adding the last value in the array
			// and the current parameter's attribute count
			offsets.push(offsets[offsets.length - 1] + p.attributes.length);
		});

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
				//console.log("adding", parameter.attributes)
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
	
	$http.get("/api/ratings").success(function(data) {
		$scope.ratings = data.ratings;
	});
	// then load all present compatibilities
	//$http.get("/api/problems/" + window.PROBLEM_ID + "/compatibilities").success(function(data) {
	//	$scope.compatibilities = data.compatibilities;
	//});

}]);

