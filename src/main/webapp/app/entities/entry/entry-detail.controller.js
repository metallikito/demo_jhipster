(function() {
    'use strict';

    angular
        .module('demoJhipsterApp')
        .controller('EntryDetailController', EntryDetailController);

    EntryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Entry', 'Goal', 'Metric'];

    function EntryDetailController($scope, $rootScope, $stateParams, entity, Entry, Goal, Metric) {
        var vm = this;

        vm.entry = entity;

        var unsubscribe = $rootScope.$on('demoJhipsterApp:entryUpdate', function(event, result) {
            vm.entry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
