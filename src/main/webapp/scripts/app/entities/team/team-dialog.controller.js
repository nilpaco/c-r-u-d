'use strict';

angular.module('cRUDApp').controller('TeamDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Team', 'Player',
        function($scope, $stateParams, $uibModalInstance, entity, Team, Player) {

        $scope.team = entity;
        $scope.players = Player.query();
        $scope.load = function(id) {
            Team.get({id : id}, function(result) {
                $scope.team = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('cRUDApp:teamUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.team.id != null) {
                Team.update($scope.team, onSaveSuccess, onSaveError);
            } else {
                Team.save($scope.team, onSaveSuccess, onSaveError);
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
