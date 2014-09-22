morphBMC.controller("CompatibilityController", ['$scope', '$http', '$filter', function($scope, $http) {

	$scope.adding = false;
	$scope.compatibilities = null;

	/**
	 * saves a compatibility
	 * @param {array} attributes the pair of attribute ids
	 * @param rating the rating as an object with fields ids and rating
	 */
	$scope.addCompatibility = function(compatibility) {

		$http.post("/api/problems/" + window.PROBLEM_ID +"/compatibilities", compatibility).success(function(data) {
			// overwrite on success
			$scope.compatibilities = data.compatibilities;
		});

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
		var ratings = null;
		angular.forEach($scope.compatibilities, function(c) {
			if (c.attr1.id == id || c.attr2.id == id) {
				ratings.push(c.rating.value);
			}
		});
		return ratings;
	};

	/**
	 * sums up average rating of one single attribute
	 */
	$scope.getAverageRating = function(id) {

		var ratings = $scope.getCompatibilityRatingsForAttribute(id);
		var v = 0;
		for (var i=0; i<ratings.length; i++) {
			// must cast to a Number here, otherwise JS builds a string
			v+= (+ratings[i]);
		};
		return v/ratings.length;
	};

	/**
	 * get compatibility for a pair of attributes
	 */
	$scope.getCompatibilityRating = function(attr1, attr2) {
		//if ($scope.compatibilities) console.log("nothing yet");
		var _c = null;
		angular.forEach($scope.compatibilities, function(c) {
			// cant break out of foreach
			if (_c === null) {
				// arrays should be the same
				//console.log("checking %d", c.id)
				if ( (attr1 === c.attr1.id && attr2 === c.attr2.id) || (attr2 === c.attr1.id && attr1 === c.attr2.id) ) {
					console.log("found %d", c.id)
					_c = c;
				}
			}

		});
		//console.log(_c);
		return _c;
	};



	// grab all existing parameters to build table
	$http.get("/api/problems").success(function(data) {
		// contains problem properties and parameters with their attributes,
		// if present yet
		$scope.parameters = data.problem.parameters;

		// after receiving problem id, load existing compatibilities
		$http.get("/api/problems/" + window.PROBLEM_ID+ "/compatibilities").success(function(data) {
			$scope.compatibilities = data.compatibilities;
		});
	});

	$http.get("/api/ratings").success(function(data) {
		$scope.ratings = data.ratings;
	});



}]);/*.service("CompatibilityService", function($http, $q) {
	return {
		getCompatibilities: function() {
			var deferred = $q.defer();
			$http.get("/api/problems/" +  window.PROBLEM_ID + "/compatibilities").success(function(data) {
				deferred.resolve({
					compatibilities: data.compatibilities;
				})
			});
			return deferred.promise;
		}
	}
});*/