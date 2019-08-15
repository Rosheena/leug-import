var LuegImportApp =
    angular
        .module("LuegImportApp", ['ui.router', 'ngSanitize', 'ui.bootstrap', 'ngMessages', 'ui-notification', 'ngTable', 'angularFileUpload'])
        .config(
            function ($httpProvider, $qProvider){
                $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
                $httpProvider.interceptors.push(
                    function ($q, $state, $templateCache) {
                        return {
                            'request': function (config) {
                                if($templateCache.get(config.url)){
                                    return config;
                                }

                                config.url = $CONTEXT_PATH$ + config.url;
                                return config;
                            },
                            'responseError': function (rejection) {
                                if(rejection && rejection.status === 401){
                                    $state.go('login');
                                }
                                return $q.reject(rejection);
                            }
                        }
                    }
                );

                $qProvider.errorOnUnhandledRejections(false);
            }
        );

LuegImportApp.run(
    function ($rootScope, $state, AuthenticationService) {
        $rootScope.navigateTo = function (stateName) {
            $state.go(stateName);
        };

        AuthenticationService.registerObserverCallback(
            function (user) {
                $rootScope.current_user = user;
            }
        );

        AuthenticationService.setupAuthorization();
    }
);

/**
 @param {function(T, number, obj): boolean} predicate
 @param value
 */
Array.prototype.replace = function (predicate, value) {
    let index = this.findIndex(predicate);
    if (index >= 0) {
        this[index] = value;
    }
};