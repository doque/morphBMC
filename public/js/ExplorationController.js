morphBMC.controller("ExplorationController", ['$scope', '$http', '$filter', function($scope, $http) {

	// initial view template variables
	$scope.selected = [];
	$scope.attributes = [];
	$scope.average = 0;
	$scope.compatibilities = [];
	$scope.better = [];
	$scope.suggested = [];

	/**
	 * calculates all attributes to which there is a better-than-average compatibility
	 * TODO extend this with a filter (instead of simple average)
	 */
	$scope.getBetterThanAverage = function(id) {
		// holds the parameter of the chosen attribute, we don't need any compatibilities for that
		var thisParam = getParameterByAttribute(id);
		// initial average rating
		var avg = 0;
		// calculate average of all compatibilities that include the chosen attribute
		angular.forEach($scope.selected, function(id) {
			avg += getAverageRating(id);
		});
		avg /= $scope.selected.length;

		// array to hold all attribute ids of "better" compatibilities
		var better = [];
		for (var i=0; i<$scope.compatibilities.length; i++) {
			var c = $scope.compatibilities[i];
			// skip all compatibilities that include the current parameter
			if (getParameterByAttribute(c.attr1.id).id === thisParam ||
				getParameterByAttribute(c.attr2.id).id === thisParam) {
				continue;
			}
			// check if either one of a compatibility's attribute is the chosen attribute
			// and if that compatibility's rating is "better", i.e. higher than average
			if ((c.attr1.id === id || c.attr2.id === id) && c.rating.value >= avg) {
				// get the calculated id
				var suggestedId = c.attr1.id === id ? c.attr2.id : c.attr1.id
				if ($scope.selected.indexOf(suggestedId) === -1) {
					better.push(suggestedId);
				}
			}
		}
		// apply the two calculated values to the view template
		$scope.average = avg;
		$scope.better = better;
	};


	/**
	 * adds or removes a selected attribute to selected attributes
	 */
	$scope.toggleSelect = function(id) {

		var siblings = getSiblingAttributes(id);

		// deselect all sibling attribtues of chosen attribtues
		// to only allow selection of one attribute per parameter
		angular.forEach(siblings, function(s) {
			remove($scope.selected, s);
		});

		// toggle element selection
		if ($scope.selected.indexOf(id) > -1) {
			remove($scope.selected, id);
			return;
		}

		$scope.selected.push(id);

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
	 * returns all rated compatibilities that include the given attribute
	 *
	 * @param id - the attribute id
	 */
	$scope.getCompatibilityRatingsForAttribute = function(id) {
		var ratings = [];
		angular.forEach($scope.compatibilities, function(c) {
			if (c.attr1.id == id || c.attr2.id == id) {
				ratings.push(c.rating);
			}
		});
		return ratings;
	};



	// TODO get adjacent attribtues!
	function getSiblingAttributes(id) {
		var p = getParameterByAttribute(id);
		var siblings = [];
		for (var i=0; i<p.attributes.length; i++) {
			var a = p.attributes[i];
			if (a.id === id) {
				continue;
			}
			siblings.push(a.id);
		}
		return siblings;
	};

	/**
	 * sums up average rating of one single attribute
	 */
	function getAverageRating(id) {
		var ratings = $scope.getCompatibilityRatingsForAttribute(id);
		var v = 0;
		for (var i=0; i<ratings.length; i++) {
			// must cast to a Number here, otherwise JS builds a string
			v+= (+ratings[i].value);
		};

		return v/ratings.length;
	};
	

	/**
	 * returns the parameter that a single attribute belongs to
	 * @param id - the attribute id
	 */
	function getParameterByAttribute(id) {
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
 * helper function that removes an int from an array
 */
	function remove(arr, int) {
		if (arr.indexOf(int) >= 0) {
			if (arr.length === 1) {
				arr.pop();
			}
			arr.splice(arr.indexOf(int), 1);
		}
		return arr;
	}
	

	// set up environment on load
	$http.get("/api/problems").success(function(data) {
		// contains problem properties and parameters with their attributes,
		// if present yet
		$scope.parameters = data.problem.parameters;
		angular.forEach($scope.parameters, function(p) {
			angular.forEach(p.attributes, function(a) {
				$scope.attributes.push(a.id);
			});
		});

		$http.get("/api/problems/" + window.PROBLEM_ID+ "/compatibilities").success(function(data) {
			$scope.compatibilities = data.compatibilities;
		});
	});

}]);