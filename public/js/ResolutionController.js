app.controller("ResolutionController", ['$scope', '$http', '$filter', function($scope, $http) {

	$scope.adding = false;
	$scope.compatibilities = null;

	$scope.hoverX = 0;
	$scope.hoverY = 0;

	$scope.rendering = true;

	/**
	 * saves a compatibility
	 * @param {compatiblity) the compatibility object
	 */
	$scope.addCompatibility = function(compatibility) {

		$http.post("/api/problems/" + window.PROBLEM_ID +"/compatibilities", compatibility).success(function(data) {
			// overwrite on success
			$scope.compatibilities = data.compatibilities;
		});

	};

	/**
	 * provides hover effect for parent tds
	 */
	$scope.hover = function(x, y, redundant) {
		if (!redundant) {
			$scope.hoverX = x;
			$scope.hoverY = y;
		}
	};

	/**
	 * calculates the offsets of parameter arributes
	 * needed for rowspan calculation on compatibility table
	 * e.g. if first parameter has 2 attributes and second has
	 * 4, the offset array will be [0, 2, 6]
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
	 * get compatibility for a pair of attributes
	 */
	$scope.getCompatibilityRating = function(attr1, attr2) {
		var _c = null;
		angular.forEach($scope.compatibilities, function(c) {
			// cant break out of foreach
			if (_c === null) {
				// arrays should be the same
				if ( (attr1 === c.attr1.id && attr2 === c.attr2.id) || (attr2 === c.attr1.id && attr1 === c.attr2.id) ) {
					_c = c;
				}
			}

		});
		return _c;
	};



	// grab all existing parameters to build table
	$http.get("/api/problems/"+window.PROBLEM_ID).success(function(data) {
		// contains problem properties and parameters with their attributes,
		// if present yet
		$scope.parameters = data.problem.parameters;



		// after receiving problem id, load existing compatibilities
		$http.get("/api/problems/" + window.PROBLEM_ID+ "/compatibilities").success(function(data) {
			$scope.compatibilities = data.compatibilities;
			console.log($scope)
		});
	});

	$http.get("/api/ratings").success(function(data) {
		$scope.ratings = data.ratings;
	});

}]);