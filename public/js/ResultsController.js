morphBMC.controller("ResultsController", ['$scope', '$http', '$filter', function($scope, $http) {

	$scope.parameters = [];
	$scope.attributes = [];
	$scope.configurations = [];

	$scope.allPossible = function() {
		$scope.configurations = getAllCombinations($scope.attributes);

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
	        combinations.push(getPermutation(i, arraysToCombine));
	    }
	    return combinations;
	}


	// set up environment on load
	$http.get("/api/problems").success(function(data) {
		// contains problem properties and parameters with their attributes,
		// if present yet
		$scope.parameters = data.problem.parameters;
		angular.forEach($scope.parameters, function(p) {
			$scope.attributes.push(p.attributes);
		});
		$scope.allPossible();

		$http.get("/api/problems/" + window.PROBLEM_ID+ "/compatibilities").success(function(data) {
			$scope.compatibilities = data.compatibilities;
		});
	});

}]);