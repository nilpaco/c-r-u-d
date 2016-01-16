'use strict';

angular.module('cRUDApp')
	.controller('TeamDeleteController', function($scope, $uibModalInstance, entity, Team) {

        $scope.team = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Team.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
