LuegImportApp.service('DocumentUploadService', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .success(function(){
            })
            .error(function(){
            });
    }
}]);


/*
LuegImportApp.service('DocumentUploadService', function ($http, FileUploader, PromptService, SpinnerService) {

    var ACCEPTED_FILE_TYPES = ['csv', 'text/csv'];

    this.uploader = null;

    this.initUploader = function (afterUploadCompleteCallback) {

        let uploader = new FileUploader({
            url: '/app/partner/gama/upload',
            headers: {
                Authorization: StorageService.getItem("AUTHORIZATION")
            },
            removeAfterUpload: true,
            queueLimit: 1
        });

        uploader.filters.push({
            name: 'csvOnlyFilter',
            fn: function (item, options) {
                let extension = item.name.split('.').pop().toLowerCase();

                if (!_.includes(ACCEPTED_FILE_TYPES, extension)) {
                    return false;
                }

                if (item.size < 50) {
                    return false;
                }

                return this.queue.length < 1;
            }
        });

        uploader.onWhenAddingFileFailed = function (item, filter, options) {
            let extension = item.name.split('.').pop();

            if (!_.includes(ACCEPTED_FILE_TYPES, extension)) {
                PromptService.message('Invalid File', 'Only CSV Files are accepted.', 'prompt-modal-lg-widow');
                return;
            }

            if (item.size < 10) {
                PromptService.message('Invalid File', 'File doesn\'t seem to be a valid CSV file', 'prompt-modal-lg-widow');
                return;
            }

            if (this.queue.length === 1) {
                PromptService.message('Invalid File', 'A file is already selected', 'modal-sm');
            }
        };

        uploader.onAfterAddingFile = function (fileItem) {
            let applySaving = function () {


                const applyUpload = function () {
                    SpinnerService.start();
                    uploader.uploadAll();
                };

                const clearQueue = function () {
                    uploader.clearQueue()
                };

                PromptService.prompt(
                    'Upload CSV File',
                    'Are you sure you want to upload [' + fileItem.file.name + ']? ',
                    'prompt-modal-lg-widow',
                    [
                        {
                            class: 'btn btn-success',
                            label: 'Yes - Upload',
                            hideWhenClick: true,
                            callback: applyUpload
                        },
                        {
                            class: 'btn btn-default',
                            label: 'No - Do Nothing',
                            hideWhenClick: true,
                            callback: clearQueue
                        }
                    ]
                );

            };
        };

        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            SpinnerService.stop();
            uploader.clearQueue();

            if (status !== 200) {
                afterUploadCompleteCallback('Error Occurred', null);
            } else {
                afterUploadCompleteCallback(null, response);
            }
        };


        return this.uploader = uploader;
    };

    this.submitSpreadsheet = function () {
        SpinnerService.start();
        this.display_view = "uploading";
        this.uploader.uploadAll();
    };

});*/
