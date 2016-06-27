(function() {
    'use strict';

    angular
        .module('demoJhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('metric', {
            parent: 'entity',
            url: '/metric?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'demoJhipsterApp.metric.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metric/metrics.html',
                    controller: 'MetricController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('metric');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('metric-detail', {
            parent: 'entity',
            url: '/metric/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'demoJhipsterApp.metric.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metric/metric-detail.html',
                    controller: 'MetricDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('metric');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Metric', function($stateParams, Metric) {
                    return Metric.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('metric.new', {
            parent: 'metric',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric/metric-dialog.html',
                    controller: 'MetricDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('metric', null, { reload: true });
                }, function() {
                    $state.go('metric');
                });
            }]
        })
        .state('metric.edit', {
            parent: 'metric',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric/metric-dialog.html',
                    controller: 'MetricDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Metric', function(Metric) {
                            return Metric.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('metric', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('metric.delete', {
            parent: 'metric',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric/metric-delete-dialog.html',
                    controller: 'MetricDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Metric', function(Metric) {
                            return Metric.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('metric', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
