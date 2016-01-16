'use strict';

angular.module('cRUDApp')
    .factory('TeamSearch', function ($resource) {
        return $resource('api/_search/teams/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
