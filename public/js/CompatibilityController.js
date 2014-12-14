app.controller("CompatibilityController", ['$scope', '$http', '$timeout',
	function($scope, $http, $timeout) {

		$scope.adding = false;
		$scope.compatibilities = null;

		$scope.batch = [];

		$scope.hoverX = 0;
		$scope.hoverY = 0;

		$scope.rendering = true;
		$scope.remaining = [];

		
		$scope.$on('REMAINING_COMPATIBILITIES', function(event, remaining) {
			$scope.remaining = remaining;
		});

		/**
		 * calculates compatibility pairs that haven't been rated yet by anyone.
		 * @param  {[type]}  a1 [description]
		 * @param  {[type]}  a2 [description]
		 * @return {Boolean}    [description]
		 */
		$scope.isRemaining = function(a1, a2) {
			var is = false;
			angular.forEach($scope.remaining, function(arr) {
				if (arr.indexOf(a1) > -1 && arr.indexOf(a2) > -1) {
					is = true;
					return;
				}
			});

			return is;
		};

		/**
		 * saves a compatibility
		 * @param {compatiblity) the compatibility object
		 * @param reload whether the http response should be applied to $scope immediately. only used for single adding
		 */
		$scope.addCompatibility = function(compatibility, reload) {
			$http.post("/api/problems/" + window.PROBLEM_ID + "/compatibilities", compatibility).success(function(data) {
				// after receiving problem id, load existing compatibilities
				if (reload !== false) {
					$scope.compatibilities = data.compatibilities;
				}
			});
		};

		/**
		 * rates a number of compatibilities at once
		 * @param  array compatibilities the compatibiltiies
		 *
		 */
		$scope.rateBatch = function(batchrating) {
			// yeah this is a hack but
			// http://stackoverflow.com/questions/26616448/angularjs-view-doesnt-update-when-assigning-a-http-response-to-scope
			angular.forEach($scope.compatibilities, function(c) {
				if ($scope.batch.indexOf(c.id) > -1) {
					c.rating = batchrating;
					c.userId = $scope.userI
					// dont bind rest response here, do it below and only once.
					$scope.addCompatibility(c, false);
				}
			});

			$http.get("/api/problems/" + window.PROBLEM_ID + "/compatibilities").success(function(data) {
				$scope.compatibilities = data.compatibilities;
			});

			$scope.batch = [];
			$('#batchrating').blur();

		};

		/**
		 * adds a compatiblity to the batch
		 * if it is already present  it will be ignored
		 */
		$scope.addToBatch = function(id, xOrY) {
			angular.forEach($scope.compatibilities, function(c) {
				
				var attr = xOrY === 'x' ? c.attr2 : c.attr1;

				if (attr.id === id) {

					if ($scope.isInBatch(c.id) === false && c.rating.value === 0) {
						$scope.batch.push(c.id);

					} else {
						remove($scope.batch, c.id);
					}
				} 
			});
		};

		/**
		 * determines if a compatibility is in the batch
		 */
		$scope.isInBatch = function(id) {
			return $scope.batch.indexOf(id) > -1;
		}

		/**
		 * provides hover effect for parent tds
		 */
		$scope.hover = function(x, y, redundant) {
			if (!redundant) {
				$scope.hoverX = x;
				$scope.hoverY = y;
			}
		};

		/**
		 * calculates the offsets of parameter arributes
		 * needed for rowspan calculation on compatibility table
		 * e.g. if first parameter has 2 attributes and second has
		 * 4, the offset array will be [0, 2, 6]
		 * during iteration of <tr> elements, only everytime $index is in offset,
		 * a td with large rowspan is rendered since that is enough for all attributes
		 * @return {array}
		 */
		$scope.getOffsets = function() {
			var offsets = [0];
			angular.forEach($scope.parameters, function(p, i) {
				// offsets are calculated by adding the last value in the array
				// and the current parameter's attribute count
				offsets.push(offsets[offsets.length - 1] + p.attributes.length);
			});
			return offsets;
		};

		/**
		 * returns all attributes from all problems
		 */
		$scope.getAllAttributes = function() {
			var attributes = [];
			// merges all attributes from each parameter into one array
			angular.forEach($scope.parameters, function(parameter, i) {
				if (parameter.attributes.length) {
					//console.log("adding", parameter.attributes)
					attributes = attributes.concat(parameter.attributes);
				}
			});

			return attributes;
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


		/**
		 * get compatibility for a pair of attributes
		 */
		$scope.getCompatibilityRating = function(attr1, attr2) {
			var _c = null;
			angular.forEach($scope.compatibilities, function(c) {
				// cant break out of foreach
				if (_c === null) {
					// arrays should be the same
					if ((attr1 === c.attr1.id && attr2 === c.attr2.id) || (attr2 === c.attr1.id && attr1 === c.attr2.id)) {
						_c = c;
					}
				}

			});
			return _c;
		};

		/**
		 * helper function that removes an int from an array
		 */
		function remove(arr, int) {
			if (arr.indexOf(int) >= 0) {
				if (arr.length === 1) {
					arr.pop();
				}
				arr.splice(arr.indexOf(int), 1);
			}
			return arr;
		}


		// grab all existing parameters to build table
		$http.get("/api/problems/" + window.PROBLEM_ID + "/parameters?all=yes").success(function(data) {
			// contains problem properties and parameters with their attributes,
			// if present yet
			$scope.parameters = data.parameters;

			// after receiving problem id, load existing compatibilities
			$http.get("/api/problems/" + window.PROBLEM_ID + "/compatibilities").success(function(data) {
				$scope.compatibilities = data.compatibilities;
			});
		});

		$http.get("/api/ratings").success(function(data) {
			$scope.ratings = data.ratings;
			data.ratings.shift();
			$scope.batchRatings = data.ratings;
		});

	}
]);