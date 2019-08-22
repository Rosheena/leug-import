LuegImportApp.service('DocumentManageService', ['CrudHTTPService', 'Notification', function (CrudHTTPService, Notification) {

    this.saveDocumentEdits = function (document, callback) {
        let url = 'app/lueg/document/validate-document';
        CrudHTTPService.create(url, document, callback);
    };

    this.isDocumentEditValid = function (document) {
        if (!document.objectType) {
            Notification.error('Object type is required');
            return false;
        } else if (!document.objectName) {
            Notification.error('Object name is required');
            return false;
        } else if (!document.cpId) {
            Notification.error('Cp Id is required');
            return false;
        } else if (!document.subType) {
            Notification.error('Sub Type is required');
            return false;
        }

        return true;
    };

}]);
