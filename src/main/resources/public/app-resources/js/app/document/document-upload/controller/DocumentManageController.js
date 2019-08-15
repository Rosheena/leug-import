LuegImportApp.controller('DocumentManageController', ['DocumentUploadService', 'Notification', 'SpinnerService', 'PromptService', 'StorageService', function (DocumentUploadService, Notification, SpinnerService) {

    let vm = this;
    vm.uploader = null;
    vm.file = null;
    vm.display_view = "selector";
    vm.uploadingResults = null;

    vm.openFileDialog = function () {
        document.getElementById("openFileSelector").click();
    }

    vm.init = function () {
        vm.uploader = DocumentUploadService.initUploader(this.afterUploadComplete, this.addingFile);
    }

    vm.submitSpreadsheet = function () {
        SpinnerService.start();
        vm.display_view = "uploading";
        vm.uploader.uploadAll();
    };

    vm.addingFile = function(file){
        vm.file = file;
        vm.uploadingResults = null;
        console.log("final file");
        console.log(vm.file);
    }

    vm.afterUploadComplete = function (err, response, display) {

        if (err) {
            console.log('afterUploadComplete',err);
            vm.clearFile();
            handleError(err, "Errors while uploading file");
        } else {
            vm.uploadingResults = response;
        }
        vm.uploadingResults = response;
        vm.display_view = display;
        vm.clearFile();
    };

    vm.initializeUploader = function () {
        vm.clearFile();
        vm.display_view = "selector";
        vm.uploadingResults = null;
    };

    vm.clearFile = function () {
        vm.file = null;
        $("#openFileSelector").val(null);
    };

}]);