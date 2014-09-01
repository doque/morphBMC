morphBMC.controller("DefinitionController", ['$scope', '$http', function($scope, $http) {

	$scope.problemId = null;

	$scope.addAttribute = function(parameter) {

		if (parameter.attribute.name.length === 0) {
			parameter.addingAttribute = false;
			return;
		}

		$http.post("/api/parameters/"+parameter.id+"/attributes", {
			"name": parameter.attribute.name
		}).success(function(data) {
			parameter.attributes.unshift(data.attribute);
			parameter.addingAttribute = false;
			parameter.attribute.name = "";
		});
	};


	$scope.addParameter = function() {

		// sanity check
		if ($scope.parameter.name.length === 0) {
			// no value given
			$scope.addingParameter = false;
			return;
		}

		$http.post("/api/parameters", {
			"name": $scope.parameter.name
		}).success(function(data) {
			$scope.addingParameter = false;
			// reset input field model
			$scope.parameter.name = "";
			$scope.parameters.push(data.parameter);
		});
	};

	// grab from json

	
	$scope.parameters = [
		{
			id: 20,
			userid: "abc",
			name: "Distance",
			attributes: [
				{
					id: 1,
					userid: "def",
					name: "500km"
				},
				{
					id: 2,
					userid: "def",
					name: "100km"
				}
			]
		}, 
		{
			id: 100,
			userid: "def",
			name: "Kosten",
			attributes: [
				{
					id: 3,
					userid: "def",
					name: "500EUR"
				},
				{
					id: 4,
					userid: "def",
					name: "100EUR"
				}
			]
		}
	];
}]);