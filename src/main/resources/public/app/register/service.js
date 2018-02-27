angular.module('register.services', [])
.service('registerService', ['$http',
    function ($http) {
    var path = "/user";
    this.register = function(user) {
        return $http.post(path + '/register', user);
    }
}]);