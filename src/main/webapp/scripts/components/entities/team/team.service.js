'use strict';

angular.module('cRUDApp')
    .factory('Team', function ($resource, DateUtils) {
        return $resource('api/teams/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.birthday = DateUtils.convertLocaleDateFromServer(data.birthday);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.birthday = DateUtils.convertLocaleDateToServer(data.birthday);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.birthday = DateUtils.convertLocaleDateToServer(data.birthday);
                    return angular.toJson(data);
                }
            }
        });
    });
