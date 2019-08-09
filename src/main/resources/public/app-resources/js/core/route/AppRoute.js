LuegImportApp
    .config(
        function ($stateProvider, $urlRouterProvider) {

            let authResolver = function (permittedRoles) {
                return {
                    loggedIn: ['URLSecurity', function (URLSecurity) {
                        return URLSecurity.is_authenticated(permittedRoles);
                    }]
                }
            };

            $stateProvider
                .state({
                    name: 'login',
                    url: '/login',
                    controller: 'LoginController',
                    controllerAs: 'loginCtrl',
                    templateUrl: 'app-resources/views/login/login.html'
                })
                .state({
                    abstract:true,
                    name: 'gama',
                    url: '/gama',
                    templateUrl: 'partner/app-resources/views/gama/gama.html',
                    resolve: authResolver(['ROLE_SUPER', 'ROLE_GAMA'])
                })
                .state({
                    name: 'gama.addPilot',
                    url: '/addPilot',
                    controller: 'GamaAddPilotController',
                    controllerAs: 'addPilotCtrl',
                    templateUrl: 'partner/app-resources/views/gama/gama-add-pilot.html'
                })
                .state({
                    name: 'gama.managePilots',
                    url: '/managePilots',
                    controller: 'GamaManagePilotsController',
                    controllerAs: 'managePilotsCtrl',
                    templateUrl: 'partner/app-resources/views/gama/gama-manage-pilots.html'
                })
                .state({
                    name: 'gama.uploadPilots',
                    url: '/uploadPilots',
                    controller: 'GamaUploadPilotsController',
                    controllerAs: 'uploadPilotsCtrl',
                    templateUrl: 'partner/app-resources/views/gama/gama-upload-pilots.html'
                })
                .state({
                    abstract:true,
                    name: 'tmc',
                    url: '/tmc',
                    templateUrl: 'partner/app-resources/views/tmc/tmc.html',
                    resolve: authResolver(['ROLE_SUPER', 'ROLE_TMC'])
                })
                .state({
                    name: 'tmc.addPilot',
                    url: '/add-pilot',
                    controller: 'TmcAddPilotController',
                    controllerAs: 'tmcAddPilotCtrl',
                    templateUrl: 'partner/app-resources/views/tmc/tmc-add-pilot.html'
                })
                .state({
                    name: 'tmc.managePilots',
                    url: '/manage-pilots',
                    controller: 'TmcManagePilotsController',
                    controllerAs: 'tmcManagePilotsCtrl',
                    templateUrl: 'partner/app-resources/views/tmc/tmc-manage-pilots.html'
                })
                .state({
                    name: 'tmc.uploadPilots',
                    url: '/upload-pilots',
                    controller: 'TmcUploadPilotsController',
                    controllerAs: 'tmcUploadPilotsCtrl',
                    templateUrl: 'partner/app-resources/views/tmc/tmc-upload-pilots.html'
                });

            // For any unmatched url, send to login page
            $urlRouterProvider.otherwise("/login");
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

