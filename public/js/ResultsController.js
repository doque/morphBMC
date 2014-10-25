app.controller("ResultsController", ['$scope', '$http', function($scope, $http) {

	$scope.parameters = [];
	$scope.attributes = [];
	$scope.configurations = [];

	$scope.rendering = true;

	$scope.mediumConsistency = 0;
	$scope.betterThanMedium = 0;

	/**
	 * creates nice complex objects that contain all possible configurations
	 * @return {array} array of configuration objects
	 */
	$scope.allPossible = function() {
		// only holds IDs of attributes, but no further information
		// in order to display this nicely, we'll have to build a complex object
		var all = getAllCombinations($scope.attributes);
		// holds all configurations, will be assigned to scope below
		var configurations = [];

		// walk all permutations
		angular.forEach(all, function(permutation) {

			// build configuration object here
			var configuration = {
				consistencyValue: 0
			};
			// walk all attributes of that configuration,
			// assign to appropriate parameter
			var sum = 0;

			for (var i=0; i<permutation.length; i++) {
				for (var x=0; x<permutation.length; x++) {
					if (permutation[i].id === permutation[x].id) continue;
					var ratings = getCompatibilityRatingsForAttributes(permutation[x].id, permutation[i].id);
					sum += ratings.reduce(function(attr1, attr2) {
						return attr1 + (+attr2.value);
					}, 0);
				}
				var attribute = permutation[i], param = getParameterByAttribute(attribute.id);
				// each configuration holds an array of parameters,
				// their id and name
				// and the chosen attribute for that parameter
				configuration[param.name] = attribute.name;
			}
			// calculate average rating value
			configuration.consistencyValue = sum/permutation.length;

			// push it
			configurations.push(configuration);
		});

		// calculate average consistency of all configurations
		$scope.mediumConsistency = configurations.reduce(function(a,b) {
			return a + +b.consistencyValue;
		}, 0) / configurations.length;

		// how many configurations have above-average consistency?
		$scope.betterThanMedium = configurations.reduce(function(a,b) {
			if (b.consistencyValue > $scope.mediumConsistency) return a+ +1;
			return a+ +0;
		}, 0);

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
	 * returns all compatibilities for a pair of attributes
	 */
	function getCompatibilityRatingsForAttributes(x, y) {
		var ratings = [];
		angular.forEach($scope.compatibilities, function(c) {
			if ((c.attr1.id === x && c.attr2.id === y) ||
				(c.attr1.id === y && c.attr2.id === x)) {
				ratings.push(c.rating);
			}
		});
		return ratings;
	};

	// set up environment on load
	$http.get("/api/problems/"+window.PROBLEM_ID).success(function(data) {

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



function Configuration(attributes) {
	this.attributes = attributes || [];
	this.consistencyValue = 0;
	this.calculateConsistency();

	function calculateConsistency() {
		var consistency = 0;
		angular.foreach(this.attributes, function(attr) {
			var ratings = getCompatibilityRatingsForAttribute(attr.id);
			consistency = ratings.reduce(function(attr1, attr2) {
				return attr1 + (+attr2.value);
			}, 0);
		});
		this.consistencyValue = consistency;
	}

	

	return {
		consistencyValue: consistencyValue
	};
}
