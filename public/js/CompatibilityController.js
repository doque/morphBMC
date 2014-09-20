morphBMC.controller("CompatibilityController", ['$scope', '$http', function($scope, $http) {

	$scope.adding = false;
	$scope.rating = "";

	/**
	 * saves a compatibility
	 * @param {array} attributes the pair of attribute ids
	 * @param rating the rating as an object with fields ids and rating
	 */
	$scope.addCompatibility = function(compatibility) {
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
	 * 
	 * @param id - the problem id
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
	 * @param id - the attribute id
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

	/**
	 * returns all rated compatibilities that include the given attribute
	 *
	 * @param id - the attribute id
	 */
	$scope.getCompatibilityRatingsForAttribute = function(id) {
		var ratings = [];
		angular.forEach($scope.compatibilities, function(c) {
			if (c.attr1.id == id || c.attr2.id == id) {
				ratings.push(c.rating.value);
			}
		});
		return ratings;
	};

	/**
	 * 
	 */
	$scope.getAverageRating = function(id) {
		$scope.rating = $scope.getCompatibilityRatingsForAttribute(id).avg();
		console.log("rating changed to %d", $scope.rating);
	};	

	// grab all existing parameters to build table
	$http.get("/api/problems").success(function(data) {
		// contains problem properties and parameters with their attributes,
		// if present yet
		$scope.parameters = data.problem.parameters;

		// after receiving problem id, load existing compatibilities
		$http.get("/api/problems/" + window.PROBLEM_ID + "/compatibilities").success(function(data) {
			console.log(data.compatibilities);
			$scope.compatibilities = data.compatibilities;
		});
	});
	$http.get("/api/ratings").success(function(data) {
		$scope.ratings = data.ratings;
	});
	// then load all present compatibilities
	//$http.get("/api/problems/" + window.PROBLEM_ID + "/compatibilities").success(function(data) {
	//	$scope.compatibilities = data.compatibilities;
	//});

}]);

/**
 * calculates the average value of all elements in an array
 * @return float the average value
 */
Array.prototype.avg = function() {
	var v = 0;
	for (var i=0; i<this.length; i++) {
		// must cast to a Number here, otherwise JS builds a string
		v+= (+this[i]);
	};
	return v/this.length;
};

