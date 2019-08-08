LuegImportApp.controller('LoginController', ['$rootScope', '$state', '$q', '$http', 'AuthenticationService', function ($rootScope, $state, $q, $http, AuthenticationService, RolesService) {

    let vm = this;

    vm.username = null;
    vm.password = null;
    vm.error = null;

    vm.init = function () {
        AuthenticationService.clearAuthorization();
    };

    vm.login = function () {
        vm.error = null;

        AuthenticationService.authenticate(vm.username, vm.password)
            .then(
                function (user) {

                    switch (user.authority) {
                        // these are the default states for each role when they login
                        case RolesService.roles.gama :
                            $state.go('gama.managePilots');
                            break;
                        case RolesService.roles.tmc :
                            console.log("in tmc, going to tmc")
                            $state.go('tmc.managePilots');
                            break;
                        case RolesService.roles.super :
                            $state.go('gama.managePilots');
                            break;
                        default :
                            $state.go('login');
                    }

                },
                function (err) {
                    vm.error = err;
                }
            )
    }
}]);

