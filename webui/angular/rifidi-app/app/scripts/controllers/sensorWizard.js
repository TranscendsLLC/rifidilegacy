'use strict';

/**
 * @ngdoc function
 * @name rifidiApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the rifidiApp
 */
angular.module('rifidiApp')
  .controller('SensorWizardCtrl', function ($scope, $http, $routeParams) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];

      console.log("cleaning readerTypes: ");
      $scope.readerTypes = [];
      $scope.selectedReaderType = "";

      //loadReaderTypes();

      //Call the reader types service, and create a list with reader types
      //$scope.loadReaderTypes = function () {

        //retrieve the reader types
        var restProtocol = $routeParams.restProtocol;
        var ipAddress = $routeParams.ipAddress;
        var restPort = $routeParams.restPort;

        var host = restProtocol + "://" + ipAddress + ":" + restPort;

        console.log("host: " + host);



      /*
        $scope.readerTypes = [
          { id: 1, name: 'Foo111', factoryID: 'xxx111' },
          { id: 2, name: 'Bar222', factoryID: 'yyy222' },
          { id: 22, name: 'Bar222BB', factoryID: 'yyy222BB' }
        ];
        */

        $http.get(host + '/readertypes')
            .success(function(data, status, headers, config) {

              //var sessionsResponseHost = headers('host');

              //console.log("data for readerstatus from host: " + sessionsResponseHost + ": " + data);

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

              //get the xml response and extract the values to construct the reader types object

              var readerTypesXmlVector = xmlReaderTypes.getElementsByTagName("sensor");




              for(var index = 0; index < readerTypesXmlVector.length; index++) {

                var factoryID = readerTypesXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];
                var description = readerTypesXmlVector[index].getElementsByTagName("description")[0].childNodes[0];

                //Add the reader type to the reader types list

                //define an empty command structure to hold the reader type
                var readerTypeElement = {
                  "factoryID": factoryID.nodeValue,
                  "description": description.nodeValue
                };

                //console.log("readerTypeElement: ");
                //console.log(readerTypeElement);
                $scope.readerTypes.push(readerTypeElement);

              }
/*
              $scope.readerTypes = [
                { id: 3, name: 'Foo33', factoryID: 'xxx33' },
                { id: 4, name: 'Bar44', factoryID: 'yyy44' }
              ];
*/
              console.log("$scope.readerTypes");
              console.log($scope.readerTypes);

            })
            .error(function(data, status, headers, config) {
              console.log("error reading reader types for sensor wizard");

              // called asynchronously if an error occurs
              // or server returns response with an error status.
            });

      //}



  });
