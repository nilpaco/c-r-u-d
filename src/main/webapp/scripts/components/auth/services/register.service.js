'use strict';

angular.module('cRUDApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


