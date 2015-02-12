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
      $scope.readersConnectionProperties = [];
      $scope.customReaderId = "";
      $scope.selectedReaderConnectionProperties = [];

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


      //load the connection properties
      $http.get(host + '/readermetadata')
          .success(function(data, status, headers, config) {


            var xmlMetadata;
            if (window.DOMParser)
            {
              var parser = new DOMParser();
              xmlMetadata = parser.parseFromString(data,"text/xml");
            }
            else // Internet Explorer
            {
              xmlMetadata = new ActiveXObject("Microsoft.XMLDOM");
              xmlMetadata.async=false;
              xmlMetadata.loadXML(data);
            }

            //get the xml response and extract the values to construct the connection properties for all readers

            var readerMetadataXmlVector = xmlMetadata.getElementsByTagName("reader");

            //console.log("readerMetadataXmlVector.length: " + readerMetadataXmlVector.length);


            for(var index = 0; index < readerMetadataXmlVector.length; index++) {


              var readerid = readerMetadataXmlVector[index].getElementsByTagName("readerid")[0].childNodes[0];
              var propertiesXmlVector = readerMetadataXmlVector[index].getElementsByTagName("property");

              console.log("readerid: " + readerid.nodeValue);
              console.log("properties length: " + propertiesXmlVector.length);
              //console.log("propertiesXmlVector");
              //console.log(propertiesXmlVector);

              //Create a properties object for this reader
              var readerConnectionProperties = {
                "readerid": readerid.nodeValue,
                "startSessionAut": false,
                "properties": []
              };



              for(var indexProp = 0; indexProp < propertiesXmlVector.length; indexProp++) {

                console.log("indexProp: " + indexProp);

                var category = propertiesXmlVector[indexProp].getElementsByTagName("category")[0].childNodes[0];

                //console.log("category: '" + category.nodeValue + "'");

                if (category.nodeValue == 'connection') {

                  var name = propertiesXmlVector[indexProp].getElementsByTagName("name")[0].childNodes[0];
                  var displayname = propertiesXmlVector[indexProp].getElementsByTagName("displayname")[0].childNodes[0];
                  //console.log("displayname: " + displayname.nodeValue);
                  var defaultvalue = propertiesXmlVector[indexProp].getElementsByTagName("defaultvalue")[0].childNodes[0];
                  var description = propertiesXmlVector[indexProp].getElementsByTagName("description")[0].childNodes[0];
                  var type = propertiesXmlVector[indexProp].getElementsByTagName("type")[0].childNodes[0];
                  var maxvalue = 0;
                  var minvalue = 0;
                  if (type == 'java.lang.Integer') {
                    maxvalue = propertiesXmlVector[indexProp].getElementsByTagName("maxvalue")[0].childNodes[0];
                    minvalue = propertiesXmlVector[indexProp].getElementsByTagName("minvalue")[0].childNodes[0];
                  }


                  var writable = propertiesXmlVector[indexProp].getElementsByTagName("writable")[0].childNodes[0];
                  var ordervalue = propertiesXmlVector[indexProp].getElementsByTagName("ordervalue")[0].childNodes[0];

                  //if category equals to connection, then extract that property
                  if (category.nodeValue == 'connection') {

                    var propertyElement = {
                      "name": name.nodeValue,
                      "displayname": displayname.nodeValue,
                      "description": description.nodeValue,
                      "type": type.nodeValue,
                      "maxvalue": maxvalue.nodeValue,
                      "minvalue": minvalue.nodeValue,
                      "category": category.nodeValue,
                      "writable": writable.nodeValue,
                      "ordervalue": ordervalue.nodeValue,
                      "value": defaultvalue.nodeValue,
                      "defaultvalue": defaultvalue.nodeValue
                    };

                    //Add the property to properties list
                    readerConnectionProperties.properties.push(propertyElement);

                  }
                }


              }


              $scope.readersConnectionProperties.push(readerConnectionProperties);


            }

            console.log("$scope.readersConnectionProperties");
            console.log($scope.readersConnectionProperties);

          })
          .error(function(data, status, headers, config) {
            console.log("error reading readermetadata for sensor wizard");

            // called asynchronously if an error occurs
            // or server returns response with an error status.
          });

      $scope.prepareCreateSessionStep = function(){
        console.log("prepareCreateSessionStep called");

        //Clean variable
        $scope.selectedReaderConnectionProperties = [];

        //console.log("$scope.selectedReaderType");
        //console.log($scope.selectedReaderType);

        //console.log("$scope.customReaderId");
        //console.log($scope.customReaderId);



        //$scope.selectedReaderType = selectedReaderType;

        //console.log("$scope.selectedReaderType");
        //console.log($scope.selectedReaderType);

        //console.log("selectedReaderType");
        //console.log(selectedReaderType);

        //load the properties for selected reader type
        $scope.readersConnectionProperties.forEach(function(readerConnectionProperties){




          if (readerConnectionProperties.readerid == $scope.selectedReaderType.factoryID){

            console.log("match");

            //Add the property, assigning a copy of the property
            $scope.selectedReaderConnectionProperties = angular.copy(readerConnectionProperties);

          }

        });

        console.log("$scope.selectedReaderConnectionProperties");
        console.log($scope.selectedReaderConnectionProperties);



      }

      $scope.readerTypeSelectAction = function(selectedReaderType){

        $scope.selectedReaderType = selectedReaderType;
        $scope.prepareCreateSessionStep();
      }

      $scope.readerIdChangeAction = function(customReaderId){

        $scope.customReaderId = customReaderId;

        console.log("readerIdChangeAction:");
        console.log($scope.customReaderId);

      }


  });
