'use strict';

angular.module('cRUDApp')
    .controller('PlayerDetailController', function ($scope, $rootScope, $stateParams, entity, Player, Position, Team) {
        $scope.player = entity;
        $scope.load = function (id) {
            Player.get({id: id}, function(result) {
                $scope.player = result;
            });
        };
        var unsubscribe = $rootScope.$on('cRUDApp:playerUpdate', function(event, result) {
            $scope.player = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
