morphBMC.controller("CompatibilityController", ['$scope', '$http', function($scope, $http) {

	$scope.addCompatibility() = function(attributes, rating) {
		// attributes contains two attribute ids
		if (attributes.length !== 2) {
			console.log("invalid attribute parameter, length is %d", attributes.length);
			return;
		}
		$http.post("/api/compatibility", {
		});
	};

	// grab all existing parameters to build table
	$http.get("/api/problems/" + problemId).success(function(data) {
		// contains problem properties and parameters with their attributes,
		// if present yet
		$scope.parameters = data.parameters;
	});
	// then load all present compatibilities
	$http.get("/api/problems/" + problemId + "/compatibilities").success(function(data) {
		$scope.compatibilities = data.compatibilities;
	});

}]);

