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

          $scope.saveServer = function(){
              console.log("saveServer");
              console.log("$scope.elementSelected");
              console.log($scope.elementSelected);
              console.log("$scope.elementTree.currentNode");
              console.log($scope.elementTree.currentNode);
              console.log("show alert dialog to confirm");

          }

          $http.get('scripts/controllers/components/menu/servers.json').
          success(function(data, status, headers, config) {
            console.log("funciono lectura servers ");
            $scope.servers = data;

            var partialElementList = [ { "elementName" : "Servers", "elementId" : "servers", "collapsed":true, "children" : []} ];

            data.forEach(function(server){

                server.colapsed = true;
                server.elementName = server.displayName;
                server.elementId = "server";
                server.iconClass = "server-disconnected";
                server.contextMenuId = "contextMenuServer";
                server.children = [];
                server.host = server.restProtocol + "://" + server.ipAddress + ":" + server.restPort;
                server.status = 'CONNECTING';

                partialElementList[0].children.push(server);

                //for each server make an asynchronous call to test whether ping operation returns success
                $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/ping')
                    .success(function(data, status, headers, config) {

                        var pingResponseHost = headers('host');

                        var xmlPingTimestamp;
                        if (window.DOMParser)
                        {
                            var parser = new DOMParser();
                            xmlPingTimestamp = parser.parseFromString(data,"text/xml");
                        }
                        else // Internet Explorer
                        {
                            xmlPingTimestamp = new ActiveXObject("Microsoft.XMLDOM");
                            xmlPingTimestamp.async=false;
                            xmlPingTimestamp.loadXML(data);
                        }

                        //get the xml response and extract the ping timestamp value
                        var timestampXmlVector = xmlPingTimestamp.getElementsByTagName("timestamp");

                        var serverTimestamp = timestampXmlVector[0].childNodes[0].nodeValue;

                        if (serverTimestamp){
                            console.log("server ping, ip: " + pingResponseHost + ", timestamp: " + serverTimestamp);

                            //change server connecting status to connected

                            partialElementList[0].children.forEach(function(server) {
                                if (server.host == pingResponseHost){

                                    //change server status
                                    server.status = 'CONNECTED';
                                    server.iconClass = "server-connected";
                                }
                            });

                        }

                    })
                    .error(function(data, status, headers, config) {
                        console.log("error haciendo ping server.ipAddress: " + server.ipAddress);

                        //server.iconClass = "server";

                        // called asynchronously if an error occurs
                        // or server returns response with an error status.

                        //var pingResponseHost = headers('host');
                        //console.log("headers in error calling ping:");
                        //console.log(headers());

                        //console.log("data in error calling ping:");
                        //console.log(data);

                        //console.log("status in error calling ping:");
                        //console.log(status);

                        console.log("config.url in error calling ping:");
                        console.log(config.url);

                        partialElementList[0].children.forEach(function(server) {

                            //console.log("error calling ping operation, inside loop, server.host: "+ server.host + ", pingResponseHost: " + pingResponseHost);
/*
                            if (server.host == pingResponseHost){

                                console.log("going to change status to unable to connect for server: " + server.host);
                                //change server status
                                server.status = 'UNABLE TO CONNECT';
                            }
                            */
                        });

                    });




                //for each server, add the sensor management element

                //sensor management element:
                var sensorManagementElement = {
                    "host" : server.restProtocol + "://" + server.ipAddress + ":" + server.restPort,
                    "restProtocol":server.restProtocol,
                    "ipAddress":server.ipAddress,
                    "restPort":server.restPort,
                    "elementName" : "Sensor Management",
                    "elementId" : "sensorManagement",
                    "collapsed":true,
                    "contextMenuId": "contextMenuSensorManagement",
                    "children" : [] };
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

                            var sensorElement = {
                                "elementName" : serviceID.nodeValue,
                                "elementId" : serviceID.nodeValue,
                                "collapsed":true,
                                "contextMenuId" : "contextMenuSensor",
                                "children" : [] };

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
                                            "contextMenuId" : "contextMenuSession",
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
                                                    /*
                                                    console.log("sensor.elementId");
                                                    console.log(sensor.elementId);
                                                    console.log("responseServiceId");
                                                    console.log(responseServiceId);
                                                    console.log("-------------------------------------");
                                                    */



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

                //for each server, add the command management element
                //command management element:
                var commandManagementElement = { "host" : server.ipAddress + ":" + server.restPort, "elementName" : "Command Management", "elementId" : "commandManagement", "collapsed":true, "children" : [] };
                server.children.push(commandManagementElement);


                //call the readertypes operation
                $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/readertypes')
                    .success(function(data, status, headers, config) {

                        var readerTypesResponseHost = headers('host');

                        var xmlReaderTypes;
                        if (window.DOMParser)
                        {
                            var parser = new DOMParser();
                            xmlReaderTypes = parser.parseFromString(data,"text/xml");
                        }
                        else // Internet Explorer
                        {
                            xmlReaderTypes = new ActiveXObject("Microsoft.XMLDOM");
                            xmlReaderTypes.async=false;
                            xmlReaderTypes.loadXML(data);
                        }

                        //get the xml response and extract the values to construct the reader type object
                        var readerTypesXmlVector = xmlReaderTypes.getElementsByTagName("sensor");

                        for(var index = 0; index < readerTypesXmlVector.length; index++) {

                            var factoryID = readerTypesXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];
                            var description = readerTypesXmlVector[index].getElementsByTagName("description")[0].childNodes[0];

                            //Add the reader type to the command management element

                            partialElementList[0].children.forEach(function(server) {

                                if (server.host == readerTypesResponseHost) {

                                    //add the reader type under Command Management element

                                    //define an empty command structure to hold the current command
                                    var readerTypeElement = {
                                        "elementName": factoryID.nodeValue + " Commands",
                                        "elementId": factoryID.nodeValue + " Commands",
                                        "collapsed": true,
                                        "factoryID": factoryID.nodeValue,
                                        "description": description.nodeValue,
                                        "children": []
                                    };

                                    server.children[1].children.push(readerTypeElement);

                                }

                            });

                        }

                        //load the command types and place them under corresponding reader type

                        $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/commandtypes')
                            .success(function(data, status, headers, config) {

                                var commandTypesResponseHost = headers('host');

                                var xmlCommandTypes;
                                if (window.DOMParser)
                                {
                                    var parser = new DOMParser();
                                    xmlCommandTypes = parser.parseFromString(data,"text/xml");
                                }
                                else // Internet Explorer
                                {
                                    xmlCommandTypes = new ActiveXObject("Microsoft.XMLDOM");
                                    xmlCommandTypes.async=false;
                                    xmlCommandTypes.loadXML(data);
                                }

                                //get the xml response and extract the values to construct the local command type object
                                var commandTypeXmlVector = xmlCommandTypes.getElementsByTagName("command");



                                for(var index = 0; index < commandTypeXmlVector.length; index++) {

                                    var factoryID = commandTypeXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];
                                    var description = commandTypeXmlVector[index].getElementsByTagName("description")[0].childNodes[0];
                                    var readerFactoryID = commandTypeXmlVector[index].getElementsByTagName("readerFactoryID")[0].childNodes[0];

                                    //Iterate the children of command management element associated to the server that is equals to the server
                                    //that sends this response, and add the command type under appropriate reader type

                                    partialElementList[0].children.forEach(function(server) {

                                        if (server.host == commandTypesResponseHost){

                                            server.children[1].children.forEach(function(readerTypeElement) {

                                                //console.log("evaluating readerFactoryElement.readerFactoryID: " + readerTypeElement.readerFactoryID);

                                                if (readerTypeElement.factoryID == readerFactoryID.nodeValue){

                                                    //define a command type and add it to this reader type                                                    //add the factory id element to this readerFactoryElement
                                                    var commandTypeElement = {
                                                        "elementName": factoryID.nodeValue,
                                                        "elementId": factoryID.nodeValue,
                                                        "collapsed": true,
                                                        "factoryID": factoryID.nodeValue,
                                                        "description": description.nodeValue,
                                                        "readerFactoryID": readerFactoryID.nodeValue,
                                                        "children": []

                                                    }

                                                    readerTypeElement.children.push(commandTypeElement);

                                                }

                                            });

                                        }
                                    });

                                }

                                //call the commands service to load the commands associated with each command type

                                $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/commands')
                                    .success(function(data, status, headers, config) {

                                        var commandsResponseHost = headers('host');

                                        var xmlCommands;
                                        if (window.DOMParser)
                                        {
                                            var parser = new DOMParser();
                                            xmlCommands = parser.parseFromString(data,"text/xml");
                                        }
                                        else // Internet Explorer
                                        {
                                            xmlCommands = new ActiveXObject("Microsoft.XMLDOM");
                                            xmlCommands.async=false;
                                            xmlCommands.loadXML(data);
                                        }

                                        //get the xml response and extract the values to construct the local command object
                                        var commandXmlVector = xmlCommands.getElementsByTagName("command");

                                        for(var index = 0; index < commandXmlVector.length; index++) {

                                            var commandID = commandXmlVector[index].getElementsByTagName("commandID")[0].childNodes[0];
                                            var factoryID = commandXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];

                                            //Iterate the children of command factory id element associated to the server that is equals to the server
                                            //that sends this response, and if factoryID are equal, then associate the command to that command factory

                                            partialElementList[0].children.forEach(function(server) {

                                                if (server.host == commandsResponseHost) {

                                                    //search for this server, if there exist a commandElement with this factoryID

                                                    server.children[1].children.forEach(function (readerFactoryElement) {

                                                        readerFactoryElement.children.forEach(function (factoryElement) {

                                                            if (factoryElement.factoryID ==  factoryID.nodeValue){

                                                                //define a command structure to hold the current command
                                                                var commandElement = {
                                                                    "elementName": commandID.nodeValue,
                                                                    "elementId": commandID.nodeValue,
                                                                    "collapsed": true,
                                                                    "commandID": commandID.nodeValue,
                                                                    "factoryID": factoryID.nodeValue,
                                                                    "children": []
                                                                };

                                                                factoryElement.children.push(commandElement);

                                                            }

                                                        });

                                                    });

                                                }

                                            });

                                        }

                                    }).
                                    error(function(data, status, headers, config) {
                                        console.log("error reading commands");


                                        // called asynchronously if an error occurs
                                        // or server returns response with an error status.
                                    });


                            }).
                            error(function(data, status, headers, config) {
                                console.log("error reading command types");


                                // called asynchronously if an error occurs
                                // or server returns response with an error status.
                            });












                    }).
                    error(function(data, status, headers, config) {
                        console.log("error reading reader types");


                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                    });



                //aca iba






                //for each server, add the app management element
                //app management element:
                var appManagementElement = { "elementName" : "App Management", "elementId" : "App Management", "collapsed":true, "children" : [] };
                server.children.push(appManagementElement);

                //add application groups element

                var appGroupsElement = { "elementName" : "App Groups", "elementId" : "App Group", "collapsed":true, "children" : [] };

                appManagementElement.children.push(appGroupsElement);

                //load app groups

                //call apps command
                $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/apps')

                    .success(function(data, status, headers, config) {

                        var appsResponseHost = headers('host');

                        var xmlApps;
                        if (window.DOMParser)
                        {
                            var parser = new DOMParser();
                            xmlApps = parser.parseFromString(data,"text/xml");
                        }
                        else // Internet Explorer
                        {
                            xmlApps = new ActiveXObject("Microsoft.XMLDOM");
                            xmlApps.async=false;
                            xmlApps.loadXML(data);
                        }

                        //get the xml response and extract the values to construct the app groups
                        var appsXmlVector = xmlApps.getElementsByTagName("app");

                        for(var index = 0; index < appsXmlVector.length; index++) {

                            var id = appsXmlVector[index].getElementsByTagName("id")[0].childNodes[0];
                            var number = appsXmlVector[index].getElementsByTagName("number")[0].childNodes[0];
                            var status = appsXmlVector[index].getElementsByTagName("status")[0].childNodes[0];

                            var groupName = id.nodeValue.split(":")[0];
                            var appName = id.nodeValue.split(":")[1];

                            //Iterate the children of app groups element associated to the server that is equals to the server
                            //that sends this response, and if this app group does not exist under that structure, then create it

                            partialElementList[0].children.forEach(function(server) {

                                if (server.host == appsResponseHost){

                                    //search for this server, if there exist an appGroupElement with this groupName

                                    var appGroupFound = false;

                                    server.children[2].children[0].children.forEach(function(appGroupElement) {

                                        if (appGroupElement.groupName == groupName){

                                            console.log("readerFactoryFound");

                                            appGroupFound = true;
                                            //TODO How to break here this loop (when appGroupFound == true)

                                        }

                                    });

                                    if (appGroupFound == false){

                                        //create the appGroupElement and add it
                                        var appGroupElement = {
                                            "elementName" : groupName,
                                            "elementId" : groupName,
                                            "collapsed":true,
                                            "groupName": groupName,
                                            "readzoneAppId": "",
                                            "children" : []
                                        };

                                        //Add Apps element label
                                        var appsElement = {
                                            "elementName" : "Apps",
                                            "elementId" : "Apps",
                                            "collapsed":true,
                                            "groupName": groupName,
                                            "children" : []
                                        };

                                        appGroupElement.children.push(appsElement);

                                        //Add Readzones element label
                                        var readZonesElement = {
                                            "elementName" : "ReadZones",
                                            "elementId" : "ReadZones",
                                            "collapsed":true,
                                            "groupName": groupName,
                                            "children" : []
                                        };

                                        appGroupElement.children.push(readZonesElement);

                                        server.children[2].children[0].children.push(angular.copy(appGroupElement));


                                    }

                                    //After adding the appGroup if it did not exist, we have to associate the current
                                    //application to its corresponding app group

                                    //iterate the apps elements

                                    server.children[2].children[0].children.forEach(function(appGroupElement) {

                                        if ( appGroupElement.groupName == groupName ){

                                            //add the app to this appGroup
                                            var appElement = {
                                                "elementName": appName,
                                                "elementId": appName,
                                                "collapsed": true,
                                                "groupName": groupName,
                                                "appName": appName,
                                                "number": number.nodeValue,
                                                "status": status.nodeValue,
                                                "children": []

                                            }

                                            appGroupElement.children[0].children.push(appElement);

                                            //Add the application number to this application group in order to later add the associated read zones
                                            if (appGroupElement.readzoneAppId == ""){
                                                appGroupElement.readzoneAppId = appElement.number;
                                            }


                                        }

                                    });


                                }
                            });

                        }

                        //add the readzones

                        //call readzones command
                        //iterate the app groups an call the readzones command


                        server.children[2].children[0].children.forEach(function(appGroupElement) {



                            $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/getReadZones/' + appGroupElement.readzoneAppId)

                                .success(function(data, status, headers, config) {

                                    //console.log("config en getreadzones:");
                                    //console.log(config);

                                    var successReadzoneAppId = config.url.substring(config.url.lastIndexOf("/") + 1, config.url.length);
                                    //console.log("successReadzoneAppId:" + successReadzoneAppId);

                                    var readzonesResponseHost = headers('host');

                                    var xmlReadzones;
                                    if (window.DOMParser)
                                    {
                                        var parser = new DOMParser();
                                        xmlReadzones = parser.parseFromString(data,"text/xml");
                                    }
                                    else // Internet Explorer
                                    {
                                        xmlReadzones = new ActiveXObject("Microsoft.XMLDOM");
                                        xmlReadzones.async=false;
                                        xmlReadzones.loadXML(data);
                                    }

                                    //get the xml response and extract the values to construct the readzone element
                                    var readzonesXmlVector = xmlReadzones.getElementsByTagName("entry");


                                    partialElementList[0].children.forEach(function(server) {

                                        if (server.host == readzonesResponseHost){

                                            //iterate to find the app group where to add this readzone
                                            server.children[2].children[0].children.forEach(function(appGroupElement) {

                                                if(appGroupElement.readzoneAppId == successReadzoneAppId){

                                                    for(var index = 0; index < readzonesXmlVector.length; index++) {

                                                        var readzone = readzonesXmlVector[index].getElementsByTagName("value")[0].childNodes[0].nodeValue;

                                                        //create the readzoneElement and add it
                                                        var readzoneElement = {
                                                            "elementName": readzone,
                                                            "elementId": readzone,
                                                            "collapsed": true,
                                                            "readzone": readzone,
                                                            "children": []
                                                        };

                                                        appGroupElement.children[1].children.push(angular.copy(readzoneElement));
                                                    }

                                                }

                                            });


                                        }

                                    });



                                }).
                                error(function(data, status, headers, config) {
                                    console.log("error reading readzones");


                                    // called asynchronously if an error occurs
                                    // or server returns response with an error status.
                                });

                        });



                    }).
                    error(function(data, status, headers, config) {
                        console.log("error reading apps");


                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                    });




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
              console.log("newObj:");
              console.log(newObj);
              //console.log("oldObj:");
              //console.log(oldObj);
              if( $scope.elementTree && angular.isObject($scope.elementTree.currentNode) ) {
                  $scope.elementSelected=$scope.elementTree.currentNode;
                  $scope.propertyType=$scope.elementTree.currentNode.elementId;



              }

              console.log("$scope.elementSelected:");
              console.log($scope.elementSelected);
          }, false);

      },
      templateUrl: 'scripts/controllers/components/menu/menuRifidi.html'
    };

  });

