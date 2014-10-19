morphBMC.controller("ExplorationController", ['$scope', '$http', '$filter', function($scope, $http) {

	// initial view template variables
	$scope.selected = [];
	$scope.attributes = [];
	$scope.good = [];
	$scope.ok = [];
	$scope.bad = [];

	$scope.compatibilities = [];

	/**
	 * calculates all attributes to which there is a "good" or "ok" compatibility
	 * all "bad" compatibilities will be ignored and faded out
	 */
	$scope.getCompatibleAttributes = function(id) {

		// UI stuff
		toggleSelect(id);

		// these hold the attributes
		var good = [], ok = [], bad = [];

		// holds all siblings of selected attributes
		// these will be ignored for suggestions 
		var siblings = [];

		angular.forEach($scope.selected, function(selected) {

			// remember all parameters of selected attributes
			siblings = siblings.concat(getSiblingAttributes(selected));
			// cross check all compatibilities
			for (var i=0; i<$scope.compatibilities.length; i++) {

				// shortcut
				var c = $scope.compatibilities[i];

				//console.log(c.attr1.id, c.attr2.id, selected);
				// grab compatibilities that include the current id

				if (c.attr1.id === selected || c.attr2.id === selected) {

					// grab the id of a possible attribute, the other ID of the
					// compatibility object is the currently walked ID
					var ratableId = c.attr1.id === selected ? c.attr2.id : c.attr1.id;

					// push to the corresponding array
					// precedence order is:
					// 
					// BAD > OK > GOOD
					var isBad = bad.indexOf(ratableId) > -1;
					var isOk = ok.indexOf(ratableId) > -1;
					var isGood = good.indexOf(ratableId) > -1;

					if (c.rating.value === 9 && !isGood && (!isOk && !isBad)) {
						good.push(ratableId);
					}
					else if (c.rating.value === 4 && !isOk && !isBad) {
						ok.push(ratableId);
					} else if (!isBad && c.rating.value === 1) {
						bad.push(ratableId);
					}
				}
			}

		});
		
		// siblings holds all the attributes that belong to a parameter of which the
		// user already has made a choice. so we don't need it 
		angular.forEach(siblings, function(s) {
			remove(good, s);
			remove(bad, s);
			remove(ok, s);
		});

		// bind to view
		$scope.good = good;
		$scope.bad = bad;
		$scope.ok = ok;
	};

	/**
	 * adds or removes a selected attribute to selected attributes
	 */
	var toggleSelect = function(id) {
		var siblings = getSiblingAttributes(id);
		// deselect all sibling attribtues of chosen attribtues
		// to only allow selection of one attribute per parameter
		angular.forEach(siblings, function(s) {
			remove($scope.selected, s);
		});

		// toggle element selection
		if ($scope.selected.indexOf(id) > -1) {
			remove($scope.selected, id);
			return;
		}

		$scope.selected.push(id);

	};

	/**
	 * returns attributes for a specific problem
	 * 
	 * @param id - the problem id
	 */
	$scope.getAttributesByParameter = function(id) {
		var attributes = [];
		angular.forEach($scope.parameters, function(p) {
			if (p.id === id) {
				attributes = attributes.concat(p.attributes);
			}
		});
		return attributes;
	};

	
	/**
	 * returns all rated compatibilities that include the given attribute
	 *
	 * @param id - the attribute id
	 */
	$scope.getCompatibilityRatingsForAttribute = function(id) {
		var ratings = [];
		angular.forEach($scope.compatibilities, function(c) {
			if (c.attr1.id == id || c.attr2.id == id) {
				ratings.push(c.rating);
			}
		});
		return ratings;
	};


	// TODO get adjacent attribtues!
	function getSiblingAttributes(id) {
		var p = getParameterByAttribute(id);
		var siblings = [];
		for (var i=0; i<p.attributes.length; i++) {
			var a = p.attributes[i];
			if (a.id === id) {
				continue;
			}
			siblings.push(a.id);
		}
		return siblings;
	};

	

	/**
	 * returns the parameter that a single attribute belongs to
	 * @param id - the attribute id
	 */
	function getParameterByAttribute(id) {
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