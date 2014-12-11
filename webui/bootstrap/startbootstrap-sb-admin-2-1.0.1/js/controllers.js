//var rifidiApp = angular.module('rifidiApp', ['serverServices']);
//var rifidiApp = angular.module('rifidiApp', ['app.ui-map', 'ui.map']);
var rifidiApp = angular.module('rifidiApp', []);
//var rifidiControllers = angular.module('rifidiControllers', []);

//rifidiControllers.controller('ServerListCtrl', ['$scope', 'Server', function ($scope, Server) {
//	$scope.servers = Server.query();
//	}]);

rifidiApp.controller('ServerListCtrl', function ($scope, $http) {

 $http.get('api/servers.xml').success(function(response) {
    //$scope.servers = data;
	console.log('response: ' + response);
	//console.log('response.data: ' + response.data);
	
	var myXml = stringToXML(response);
	
	console.log('myXml: ' + myXml);
	//console.log('myXml.data: ' + myXml.data);
	
	
	var serverVector = myXml.getElementsByTagName("server");
	
	console.log('serverVector: ' + serverVector);
	console.log('serverVector.length: ' + serverVector.length);
	
	$scope.servers = [];
                
                for(index = 0; index < serverVector.length; index++){
				
				
                    localDisplayName = serverVector[index].getElementsByTagName("displayName")[0].childNodes[0];
                    localIpAddress = serverVector[index].getElementsByTagName("ipAddress")[0].childNodes[0];
					localRestProtocol = serverVector[index].getElementsByTagName("restProtocol")[0].childNodes[0];
                    localRestPort = serverVector[index].getElementsByTagName("restPort")[0].childNodes[0];
                
				$scope.servers[$scope.servers.length] = {displayName:localDisplayName.textContent, ipAddress:localIpAddress.textContent, restProtocol: localRestProtocol.textContent, restPort:localRestPort.textContent};
					
                }
				
	});
	
	
	
	
	var stringToXML = function (text){
		if (window.ActiveXObject){
					  var doc=new ActiveXObject('Microsoft.XMLDOM');
					  doc.async='false';
					  doc.loadXML(text);
					} else {
					  var parser=new DOMParser();
					  var doc=parser.parseFromString(text,'text/xml');
		}
		return doc;
	}
	
/*
$scope.servers = [
    {'displayName': 'Rifidi11A',
     'restProtocol': 'http'},
    {'displayName': 'Rifidi22',
     'restProtocol': 'http'},
    {'displayName': 'Rifidi33',
     'restProtocol': 'http'}
  ];
*/


/*  $http.get('api/servers.xml').success(function(data) {
    $scope.servers = data;
  });
  */

  //$scope.orderProp = 'age';
});
	
	
  
  
