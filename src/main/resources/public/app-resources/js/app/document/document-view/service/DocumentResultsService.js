LuegImportApp.service('DocumentResultsService', ['CrudHTTPService', 'Notification', function (CrudHTTPService, Notification) {


    this.processedResults = [];
    this.userHasSearched = false;

    this.getProcessedDocuments = function (callback) {
        let url = 'app/lueg/document/processed-results';
        CrudHTTPService.read(url, callback);
    }

}]);
