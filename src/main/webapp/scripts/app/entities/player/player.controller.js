'use strict';

angular.module('cRUDApp')
    .controller('PlayerController', function ($scope, $state, Player, PlayerSearch, ParseLinks) {

        $scope.players = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Player.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.players = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            PlayerSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.players = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.player = {
                name: null,
                points: null,
                rebounds: null,
                assits: null,
                birthday: null,
                id: null
            };
        };
    });
