angular.module('register.controllers', [])
    .controller('registerController', ['$scope', '$rootScope', '$window', '$state', 'registerService',
        function ($scope, $rootScope, $window, $state, registerService) {
            $scope.register = function(user) {
                if ( user.password !== user.repeatPassword ) {
                    alert('Passwords do not match!');
                    return;
                }
                user.email = user.email + '@sparkmail.com';
                registerService.register(user).then(function() {
                    alert('Success! \nYou will now be redirected to login page.')
                    $state.go('login');
                }, function(response) {
                    $scope.user.email = $scope.user.email.split('@')[0];
                    alert(response.data);
                })
            }

        }]
    );