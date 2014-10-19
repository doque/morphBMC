morphBMC.controller("ResultsController", ['$scope', '$http', function($scope, $http) {

	$scope.parameters = [];
	$scope.attributes = [];
	$scope.configurations = [];

	/**
	 * creates nice complex objects that contain all possible configurations
	 * @return {array} array of configuration objects
	 */
	$scope.allPossible = function() {
		// only holds IDs of attributes, but no further information
		// in order to display this nicely, we'll have to build a complex object
		var all = getAllCombinations($scope.attributes);
		var configurations = [];

		// walk all permutations
		angular.forEach(all, function(permutation) {
			// build object
			var configuration = {
				consistencyValue: 0
			};

			// walk this configurations attribute IDs
			var sum = 0;
			for (var i=0; i<permutation.length; i++) {
				var attribute = permutation[i], param = getParameterByAttribute(attribute.id);
				// each configuration holds an array of parameters,
				// their id and name
				// and the chosen attribute for that parameter
				configuration[param.name] = attribute.name;

				// calculate sum of all average ratings for that attribute for consistency rating
				sum += getAverageRating(attribute.id);
			}
			// calculate average rating value
			configuration.consistencyValue = (sum/permutation.length).toFixed(3);

			configurations.push(configuration);
		});

		$scope.configurations = configurations;
	};

	/**
	 * returns the parameter that a single attribute belongs to
	 * @param id - the attribute id
	 */
	var getParameterByAttribute = function(id) {
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
	 * calculates all possible permutations of a list of lists
	 * in this case, it permutes all attributes of each parameter with each other
	 * @param  {array} arraysToCombine the list of arrays to permute
	 * @return {array} a list of permutations, each of which contains exactly one attribute of one parameter.
	 */
	function getAllCombinations(arraysToCombine) {
		var divisors = [];
		for (var i = arraysToCombine.length - 1; i >= 0; i--) {
		   divisors[i] = divisors[i + 1] ? divisors[i + 1] * arraysToCombine[i + 1].length : 1;
		}
		
		function getPermutation(n, arraysToCombine) {
		   var result = [], 
			   curArray;
		   for (var i = 0; i < arraysToCombine.length; i++) {
			  curArray = arraysToCombine[i];
			  result.push(curArray[Math.floor(n / divisors[i]) % curArray.length]);
		   }    
		   return result;
		}
		
		var numPerms = arraysToCombine[0].length;
		for(var i = 1; i < arraysToCombine.length; i++) {
			numPerms *= arraysToCombine[i].length;
		}
		
		var combinations = [];
		for(var i = 0; i < numPerms; i++) {
			combinations.push( getPermutation(i, arraysToCombine));
		}
		return combinations;
	}

	/**
	 * sums up average rating of one single attribute
	 */
	function getAverageRating(id) {
		var ratings = getCompatibilityRatingsForAttribute(id);
		var v = 0;
		for (var i=0; i<ratings.length; i++) {
			// must cast to a Number here, otherwise JS builds a string
			v+= (+ratings[i].value);
		};
		return v/ratings.length;
	};

	function getCompatibilityRatingsForAttribute(id) {
		var ratings = [];
		angular.forEach($scope.compatibilities, function(c) {
			if (c.attr1.id == id || c.attr2.id == id) {
				ratings.push(c.rating);
			}
		});
		return ratings;
	};


	// set up environment on load
	$http.get("/api/problems").success(function(data) {

		$scope.parameters = data.problem.parameters;

		// contains problem properties and parameters with their attributes,
		// if present yet
		angular.forEach($scope.parameters, function(p) {
			$scope.attributes.push(p.attributes);
		});

		$http.get("/api/problems/" + window.PROBLEM_ID+ "/compatibilities").success(function(data) {
			$scope.compatibilities = data.compatibilities;
			$scope.allPossible();

		});

	
	});


}]);