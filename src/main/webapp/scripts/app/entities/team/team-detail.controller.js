'use strict';

angular.module('cRUDApp')
    .controller('TeamDetailController', function ($scope, $rootScope, $stateParams, entity, Team, Player) {
        $scope.team = entity;
        $scope.load = function (id) {
            Team.get({id: id}, function(result) {
                $scope.team = result;
            });
        };
        var unsubscribe = $rootScope.$on('cRUDApp:teamUpdate', function(event, result) {
            $scope.team = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
