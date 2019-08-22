let NOTIFICATIONS = [];

const handleError = function (err, defaultMessage) {
    if (_.isObject(err)) {

        if (_notIdle(err.longMessage)) {
            notification(err.longMessage, 'red', 10000);

            return;
        }

        if (_notIdle(err.message)) {
            notification(err.message, 'red', 10000);

            return;
        }
    }

    if (_notIdle(defaultMessage)) {
        notification(defaultMessage, 'red', 10000);

        return;
    }

    notification("Error occurred", 'red', 10000);
};

const handleSuccessMessage = function (defaultMessage) {

    if (_notIdle(defaultMessage)) {
        notification(defaultMessage, 'green', 10000);

        return;
    }

};

const notification = function (message, color = 'red', time = 5000, callback) {
    let thisNotificationId = NOTIFICATIONS.length + 1;
    let thisNotificationPosition = NOTIFICATIONS.map(notification => notification.height).reduce((a, b) => a + b + 10, 0);

    $('body').append(
        '<div notification-id="' + thisNotificationId + '"   ' +
        '     class="notifications notifications-' + color + ' "   ' +
        '     style="bottom:' + thisNotificationPosition + 'px; visibility: hidden;">' +
        '    <div class="clearfix" style="margin:5px 5px 0 0;">' +
        '        <a class="btn-dismiss-notification pull-right glyphicon-18" style="color:#fff;" onclick="closeNotification(' + thisNotificationId + ')">' +
        '           <span class="glyphicon glyphicon-remove-circle"></span>' +
        '        </a>' +
        '    </div>' +
        '    <div class="notification-body">' +
        '        <p>' + message + '</p>' +
        '    </div>' +
        '</div>');


    let thisNotification = $("div[notification-id='" + thisNotificationId + "']");

    NOTIFICATIONS.push({
        id: thisNotificationId,
        height: thisNotification.height()
    });

    thisNotification.addClass("hide");
    thisNotification.css('visibility', '');
    thisNotification.removeClass("hide").slideDown();

    setTimeout(function () {
        thisNotification.slideUp(function () {
            _.remove(NOTIFICATIONS, notification => notification.id === Number(thisNotification.attr("notification-id")));

            thisNotification.remove();

            if (_.isFunction(callback)) {
                callback();
            }
        });
    }, time);
};

const closeNotification = function (thisNotificationId) {
    let thisNotification = $("div[notification-id='" + thisNotificationId + "']");

    _.remove(NOTIFICATIONS, notification => notification.id === Number(thisNotification.attr("notification-id")));

    thisNotification.remove();
};

_notIdle = function (obj) {
    if (_.isNaN(obj)) {
        return false;
    }

    if (_.isNumber(obj)) {
        return true;
    }

    return !_.isUndefined(obj) && !_.isEmpty(obj) && !_.isNull(obj);
};