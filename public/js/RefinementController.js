app.controller("RefinementController", ['$scope', '$http', 'SocketService',
	function($scope, $http, SocketsService) {

		$scope.dragOptions = {
			handle: 'h5',
			hoverClass: 'drophover',
			opacity: .5,
			revert: true
		};



		/**
		 * send a POST request to add a new attribute
		 * @param {[type]} parameter which parameter the attribute belongs to
		 */
		$scope.addAttribute = function(parameter, attribute) {

			if (!attribute || !attribute.name) return;

			$http.post("/api/problems/" + window.PROBLEM_ID + "/parameters/" + parameter.id + "/attributes", attribute).success(function(data) {
				parameter.attributes.unshift(data.attribute);
				attribute.name = "";
			});
		};

		$scope.mergeAttributes = function(from, to) {
			$http.post("/api/problems/" + window.PROBLEM_ID + "/parameters/merge", { "from": from, "to": to }).success(function() {
				// meh
			});
		};

		// listen for incoming socket events, refresh definitions
		$scope.$on('DEFINITION_UPDATED', function(event, args) {
			$http.get("/api/problems/" + window.PROBLEM_ID + "/parameters?all=yes").success(function(data) {
				$scope.parameters = data.parameters;
			});
		});


		/**
		 * callback function for dropping a dragged parameter
		 */
		$scope.dropped = function(evt, ui) {
			angular.element(ui.draggable).hide();
			var oldParam = angular.element(ui.draggable).scope().parameter;
			var target = angular.element(evt.target).scope().parameter;
			$scope.mergeAttributes(oldParam.id, target.id);
		};

		/**
		 * send a post request to add a new parameter
		 */
		$scope.addParameter = function() {
			if (!$scope.parameter) return;
			// sanity check
			if ($scope.parameter.name.length === 0) {
				// no value given
				$scope.addingParameter = false;
				return;
			}

			$http.post("/api/problems/" + window.PROBLEM_ID + "/parameters", {
				"name": $scope.parameter.name
			}).success(function(data) {
				$scope.addingParameter = false;
				// reset input field model
				$scope.parameter.name = "";
				data.parameter.addingAttribute = true;
				$scope.parameters.push(data.parameter);
			});
		};

		/**
		 * sends a DELETE request to remove a parameter
		 * and kicks it from the $scope
		 */
		$scope.removeParameter = function(parameter)  {
			$http.delete("/api/problems/" + window.PROBLEM_ID + "/parameters/" + parameter.id).success(function() {
				// remove from scope
				angular.forEach($scope.parameters, function(p, index) {
					if (p.id === parameter.id) {
						$scope.parameters.splice(index, 1);
						return;
					}
				});

			});
		};

		/**
		 * sends a DELETE request to remove an attribute
		 * and kicks it from the $scope
		 */
		$scope.removeAttribute = function(parameter, attribute)  {
			$http.delete("/api/problems/" + window.PROBLEM_ID + "/parameters/attributes/" + attribute.id).success(function() {
				// remove from scope
				angular.forEach(parameter.attributes, function(att, index) {
					if (att.id === attribute.id) {
						parameter.attributes.splice(index, 1);
						return;
					}
				});

			});
		};


		// set up environment on load
		$http.get("/api/problems/" + window.PROBLEM_ID + "/parameters?all=yes").success(function(data) {
			$scope.parameters = data.parameters;
			$scope.userId = window.USER_ID;
		});

	}
]);