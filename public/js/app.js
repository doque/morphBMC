// define my own module with required modules
var morphBMC = angular.module('morphBMC', ['ngRoute']).config(
    function ($routeProvider, $httpProvider) {
    	// enable strict mode for JS parser
        'use strict';

        // Route for Definition
        $routeProvider.when('/definition', {
        	templateUrl: '/assets/views/definition.html',
        	controller: 'DefinitionController',
        });

        // Compatibility
        $routeProvider.when('/compatibility', {
        	templateUrl: '/assets/views/compatibility.html',
        	controller: 'CompatibilityController'
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
).directive('autofocus', function($timeout) {
    return {
        link : function($scope,$element,$attr) {
            $scope.$watch($attr.autofocus, function(_focusVal) {
                $timeout(function() {
                    if (_focusVal) $element.focus();
                });
            });
        }
    }
});