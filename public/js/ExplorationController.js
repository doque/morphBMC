morphBMC.controller("ExplorationController", ['$scope', '$http', '$filter', function($scope, $http) {

	// großes array bauen mit allen möglichen attributes
	// 
	// ein attribut wählen, dann alle compatibilities ausgehend davon wählen (hier: filter angeben?)
	// alles aus dem array kicken was nicht in den compatibilitites ist
	// 
	// oder parameterweise durchgehen: einen wählen, seine ID wo reinpacken
	// dann alle übrigen compatibilities, in denen das attribute nicht diese parameterID hat highlighten (mit filter)
	// 
	// filter: rating > mittelwert!

	$scope.selected = [];
	$scope.attributes = [];
	$scope.average = 0;
	$scope.compatibilities = [];
	$scope.better = [];

	/**
	 * calculates all attributes to which there is a better-than-average compatibility
	 */
	$scope.getBetterThanAverage = function(id) {
		var avg = 0;
		angular.forEach($scope.selected, function(id) {
			var thisAvg = getAverageRating(id);
			console.log(thisAvg)
			avg += thisAvg;
		});
		avg /= $scope.selected.length;

		var better = [];
		angular.forEach($scope.compatibilities, function(c) {
			if ((c.attr1.id === id || c.attr2.id === id) && c.rating.value >= avg) {
				console.log("better than avg: ", c.attr1.name, c.attr2.name, "with rating ov ", c.rating.value);
				// push "the other" id.
				better.push( c.attr1.id === id ? c.attr2.id : c.attr1.id);
			}
		});
		$scope.average = avg;
		$scope.better = better;
	}




	/**
	 * adds or removes a selected attribute to selected attributes
	 */
	$scope.toggleSelect = function(id) {
		getSiblingAttributes(id);

		if ($scope.selected.indexOf(id) >= 0) {
			$scope.selected.splice($scope.selected.indexOf(id), 1);
		} else {
			$scope.selected.push(id);
		}
		if ($scope.selected.length === 0) {
			$scope.better = [];
		}
	}

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
		angular.forEach(p.attributes, function(a) {
			if (a.id === id) return;
			siblings.push(a.id);
		});

		console.log(siblings);
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