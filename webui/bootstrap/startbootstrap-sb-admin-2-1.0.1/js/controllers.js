var rifidiApp = angular.module('rifidiApp', []);

rifidiApp.controller('ServerListCtrl', function ($scope) {
  $scope.servers = [
    {'displayName': 'Rifidi11A',
     'restProtocol': 'http'},
    {'displayName': 'Rifidi22',
     'restProtocol': 'http'},
    {'displayName': 'Rifidi33',
     'restProtocol': 'http'}
  ];
});