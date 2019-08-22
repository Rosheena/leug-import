LuegImportApp.service('CrudHTTPService', ['$http', '$q', function ($http, $q) {

    this.create = function (url, obj, callback) {
        let $Deferred = $q.defer();

        console.log(":obj")
        console.log(obj)

        $http.post(url, obj)
            .then(function (response) {
                if (callback) {
                    callback(null, response.data);
                }

                $Deferred.resolve(response.data);
            }, function (errorMessage) {
                if (callback) {
                    callback(getErrorMessage(errorMessage), null);
                }

                $Deferred.reject(getErrorMessage(errorMessage));
            });

        return $Deferred.promise;
    };


    let getErrorMessage = function (errorMessage) {
        return errorMessage || 'Error Occurred';
    }
}]);