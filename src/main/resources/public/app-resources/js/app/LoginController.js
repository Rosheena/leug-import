LuegImportApp.controller('LoginController',
    function ($scope, $rootScope, $q, $http, AuthenticationService){

        $scope.username = null;
        $scope.password = null;
        $scope.error = null;

        $scope.init = function (){
            AuthenticationService.clearAuthorization();
            $rootScope.current_user = null;
        };

        $scope.login = function () {
            $scope.error = null;

            AuthenticationService.authenticate($scope.username, $scope.password)
                .then(
                    function () {
                        console.log("to home")
                        $rootScope.navigateTo('/home')
                    },
                    function (err) {
                        $scope.error = err;
                    }
                )
        }
    });