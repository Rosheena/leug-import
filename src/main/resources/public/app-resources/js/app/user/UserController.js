LuegImportApp.controller('UserController', ['$rootScope', '$state', '$transitions', function ($rootScope, $state, $transitions) {

    let vm = this;

    vm.currentUser = {};
    vm.currentPage = '';

    vm.init = function () {
        vm.getCurrentPageHeader();
        vm.currentUser = $rootScope.current_user;
    };

    $transitions.onSuccess({}, function() {
        vm.getCurrentPageHeader();
    });

    vm.getUserDisplayName = function () {
        return vm.currentUser.firstName + ' ' + vm.currentUser.lastName;
    };

    vm.getCurrentPageHeader = function () {
        // In ui-router, state names are dot-separated. The parent state comes first.
        vm.currentPage = $state.current.name.split('.')[0];
    };

}]);