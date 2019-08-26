LuegImportApp.controller('DocumentResultsController', [ 'DocumentResultsService', 'Notification', 'SpinnerService', 'PromptService', 'StorageService', 'NgTableParams', function (DocumentResultsService, Notification, SpinnerService, PromptService, StorageService, NgTableParams) {

    let vm = this;

    vm.processedResults = [];

    vm.init = function () {
        (DocumentResultsService.userHasSearched) ? loadProcessedResults(DocumentResultsService.documentResults) : vm.getProcessedDocuments();
    }

    vm.getProcessedDocuments = function () {
        SpinnerService.start();
        DocumentResultsService.userHasSearched = true;
        DocumentResultsService.getProcessedDocuments(function (err, results) {
            SpinnerService.stop();
            if (err) {
                handleError(err, "Error Getting Processed Document Results");
            } else {
                loadProcessedResults(results);
            }
        });
    }

    let loadProcessedResults = function (processedResults) {
        vm.processedResults = processedResults;
        DocumentResultsService.documentResults = vm.processedResults;
        vm.tableParams = new NgTableParams({ count : 100 }, { dataset : vm.processedResults });
    }

}]);