(function() {
    'use strict';

    angular
        .module('demoJhipsterApp')
        .controller('GoalDetailController', GoalDetailController);

    GoalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Goal', 'User'];

    function GoalDetailController($scope, $rootScope, $stateParams, entity, Goal, User) {
        var vm = this;

        vm.goal = entity;

        var unsubscribe = $rootScope.$on('demoJhipsterApp:goalUpdate', function(event, result) {
            vm.goal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
