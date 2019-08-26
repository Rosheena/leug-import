LuegImportApp.controller('DocumentResultsController', [ 'DocumentResultsService', 'Notification', 'SpinnerService', 'PromptService', 'StorageService', 'NgTableParams', function (DocumentResultsService, Notification, SpinnerService, PromptService, StorageService, NgTableParams) {

    let vm = this;

    vm.init = function () {

    }

    vm.getProcessedDocuments = function () {
        SpinnerService.start();
        DocumentResultsService.userHasSearched = true;
        DocumentResultsService.getProcessedDocuments(function (err, results) {

        });
    }

}]);