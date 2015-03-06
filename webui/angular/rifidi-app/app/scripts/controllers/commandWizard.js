'use strict';

/**
 * @ngdoc function
 * @name rifidiApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the rifidiApp
 */
angular.module('rifidiApp')
  .controller('CommandWizardCtrl', function ($scope, $http, $routeParams) {

      //retrieve the server data
      var restProtocol = $routeParams.restProtocol;
      var ipAddress = $routeParams.ipAddress;
      var restPort = $routeParams.restPort;
      var readerType = $routeParams.readerType;

      var host = restProtocol + "://" +  ipAddress + ":" + restPort;

      //load the command types

      //load command templates for selected reader type
      $http.get(host + '/commandtypes')
          .success(function(data, status, headers, config) {


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

            $scope.commandWizardData = {};
            $scope.commandWizardData.commandTypes = [];

            for(var index = 0; index < commandTypeXmlVector.length; index++) {

              var factoryID = commandTypeXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];
              var description = commandTypeXmlVector[index].getElementsByTagName("description")[0].childNodes[0];
              var readerFactoryID = commandTypeXmlVector[index].getElementsByTagName("readerFactoryID")[0].childNodes[0];

              if (readerFactoryID.nodeValue == readerType){

                //Add the command type
                var commandTypeElement = {
                  "factoryID": factoryID.nodeValue,
                  "description": description.nodeValue,
                  "readerFactoryID": readerFactoryID.nodeValue

                }

                $scope.commandWizardData.commandTypes.push(commandTypeElement);
              }

            }

          })
          .error(function(data, status, headers, config) {
            console.log("error reading command types for command wizard");

            // called asynchronously if an error occurs
            // or server returns response with an error status.
          });


      $scope.commandTypeSelectAction = function(selectedCommandType){

        //clear command instances list
        $scope.commandWizardData.commandInstances = [];
        //$scope.commandWizardData.commandType = selectedCommandType;

        //load the command instances for selected command type
        $http.get(host + '/commands')
            .success(function(data, status, headers, config) {

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

                //console.log("readerFactoryID.nodeValue:");
                //console.log(readerFactoryID.nodeValue);

                //console.log("$scope.selectedReaderType:");
                //console.log($scope.selectedReaderType);

                if (factoryID.nodeValue == selectedCommandType.factoryID){

                  //Add the command instance
                  var commandInstanceElement = {
                    "commandID": commandID.nodeValue,
                    "factoryID": factoryID.nodeValue
                  }

                  $scope.commandWizardData.commandInstances.push(commandInstanceElement);
                }

              }

              //Add the New command instance label
              var commandInstanceNewElement = {
                "commandID": "<New>",
                "factoryID": selectedCommandType.factoryID
              }

              $scope.commandWizardData.commandInstances.push(commandInstanceNewElement);

            })
            .error(function(data, status, headers, config) {
              console.log("error reading command instances for command wizard");

              // called asynchronously if an error occurs
              // or server returns response with an error status.
            });



      }


  });
