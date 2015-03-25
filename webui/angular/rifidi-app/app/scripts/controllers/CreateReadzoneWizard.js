'use strict';

/**
 * @ngdoc function
 * @name rifidiApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the rifidiApp
 */
angular.module('rifidiApp')
  .controller('CreateReadzoneWizardCtrl', function ($rootScope, $scope, $http, $routeParams, $location, ngDialog, commonVariableService) {


      var getSuccessMessage = function () {
        return commonVariableService.getSuccessMessage();
      };

      var setSuccessMessage = function (msg) {
        if (msg != '') {
          commonVariableService.setSuccessMessage(msg);
        }
      };

      $scope.go = function ( path ) {
        $location.path( path );
      };

      initWizardData();


      function initWizardData() {

        $scope.readzoneProperties = null;

        //get the app id
        var appId = $scope.elementSelected.appId;

        //call getReadZoneProperties operation
        var host = angular.copy($scope.elementSelected.host);

        $scope.readzoneProperties = {
          "host": host,
          "appId": appId,
          "readzone": "",
          "properties": []
        }

        //As readzones have four fixed properties: readerID antennas tagPattern matchPattern,
        //then create a list with those properties

          var propertyElement = {
            "key": 'readerID',
            "value": ''
          }

          $scope.readzoneProperties.properties.push(angular.copy(propertyElement));

          propertyElement = {
            "key": 'antennas',
            "value": ''
          }

          $scope.readzoneProperties.properties.push(angular.copy(propertyElement));

          var propertyElement = {
            "key": 'tagPattern',
            "value": ''
          }

          $scope.readzoneProperties.properties.push(angular.copy(propertyElement));

          var propertyElement = {
            "key": 'matchPattern',
            "value": ''
          }

          $scope.readzoneProperties.properties.push(angular.copy(propertyElement));

      };


      $scope.openCreateReadzoneDialog = function(){

        ngDialog.openConfirm({template: 'createReadzoneDialogTmpl.html',

          scope: $scope, //Pass the scope object if you need to access in the template

          showClose: false,

          closeByEscape: true,

          closeByDocument: false

        }).then(

            function(value) {

              //confirm operation
              if (value == 'Create'){
                console.log("to create");

                //submit the command
                createReadzone();

              }

            },

            function(value) {

              //Cancel or do nothing
              console.log("cancel");

            }

        );

      };

      var createCommand = function() {

        var strCommandProperties = "";

        for (var idxCat = 0; idxCat < $scope.commandWizardData.commandProperties.propertyCategoryList.length; idxCat++) {

          for (var idxProp = 0; idxProp < $scope.commandWizardData.commandProperties.propertyCategoryList[idxCat].properties.length; idxProp++) {

            strCommandProperties += $scope.commandWizardData.commandProperties.propertyCategoryList[idxCat].properties[idxProp].name + "="
            + $scope.commandWizardData.commandProperties.propertyCategoryList[idxCat].properties[idxProp].value + ";"
          }

        }

        //Quit the last semicolon ;
        if (strCommandProperties.length > 0) {
          strCommandProperties = strCommandProperties.substring(0, strCommandProperties.length - 1);
        }

        console.log("strCommandProperties");
        console.log(strCommandProperties);

        //Create command
        console.log("going to create command");
        $scope.commandWizardData.commandCreationResponseStatus = {};

        //create command
        $http.get($scope.elementSelected.host + '/createcommand/' + $scope.elementSelected.factoryID + "/" + strCommandProperties)
            .success(function (data, status, headers, config) {

              console.log("success response creating command in wizard");

              var xmlCreateCommandResponse;
              if (window.DOMParser) {
                var parser = new DOMParser();
                xmlCreateCommandResponse = parser.parseFromString(data, "text/xml");
              }
              else // Internet Explorer
              {
                xmlCreateCommandResponse = new ActiveXObject("Microsoft.XMLDOM");
                xmlCreateCommandResponse.async = false;
                xmlCreateCommandResponse.loadXML(data);
              }

              //get the xml response and extract the values for properties
              var createCommandMessage = xmlCreateCommandResponse.getElementsByTagName("message")[0].childNodes[0].nodeValue;

              $scope.commandWizardData.commandCreationResponseStatus.message = createCommandMessage;

              if (createCommandMessage == 'Success') {
                console.log("success creating command by wizard");

                var commandID = xmlCreateCommandResponse.getElementsByTagName("commandID")[0].childNodes[0].nodeValue;

                setSuccessMessage("Success creating command: " + commandID);
                $rootScope.operationSuccessMsg = getSuccessMessage();

                //continueExecutingCommand(commandID);

              } else {
                var createCommandDescription = xmlCreateCommandResponse.getElementsByTagName("description")[0].childNodes[0].nodeValue;
                $scope.commandWizardData.commandCreationResponseStatus.description = createCommandDescription;
                console.log("fail creating command by wizard");
                console.log(createCommandDescription);
                showErrorDialog('Error creating command: ' + createCommandDescription);
              }


            }).
            error(function (data, status, headers, config) {
              console.log("error creating command in wizard");

              showErrorDialog('Error creating command');


              // called asynchronously if an error occurs
              // or server returns response with an error status.
            });
      }



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


  });
