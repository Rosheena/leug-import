LuegImportApp
    .factory("URLSecurity", function ($q, $location, AuthenticationService) {
        return {
            is_authenticated: function () {
                var deferred = $q.defer();

                AuthenticationService.validateAuthorization()
                    .then( function () {
                        deferred.resolve();

                    }, function () {
                        deferred.reject()

                        $location.path('/login')
                    });
                return deferred.promise;
            }
        };
    })
    .config(
        function ($routeProvider) {
            $routeProvider
                .when('/', {
                    templateUrl: 'app-resources/view/home.html',
                    controller: 'HomeController',
                    resolve: {
                        loggedIn: function (URLSecurity) {
                            return URLSecurity.is_authenticated();
                        }
                    }
                })
                .when('/login', {
                    templateUrl: 'app-resources/view/login.html',
                    controller: 'LoginController',
                    name: 'login'
                })

                .when('/logout', {redirectTo: '/login'})
                .otherwise({redirectTo: "/"});
        });

LuegImportApp.run(
    function ($rootScope, $location, AuthenticationService) {
        $rootScope.navigateTo = function (view) {
            $location.path(view);
        };

        AuthenticationService.registerObserverCallback(
            function (user) {
                $rootScope.current_user = user;
            }
        );

        AuthenticationService.setupAuthorization();
    }
);