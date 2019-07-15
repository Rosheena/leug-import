var LuegImportApp =
    angular
        .module("LuegImportApp", ['ngRoute', 'ngSanitize', 'ui.bootstrap', 'ngTable', 'angularFileUpload'])
        .config(
            function ($httpProvider, $qProvider){
                $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
                $httpProvider.interceptors.push(
                    function ($q, $location, $templateCache) {
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
                                    $location.path('/login')
                                }
                                return $q.reject(rejection);
                            }
                        }
                    }
                );

                $qProvider.errorOnUnhandledRejections(false);
            }
        );