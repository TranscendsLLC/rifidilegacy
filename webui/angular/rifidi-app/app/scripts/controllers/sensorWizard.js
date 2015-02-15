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

      //console.log("cleaning readerTypes: ");
      $scope.readerTypes = [];
      $scope.commandTypes = [];
      $scope.commandInstances = [];
      $scope.selectedReaderType = "";
      $scope.selectedCommandType = {};
      $scope.selectedCommandInstance = {};
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

        //console.log("host: " + host);



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
              //console.log("$scope.readerTypes");
              //console.log($scope.readerTypes);

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

              //console.log("readerid: " + readerid.nodeValue);
              //console.log("properties length: " + propertiesXmlVector.length);
              //console.log("propertiesXmlVector");
              //console.log(propertiesXmlVector);

              //Create a properties object for this reader
              var readerConnectionProperties = {
                "readerid": readerid.nodeValue,
                "startSessionAut": false,
                "properties": []
              };



              for(var indexProp = 0; indexProp < propertiesXmlVector.length; indexProp++) {

                //console.log("indexProp: " + indexProp);

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
                  if (type.nodeValue == 'java.lang.Integer') {
                    //maxvalue = propertiesXmlVector[indexProp].getElementsByTagName("maxvalue")[0].childNodes[0];
                    //minvalue = propertiesXmlVector[indexProp].getElementsByTagName("minvalue")[0].childNodes[0];
                  }


                  var writable = propertiesXmlVector[indexProp].getElementsByTagName("writable")[0].childNodes[0];
                  var ordervalue = propertiesXmlVector[indexProp].getElementsByTagName("ordervalue")[0].childNodes[0];

                  //if category equals to connection, then extract that property
                  if (category.nodeValue == 'connection') {

                    var customdefaultvalue;

                    if (type.nodeValue == 'java.lang.Integer'){
                      customdefaultvalue = parseInt(defaultvalue.nodeValue);
                    } else {
                      customdefaultvalue = defaultvalue.nodeValue;
                    }

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
                      "value": customdefaultvalue,
                      "defaultvalue": customdefaultvalue
                    };

                    //Add the property to properties list
                    readerConnectionProperties.properties.push(propertyElement);

                  }
                }


              }


              $scope.readersConnectionProperties.push(readerConnectionProperties);


            }

            //console.log("$scope.readersConnectionProperties");
            //console.log($scope.readersConnectionProperties);

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

            //console.log("match");

            //Add the property, assigning a copy of the property
            $scope.selectedReaderConnectionProperties = angular.copy(readerConnectionProperties);

          }

        });

        //console.log("$scope.selectedReaderConnectionProperties");
        //console.log($scope.selectedReaderConnectionProperties);



      }

      $scope.prepareCreateCommandStep = function(){
        console.log("prepareCreateCommandStep called");

        //Clean variables
        $scope.selectedCommandType = {};
        $scope.selectedCommandInstance = {};

        $scope.commandTypes = [];
        $scope.commandInstances = [];
        $scope.commandProperties = [];


        //console.log("$scope.selectedReaderType");
        //console.log($scope.selectedReaderType);

        //console.log("$scope.customReaderId");
        //console.log($scope.customReaderId);



        //$scope.selectedReaderType = selectedReaderType;

        //console.log("$scope.selectedReaderType");
        //console.log($scope.selectedReaderType);

        //console.log("selectedReaderType");
        //console.log(selectedReaderType);

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



              for(var index = 0; index < commandTypeXmlVector.length; index++) {

                var factoryID = commandTypeXmlVector[index].getElementsByTagName("factoryID")[0].childNodes[0];
                var description = commandTypeXmlVector[index].getElementsByTagName("description")[0].childNodes[0];
                var readerFactoryID = commandTypeXmlVector[index].getElementsByTagName("readerFactoryID")[0].childNodes[0];

                /*
                console.log("readerFactoryID.nodeValue:");
                console.log(readerFactoryID.nodeValue);

                console.log("$scope.selectedReaderType:");
                console.log($scope.selectedReaderType);
                */

                if (readerFactoryID.nodeValue == $scope.selectedReaderType.factoryID){

                  //Add the command type
                  var commandTypeElement = {
                    "factoryID": factoryID.nodeValue,
                    "description": description.nodeValue,
                    "readerFactoryID": readerFactoryID.nodeValue

                  }

                  $scope.commandTypes.push(commandTypeElement);
                }

              }

            })
            .error(function(data, status, headers, config) {
              console.log("error reading command types for sensor wizard");

              // called asynchronously if an error occurs
              // or server returns response with an error status.
            });



      }

      $scope.readerTypeSelectAction = function(selectedReaderType){

        $scope.selectedReaderType = selectedReaderType;
        $scope.prepareCreateSessionStep();
        $scope.prepareCreateCommandStep();
      }

      $scope.commandTypeSelectAction = function(selectedCommandType){

        //clear command instances list
        $scope.commandInstances = [];

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

                  $scope.commandInstances.push(commandInstanceElement);
                }

              }

              //Add the New command instance label
              var commandInstanceNewElement = {
                "commandID": "<New>",
                "factoryID": selectedCommandType.factoryID
              }

              $scope.commandInstances.push(commandInstanceNewElement);

            })
            .error(function(data, status, headers, config) {
              console.log("error reading command instances for sensor wizard");

              // called asynchronously if an error occurs
              // or server returns response with an error status.
            });



      }

      $scope.readerIdChangeAction = function(customReaderId){

        $scope.customReaderId = customReaderId;

        //console.log("readerIdChangeAction:");
        //console.log($scope.customReaderId);

      }

      $scope.commandInstanceSelectAction = function(selectedCommandInstance){

        $scope.selectedCommandInstance = selectedCommandInstance;

        //Get the properties for the selected command type, from readermetadata
        //$scope.selectedReaderType
        //$scope.selectedCommandType

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

              //get the xml response and extract the values to construct the command properties for selected command type

              var commandMetadataXmlVector = xmlMetadata.getElementsByTagName("command");

              //console.log("commandMetadataXmlVector.length: " + commandMetadataXmlVector.length);


              for(var index = 0; index < commandMetadataXmlVector.length; index++) {


                var propertiesXmlVector = commandMetadataXmlVector[index].getElementsByTagName("property");
                var id = commandMetadataXmlVector[index].getElementsByTagName("id")[0].childNodes[0];
                var readerID = commandMetadataXmlVector[index].getElementsByTagName("readerID")[0].childNodes[0];

                /*
                console.log("id: " + id.nodeValue);
                console.log("readerID: " + readerID.nodeValue);
                console.log("properties length: " + propertiesXmlVector.length);
                console.log("propertiesXmlVector");
                console.log(propertiesXmlVector);
                */

                //check if current command is the required one
                /*
                console.log("readerID.nodeValue:");
                console.log(readerID.nodeValue);
                console.log("$scope.selectedReaderType:");
                console.log($scope.selectedReaderType);
                console.log("id.nodeValue:");
                console.log(id.nodeValue);
                console.log("selectedCommandInstance.factoryID:");
                console.log(selectedCommandInstance.factoryID);
                */

                if (readerID.nodeValue == $scope.selectedReaderType.factoryID && id.nodeValue ==  selectedCommandInstance.factoryID){

                  //console.log("found selectedCommandInstance.factoryID: " + selectedCommandInstance.factoryID);

                  //Create the properties object for this command
                  $scope.commandProperties = {
                    "readerID": readerID.nodeValue,
                    "id": id.nodeValue,
                    "propertyCategoryList": []

                  };

                  //extract the properties
                  for(var indexProp = 0; indexProp < propertiesXmlVector.length; indexProp++) {


                    var name = propertiesXmlVector[indexProp].getElementsByTagName("name")[0].childNodes[0];
                    var displayname = propertiesXmlVector[indexProp].getElementsByTagName("displayname")[0].childNodes[0];
                    var description = propertiesXmlVector[indexProp].getElementsByTagName("description")[0].childNodes[0];
                    var type = propertiesXmlVector[indexProp].getElementsByTagName("type")[0].childNodes[0];
                    var maxvalue = 0;
                    var minvalue = 0;
                    if (type.nodeValue == 'java.lang.Integer') {
                      maxvalue = propertiesXmlVector[indexProp].getElementsByTagName("maxvalue")[0].childNodes[0];
                      minvalue = propertiesXmlVector[indexProp].getElementsByTagName("minvalue")[0].childNodes[0];
                    }
                    var category = propertiesXmlVector[indexProp].getElementsByTagName("category")[0].childNodes[0];
                    var writable = propertiesXmlVector[indexProp].getElementsByTagName("writable")[0].childNodes[0];
                    var ordervalue = propertiesXmlVector[indexProp].getElementsByTagName("ordervalue")[0].childNodes[0];

                    //check if current category already exists in propertyCategories
                    var existingCategory = false;

                    $scope.commandProperties.propertyCategoryList.forEach(function (propertyCategory) {

                      if (category.nodeValue == propertyCategory.category){
                        existingCategory = true;
                      }

                    });

                    if (!existingCategory){

                      var propertyCategory = {
                        "category": category.nodeValue,
                        "properties": []
                      };

                      $scope.commandProperties.propertyCategoryList.push(propertyCategory);
                    }

                    /*
                    var customdefaultvalue;

                    if (type.nodeValue == 'java.lang.Integer'){
                      customdefaultvalue = parseInt(defaultvalue.nodeValue);
                    } else {
                      customdefaultvalue = defaultvalue.nodeValue;
                    }
                    */

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
                      "value": ""
                    };

                    //Add the property to appropriate property category list
                    $scope.commandProperties.propertyCategoryList.forEach(function (propertyCategory) {

                      if (category.nodeValue == propertyCategory.category){

                        propertyCategory.properties.push(angular.copy(propertyElement));

                      }

                    });

                  }

                  //If user selects a command instance (not the <New> option), then load the current values for every property
                  if (selectedCommandInstance.commandID != '<New>'){

                    console.log("selectedCommandInstance.commandID != '<New>'");


                    //call the service to get properties of command instance
                    $http.get(host + '/getproperties/' + selectedCommandInstance.commandID)
                        .success(function(data, status, headers, config) {

                          var xmlCommandProperties;
                          if (window.DOMParser)
                          {
                            var parser = new DOMParser();
                            xmlCommandProperties = parser.parseFromString(data,"text/xml");
                          }
                          else // Internet Explorer
                          {
                            xmlCommandProperties = new ActiveXObject("Microsoft.XMLDOM");
                            xmlCommandProperties.async=false;
                            xmlCommandProperties.loadXML(data);
                          }

                          //get the xml response and extract the values for properties
                          var propertiesXmlVector = xmlCommandProperties.getElementsByTagName("entry");

                          for(var indexPropertyValue = 0; indexPropertyValue < propertiesXmlVector.length; indexPropertyValue++) {

                            var key = propertiesXmlVector[indexPropertyValue].getElementsByTagName("key")[0].childNodes[0];
                            var value = propertiesXmlVector[indexPropertyValue].getElementsByTagName("value")[0].childNodes[0];

                            //Iterate the loaded properties and set this value when property key matches
                            $scope.commandProperties.propertyCategoryList.forEach(function (propertyCategory) {

                              //Iterate al categories to find this property and set the value
                              propertyCategory.properties.forEach(function (property) {

                                if (property.name == key.nodeValue){

                                  //Set the value for property

                                  if (property.type == 'java.lang.Integer'){
                                    property.value = angular.copy(parseInt(value.nodeValue));
                                  } else {
                                    property.value = angular.copy(value.nodeValue);
                                  }
                                  
                                }

                              });


                            });


                          }

                        }).
                        error(function(data, status, headers, config) {
                          console.log("error reading command instance properties");


                          // called asynchronously if an error occurs
                          // or server returns response with an error status.
                        });

                  }

                }


              }


            })
            .error(function(data, status, headers, config) {
              console.log("error reading readermetadata for sensor wizard: command instance selection");

              // called asynchronously if an error occurs
              // or server returns response with an error status.
            });






      }


  });
