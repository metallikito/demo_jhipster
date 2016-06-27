(function() {
    'use strict';

    angular
        .module('demoJhipsterApp')
        .controller('EntryDialogController', EntryDialogController);

    EntryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Entry', 'Goal', 'Metric'];

    function EntryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Entry, Goal, Metric) {
        var vm = this;

        vm.entry = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.goals = Goal.query();
        vm.metrics = Metric.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.entry.id !== null) {
                Entry.update(vm.entry, onSaveSuccess, onSaveError);
            } else {
                Entry.save(vm.entry, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('demoJhipsterApp:entryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
