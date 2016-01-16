'use strict';

angular.module('cRUDApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('team', {
                parent: 'entity',
                url: '/teams',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Teams'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/team/teams.html',
                        controller: 'TeamController'
                    }
                },
                resolve: {
                }
            })
            .state('team.detail', {
                parent: 'entity',
                url: '/team/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Team'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/team/team-detail.html',
                        controller: 'TeamDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Team', function($stateParams, Team) {
                        return Team.get({id : $stateParams.id});
                    }]
                }
            })
            .state('team.new', {
                parent: 'team',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/team/team-dialog.html',
                        controller: 'TeamDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    city: null,
                                    birthday: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('team', null, { reload: true });
                    }, function() {
                        $state.go('team');
                    })
                }]
            })
            .state('team.edit', {
                parent: 'team',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/team/team-dialog.html',
                        controller: 'TeamDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Team', function(Team) {
                                return Team.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('team', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('team.delete', {
                parent: 'team',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/team/team-delete-dialog.html',
                        controller: 'TeamDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Team', function(Team) {
                                return Team.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('team', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
