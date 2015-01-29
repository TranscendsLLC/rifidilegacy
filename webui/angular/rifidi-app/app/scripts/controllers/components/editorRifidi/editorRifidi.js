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

            var partialElementList = [{ "elementName" : "Servers", "elementId" : "servers", "collapsed":true, "children" : []}];

            data.forEach(function(server){

                server.colapsed = true;
                server.elemeIntName = server.displayName;
                server.elementId = "server";
                partialElementList[0].children.push(server);
            });





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

                data.forEach( function( item ) {
                    console.log( "ite: " + item );
                });
            // this callback will be called asynchronously
            // when the response is available
          }).
          error(function(data, status, headers, config) {
            console.log("error leyendo servidores ")


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

