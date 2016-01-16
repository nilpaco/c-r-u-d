'use strict';

angular.module('cRUDApp')
    .factory('PlayerSearch', function ($resource) {
        return $resource('api/_search/players/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
