'use strict';

angular.module('cRUDApp')
    .factory('Position', function ($resource, DateUtils) {
        return $resource('api/positions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
