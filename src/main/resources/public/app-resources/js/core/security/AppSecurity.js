LuegImportApp.factory("URLSecurity", ['$q', '$state', 'AuthenticationService', function ($q, $state, AuthenticationService) {
    return {
        is_authenticated: function () {
            var deferred = $q.defer();

            AuthenticationService.validateAuthorization()
                .then(function () {
                    deferred.resolve();

                }, function () {
                    deferred.reject();
                    $state.go('/login');
                });

            return deferred.promise;
        }
    };
}]);