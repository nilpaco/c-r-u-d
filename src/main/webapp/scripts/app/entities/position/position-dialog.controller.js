'use strict';

angular.module('cRUDApp').controller('PositionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Position', 'Player',
        function($scope, $stateParams, $uibModalInstance, entity, Position, Player) {

        $scope.position = entity;
        $scope.players = Player.query();
        $scope.load = function(id) {
            Position.get({id : id}, function(result) {
                $scope.position = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('cRUDApp:positionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.position.id != null) {
                Position.update($scope.position, onSaveSuccess, onSaveError);
            } else {
                Position.save($scope.position, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
