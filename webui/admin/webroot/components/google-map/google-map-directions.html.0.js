
    Polymer({

      /**
       * The Google map object.
       *
       * @attribute map
       * @type google.maps.Map
       * @default null
       */
      map: null,

      /**
       * Start address or latlng to get directions from.
       *
       * @attribute startAddress
       * @type string|google.maps.LatLng
       * @default null
       */
      startAddress: null,

      /**
       * End address or latlng for directions to end.
       *
       * @attribute endAddress
       * @type string|google.maps.LatLng
       * @default null
       */
      endAddress: null,

      /**
       * Travel mode to use.  One of 'DRIVING', 'WALKING', 'BICYCLING', 'TRANSIT'.
       *
       * @attribute travelMode
       * @type string
       * @default 'DRIVING'
       */
      travelMode: 'DRIVING',

      /**
       * A comma separated list (e.g. "places,geometry") of libraries to load
       * with this map. Defaults to "places". For more information see
       * https://developers.google.com/maps/documentation/javascript/libraries.
       *
       * Note, this needs to be set to the same value as the one used on <google-map>.
       * If you're overriding that element's `libraries` property, this one also
       * needs to be set to the Maps API loads the library code.
       *
       * @attribute libraries
       * @type string
       * @default "places"
       */
      libraries: "places",

      /**
       * The response from the directions service.
       *
       * @attribute response
       * @type object
       */

      observe: {
        startAddress: 'route',
        endAddress: 'route',
        travelMode: 'route'
      },
      mapApiLoaded: function() {
        this.route();
      },
      responseChanged: function() {
        if (this.directionsRenderer && this.response) {
          this.directionsRenderer.setDirections(this.response);
        }
      },
      mapChanged: function() {
        if (this.map && this.map instanceof google.maps.Map) {
          if (!this.directionsRenderer) {
            this.directionsRenderer = new google.maps.DirectionsRenderer();
          }
          this.directionsRenderer.setMap(this.map);
          this.responseChanged();
        } else {
          // If there is no more map, remove the directionsRenderer from the map and delete it.
          this.directionsRenderer.setMap(null);
          this.directionsRenderer = null;
        }
      },
      route: function() {
        // Abort attempts to route if the API is not available yet or the
        // required attributes are blank.
        if (typeof google == 'undefined' || typeof google.maps == 'undefined' ||
            !this.startAddress || !this.endAddress) {
          return;
        }

        // Construct a directionsService if necessary.
        // Wait until here where the maps api has loaded and directions are actually needed.
        if (!this.directionsService) {
          this.directionsService = new google.maps.DirectionsService();
        }

        var request = {
          origin: this.startAddress,
          destination: this.endAddress,
          travelMode: this.travelMode
        };
        this.directionsService.route(request, function(response, status) {
          if (status == google.maps.DirectionsStatus.OK) {
            this.response = response;
            this.fire('google-map-response', {response: response});
          }
        }.bind(this));
      }
    });
  