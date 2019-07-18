LuegImportApp.service('AuthenticationService',
    function ($http, $rootScope, $q, StorageService) {
        const AUTHORIZATION = "AUTHORIZATION";
        var observerCallbacks = [];

        this.registerObserverCallback = function (callback) {
            observerCallbacks.push(callback);
        };

        this.setupAuthorization = function (){
            var authorization = getAuthorization();

            if(angular.isString(authorization)){
                $http.defaults.headers.common.Authorization = authorization;
            }
        };
        
        this.clearAuthorization = function () {
            delete $http.defaults.headers.common.Authorization;

            StorageService.removeItem(AUTHORIZATION)
        };

        this.authenticate = function (username, password) {
            var _this = this;

            _this.clearAuthorization();

            var authorization = "Basic " + btoa(username + ":" + password);

            return $q(
                function (resolve, reject) {
                    $http.get("api/me", {
                        headers: {
                            Authorization: authorization
                        }
                    })
                        .then(function (payload) {
                            var user = payload.data;

                            console.log('User ' + user.username + ' is authenticated');

                            StorageService.setItem(AUTHORIZATION, authorization);

                            _this.setupAuthorization();

                            notify
                        })
                }
            )
        };

        this.validateAuthorization = function () {
            var _this = this;

            return $q(
                function (resolve, reject) {
                    var authorization = getAuthorization();

                    if(angular.isString(authorization)){
                        $http.get("api/me")
                            .then(function (payload) {
                                var user = payload.data;

                                console.log('User ' + user.username + ' is authenticated');

                                notifyObservers(user);

                                resolve(user);

                            }, function (err) {
                                console.log(err);

                                _this.clearAuthorization();

                                reject();
                            });
                    } else {
                        reject();
                    }
                }
            );

        };

        var getAuthorization = function () {
            return StorageService.getItem(AUTHORIZATION);
        };

        var notifyObservers = function (user)    {
            return $q(
                function (resolve, reject){
                    angular.forEach(observerCallbacks,
                        function (callback) {
                            callback(user)
                        }
                    );

                    resolve();
                }
            );
        };
    }

);