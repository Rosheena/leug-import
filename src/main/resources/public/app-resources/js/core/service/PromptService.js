LuegImportApp.service('PromptService', function ($q, $uibModal) {
   this.prompt = function (title, message, windowClass, controls) {
       $uibModal.open({
           templateUrl: 'app-resources/views/common/PromptMessageModal.html',
           controller: 'PromptServiceModalController',
           windowClass: windowClass,
           backdrop: 'static',
           resolve: {
               $uibModalParams: function () {
                   return {
                       title: title,
                       message: message,
                       controls: controls

                   };
               }
           }
       });
   };

    this.message = function (title, message, windowClass) {
        $uibModal.open({
            templateUrl: 'app-resources/views/common/PromptMessageModal.html',
            controller: 'PromptServiceModalController',
            windowClass: windowClass,
            backdrop: 'static',
            resolve: {
                $uibModalParams: function () {
                    return {
                        title: title,
                        message: message,
                        controls: [
                            {
                                class: 'btn btn-default',
                                label: 'Close',
                                hideWhenClick: true,
                            }
                        ]

                    };
                }
            }
        });
    };
});

LuegImportApp.controller('PromptServiceModalController', function ($scope, $uibModalInstance, $uibModalParams, $sce) {
    $scope.init = function () {
        $scope.title = $sce.trustAsHtml($uibModalParams.title);
        $scope.message = $sce.trustAsHtml($uibModalParams.message);
        $scope.controls = $uibModalParams.controls;
    };

    $scope.process = function (control) {
        if(_.isFunction(control.callback)){
            control.callback();
        }

        if(control.hideWhenClick === true){
            $uibModalInstance.close();
        }
    };

    $scope.close = function () {
        $uibModalInstance.close();
    };
});