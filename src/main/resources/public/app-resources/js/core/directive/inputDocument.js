LuegImportApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function() {
                    modelSetter(scope, element[0].files[0]);
                    evt.target.value = "";
                });
                document.getElementById("test").classList.remove("open");
                <!--   scope.uploadFile(); -->
            });
        }
    };
}]);


LuegImportApp.controller('DocumentUploadController', ['$scope', 'DocumentUploadService', function($scope, fileUpload){

    $scope.uploadFile = function(){
        var file = $scope.myFile;
        console.log('file is ' );
        console.dir(file);
        var uploadUrl = "/app/partner/gama/upload";
        fileUpload.uploadFileToUrl(file, uploadUrl);
    };

}]);