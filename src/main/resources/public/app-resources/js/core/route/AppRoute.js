LuegImportApp
    .config(
        function ($stateProvider, $urlRouterProvider) {

            let authResolver = function () {
                return {
                    loggedIn: ['URLSecurity', function (URLSecurity) {
                        return URLSecurity.is_authenticated();
                    }]
                }
            };

            $stateProvider
                .state({
                    name: 'login',
                    url: '/login',
                    controller: 'LoginController',
                    controllerAs: 'loginCtrl',
                    templateUrl: 'app-resources/views/login/login.html'
                })
                .state({
                    abstract: true,
                    name: 'document-upload',
                    url: '/document-upload',
                    templateUrl: 'partner/app-resources/views/document/document-upload/upload-document.html',
                    resolve: authResolver()
                })
                .state({
                    name: 'document-upload.manage',
                    url: '/manage',
                    controller: 'DocumentUploadController',
                    controllerAs: 'docUploadCtrl',
                    templateUrl: 'partner/app-resources/views/document/document-upload/upload-document-manage.html'
                })
                .state({
                    abstract: true,
                    name: 'document-view',
                    url: '/document-view',
                    templateUrl: 'partner/app-resources/views/document/document-view/view-document.html',
                    resolve: authResolver()
                })
                .state({
                    name: 'document-view.results',
                    url: '/results',
                    controller: 'DocumentResultsController',
                    controllerAs: 'docResultsCtrl',
                    templateUrl: 'partner/app-resources/views/document/document-view/view-document-results.html'
                });

            // For any unmatched url, send to login page
            $urlRouterProvider.otherwise("/login");

        });