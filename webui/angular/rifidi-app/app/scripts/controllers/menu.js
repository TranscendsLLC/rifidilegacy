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
var module = angular.module('rifidiApp')
  .controller('MenuController', function ($scope, $http, ngDialog, TreeViewPainting) {


        $scope.elementSelected={};
        $scope.temporaryNode = {
              children: []
          };
        $scope.propertyType="servers";

        console.log("set propertyType = servers");
        $scope.mode = undefined;

        $scope.done = function () {
          /* reset */
          $scope.elementTree.currentNode.selected = undefined;
          $scope.elementTree.currentNode = undefined;
          $scope.mode = undefined;
        };

          /*
        $scope.callPaintTreeView = function(){
            paintTreeView();
        };
        */

        $scope.addServerDone = function () {
          console.log("addServerDone");
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

          var deleteSensor = function(){
              console.log("deleteSensor called");

              //call the rest operation to delete sensor
              $http.get($scope.elementSelected.host + '/deletereader/' + $scope.elementSelected.elementId)
                  .success(function (data, status, headers, config) {

                      var deleteReaderCommandResponse;
                      if (window.DOMParser) {
                          var parser = new DOMParser();
                          deleteReaderCommandResponse = parser.parseFromString(data, "text/xml");
                      }
                      else // Internet Explorer
                      {
                          deleteReaderCommandResponse = new ActiveXObject("Microsoft.XMLDOM");
                          deleteReaderCommandResponse.async = false;
                          deleteReaderCommandResponse.loadXML(data);
                      }

                      //get the xml response and extract the message response
                      var message = deleteReaderCommandResponse.getElementsByTagName("message")[0].childNodes[0].nodeValue;

                      //$scope.setCommandPropertiesResponseStatus.message = message;

                      if (message == 'Success') {
                          console.log("success deleting sensor");
                          TreeViewPainting.paintTreeView($scope);

                      } else {
                          var deleteReaderCommandDescription = deleteReaderCommandResponse.getElementsByTagName("description")[0].childNodes[0].nodeValue;
                          //$scope.setCommandPropertiesResponseStatus.description = setCommandPropertiesDescription;
                          console.log("fail deleting sensor");
                          console.log(deleteReaderCommandDescription);

                          //show modal dialog with error
                          showErrorDialog('Error deleting sensor: ' + deleteReaderCommandDescription);

                      }


                  }).
                  error(function (data, status, headers, config) {
                      console.log("error deleting sensor");

                      //show modal dialog with error
                      showErrorDialog('Error deleting sensor');


                      // called asynchronously if an error occurs
                      // or server returns response with an error status.
                  });


          };

          $scope.createSession = function(){

              console.log("createSession called");

              //call the rest operation to create session
              $http.get($scope.elementSelected.host + '/createsession/' + $scope.elementSelected.elementId)
                  .success(function (data, status, headers, config) {

                      var createSessionCommandResponse;
                      if (window.DOMParser) {
                          var parser = new DOMParser();
                          createSessionCommandResponse = parser.parseFromString(data, "text/xml");
                      }
                      else // Internet Explorer
                      {
                          createSessionCommandResponse = new ActiveXObject("Microsoft.XMLDOM");
                          createSessionCommandResponse.async = false;
                          createSessionCommandResponse.loadXML(data);
                      }

                      //get the xml response and extract the message response
                      var message = createSessionCommandResponse.getElementsByTagName("message")[0].childNodes[0].nodeValue;

                      //$scope.setCommandPropertiesResponseStatus.message = message;

                      if (message == 'Success') {
                          console.log("success creating session");
                          TreeViewPainting.paintTreeView($scope);

                      } else {
                          var createSessionCommandDescription = createSessionCommandResponse.getElementsByTagName("description")[0].childNodes[0].nodeValue;
                          console.log("fail creating session");
                          console.log(createSessionCommandDescription);

                          //show modal dialog with error
                          showErrorDialog('Error creating session: ' + createSessionCommandDescription);

                      }


                  }).
                  error(function (data, status, headers, config) {
                      console.log("error creating session");

                      //show modal dialog with error
                      showErrorDialog('Error creating session');


                      // called asynchronously if an error occurs
                      // or server returns response with an error status.
                  });
          };

          var deleteSession = function(){
              console.log("deleteSession called");

              //call the rest operation to delete session
              $http.get($scope.elementSelected.sensor.host + '/deletesession/' + $scope.elementSelected.sensor.elementId + "/" + $scope.elementSelected.sessionID)
                  .success(function (data, status, headers, config) {

                      var deleteSessionCommandResponse;
                      if (window.DOMParser) {
                          var parser = new DOMParser();
                          deleteSessionCommandResponse = parser.parseFromString(data, "text/xml");
                      }
                      else // Internet Explorer
                      {
                          deleteSessionCommandResponse = new ActiveXObject("Microsoft.XMLDOM");
                          deleteSessionCommandResponse.async = false;
                          deleteSessionCommandResponse.loadXML(data);
                      }

                      //get the xml response and extract the message response
                      var message = deleteSessionCommandResponse.getElementsByTagName("message")[0].childNodes[0].nodeValue;

                      if (message == 'Success') {
                          console.log("success deleting session");
                          TreeViewPainting.paintTreeView($scope);

                      } else {
                          var deleteSessionCommandDescription = deleteSessionCommandResponse.getElementsByTagName("description")[0].childNodes[0].nodeValue;
                          console.log("fail deleting session");
                          console.log(deleteSessionCommandDescription);

                          //show modal dialog with error
                          showErrorDialog('Error deleting session: ' + deleteSessionCommandDescription);

                      }


                  }).
                  error(function (data, status, headers, config) {
                      console.log("error deleting session");

                      //show modal dialog with error
                      showErrorDialog('Error deleting session');


                      // called asynchronously if an error occurs
                      // or server returns response with an error status.
                  });


          };

          $scope.startSession = function(){
              console.log("startSession called");

              //call the rest operation to start session
              $http.get($scope.elementSelected.sensor.host + '/startsession/' + $scope.elementSelected.sensor.elementId + "/" + $scope.elementSelected.sessionID)
                  .success(function (data, status, headers, config) {

                      var startSessionCommandResponse;
                      if (window.DOMParser) {
                          var parser = new DOMParser();
                          startSessionCommandResponse = parser.parseFromString(data, "text/xml");
                      }
                      else // Internet Explorer
                      {
                          startSessionCommandResponse = new ActiveXObject("Microsoft.XMLDOM");
                          startSessionCommandResponse.async = false;
                          startSessionCommandResponse.loadXML(data);
                      }

                      //get the xml response and extract the message response
                      var message = startSessionCommandResponse.getElementsByTagName("message")[0].childNodes[0].nodeValue;

                      if (message == 'Success') {
                          console.log("success starting session");
                          TreeViewPainting.paintTreeView($scope);

                      } else {
                          var startSessionCommandDescription = startSessionCommandResponse.getElementsByTagName("description")[0].childNodes[0].nodeValue;
                          console.log("fail starting session");
                          console.log(startSessionCommandDescription);

                          //show modal dialog with error
                          showErrorDialog('Error starting session: ' + startSessionCommandDescription);

                      }


                  }).
                  error(function (data, status, headers, config) {
                      console.log("error starting session");

                      //show modal dialog with error
                      showErrorDialog('Error starting session');


                      // called asynchronously if an error occurs
                      // or server returns response with an error status.
                  });


          };

          $scope.stopSession = function(){
              console.log("stopSession called");

              //call the rest operation to stop session
              $http.get($scope.elementSelected.sensor.host + '/stopsession/' + $scope.elementSelected.sensor.elementId + "/" + $scope.elementSelected.sessionID)
                  .success(function (data, status, headers, config) {

                      var stopSessionCommandResponse;
                      if (window.DOMParser) {
                          var parser = new DOMParser();
                          stopSessionCommandResponse = parser.parseFromString(data, "text/xml");
                      }
                      else // Internet Explorer
                      {
                          stopSessionCommandResponse = new ActiveXObject("Microsoft.XMLDOM");
                          stopSessionCommandResponse.async = false;
                          stopSessionCommandResponse.loadXML(data);
                      }

                      //get the xml response and extract the message response
                      var message = stopSessionCommandResponse.getElementsByTagName("message")[0].childNodes[0].nodeValue;

                      if (message == 'Success') {
                          console.log("success stopping session");
                          TreeViewPainting.paintTreeView($scope);

                      } else {
                          var stopSessionCommandDescription = stopSessionCommandResponse.getElementsByTagName("description")[0].childNodes[0].nodeValue;
                          console.log("fail stopping session");
                          console.log(stopSessionCommandDescription);

                          //show modal dialog with error
                          showErrorDialog('Error stopping session: ' + stopSessionCommandDescription);

                      }


                  }).
                  error(function (data, status, headers, config) {
                      console.log("error stopping session");

                      //show modal dialog with error
                      showErrorDialog('Error stopping session');


                      // called asynchronously if an error occurs
                      // or server returns response with an error status.
                  });


          };


          function showErrorDialog(errorMsg) {

              $scope.errorMsg = errorMsg;

              ngDialog.openConfirm({template: 'errorDialogTmpl.html',

                  scope: $scope, //Pass the scope object if you need to access in the template

                  showClose: false,

                  closeByEscape: true,

                  closeByDocument: false

              }).then(

                  function(value) {

                      //confirm operation
                      console.log("first");

                  },

                  function(value) {

                      //Cancel or do nothing
                      console.log("second");
                      console.log("value: " + value);




                  }

              );

          };

          $scope.openDeleteServerDialog = function() {

              ngDialog.openConfirm({template: 'deleteServerDialogTmpl.html',

                  scope: $scope, //Pass the scope object if you need to access in the template

                  showClose: false,

                  closeByEscape: true,

                  closeByDocument: false

              }).then(

                  function(value) {

                      //confirm operation
                      if (value == 'Delete'){
                          console.log("to delete");

                          //call delete server operation
                      }

                  },

                  function(value) {

                      //Cancel or do nothing
                      console.log("cancel");

                  }

              );

          };


          $scope.openDeleteSensorDialog = function() {

              ngDialog.openConfirm({template: 'deleteSensorDialogTmpl.html',

                  scope: $scope, //Pass the scope object if you need to access in the template

                  showClose: false,

                  closeByEscape: true,

                  closeByDocument: false

              }).then(

                  function(value) {

                      //confirm operation
                      if (value == 'Delete'){
                          console.log("to delete");

                          //call delete sensor operation
                          deleteSensor();

                      }

                  },

                  function(value) {

                      //Cancel or do nothing
                      console.log("cancel");

                  }

              );

          };

          $scope.openDeleteSessionDialog = function() {

              ngDialog.openConfirm({template: 'deleteSessionDialogTmpl.html',

                  scope: $scope, //Pass the scope object if you need to access in the template

                  showClose: false,

                  closeByEscape: true,

                  closeByDocument: false

              }).then(

                  function(value) {

                      //confirm operation
                      if (value == 'Delete'){
                          console.log("to delete");

                          //call delete sensor operation
                          deleteSession();

                      }

                  },

                  function(value) {

                      //Cancel or do nothing
                      console.log("cancel");

                  }

              );

          };

          $scope.openSaveServerPropertiesDialog = function() {

              ngDialog.openConfirm({template: 'saveServerPropertiesDialogTmpl.html',

                  scope: $scope, //Pass the scope object if you need to access in the template

                  showClose: false,

                  closeByEscape: true,

                  closeByDocument: false

                  /*
                  preCloseCallback: function(value) {

                      console.log("preCloseCallback value");
                      console.log(value);

                      if(confirm('Are you sure you want to close without saving your changes?')) {
                          return true;
                      }
                      return false;
                  }
                  */


              }).then(

                  function(value) {

                      //confirm operation
                      if (value == 'Save'){
                          console.log("to save");
                          console.log("old display name:");
                          console.log($scope.elementTree.currentNode.displayName);
                          console.log("new server values:");
                          console.log($scope.elementSelected);

                          //Validate server display name is unique only if display name has changed

                          console.log("$scope.elementTree.currentNode.displayName:");
                          console.log($scope.elementTree.currentNode.displayName);
                          console.log("$scope.elementSelected.displayName:");
                          console.log($scope.elementSelected.displayName);

                          if ($scope.elementTree.currentNode.displayName != $scope.elementSelected.displayName) {

                              console.log("displayName with change");

                              $http.get('scripts/controllers/components/menu/servers.json').
                                  success(function (data, status, headers, config) {

                                      var displayNameisUnique = true;

                                      data.forEach(function (server) {

                                          if (server.displayName == $scope.elementSelected.displayName) {

                                              //displayName already exists, then can not be created again
                                              displayNameisUnique = false;
                                          }

                                      });

                                      if (displayNameisUnique) {

                                          //save server properties
                                          saveServerProperties($scope.elementSelected);


                                      } else {

                                          console.log("duplicate server display name");

                                          $scope.errorMessage = "Duplicate server display name " + $scope.elementSelected.displayName;

                                          //do not create server, and display error message
                                          ngDialog.openConfirm({
                                              template: 'errorMsgModifyingServerPropertiesTmpl.html',

                                              scope: $scope, //Pass the scope object if you need to access in the template

                                              showClose: false,

                                              closeByEscape: true,

                                              closeByDocument: false


                                          }).then(
                                              function (value) {
                                                  console.log("ok");

                                              },

                                              function (value) {

                                                  //Cancel or do nothing
                                                  console.log("cancel");


                                              }
                                          );


                                          //$scope.serverCreationStatus.status = 'Fail';
                                          //$scope.serverCreationStatus.message = 'Display name ' + $scope.serverToCreate.displayName + ' already exists';

                                      }

                                  }).
                                  error(function (data, status, headers, config) {
                                      console.log("error reading servers on modifying server properties");


                                      // called asynchronously if an error occurs
                                      // or server returns response with an error status.
                                  });
                          } else {

                              saveServerProperties($scope.elementSelected);

                          }

                      }

                  },

                  function(value) {

                      //Cancel or do nothing
                      console.log("cancel");

                  }

              );

          };


          $scope.deleteServer=function(){
              console.log("delete $scope.elementTree.currentNode")

              //console.log($scope.elementTree.currentNode);
              console.log("$scope.elementTree");
              console.log($scope.elementTree.currentNode);

              //ngDialog.open({ template: 'popupTmpl.html' });
              //ngDialog.open({ template: '<p>my template</p>', plain:true });

              /*
              ngDialog.open({
                  preCloseCallback: function(value) {
                      if(confirm('Are you sure you want to close without saving your changes?')) {
                          return true;
                      }
                      return false;
                  }
              });
              */

              /*
              ngDialog.open({
                  preCloseCallback: function(value) {
                      var nestedConfirmDialog = ngDialog.openConfirm({
                          template:'\
                <p>Are you sure you want to close the parent dialog?</p>\
                <div class="ngdialog-buttons">\
                    <button type="button" class="ngdialog-button ngdialog-button-secondary" ng-click="closeThisDialog(0)">No</button>\
                    <button type="button" class="ngdialog-button ngdialog-button-primary" ng-click="confirm(1)">Yes</button>\
                </div>',
                          plain: true
                      });

                      // NOTE: return the promise from openConfirm
                      return nestedConfirmDialog;
                  }
              });
              */




          };

        var saveServerProperties = function(server) {

            console.log("saveServerProperties op success");
            $scope.operationSuccessMsg = "Save server properties operation success";
            //$scope.$apply();
        }

          $scope.saveServer = function(){
              console.log("saveServer");
              console.log("$scope.elementSelected");
              console.log($scope.elementSelected);
              console.log("$scope.elementTree.currentNode");
              console.log($scope.elementTree.currentNode);
              console.log("show alert dialog to confirm");

          }

          //$scope.refreshMenuTreeView;

          //$scope.refreshMenuTreeView = function () {

          TreeViewPainting.paintTreeView($scope);

              $http.get('scripts/controllers/components/menu/servers.json').
                  success(function (data, status, headers, config) {
                      console.log("funciono lectura servers ");
                      $scope.servers = data;

                      var partialElementList = [{
                          "elementName": "Servers",
                          "elementId": "servers",
                          "collapsed": true,
                          "contextMenuId": "contextMenuServers",
                          "iconClass":"server",
                          "children": []
                      }];

                      data.forEach(function (server) {

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
                              .success(function (data, status, headers, config) {

                                  var pingResponseHost = headers('host');

                                  var xmlPingTimestamp;
                                  if (window.DOMParser) {
                                      var parser = new DOMParser();
                                      xmlPingTimestamp = parser.parseFromString(data, "text/xml");
                                  }
                                  else // Internet Explorer
                                  {
                                      xmlPingTimestamp = new ActiveXObject("Microsoft.XMLDOM");
                                      xmlPingTimestamp.async = false;
                                      xmlPingTimestamp.loadXML(data);
                                  }

                                  //get the xml response and extract the ping timestamp value
                                  var timestampXmlVector = xmlPingTimestamp.getElementsByTagName("timestamp");

                                  var serverTimestamp = timestampXmlVector[0].childNodes[0].nodeValue;

                                  if (serverTimestamp) {
                                      console.log("server ping, ip: " + pingResponseHost + ", timestamp: " + serverTimestamp);

                                      //change server connecting status to connected

                                      partialElementList[0].children.forEach(function (server) {
                                          if (server.host == pingResponseHost) {

                                              //change server status
                                              server.status = 'CONNECTED';
                                              server.iconClass = "server-connected";
                                          }
                                      });

                                  }

                              })
                              .error(function (data, status, headers, config) {
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

                                  partialElementList[0].children.forEach(function (server) {

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
                              "host": server.host,
                              "restProtocol": server.restProtocol,
                              "ipAddress": server.ipAddress,
                              "restPort": server.restPort,
                              "elementName": "Sensor Management",
                              "elementId": "sensorManagement",
                              "collapsed": true,
                              "iconClass":"reader-cog",
                              "contextMenuId": "contextMenuSensorManagement",
                              "children": []
                          };
                          server.children.push(sensorManagementElement);

                          //for each server, connect and query the list of sensors and place them under sensor management element
                          $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/readers')
                              .success(function (data, status, headers, config) {

                                  var sensorsResponseHost = headers('host');

                                  var xmlSensors;
                                  if (window.DOMParser) {
                                      var parser = new DOMParser();
                                      xmlSensors = parser.parseFromString(data, "text/xml");
                                  }
                                  else // Internet Explorer
                                  {
                                      xmlSensors = new ActiveXObject("Microsoft.XMLDOM");
                                      xmlSensors.async = false;
                                      xmlSensors.loadXML(data);
                                  }

                                  //get the xml response and extract the values to construct the local sensor object
                                  var sensorXmlVector = xmlSensors.getElementsByTagName("sensor");

                                  for (var index = 0; index < sensorXmlVector.length; index++) {
                                      var serviceID = sensorXmlVector[index].getElementsByTagName("serviceID")[0].childNodes[0];
                                      var factoryID = sensorXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];

                                      //console.log("serviceID: " + serviceID.nodeValue);
                                      //console.log("factoryID: " + factoryID.nodeValue);

                                      var sensorElement = {
                                          "elementName": serviceID.nodeValue,
                                          "elementId": serviceID.nodeValue,
                                          "collapsed": true,
                                          "contextMenuId": "contextMenuSensor",
                                          "iconClass":"reader",
                                          "allowCreateSession": false,
                                          "children": []
                                      };

                                      //for this responseHost search which sensorManagementElement is associated with, and associate the sensorElement
                                      //console.log("headers('host') from readers service: " + headers('host'));

                                      partialElementList[0].children.forEach(function (server) {
                                          if (server.host == sensorsResponseHost) {

                                              //add this sensor to the server
                                              sensorElement.host = server.host;
                                              server.children[0].children.push(sensorElement);
                                          }
                                      });

                                      //retrieve the sessions associated with this sensor element
                                      $http.get(sensorsResponseHost + '/readerstatus/' + serviceID.nodeValue)
                                          .success(function (data, status, headers, config) {

                                              var sessionsResponseHost = headers('host');

                                              //console.log("data for readerstatus from host: " + sessionsResponseHost + ": " + data);

                                              var xmlSessions;
                                              if (window.DOMParser) {
                                                  var parser = new DOMParser();
                                                  xmlSessions = parser.parseFromString(data, "text/xml");
                                              }
                                              else // Internet Explorer
                                              {
                                                  xmlSessions = new ActiveXObject("Microsoft.XMLDOM");
                                                  xmlSessions.async = false;
                                                  xmlSessions.loadXML(data);
                                              }

                                              //get the xml response and extract the values to construct the local session object

                                              var responseServiceId = xmlSessions.getElementsByTagName("sensor")[0].getElementsByTagName("serviceID")[0].childNodes[0].nodeValue;
                                              //console.log("responseServiceId: ");
                                              //console.log(responseServiceId);

                                              var sessionXmlVector = xmlSessions.getElementsByTagName("session");

                                              var allowCreateSession = (sessionXmlVector.length == 0);

                                              //search the reader with reader id = responseServiceId and assign allowCreateSession

                                              server.children[0].children.forEach(function (sensor) {

                                                  if (sensor.elementId == responseServiceId) {

                                                      sensor.allowCreateSession = allowCreateSession;
                                                  }

                                              });


                                              for (var index = 0; index < sessionXmlVector.length; index++) {
                                                  var sessionID = sessionXmlVector[index].getElementsByTagName("ID")[0].childNodes[0];
                                                  var sessionStatus = sessionXmlVector[index].getElementsByTagName("status")[0].childNodes[0];

                                                  //console.log("sessionID: " + sessionID.nodeValue);
                                                  //console.log("sessionStatus: " + sessionStatus.nodeValue);

                                                  var sessionElement = {
                                                      "sessionID": sessionID.nodeValue,
                                                      "elementName": "session " + sessionID.nodeValue,
                                                      "elementId": "session " + sessionID.nodeValue,
                                                      "collapsed": true,
                                                      "status": sessionStatus.nodeValue,
                                                      "contextMenuId": "contextMenuSession",
                                                      "children": []
                                                  };

                                                  //Assign the icon depending on status:
                                                  if (sessionStatus.nodeValue == 'CREATED' || sessionStatus.nodeValue == 'CLOSED'){
                                                      sessionElement.iconClass = 'link-red';
                                                  } else if (sessionStatus.nodeValue == 'CONNECTING'){
                                                      sessionElement.iconClass = 'link-yellow';
                                                  }  else if (sessionStatus.nodeValue == 'PROCESSING'){
                                                      sessionElement.iconClass = 'link-green';
                                                  }



                                                  //extract the executing commands form this session and append as childern element
                                                  var executingCommandsXmlVector = xmlSessions.getElementsByTagName("executingcommand");

                                                  for (var index = 0; index < executingCommandsXmlVector.length; index++) {
                                                      var commandID = executingCommandsXmlVector[index].getElementsByTagName("commandID")[0].childNodes[0];
                                                      var commandInterval = executingCommandsXmlVector[index].getElementsByTagName("interval")[0].childNodes[0];

                                                      var commandElement = {
                                                          "elementName": commandID.nodeValue,
                                                          "elementId": commandID.nodeValue,
                                                          "collapsed": true,
                                                          "interval": commandInterval.nodeValue,
                                                          "iconClass": 'script-gear',
                                                          "children": []
                                                      };

                                                      sessionElement.children.push(commandElement);

                                                  }


                                                  //for this responseHost search which sensorElement is associated with, and associate the sensorElement
                                                  //console.log("headers('host') from sessions service: " + headers('host'));

                                                  partialElementList[0].children.forEach(function (server) {


                                                      if (server.host == sessionsResponseHost) {

                                                          //console.log("server.host == sessionsResponseHost:");
                                                          //console.log(server.host);

                                                          //console.log("XX server.host:");
                                                          //console.log(server.host);

                                                          //console.log("sessionsResponseHost:");
                                                          //console.log(sessionsResponseHost);

                                                          //responseServiceId is the service id that this response belongs to

                                                          //search for this server, which sensor is going to hold the session

                                                          server.children[0].children.forEach(function (sensor) {

                                                              //console.log("iter sensor:");
                                                              //console.log(sensor);
                                                              /*
                                                               console.log("sensor.elementId");
                                                               console.log(sensor.elementId);
                                                               console.log("responseServiceId");
                                                               console.log(responseServiceId);
                                                               console.log("-------------------------------------");
                                                               */


                                                              if (sensor.elementId == responseServiceId) {
                                                                  /*
                                                                   console.log("ZZ server.host:");
                                                                   console.log(server.host);

                                                                   console.log("sensor.elementId:");
                                                                   console.log(sensor.elementId);

                                                                   console.log("responseServiceId:");
                                                                   console.log(responseServiceId);
                                                                   */
                                                                  sessionElement.sensor = sensor;
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
                                          .error(function (data, status, headers, config) {
                                              console.log("error leyendo sesiones serviceID.nodeValue: " + serviceID.nodeValue);

                                              // called asynchronously if an error occurs
                                              // or server returns response with an error status.
                                          });


                                      //this.apps[this.apps.length] = {id:localId.textContent, number:localNumber.textContent, status:tLocalStatus.textContent};
                                  }


                              })
                              .error(function (data, status, headers, config) {
                                  console.log("error leyendo sensores server.ipAddress: " + server.ipAddress);

                                  // called asynchronously if an error occurs
                                  // or server returns response with an error status.
                              });

                          //for each server, add the command management element
                          //command management element:
                          var commandManagementElement = {
                              "host": server.ipAddress + ":" + server.restPort,
                              "elementName": "Command Management",
                              "elementId": "commandManagement",
                              "collapsed": true,
                              "iconClass": 'cog',
                              "children": []
                          };
                          server.children.push(commandManagementElement);


                          //call the readertypes operation
                          $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/readertypes')
                              .success(function (data, status, headers, config) {

                                  var readerTypesResponseHost = headers('host');

                                  var xmlReaderTypes;
                                  if (window.DOMParser) {
                                      var parser = new DOMParser();
                                      xmlReaderTypes = parser.parseFromString(data, "text/xml");
                                  }
                                  else // Internet Explorer
                                  {
                                      xmlReaderTypes = new ActiveXObject("Microsoft.XMLDOM");
                                      xmlReaderTypes.async = false;
                                      xmlReaderTypes.loadXML(data);
                                  }

                                  //get the xml response and extract the values to construct the reader type object
                                  var readerTypesXmlVector = xmlReaderTypes.getElementsByTagName("sensor");

                                  for (var index = 0; index < readerTypesXmlVector.length; index++) {

                                      var factoryID = readerTypesXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];
                                      var description = readerTypesXmlVector[index].getElementsByTagName("description")[0].childNodes[0];

                                      //Add the reader type to the command management element

                                      partialElementList[0].children.forEach(function (server) {

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
                                      .success(function (data, status, headers, config) {

                                          var commandTypesResponseHost = headers('host');

                                          var xmlCommandTypes;
                                          if (window.DOMParser) {
                                              var parser = new DOMParser();
                                              xmlCommandTypes = parser.parseFromString(data, "text/xml");
                                          }
                                          else // Internet Explorer
                                          {
                                              xmlCommandTypes = new ActiveXObject("Microsoft.XMLDOM");
                                              xmlCommandTypes.async = false;
                                              xmlCommandTypes.loadXML(data);
                                          }

                                          //get the xml response and extract the values to construct the local command type object
                                          var commandTypeXmlVector = xmlCommandTypes.getElementsByTagName("command");


                                          for (var index = 0; index < commandTypeXmlVector.length; index++) {

                                              var factoryID = commandTypeXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];
                                              var description = commandTypeXmlVector[index].getElementsByTagName("description")[0].childNodes[0];
                                              var readerFactoryID = commandTypeXmlVector[index].getElementsByTagName("readerFactoryID")[0].childNodes[0];

                                              //Iterate the children of command management element associated to the server that is equals to the server
                                              //that sends this response, and add the command type under appropriate reader type

                                              partialElementList[0].children.forEach(function (server) {

                                                  if (server.host == commandTypesResponseHost) {

                                                      server.children[1].children.forEach(function (readerTypeElement) {

                                                          //console.log("evaluating readerFactoryElement.readerFactoryID: " + readerTypeElement.readerFactoryID);

                                                          if (readerTypeElement.factoryID == readerFactoryID.nodeValue) {

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
                                              .success(function (data, status, headers, config) {

                                                  var commandsResponseHost = headers('host');

                                                  var xmlCommands;
                                                  if (window.DOMParser) {
                                                      var parser = new DOMParser();
                                                      xmlCommands = parser.parseFromString(data, "text/xml");
                                                  }
                                                  else // Internet Explorer
                                                  {
                                                      xmlCommands = new ActiveXObject("Microsoft.XMLDOM");
                                                      xmlCommands.async = false;
                                                      xmlCommands.loadXML(data);
                                                  }

                                                  //get the xml response and extract the values to construct the local command object
                                                  var commandXmlVector = xmlCommands.getElementsByTagName("command");

                                                  for (var index = 0; index < commandXmlVector.length; index++) {

                                                      var commandID = commandXmlVector[index].getElementsByTagName("commandID")[0].childNodes[0];
                                                      var factoryID = commandXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];

                                                      //Iterate the children of command factory id element associated to the server that is equals to the server
                                                      //that sends this response, and if factoryID are equal, then associate the command to that command factory

                                                      partialElementList[0].children.forEach(function (server) {

                                                          if (server.host == commandsResponseHost) {

                                                              //search for this server, if there exist a commandElement with this factoryID

                                                              server.children[1].children.forEach(function (readerFactoryElement) {

                                                                  readerFactoryElement.children.forEach(function (factoryElement) {

                                                                      if (factoryElement.factoryID == factoryID.nodeValue) {

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
                                              error(function (data, status, headers, config) {
                                                  console.log("error reading commands");


                                                  // called asynchronously if an error occurs
                                                  // or server returns response with an error status.
                                              });


                                      }).
                                      error(function (data, status, headers, config) {
                                          console.log("error reading command types");


                                          // called asynchronously if an error occurs
                                          // or server returns response with an error status.
                                      });


                              }).
                              error(function (data, status, headers, config) {
                                  console.log("error reading reader types");


                                  // called asynchronously if an error occurs
                                  // or server returns response with an error status.
                              });


                          //aca iba


                          //for each server, add the app management element
                          //app management element:
                          var appManagementElement = {
                              "elementName": "App Management",
                              "elementId": "App Management",
                              "collapsed": true,
                              "iconClass": "app-dev",
                              "children": []
                          };
                          server.children.push(appManagementElement);

                          //add application groups element

                          var appGroupsElement = {
                              "elementName": "App Groups",
                              "elementId": "App Group",
                              "collapsed": true,
                              "iconClass": "appgroups",
                              "children": []
                          };

                          appManagementElement.children.push(appGroupsElement);

                          //load app groups

                          //call apps command
                          $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/apps')

                              .success(function (data, status, headers, config) {

                                  var appsResponseHost = headers('host');

                                  var xmlApps;
                                  if (window.DOMParser) {
                                      var parser = new DOMParser();
                                      xmlApps = parser.parseFromString(data, "text/xml");
                                  }
                                  else // Internet Explorer
                                  {
                                      xmlApps = new ActiveXObject("Microsoft.XMLDOM");
                                      xmlApps.async = false;
                                      xmlApps.loadXML(data);
                                  }

                                  //get the xml response and extract the values to construct the app groups
                                  var appsXmlVector = xmlApps.getElementsByTagName("app");

                                  for (var index = 0; index < appsXmlVector.length; index++) {

                                      var id = appsXmlVector[index].getElementsByTagName("id")[0].childNodes[0];
                                      var number = appsXmlVector[index].getElementsByTagName("number")[0].childNodes[0];
                                      var status = appsXmlVector[index].getElementsByTagName("status")[0].childNodes[0];

                                      var groupName = id.nodeValue.split(":")[0];
                                      var appName = id.nodeValue.split(":")[1];

                                      //Iterate the children of app groups element associated to the server that is equals to the server
                                      //that sends this response, and if this app group does not exist under that structure, then create it

                                      partialElementList[0].children.forEach(function (server) {

                                          if (server.host == appsResponseHost) {

                                              //search for this server, if there exist an appGroupElement with this groupName

                                              var appGroupFound = false;

                                              server.children[2].children[0].children.forEach(function (appGroupElement) {

                                                  if (appGroupElement.groupName == groupName) {

                                                      console.log("readerFactoryFound");

                                                      appGroupFound = true;
                                                      //TODO How to break here this loop (when appGroupFound == true)

                                                  }

                                              });

                                              if (appGroupFound == false) {

                                                  //create the appGroupElement and add it
                                                  var appGroupElement = {
                                                      "elementName": groupName,
                                                      "elementId": groupName,
                                                      "collapsed": true,
                                                      "groupName": groupName,
                                                      "readzoneAppId": "",
                                                      "iconClass": "group",
                                                      "children": []
                                                  };

                                                  //Add Apps element label
                                                  var appsElement = {
                                                      "elementName": "Apps",
                                                      "elementId": "Apps",
                                                      "collapsed": true,
                                                      "groupName": groupName,
                                                      "iconClass": "apps",
                                                      "children": []
                                                  };

                                                  appGroupElement.children.push(appsElement);

                                                  //Add Readzones element label
                                                  var readZonesElement = {
                                                      "elementName": "ReadZones",
                                                      "elementId": "ReadZones",
                                                      "collapsed": true,
                                                      "groupName": groupName,
                                                      "iconClass": "readzones",
                                                      "children": []
                                                  };

                                                  appGroupElement.children.push(readZonesElement);

                                                  server.children[2].children[0].children.push(angular.copy(appGroupElement));


                                              }

                                              //After adding the appGroup if it did not exist, we have to associate the current
                                              //application to its corresponding app group

                                              //iterate the apps elements

                                              server.children[2].children[0].children.forEach(function (appGroupElement) {

                                                  if (appGroupElement.groupName == groupName) {

                                                      //add the app to this appGroup
                                                      var appElement = {
                                                          "elementName": appName,
                                                          "elementId": appName,
                                                          "collapsed": true,
                                                          "groupName": groupName,
                                                          "appName": appName,
                                                          "number": number.nodeValue,
                                                          "status": status.nodeValue,
                                                          "iconClass": "app",
                                                          "children": []

                                                      }

                                                      appGroupElement.children[0].children.push(appElement);

                                                      //Add the application number to this application group in order to later add the associated read zones
                                                      if (appGroupElement.readzoneAppId == "") {
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


                                  server.children[2].children[0].children.forEach(function (appGroupElement) {


                                      $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/getReadZones/' + appGroupElement.readzoneAppId)

                                          .success(function (data, status, headers, config) {

                                              //console.log("config en getreadzones:");
                                              //console.log(config);

                                              var successReadzoneAppId = config.url.substring(config.url.lastIndexOf("/") + 1, config.url.length);
                                              //console.log("successReadzoneAppId:" + successReadzoneAppId);

                                              var readzonesResponseHost = headers('host');

                                              var xmlReadzones;
                                              if (window.DOMParser) {
                                                  var parser = new DOMParser();
                                                  xmlReadzones = parser.parseFromString(data, "text/xml");
                                              }
                                              else // Internet Explorer
                                              {
                                                  xmlReadzones = new ActiveXObject("Microsoft.XMLDOM");
                                                  xmlReadzones.async = false;
                                                  xmlReadzones.loadXML(data);
                                              }

                                              //get the xml response and extract the values to construct the readzone element
                                              var readzonesXmlVector = xmlReadzones.getElementsByTagName("entry");


                                              partialElementList[0].children.forEach(function (server) {

                                                  if (server.host == readzonesResponseHost) {

                                                      //iterate to find the app group where to add this readzone
                                                      server.children[2].children[0].children.forEach(function (appGroupElement) {

                                                          if (appGroupElement.readzoneAppId == successReadzoneAppId) {

                                                              for (var index = 0; index < readzonesXmlVector.length; index++) {

                                                                  var readzone = readzonesXmlVector[index].getElementsByTagName("value")[0].childNodes[0].nodeValue;

                                                                  //create the readzoneElement and add it
                                                                  var readzoneElement = {
                                                                      "elementName": readzone,
                                                                      "elementId": readzone,
                                                                      "collapsed": true,
                                                                      "readzone": readzone,
                                                                      "iconClass": "readzone",
                                                                      "children": []
                                                                  };

                                                                  appGroupElement.children[1].children.push(angular.copy(readzoneElement));
                                                              }

                                                          }

                                                      });


                                                  }

                                              });


                                          }).
                                          error(function (data, status, headers, config) {
                                              console.log("error reading readzones");


                                              // called asynchronously if an error occurs
                                              // or server returns response with an error status.
                                          });

                                  });


                              }).
                              error(function (data, status, headers, config) {
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
                  error(function (data, status, headers, config) {
                      console.log("error leyendo servidores ");


                      // called asynchronously if an error occurs
                      // or server returns response with an error status.
                  });

          //}


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
                  $scope.elementSelected = angular.copy($scope.elementTree.currentNode);
                  $scope.propertyType = angular.copy($scope.elementTree.currentNode.elementId);

                  console.log("set 2 propertyType: " + $scope.propertyType);
                  $scope.operationSuccessMsg = null;



              }

              console.log("$scope.elementSelected:");
              console.log($scope.elementSelected);
          }, false);



  });

module.service('TreeViewPainting', function($http) {
       var service = {

         paintTreeView: function (scope) {





                //Paint tree view logic
                console.log("paintTreeView called");





             $http.get('scripts/controllers/components/menu/servers.json').
                 success(function (data, status, headers, config) {
                     console.log("funciono lectura servers ");

                     //this._scope = "";
                     this._scope = scope;
                     //this._scope.servers = [];
                     this._scope.servers = data;

                     var partialElementList = [{
                         "elementName": "Servers",
                         "elementId": "servers",
                         "collapsed": true,
                         "contextMenuId": "contextMenuServers",
                         "iconClass":"server",
                         "children": []
                     }];

                     data.forEach(function (server) {

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
                             .success(function (data, status, headers, config) {

                                 var pingResponseHost = headers('host');

                                 var xmlPingTimestamp;
                                 if (window.DOMParser) {
                                     var parser = new DOMParser();
                                     xmlPingTimestamp = parser.parseFromString(data, "text/xml");
                                 }
                                 else // Internet Explorer
                                 {
                                     xmlPingTimestamp = new ActiveXObject("Microsoft.XMLDOM");
                                     xmlPingTimestamp.async = false;
                                     xmlPingTimestamp.loadXML(data);
                                 }

                                 //get the xml response and extract the ping timestamp value
                                 var timestampXmlVector = xmlPingTimestamp.getElementsByTagName("timestamp");

                                 var serverTimestamp = timestampXmlVector[0].childNodes[0].nodeValue;

                                 if (serverTimestamp) {
                                     console.log("server ping, ip: " + pingResponseHost + ", timestamp: " + serverTimestamp);

                                     //change server connecting status to connected

                                     partialElementList[0].children.forEach(function (server) {
                                         if (server.host == pingResponseHost) {

                                             //change server status
                                             server.status = 'CONNECTED';
                                             server.iconClass = "server-connected";
                                         }
                                     });

                                 }

                             })
                             .error(function (data, status, headers, config) {
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

                                 partialElementList[0].children.forEach(function (server) {

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
                             "host": server.host,
                             "restProtocol": server.restProtocol,
                             "ipAddress": server.ipAddress,
                             "restPort": server.restPort,
                             "elementName": "Sensor Management",
                             "elementId": "sensorManagement",
                             "collapsed": true,
                             "iconClass":"reader-cog",
                             "contextMenuId": "contextMenuSensorManagement",
                             "children": []
                         };
                         server.children.push(sensorManagementElement);

                         //for each server, connect and query the list of sensors and place them under sensor management element
                         $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/readers')
                             .success(function (data, status, headers, config) {

                                 var sensorsResponseHost = headers('host');

                                 var xmlSensors;
                                 if (window.DOMParser) {
                                     var parser = new DOMParser();
                                     xmlSensors = parser.parseFromString(data, "text/xml");
                                 }
                                 else // Internet Explorer
                                 {
                                     xmlSensors = new ActiveXObject("Microsoft.XMLDOM");
                                     xmlSensors.async = false;
                                     xmlSensors.loadXML(data);
                                 }

                                 //get the xml response and extract the values to construct the local sensor object
                                 var sensorXmlVector = xmlSensors.getElementsByTagName("sensor");

                                 for (var index = 0; index < sensorXmlVector.length; index++) {
                                     var serviceID = sensorXmlVector[index].getElementsByTagName("serviceID")[0].childNodes[0];
                                     var factoryID = sensorXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];

                                     //console.log("serviceID: " + serviceID.nodeValue);
                                     //console.log("factoryID: " + factoryID.nodeValue);

                                     var sensorElement = {
                                         "elementName": serviceID.nodeValue,
                                         "elementId": serviceID.nodeValue,
                                         "collapsed": true,
                                         "contextMenuId": "contextMenuSensor",
                                         "iconClass":"reader",
                                         "children": []
                                     };

                                     //for this responseHost search which sensorManagementElement is associated with, and associate the sensorElement
                                     //console.log("headers('host') from readers service: " + headers('host'));

                                     partialElementList[0].children.forEach(function (server) {
                                         if (server.host == sensorsResponseHost) {

                                             //add this sensor to the server
                                             sensorElement.host = server.host;
                                             server.children[0].children.push(sensorElement);
                                         }
                                     });

                                     //retrieve the sessions associated with this sensor element
                                     $http.get(sensorsResponseHost + '/readerstatus/' + serviceID.nodeValue)
                                         .success(function (data, status, headers, config) {

                                             var sessionsResponseHost = headers('host');

                                             //console.log("data for readerstatus from host: " + sessionsResponseHost + ": " + data);

                                             var xmlSessions;
                                             if (window.DOMParser) {
                                                 var parser = new DOMParser();
                                                 xmlSessions = parser.parseFromString(data, "text/xml");
                                             }
                                             else // Internet Explorer
                                             {
                                                 xmlSessions = new ActiveXObject("Microsoft.XMLDOM");
                                                 xmlSessions.async = false;
                                                 xmlSessions.loadXML(data);
                                             }

                                             //get the xml response and extract the values to construct the local session object

                                             var responseServiceId = xmlSessions.getElementsByTagName("sensor")[0].getElementsByTagName("serviceID")[0].childNodes[0].nodeValue;
                                             //console.log("responseServiceId: ");
                                             //console.log(responseServiceId);

                                             var sessionXmlVector = xmlSessions.getElementsByTagName("session");

                                             for (var index = 0; index < sessionXmlVector.length; index++) {
                                                 var sessionID = sessionXmlVector[index].getElementsByTagName("ID")[0].childNodes[0];
                                                 var sessionStatus = sessionXmlVector[index].getElementsByTagName("status")[0].childNodes[0];

                                                 //console.log("sessionID: " + sessionID.nodeValue);
                                                 //console.log("sessionStatus: " + sessionStatus.nodeValue);

                                                 var sessionElement = {
                                                     "elementName": "session " + sessionID.nodeValue,
                                                     "elementId": "session " + sessionID.nodeValue,
                                                     "collapsed": true,
                                                     "status": sessionStatus.nodeValue,
                                                     "contextMenuId": "contextMenuSession",
                                                     "children": []
                                                 };

                                                 //Assign the icon depending on status:
                                                 if (sessionStatus.nodeValue == 'CREATED' || sessionStatus.nodeValue == 'CLOSED'){
                                                     sessionElement.iconClass = 'link-red';
                                                 } else if (sessionStatus.nodeValue == 'CONNECTING'){
                                                     sessionElement.iconClass = 'link-yellow';
                                                 }  else if (sessionStatus.nodeValue == 'PROCESSING'){
                                                     sessionElement.iconClass = 'link-green';
                                                 }



                                                 //extract the executing commands form this session and append as childern element
                                                 var executingCommandsXmlVector = xmlSessions.getElementsByTagName("executingcommand");

                                                 for (var index = 0; index < executingCommandsXmlVector.length; index++) {
                                                     var commandID = executingCommandsXmlVector[index].getElementsByTagName("commandID")[0].childNodes[0];
                                                     var commandInterval = executingCommandsXmlVector[index].getElementsByTagName("interval")[0].childNodes[0];

                                                     var commandElement = {
                                                         "elementName": commandID.nodeValue,
                                                         "elementId": commandID.nodeValue,
                                                         "collapsed": true,
                                                         "interval": commandInterval.nodeValue,
                                                         "iconClass": 'script-gear',
                                                         "children": []
                                                     };

                                                     sessionElement.children.push(commandElement);

                                                 }


                                                 //for this responseHost search which sensorElement is associated with, and associate the sensorElement
                                                 //console.log("headers('host') from sessions service: " + headers('host'));

                                                 partialElementList[0].children.forEach(function (server) {


                                                     if (server.host == sessionsResponseHost) {

                                                         //console.log("server.host == sessionsResponseHost:");
                                                         //console.log(server.host);

                                                         //console.log("XX server.host:");
                                                         //console.log(server.host);

                                                         //console.log("sessionsResponseHost:");
                                                         //console.log(sessionsResponseHost);

                                                         //responseServiceId is the service id that this response belongs to

                                                         //search for this server, which sensor is going to hold the session

                                                         server.children[0].children.forEach(function (sensor) {

                                                             //console.log("iter sensor:");
                                                             //console.log(sensor);
                                                             /*
                                                              console.log("sensor.elementId");
                                                              console.log(sensor.elementId);
                                                              console.log("responseServiceId");
                                                              console.log(responseServiceId);
                                                              console.log("-------------------------------------");
                                                              */


                                                             if (sensor.elementId == responseServiceId) {
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
                                         .error(function (data, status, headers, config) {
                                             console.log("error leyendo sesiones serviceID.nodeValue: " + serviceID.nodeValue);

                                             // called asynchronously if an error occurs
                                             // or server returns response with an error status.
                                         });


                                     //this.apps[this.apps.length] = {id:localId.textContent, number:localNumber.textContent, status:tLocalStatus.textContent};
                                 }


                             })
                             .error(function (data, status, headers, config) {
                                 console.log("error leyendo sensores server.ipAddress: " + server.ipAddress);

                                 // called asynchronously if an error occurs
                                 // or server returns response with an error status.
                             });

                         //for each server, add the command management element
                         //command management element:
                         var commandManagementElement = {
                             "host": server.ipAddress + ":" + server.restPort,
                             "elementName": "Command Management",
                             "elementId": "commandManagement",
                             "collapsed": true,
                             "iconClass": 'cog',
                             "children": []
                         };
                         server.children.push(commandManagementElement);


                         //call the readertypes operation
                         $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/readertypes')
                             .success(function (data, status, headers, config) {

                                 var readerTypesResponseHost = headers('host');

                                 var xmlReaderTypes;
                                 if (window.DOMParser) {
                                     var parser = new DOMParser();
                                     xmlReaderTypes = parser.parseFromString(data, "text/xml");
                                 }
                                 else // Internet Explorer
                                 {
                                     xmlReaderTypes = new ActiveXObject("Microsoft.XMLDOM");
                                     xmlReaderTypes.async = false;
                                     xmlReaderTypes.loadXML(data);
                                 }

                                 //get the xml response and extract the values to construct the reader type object
                                 var readerTypesXmlVector = xmlReaderTypes.getElementsByTagName("sensor");

                                 for (var index = 0; index < readerTypesXmlVector.length; index++) {

                                     var factoryID = readerTypesXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];
                                     var description = readerTypesXmlVector[index].getElementsByTagName("description")[0].childNodes[0];

                                     //Add the reader type to the command management element

                                     partialElementList[0].children.forEach(function (server) {

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
                                     .success(function (data, status, headers, config) {

                                         var commandTypesResponseHost = headers('host');

                                         var xmlCommandTypes;
                                         if (window.DOMParser) {
                                             var parser = new DOMParser();
                                             xmlCommandTypes = parser.parseFromString(data, "text/xml");
                                         }
                                         else // Internet Explorer
                                         {
                                             xmlCommandTypes = new ActiveXObject("Microsoft.XMLDOM");
                                             xmlCommandTypes.async = false;
                                             xmlCommandTypes.loadXML(data);
                                         }

                                         //get the xml response and extract the values to construct the local command type object
                                         var commandTypeXmlVector = xmlCommandTypes.getElementsByTagName("command");


                                         for (var index = 0; index < commandTypeXmlVector.length; index++) {

                                             var factoryID = commandTypeXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];
                                             var description = commandTypeXmlVector[index].getElementsByTagName("description")[0].childNodes[0];
                                             var readerFactoryID = commandTypeXmlVector[index].getElementsByTagName("readerFactoryID")[0].childNodes[0];

                                             //Iterate the children of command management element associated to the server that is equals to the server
                                             //that sends this response, and add the command type under appropriate reader type

                                             partialElementList[0].children.forEach(function (server) {

                                                 if (server.host == commandTypesResponseHost) {

                                                     server.children[1].children.forEach(function (readerTypeElement) {

                                                         //console.log("evaluating readerFactoryElement.readerFactoryID: " + readerTypeElement.readerFactoryID);

                                                         if (readerTypeElement.factoryID == readerFactoryID.nodeValue) {

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
                                             .success(function (data, status, headers, config) {

                                                 var commandsResponseHost = headers('host');

                                                 var xmlCommands;
                                                 if (window.DOMParser) {
                                                     var parser = new DOMParser();
                                                     xmlCommands = parser.parseFromString(data, "text/xml");
                                                 }
                                                 else // Internet Explorer
                                                 {
                                                     xmlCommands = new ActiveXObject("Microsoft.XMLDOM");
                                                     xmlCommands.async = false;
                                                     xmlCommands.loadXML(data);
                                                 }

                                                 //get the xml response and extract the values to construct the local command object
                                                 var commandXmlVector = xmlCommands.getElementsByTagName("command");

                                                 for (var index = 0; index < commandXmlVector.length; index++) {

                                                     var commandID = commandXmlVector[index].getElementsByTagName("commandID")[0].childNodes[0];
                                                     var factoryID = commandXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];

                                                     //Iterate the children of command factory id element associated to the server that is equals to the server
                                                     //that sends this response, and if factoryID are equal, then associate the command to that command factory

                                                     partialElementList[0].children.forEach(function (server) {

                                                         if (server.host == commandsResponseHost) {

                                                             //search for this server, if there exist a commandElement with this factoryID

                                                             server.children[1].children.forEach(function (readerFactoryElement) {

                                                                 readerFactoryElement.children.forEach(function (factoryElement) {

                                                                     if (factoryElement.factoryID == factoryID.nodeValue) {

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
                                             error(function (data, status, headers, config) {
                                                 console.log("error reading commands");


                                                 // called asynchronously if an error occurs
                                                 // or server returns response with an error status.
                                             });


                                     }).
                                     error(function (data, status, headers, config) {
                                         console.log("error reading command types");


                                         // called asynchronously if an error occurs
                                         // or server returns response with an error status.
                                     });


                             }).
                             error(function (data, status, headers, config) {
                                 console.log("error reading reader types");


                                 // called asynchronously if an error occurs
                                 // or server returns response with an error status.
                             });


                         //aca iba


                         //for each server, add the app management element
                         //app management element:
                         var appManagementElement = {
                             "elementName": "App Management",
                             "elementId": "App Management",
                             "collapsed": true,
                             "iconClass": "app-dev",
                             "children": []
                         };
                         server.children.push(appManagementElement);

                         //add application groups element

                         var appGroupsElement = {
                             "elementName": "App Groups",
                             "elementId": "App Group",
                             "collapsed": true,
                             "iconClass": "appgroups",
                             "children": []
                         };

                         appManagementElement.children.push(appGroupsElement);

                         //load app groups

                         //call apps command
                         $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/apps')

                             .success(function (data, status, headers, config) {

                                 var appsResponseHost = headers('host');

                                 var xmlApps;
                                 if (window.DOMParser) {
                                     var parser = new DOMParser();
                                     xmlApps = parser.parseFromString(data, "text/xml");
                                 }
                                 else // Internet Explorer
                                 {
                                     xmlApps = new ActiveXObject("Microsoft.XMLDOM");
                                     xmlApps.async = false;
                                     xmlApps.loadXML(data);
                                 }

                                 //get the xml response and extract the values to construct the app groups
                                 var appsXmlVector = xmlApps.getElementsByTagName("app");

                                 for (var index = 0; index < appsXmlVector.length; index++) {

                                     var id = appsXmlVector[index].getElementsByTagName("id")[0].childNodes[0];
                                     var number = appsXmlVector[index].getElementsByTagName("number")[0].childNodes[0];
                                     var status = appsXmlVector[index].getElementsByTagName("status")[0].childNodes[0];

                                     var groupName = id.nodeValue.split(":")[0];
                                     var appName = id.nodeValue.split(":")[1];

                                     //Iterate the children of app groups element associated to the server that is equals to the server
                                     //that sends this response, and if this app group does not exist under that structure, then create it

                                     partialElementList[0].children.forEach(function (server) {

                                         if (server.host == appsResponseHost) {

                                             //search for this server, if there exist an appGroupElement with this groupName

                                             var appGroupFound = false;

                                             server.children[2].children[0].children.forEach(function (appGroupElement) {

                                                 if (appGroupElement.groupName == groupName) {

                                                     console.log("readerFactoryFound");

                                                     appGroupFound = true;
                                                     //TODO How to break here this loop (when appGroupFound == true)

                                                 }

                                             });

                                             if (appGroupFound == false) {

                                                 //create the appGroupElement and add it
                                                 var appGroupElement = {
                                                     "elementName": groupName,
                                                     "elementId": groupName,
                                                     "collapsed": true,
                                                     "groupName": groupName,
                                                     "readzoneAppId": "",
                                                     "iconClass": "group",
                                                     "children": []
                                                 };

                                                 //Add Apps element label
                                                 var appsElement = {
                                                     "elementName": "Apps",
                                                     "elementId": "Apps",
                                                     "collapsed": true,
                                                     "groupName": groupName,
                                                     "iconClass": "apps",
                                                     "children": []
                                                 };

                                                 appGroupElement.children.push(appsElement);

                                                 //Add Readzones element label
                                                 var readZonesElement = {
                                                     "elementName": "ReadZones",
                                                     "elementId": "ReadZones",
                                                     "collapsed": true,
                                                     "groupName": groupName,
                                                     "iconClass": "readzones",
                                                     "children": []
                                                 };

                                                 appGroupElement.children.push(readZonesElement);

                                                 server.children[2].children[0].children.push(angular.copy(appGroupElement));


                                             }

                                             //After adding the appGroup if it did not exist, we have to associate the current
                                             //application to its corresponding app group

                                             //iterate the apps elements

                                             server.children[2].children[0].children.forEach(function (appGroupElement) {

                                                 if (appGroupElement.groupName == groupName) {

                                                     //add the app to this appGroup
                                                     var appElement = {
                                                         "elementName": appName,
                                                         "elementId": appName,
                                                         "collapsed": true,
                                                         "groupName": groupName,
                                                         "appName": appName,
                                                         "number": number.nodeValue,
                                                         "status": status.nodeValue,
                                                         "iconClass": "app",
                                                         "children": []

                                                     }

                                                     appGroupElement.children[0].children.push(appElement);

                                                     //Add the application number to this application group in order to later add the associated read zones
                                                     if (appGroupElement.readzoneAppId == "") {
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


                                 server.children[2].children[0].children.forEach(function (appGroupElement) {


                                     $http.get(server.restProtocol + "://" + server.ipAddress + ":" + server.restPort + '/getReadZones/' + appGroupElement.readzoneAppId)

                                         .success(function (data, status, headers, config) {

                                             //console.log("config en getreadzones:");
                                             //console.log(config);

                                             var successReadzoneAppId = config.url.substring(config.url.lastIndexOf("/") + 1, config.url.length);
                                             //console.log("successReadzoneAppId:" + successReadzoneAppId);

                                             var readzonesResponseHost = headers('host');

                                             var xmlReadzones;
                                             if (window.DOMParser) {
                                                 var parser = new DOMParser();
                                                 xmlReadzones = parser.parseFromString(data, "text/xml");
                                             }
                                             else // Internet Explorer
                                             {
                                                 xmlReadzones = new ActiveXObject("Microsoft.XMLDOM");
                                                 xmlReadzones.async = false;
                                                 xmlReadzones.loadXML(data);
                                             }

                                             //get the xml response and extract the values to construct the readzone element
                                             var readzonesXmlVector = xmlReadzones.getElementsByTagName("entry");


                                             partialElementList[0].children.forEach(function (server) {

                                                 if (server.host == readzonesResponseHost) {

                                                     //iterate to find the app group where to add this readzone
                                                     server.children[2].children[0].children.forEach(function (appGroupElement) {

                                                         if (appGroupElement.readzoneAppId == successReadzoneAppId) {

                                                             for (var index = 0; index < readzonesXmlVector.length; index++) {

                                                                 var readzone = readzonesXmlVector[index].getElementsByTagName("value")[0].childNodes[0].nodeValue;

                                                                 //create the readzoneElement and add it
                                                                 var readzoneElement = {
                                                                     "elementName": readzone,
                                                                     "elementId": readzone,
                                                                     "collapsed": true,
                                                                     "readzone": readzone,
                                                                     "iconClass": "readzone",
                                                                     "children": []
                                                                 };

                                                                 appGroupElement.children[1].children.push(angular.copy(readzoneElement));
                                                             }

                                                         }

                                                     });


                                                 }

                                             });


                                         }).
                                         error(function (data, status, headers, config) {
                                             console.log("error reading readzones");


                                             // called asynchronously if an error occurs
                                             // or server returns response with an error status.
                                         });

                                 });


                             }).
                             error(function (data, status, headers, config) {
                                 console.log("error reading apps");


                                 // called asynchronously if an error occurs
                                 // or server returns response with an error status.
                             });


                     });


                     _scope.elementList = partialElementList;

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
                 error(function (data, status, headers, config) {
                     console.log("error leyendo servidores ");


                     // called asynchronously if an error occurs
                     // or server returns response with an error status.
                 });



             //$rootScope.$apply;

             }

       }

       return service;
 });


