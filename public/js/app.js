// define my own module with required modules
var app = angular.module('morphBMC', ['ngRoute', 'tableSort']).config(
	function ($routeProvider, $httpProvider) {
		// enable strict mode for JS parser
		'use strict';

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
		$routeProvider.otherwise({redirectTo: '/definition'});

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
                    scope.$eval(attrs.ngEnter);
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
	var template = '<div class="popover top">'+
				      '<div class="arrow"></div>'+
				      '<h3 class="popover-title">{{ x.name }} and {{ y.name }}</h3>'+
				      '<div class="popover-content">'+
				      	'{{ conflicts.length }} were found:'+
				        '<div ng-repeat="c in conflicts">'+
				        	'<pre>{{c}}</pre>'+
				        '</div>'+
				      '</div>'+
				    '</div>';

    return {

    	link: function(scope, element, attrs) {
    		
    		element.on("click", function(e) {

				scope.$apply(function() {
					var tpl = $compile(template)(scope);
					// grab by ID
					// show element here
					$(tpl).show();
					console.log(tpl);
				});

			});


    		/*helpers*/

	    	function getLeftLocation(e, element) {
                var mouseWidth = e.pageX;
                var pageWidth = $(window).width();
                var menuWidth = element.width();
                
                // opening menu would pass the side of the page
                if (mouseWidth + menuWidth > pageWidth &&
                    menuWidth < mouseWidth) {
                    return mouseWidth - menuWidth;
                } 
                return mouseWidth;
            }        
            
            function getTopLocation(e, element) {
                var mouseHeight = e.pageY;
                var pageHeight = $(window).height();
                var menuHeight = element.height();

                // opening menu would pass the bottom of the page
                if (mouseHeight + menuHeight > pageHeight &&
                    menuHeight < mouseHeight) {
                    return mouseHeight - menuHeight;
                } 
                return mouseHeight;
            }

	    }
    	
    };
}]);