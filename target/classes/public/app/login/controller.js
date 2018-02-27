angular.module('login.controllers', [])
.controller('loginController', ['$scope', '$rootScope', '$window', '$state', 'loginService',
    function ($scope, $rootScope, $window, $state, loginService) {
        $scope.login = function(user) {
            loginService.login(user).then(function(response) {
                if (response.data !== null) {
                    $state.go('home');
                }
            })
        };

        $scope.register = function() {
            $state.go('register');
        }


    }]
);