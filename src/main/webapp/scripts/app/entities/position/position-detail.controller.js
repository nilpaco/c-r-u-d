'use strict';

angular.module('cRUDApp')
    .controller('PositionDetailController', function ($scope, $rootScope, $stateParams, entity, Position, Player) {
        $scope.position = entity;
        $scope.load = function (id) {
            Position.get({id: id}, function(result) {
                $scope.position = result;
            });
        };
        var unsubscribe = $rootScope.$on('cRUDApp:positionUpdate', function(event, result) {
            $scope.position = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
