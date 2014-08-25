var morphBMC = angular.module('morphBMC', [
    'ngRoute',
]).config(
    function ($routeProvider, $httpProvider) {
        'use strict';
        //$routeProvider.when('/login', {templateUrl: 'view/login.html', controller: 'UserController'});
        $routeProvider.when('/define', {templateUrl: '/assets/views/definition.html', controller: 'DefinitionController'});
        $routeProvider.otherwise({redirectTo: '/define'});
        
        $httpProvider.defaults.headers.post["Content-Type"] = "application/json";
    },
    { $inject: ['$routeProvider', '$httpProvider'] }
);