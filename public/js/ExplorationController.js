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

	// set up environment on load
	$http.get("/api/problems").success(function(data) {
		// contains problem properties and parameters with their attributes,
		// if present yet
		$scope.parameters = data.problem.parameters;
	});

}]);