var serverServices = angular.module('serverServices', ['ngResource']);

serverServices.factory('Server', ['$resource',
  function($resource){
  
	
  
    return $resource('api/servers.xml', {}, {
      query: {method:'GET', params:{}, isArray:false}
	  
    });
	
  }]);