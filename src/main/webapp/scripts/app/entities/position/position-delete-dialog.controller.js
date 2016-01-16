'use strict';

angular.module('cRUDApp')
	.controller('PositionDeleteController', function($scope, $uibModalInstance, entity, Position) {

        $scope.position = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Position.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
