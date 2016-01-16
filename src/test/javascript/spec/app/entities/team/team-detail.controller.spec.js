'use strict';

describe('Controller Tests', function() {

    describe('Team Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTeam, MockPlayer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTeam = jasmine.createSpy('MockTeam');
            MockPlayer = jasmine.createSpy('MockPlayer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Team': MockTeam,
                'Player': MockPlayer
            };
            createController = function() {
                $injector.get('$controller')("TeamDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cRUDApp:teamUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
