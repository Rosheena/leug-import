LuegImportApp.service('DocumentManageService', ['CrudHTTPService', 'Notification', function (CrudHTTPService, Notification) {

    this.documentResults = [];
    this.fileName = "";
    this.userHasSearched = false;

    this.saveDocumentEdits = function (document, callback) {
        let url = 'app/lueg/document/validate-document';
        CrudHTTPService.create(url, document, callback);
    };

    this.importDocuments = function (documents, callback) {
        let url = 'app/lueg/document/process';
        CrudHTTPService.create(url, documents, callback);
    };

    /*this.isDocumentEditValid = function (document) {
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
    };*/

}]);
