angular.module('app.controllers', [])
    .controller('appController', ['$http', '$scope', '$rootScope', '$window', '$state',
        function ($http, $scope, $rootScope, $window, $state) {

            $http.get('user/isLoggedIn').then(function(){}, function() {
                $state.go('login');
            });

            $scope.logout = function () {
                $http.get("user/logout").then(function() {
                    $state.go('login');
                });
            }

        }]
    );