'use strict';

angular.module('cRUDApp').controller('PlayerDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Player', 'Position', 'Team',
        function($scope, $stateParams, $uibModalInstance, entity, Player, Position, Team) {

        $scope.player = entity;
        $scope.positions = Position.query();
        $scope.teams = Team.query();
        $scope.load = function(id) {
            Player.get({id : id}, function(result) {
                $scope.player = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('cRUDApp:playerUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.player.id != null) {
                Player.update($scope.player, onSaveSuccess, onSaveError);
            } else {
                Player.save($scope.player, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForBirthday = {};

        $scope.datePickerForBirthday.status = {
            opened: false
        };

        $scope.datePickerForBirthdayOpen = function($event) {
            $scope.datePickerForBirthday.status.opened = true;
        };
}]);
