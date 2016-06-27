(function() {
    'use strict';

    angular
        .module('demoJhipsterApp')
        .controller('MetricDetailController', MetricDetailController);

    MetricDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Metric', 'Entry', 'Goal'];

    function MetricDetailController($scope, $rootScope, $stateParams, entity, Metric, Entry, Goal) {
        var vm = this;

        vm.metric = entity;

        var unsubscribe = $rootScope.$on('demoJhipsterApp:metricUpdate', function(event, result) {
            vm.metric = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
