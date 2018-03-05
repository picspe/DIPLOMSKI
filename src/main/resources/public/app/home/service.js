angular.module('home.services',[])
.service('homeService', ['$http',
    function($http){
        var path = "/user";
        var msgPath  = "/message";
        this.logout = function() {
            return $http.get(path + "/logout");
        };

        this.isLoggedIn = function() {
            return $http.get(path + "/isLoggedIn");
        };

        this.send = function(mail) {
            return $http.post(path + "/send",mail);
        };

        this.forward = function(mail) {
            return $http.post(msgPath + "/forward");
        };

        this.reply = function(mail) {
            return $http.post(msgPath + "/reply");
        };

        this.getInbox = function() {
            return $http.get(msgPath + "/inbox");
        };

        this.getOutbox = function() {
            return $http.get(msgPath + "/outbox");
        };

        this.getTopTen = function(username) {
            return $http.get(path + "/search/" + username);
        };

        this.seen = function(message) {
            return $http.post(msgPath + "/seen", message);
        };

        this.delete = function(message) {
            return $http.post(msgPath + "/delete", message);
        }

}]);