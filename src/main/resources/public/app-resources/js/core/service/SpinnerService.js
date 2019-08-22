LuegImportApp.service('SpinnerService',
    function (){
        var spinner = [];

        this.start = function (index){
            $('body').append('<div class="spinner"><span class="spinner-helper"></span><img src="' + $CONTEXT_PATH$ + 'app-resources/images/spinner64x64.gif"></div>');
        };

        this.stop = function (index){
            $('div').remove(".spinner");
        };
    });