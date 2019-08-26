LuegImportApp.controller('DocumentManageController', ['DocumentUploadService', 'DocumentManageService', 'Notification', 'SpinnerService', 'PromptService', 'StorageService', 'NgTableParams', function (DocumentUploadService, DocumentManageService, Notification, SpinnerService, PromptService, StorageService, NgTableParams) {

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
            console.log("Results");
            console.log(vm.uploadingResults);
            vm.tableParams = new NgTableParams({ count : 100 }, { dataset : vm.uploadingResults });
            vm.fileName = vm.file.name;
        }

        vm.clearFile();
        vm.display_view = display;

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

    vm.beginDocumentEdits = function (document) {
        document.edits = angular.copy(document);
        document.editing = true;
    };

    vm.cancelDocumentEdits = function (document) {
        document.editing = false;
        delete document.edits;
    };

    vm.saveDocumentEdits = function (document, documentEdits, idx) {
       /* if (!DocumentManageService.isDocumentEditValid(document)) {
            return;
        }*/

        SpinnerService.start();
        DocumentManageService.saveDocumentEdits(documentEdits, function (err, response) {
            SpinnerService.stop();

            if (err) {
                Notification.error({ title: 'Error saving Document Information', message: 'Please try again later' });
            } else {
                for(var i=0; i<vm.uploadingResults.length; i++){
                    if(vm.uploadingResults[i].id === response.id){
                        vm.uploadingResults[i]=response;
                    }
                }
                vm.tableParams.reload();
              //  vm.tableParams = new NgTableParams({ count : 100 }, { dataset : vm.uploadingResults });
               // vm.tableParams.data.splice(idx, 1, documentEdits);
                handleSuccessMessage('Successfully saved Document information');
            }
        });
        document.editing = false;
        delete document.edits;
    };

}]);