app.controller("CompatibilityService", ['$scope', '$http', function($scope, $http) {


	/**
	 * provides hover effect for parent tds
	 */
	var hover = function(hoverX, x, hoverY, y, redundant) {
		if (!redundant) {
			hoverX = x;
			hoverY = y;
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
	var getOffsets = function(parameters) {
		var offsets = [0];
		angular.forEach(parameters, function(p, i) {
			// offsets are calculated by adding the last value in the array
			// and the current parameter's attribute count
			offsets.push(offsets[offsets.length - 1] + p.attributes.length);
		});
		return offsets;	
	};

	/**
	 * returns all attributes from all problems
	 */
	var getAllAttributes = function(parameters) {
		var attributes = [];
		// merges all attributes from each parameter into one array
		angular.forEach(parameters, function(parameter, i) {
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
	var getParameterByAttribute = function(parameters, id) {
		// walk through parameters
		var param = null;
		angular.forEach(parameters, function(p) {
			// walk through all attributes of parameters
			angular.forEach(p.attributes, function(a) {
				if (param === null && a.id == id) {
					param = p;
				}
			});
		});
		return param;
	};

	return {
		hover: hover,
		getOffsets: getOffsets,
		getAllAttributes: getAllAttributes,
		getParameterByAttribute: getParameterByAttribute
	}

}]);