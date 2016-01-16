 'use strict';

angular.module('cRUDApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-cRUDApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-cRUDApp-params')});
                }
                return response;
            }
        };
    });
