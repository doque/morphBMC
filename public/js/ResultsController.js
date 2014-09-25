morphBMC.controller("ResultsController", ['$scope', '$http', '$filter', function($scope, $http) {


	$scope.allPossible() = function() {

		var all = [];
		for (var i=0; i<$scope.parameters.length; i++) {
			var p = $scope.parameters[i];
			
		}

		// data structure:
		// [{
		// 		param1: attr2,
		// 		param2: attr1,
		// 		param3: attr4
		// }, {
		// 		...
		// }]
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