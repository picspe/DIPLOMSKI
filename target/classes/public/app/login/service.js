angular.module('login.services',[])
.service('loginService', ['$http',
    function($http){
        var path = "/user";
        this.login = function(user) {
            return $http.post(path + "/login", user);
        }
}]);