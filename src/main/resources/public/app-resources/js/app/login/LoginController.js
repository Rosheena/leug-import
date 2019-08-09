LuegImportApp.controller('LoginController', ['$rootScope', '$state', '$q', '$http', 'AuthenticationService', function ($rootScope, $state, $q, $http, AuthenticationService) {

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
                    $state.go('document-upload.manage');
                },
                function (err) {
                    vm.error = err;
                }
            )
    }
}]);

