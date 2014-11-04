// define my own module with required modules
var app = angular.module('morphBMC', ['ngRoute', 'tableSort', 'textAngular', 'ngDragDrop']).config(
	function ($routeProvider, $httpProvider) {
		// enable strict mode for JS parser
		'use strict';

		// Route for Definition
		$routeProvider.when('/statement', {
			templateUrl: '/assets/views/statement.html',
			controller: 'ProblemController',
		});

		// Route for Definition
		$routeProvider.when('/definition', {
			templateUrl: '/assets/views/definition.html',
			controller: 'DefinitionController',
		});

		// Route for Refinement
		$routeProvider.when('/refinement', {
			templateUrl: '/assets/views/refinement.html',
			controller: 'RefinementController',
		});

		// Compatibility
		$routeProvider.when('/compatibility', {
			templateUrl: '/assets/views/compatibility.html',
			controller: 'CompatibilityController'
		});

		// Route for Conflict Resolution
		$routeProvider.when('/resolution', {
			templateUrl: '/assets/views/resolution.html',
			controller: 'ResolutionController',
		});

		// Exploration
		$routeProvider.when('/exploration', {
			templateUrl: '/assets/views/exploration.html',
			controller: 'ExplorationController'
		})

		// Results
		$routeProvider.when('/results', {
			templateUrl: '/assets/views/results.html',
			controller: 'ResultsController'
		})


		// default route
		$routeProvider.otherwise({redirectTo: '/statement'});

		// since we're working with json for data transpoert, add the appropriate content type header to all $http requests
		$httpProvider.defaults.headers.post["Content-Type"] = "application/json";
	},
	{
		// dependency injection
		$inject: ['$routeProvider', '$httpProvider']
	}
/**
 * directive to set focus on an element based on a condition
 */
).directive('autofocus', function($timeout) {
	return {
		restrict: "A",
		link : function($scope, $element, $attr) {
			$scope.$watch($attr.autofocus, function(_focusVal) {
				$timeout(function() {
					if (_focusVal) $element.focus();
				});
			});
		}
	}
/**
 * directive to be fired after an ng-repeat is finished rendering
 */
}).directive('onFinishRender', function($timeout) {
	return {
		restrict: "A",
		link: function($scope, $element, $attr) {
			if ($scope.$last === true) {
				// manually grab the right scope here.
				var _scope = angular.element($(".content")).scope();
				_scope.$eval($attr.onFinishRender);
			}
		}
	}
/**
 * more elegant way to instantiate Bootstrap's tooltips
 */
}).directive('tooltip', function(){
    return {
        link: function(scope, element, attrs) {
            $(element).hover(function() {
                // on mouseenter
                $(element).tooltip('show');
            }, function(){
                // on mouseleave
                $(element).tooltip('hide');
            });
        }
    };
/**
 * catches RETURN keypress and evaluates attribute
 */
}).directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.ngEnter);
                });
                event.preventDefault();
            }
        });
    };
/**
 * catches ESCAPE keypress and evaluates attribute
 */
}).directive('ngEsc', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 27) {
                scope.$apply(function (){
                    scope.$eval(attrs.ngEsc);
                });
                event.preventDefault();
            }
        });
    };
/*
 * creates a popover for a designated element
 */
}).directive('popover', ['$compile', function($compile, $timeout) {

	// TODO inject some form if ID for this popover
	var template = '<div class="popover right">\
				      <div class="arrow"></div>\
				      <h3 class="popover-title">\
				      	{{ x.name }} and {{ y.name }}\
				      	<button\
							onclick="$(\'.popover\').remove()"\
							type="button" class="close">\
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>\
					</button>\
				      </h3>\
				      <div class="popover-content">\
				      	{{ conflicts.length }} conflicted ratings:\
				        <div class="conflicted-rating" ng-repeat="c in conflicts">\
				        	<span class="rating-{{ c.rating.name.toLowerCase() }}">{{c.rating.name}}</span>\
				        	by #{{c.userId}}\
				        </div>\
				        <br/>\
				        <div ng-init="override={ attr1: {id:x.id,name:x.name}, attr2: {name: y.name,id:y.id} }">\
				        	Choose below to override these ratings:\
							<div class="input-group">\
								<span class="input-group-addon">\
									<select ng-model="override.rating"\
											ng-options="rating.name for rating in ratings track by rating.id">\
									</select>\
							  	</span>\
							  	<input type="text" class="form-control" ng-model="override.overrideComment"\
							  		placeholder="explain your rating..."/>\
							</div>\
							<br/>\
							<div>\
								<button type="button" ng-click="overrideCompatibilities(override);" class="btn btn-sm btn-primary">\
									Save\
								</button>\
							</div>\
			        	</div>\
				      </div>\
				    </div>';

    return {

    	link: function(scope, element, attrs) {


    		element.on("click", function(e) {

				scope.$apply(function() {

					// remove all other popovers
					$(".popover").remove();

					var html = $compile(template)(scope);
					$(element).parent().append(html);
					var el = $(element).parent().find(".popover").show(),
					height = el.height(),
					width = el.width();

					el.css({
						position: "absolute",
                        left: $(element).parent().position().left + 55,
                        top: $(element).parent().position().top - height/2 - 25,
                        container: "body"
					});
				});

			});
	    }
    	
    };

}]);