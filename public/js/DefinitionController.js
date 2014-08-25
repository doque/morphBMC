morphBMC.controller("DefinitionController", ['$scope', '$http', function($scope, $http) {

	

	$scope.addParameter = function() {
		$http.post("/api/parameters", {
			"name": $scope.parameter.name
		}).success(function(data) {
			$scope.plusClicked = false;
			// reset input field model
			$scope.parameter = {};
			$scope.parameters.push(data.parameter);
		});
	};

	$scope.parameters = [
		{
			id: 0,
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
			id: 0,
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