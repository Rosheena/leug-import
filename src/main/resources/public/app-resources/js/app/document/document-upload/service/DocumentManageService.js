LuegImportApp.service('DocumentManageService', ['CrudHTTPService', 'Notification', function (CrudHTTPService, Notification) {

    this.saveDocumentEdits = function (document, callback) {
        console.log("hitting the endpoint");
        let url = 'app/lueg/document/validate-document';
        CrudHTTPService.create(url, document, callback);
    };

    this.isDocumentEditValid = function (document) {
        console.log("document");
        console.log(document);
        if (document.objectType==null || document.objectType=='') {
            handleError("Object type is required");
            return false;
        } else if (document.objectName==null || document.objectName=='') {
            handleError("Object name is required");
            return false;
        } else if (document.cpId==null || document.cpId=='') {
            handleError("Cp Id is required");
            return false;
        } else if (document.subType==null || document.subType=='') {
            handleError("Sub Type is required");
            return false;
        }

        return true;
    };

}]);
