angular.module("app", ['ui.router',
    'app.controllers', 'login.controllers', 'login.services',
    'register.controllers','register.services',
    'home.controllers' ,'home.services'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/login');

        $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: 'app/login/index.html',
                controller: 'loginController'
            })
            .state('register', {
                url: '/register',
                templateUrl: 'app/register/index.html',
                controller: 'registerController'
            })
            .state('home', {
                url: '/home',
                templateUrl: 'app/home/index.html',
                controller: 'homeController'
            });
    });