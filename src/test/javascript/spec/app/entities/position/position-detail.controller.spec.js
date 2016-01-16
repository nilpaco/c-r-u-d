'use strict';

describe('Controller Tests', function() {

    describe('Position Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPosition, MockPlayer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPosition = jasmine.createSpy('MockPosition');
            MockPlayer = jasmine.createSpy('MockPlayer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Position': MockPosition,
                'Player': MockPlayer
            };
            createController = function() {
                $injector.get('$controller')("PositionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cRUDApp:positionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
