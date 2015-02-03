/**
 * Created by tws on 09/01/2015.
 */

'use strict';
/**
 * @ngdoc function
 * @name rifidiApp.controller:MenuCtrl
 * @description
 * # AboutCtrl
 * Controller of the rifidiApp
 */
angular.module('rifidiApp')
  .directive('menuRifidi', function () {


    return {
      restrict: 'E',
      transclude: true,
      scope: {},
      controller: function($scope, $http) {
        $scope.elementSelected={};
        $scope.temporaryNode = {
              children: []
          };
        $scope.propertyType="servers";
        $scope.mode = undefined;

        $scope.done = function () {
          /* reset */
          $scope.elementTree.currentNode.selected = undefined;
          $scope.elementTree.currentNode = undefined;
          $scope.mode = undefined;
        };

        $scope.addServerDone = function () {
          console.log("addServerDone")
          /* add server */
          if( $scope.temporaryNode.elementName  ) {//TODO: Validate all values or use a require validation
              $scope.temporaryNode.elementId="server";
              $scope.temporaryNode.displayName= $scope.temporaryNode.elementName;
              console.log($scope.elementTree.currentNode);
              $scope.elementTree.currentNode.children.push( angular.copy($scope.temporaryNode) );
              //console.log("$scope.elementTree.currentNode.children");
              //console.log($scope.elementTree.currentNode.children);
          }
          /* reset */
          $scope.temporaryNode.elementId = "";
          $scope.temporaryNode.elementName = "";
          $scope.temporaryNode.displayName="";

            $scope.temporaryNode.ipAddress="";
            $scope.temporaryNode.restProtocol="";
            $scope.temporaryNode.restPort="";


            $scope.done();
        };
          $scope.deleteServer=function(){
              console.log("delete $scope.elementTree.currentNode")

              //console.log($scope.elementTree.currentNode);
              console.log("$scope.elementTree");
              console.log($scope.elementTree.currentNode);
          };

          $http.get('scripts/controllers/components/menu/servers.json').
          success(function(data, status, headers, config) {
            console.log("funciono lectura servers ");
            $scope.servers = data;

            var partialElementList = [ { "elementName" : "Servers", "elementId" : "servers", "collapsed":true, "children" : []} ];

            data.forEach(function(server){

                server.colapsed = true;
                server.elementName = server.displayName;
                server.elementId = "server";
                server.children = [];
                server.host = server.restProtocol + "://" + server.ipAddress + ":" + server.restPort;

                partialElementList[0].children.push(server);

                //for each server, add the sensor management element

                //sensor management element:
                var sensorManagementElement = { "host" : server.ipAddress + ":" + server.restPort, "elementName" : "Sensor Management", "elementId" : "sensorManagement", "collapsed":true, "children" : [] };
                server.children.push(sensorManagementElement);

                //for each server, connect and query the list of sensors and place them under sensor management element
                $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/readers')
                    .success(function(data, status, headers, config) {

                        var sensorsResponseHost = headers('host');

                        var xmlSensors;
                        if (window.DOMParser)
                        {
                            var parser = new DOMParser();
                            xmlSensors = parser.parseFromString(data,"text/xml");
                        }
                        else // Internet Explorer
                        {
                            xmlSensors = new ActiveXObject("Microsoft.XMLDOM");
                            xmlSensors.async=false;
                            xmlSensors.loadXML(data);
                        }

                        //get the xml response and extract the values to construct the local sensor object
                        var sensorXmlVector = xmlSensors.getElementsByTagName("sensor");

                        for(var index = 0; index < sensorXmlVector.length; index++){
                            var serviceID = sensorXmlVector[index].getElementsByTagName("serviceID")[0].childNodes[0];
                            var factoryID = sensorXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];

                            //console.log("serviceID: " + serviceID.nodeValue);
                            //console.log("factoryID: " + factoryID.nodeValue);

                            var sensorElement = { "elementName" : serviceID.nodeValue, "elementId" : serviceID.nodeValue, "collapsed":true, "children" : [] };

                            //for this responseHost search which sensorManagementElement is associated with, and associate the sensorElement
                            //console.log("headers('host') from readers service: " + headers('host'));

                            partialElementList[0].children.forEach(function(server) {
                                if (server.host == sensorsResponseHost){

                                    //add this sensor to the server
                                    server.children[0].children.push(sensorElement);
                                }
                            });

                            //retrieve the sessions associated with this sensor element
                            $http.get(sensorsResponseHost + '/readerstatus/' + serviceID.nodeValue)
                                .success(function(data, status, headers, config) {

                                    var sessionsResponseHost = headers('host');

                                    //console.log("data for readerstatus from host: " + sessionsResponseHost + ": " + data);

                                    var xmlSessions;
                                    if (window.DOMParser)
                                    {
                                        var parser = new DOMParser();
                                        xmlSessions = parser.parseFromString(data,"text/xml");
                                    }
                                    else // Internet Explorer
                                    {
                                        xmlSessions = new ActiveXObject("Microsoft.XMLDOM");
                                        xmlSessions.async=false;
                                        xmlSessions.loadXML(data);
                                    }

                                    //get the xml response and extract the values to construct the local session object

                                    var responseServiceId = xmlSessions.getElementsByTagName("sensor")[0].getElementsByTagName("serviceID")[0].childNodes[0].nodeValue;
                                    //console.log("responseServiceId: ");
                                    //console.log(responseServiceId);

                                    var sessionXmlVector = xmlSessions.getElementsByTagName("session");

                                    for(var index = 0; index < sessionXmlVector.length; index++) {
                                        var sessionID = sessionXmlVector[index].getElementsByTagName("ID")[0].childNodes[0];
                                        var sessionStatus = sessionXmlVector[index].getElementsByTagName("status")[0].childNodes[0];

                                        //console.log("sessionID: " + sessionID.nodeValue);
                                        //console.log("sessionStatus: " + sessionStatus.nodeValue);

                                        var sessionElement = {
                                            "elementName": "session " + sessionID.nodeValue,
                                            "elementId": "session " + sessionID.nodeValue,
                                            "collapsed": true,
                                            "status": sessionStatus.nodeValue,
                                            "children": []
                                        };

                                        //extract the executing commands form this session and append as childern element
                                        var executingCommandsXmlVector = xmlSessions.getElementsByTagName("executingcommand");

                                        for(var index = 0; index < executingCommandsXmlVector.length; index++) {
                                            var commandID = executingCommandsXmlVector[index].getElementsByTagName("commandID")[0].childNodes[0];
                                            var commandInterval = executingCommandsXmlVector[index].getElementsByTagName("interval")[0].childNodes[0];

                                            var commandElement = {
                                                "elementName": commandID.nodeValue,
                                                "elementId": commandID.nodeValue,
                                                "collapsed": true,
                                                "interval": commandInterval.nodeValue,
                                                "children": []
                                            };

                                            sessionElement.children.push(commandElement);

                                        }




                                        //for this responseHost search which sensorElement is associated with, and associate the sensorElement
                                        //console.log("headers('host') from sessions service: " + headers('host'));

                                        partialElementList[0].children.forEach(function(server) {




                                            if (server.host == sessionsResponseHost){

                                                //console.log("server.host == sessionsResponseHost:");
                                                //console.log(server.host);

                                                //console.log("XX server.host:");
                                                //console.log(server.host);

                                                //console.log("sessionsResponseHost:");
                                                //console.log(sessionsResponseHost);

                                                //responseServiceId is the service id that this response belongs to

                                                //search for this server, which sensor is going to hold the session

                                                server.children[0].children.forEach(function(sensor) {

                                                        //console.log("iter sensor:");
                                                        //console.log(sensor);
                                                    console.log("sensor.elementId");
                                                    console.log(sensor.elementId);
                                                    console.log("responseServiceId");
                                                    console.log(responseServiceId);
                                                    console.log("-------------------------------------");



                                                        if (sensor.elementId == responseServiceId){
/*
                                                            console.log("ZZ server.host:");
                                                            console.log(server.host);

                                                            console.log("sensor.elementId:");
                                                            console.log(sensor.elementId);

                                                            console.log("responseServiceId:");
                                                            console.log(responseServiceId);
*/
                                                            sensor.children.push(sessionElement);
                                                        }

                                                });


                                                //add this session to the session list of this sensor
                                                //sensorElement.children.push(sessionElement);
                                                //server.children[0].children.push(sensorElement);
                                            }
                                        });


                                    }


                                })
                                .error(function(data, status, headers, config) {
                                    console.log("error leyendo sesiones serviceID.nodeValue: " + serviceID.nodeValue);

                                    // called asynchronously if an error occurs
                                    // or server returns response with an error status.
                                });



                            //this.apps[this.apps.length] = {id:localId.textContent, number:localNumber.textContent, status:tLocalStatus.textContent};
                        }


                    })
                    .error(function(data, status, headers, config) {
                        console.log("error leyendo sensores server.ipAddress: " + server.ipAddress);

                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                    });

                //app management element:
                var appManagementElement = { "elementName" : "App Management", "elementId" : "App Management", "collapsed":true, "children" : [] };
                server.children.push(appManagementElement);





            });


                  $scope.elementList = partialElementList;

/*
                  $scope.elementList = [
                      { "elementName" : "Servers", "elementId" : "servers", "collapsed":true, "children" : [
                          { "elementName" : "Server 1", "elementId" : "server",    "displayName": "Server1",
                              "restProtocol" : "http",
                              "ipAddress" : "127.0.0.1",
                              "restPort" : "8111","collapsed":true, "children" : [

                          ] },
                          { "elementName" : "Server 2", "elementId" : "server",
                              "displayName": "Server2-increible",
                              "restProtocol" : "http",
                              "ipAddress" : "127.0.0.1",
                              "restPort" : "8111","collapsed":true, "children" : [
                              { "elementName" : "Sensor Management", "elementId" : "sensorManagement","collapsed":true, "children" : [
                                  { "elementName" : "Sensor 1", "elementId" : "sensor","collapsed":true, "children" : [
                                      { "elementName" : "Session 1", "elementId" : "Session 1","collapsed":true, "children" : [
                                          { "elementName" : "Command 1", "elementId" : "Command 1", "children" : [

                                          ] },
                                      ] },
                                  ] },
                              ] },
                              { "elementName" : "App Management", "elementId" : "App Management","collapsed":true, "children" : [
                                  { "elementName" : "App Groups", "elementId" : "App Group","collapsed":true, "children" : [
                                      { "elementName" : "App Group 1", "elementId" : "App Group 1","collapsed":true, "children" : [
                                          { "elementName" : "Apps", "elementId" : "Apps","collapsed":true, "children" : [
                                              { "elementName" : "App 1", "elementId" : "App 1", "children" : [
                                              ] },
                                              { "elementName" : "App 2", "elementId" : "App 2", "children" : [
                                              ] },
                                              { "elementName" : "App 3", "elementId" : "App 3", "children" : [
                                              ] },
                                          ] },
                                          { "elementName" : "Read Zones", "elementId" : "Read Zones","collapsed":true, "children" : [
                                              { "elementName" : "Read Zone 1", "elementId" : "Read Zone 1", "children" : [
                                              ] },
                                              { "elementName" : "Read Zone 2", "elementId" : "Read Zone 2", "children" : [
                                              ] },
                                              { "elementName" : "Read Zone 3", "elementId" : "Read Zone 3", "children" : [
                                              ] },
                                          ] },
                                      ] },
                                      { "elementName" : "App Group 2", "elementId" : "App Group 2","collapsed":true, "children" : [
                                          { "elementName" : "Apps", "elementId" : "Apps","collapsed":true, "children" : [
                                              { "elementName" : "App 1", "elementId" : "App 1", "children" : [
                                              ] },
                                              { "elementName" : "App 2", "elementId" : "App 2", "children" : [
                                              ] },
                                              { "elementName" : "App 3", "elementId" : "App 3", "children" : [
                                              ] },
                                          ] },
                                          { "elementName" : "Read Zones", "elementId" : "Read Zones","collapsed":true, "children" : [
                                              { "elementName" : "Read Zone 1", "elementId" : "Read Zone 1", "children" : [
                                              ] },
                                              { "elementName" : "Read Zone 2", "elementId" : "Read Zone 2", "children" : [
                                              ] },
                                              { "elementName" : "Read Zone 3", "elementId" : "Read Zone 3", "children" : [
                                              ] },
                                          ] },
                                      ] },
                                  ] },
                              ] },
                          ] },
                      ]},

                  ];

*/



/*
                for(var i=0; i<data.length; i++){
                    var obj = data[i];
                    console.log( "obj: " + obj );


                    for(var key in obj){

                        var attrName = key;
                        var attrValue = obj[key];

                        console.log( "attrName: " + attrName );
                        console.log( "attrValue: " + attrValue );
                    }



                }
                */

                  /*

                data.forEach( function( item ) {
                    console.log( "ite: " + item );
                });

                */
            // this callback will be called asynchronously
            // when the response is available
          }).
          error(function(data, status, headers, config) {
            console.log("error leyendo servidores ");


            // called asynchronously if an error occurs
            // or server returns response with an error status.
          });





          $scope.tabs = [
              { title:'Dynamic Title 1', content:'Dynamic content 1' },
              { title:'Dynamic Title 2', content:'Dynamic content 2', disabled: true }
          ];

          var tabsSensor=[
              { title:'Connection', content:'Dynamic content 1' },
              { title:'General', content:'Dynamic content 2'},
              { title:'General', content:'Dynamic content 2'}
          ];

          $scope.$watch( 'elementTree.currentNode', function( newObj, oldObj ) {
              console.log($scope.elementTree.currentNode);
              if( $scope.elementTree && angular.isObject($scope.elementTree.currentNode) ) {
                  $scope.elementSelected=$scope.elementTree.currentNode;
                  $scope.propertyType=$scope.elementTree.currentNode.elementId;

              }
          }, false);

      },
      templateUrl: 'scripts/controllers/components/menu/menuRifidi.html'
    };

  });

