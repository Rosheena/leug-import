LuegImportApp.service('StorageService',
    function () {

        this.getItem = function (key) {
            return sessionStorage.getItem(key);
        };

        this.setItem = function (key, value) {
            return sessionStorage.setItem(key, value);
        };

        this.removeItem = function (key) {
            return sessionStorage.removeItem(key);
        };
    }
);