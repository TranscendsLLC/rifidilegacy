
  (function() {
    'use strict';

    // Stores whether the API client is done loading.
    var clientLoaded_ = false;

    // Loaders and loading statuses for all APIs, indexed by API name.
    // This helps prevent multiple loading requests being fired at the same time
    // by multiple google-api-loader elements.
    var statuses_ = {};
    var loaders_ = {};

    Polymer('google-api-loader', {
      /**
       * Fired when the requested API is loaded. Override this name
       * by setting `successEventName`. 
       * @event google-api-load
       */

      /**
       * Fired if an error occurs while loading the requested API. Override this name
       * by setting `errorEventName`. 
       * @event google-api-load-error
       */

      /**
       * Name of the API to load, e.g. 'urlshortener'.
       *
       * You can find the full list of APIs on the
       * <a href="https://developers.google.com/apis-explorer"> Google APIs
       * Explorer</a>.
       * @attribute name
       * @type string
       * @required
       */
      name: '',

      /**
       * Version of the API to load, e.g. 'v1'.
       * @attribute version
       * @type string
       * @required
       */
      version: '',

      /**
       * App Engine application ID for loading a Google Cloud Endpoints API.
       * @attribute appId
       * @type string
       */
      appId: null,

      /**
       * Root URL where to load the API from, e.g. 'http://host/apis'.
       * For App Engine dev server this would be something like:
       * 'http://localhost:8080/_ah/api'.
       * Overrides 'appId' if both are specified.
       * @attribute root
       * @type string
       */
      root: null,

      // Used to fix events potentially being fired multiple times by
      // core-shared-lib.
      waiting: false,

      /**
       * Used to override the event that is fired when the API lib is successfully loaded.
       * @property successEventName
       * @type string
       * @default 'google-api-load'
       */
      successEventName: 'google-api-load',
      
      /**
       * Used to override the event that is fired when there is an error loading the API.
       * @property errorEventName
       * @type string
       * @default 'google-api-load-error'
       */
      errorEventName: 'google-api-load-error',

      /**
       * Returns the loaded API.
       * @property api
       * @type gapi.client
       */
      get api() {
        if (window.gapi && window.gapi.client &&
            window.gapi.client[this.name]) {
          return window.gapi.client[this.name];
        } else {
          return undefined;
        }
      },

      handleLoadResponse: function(response) {
        if (response && response.error) {
          statuses_[this.name] = 'error';
          this.fireError(response);
        } else {
          statuses_[this.name] = 'loaded';
          this.fireSuccess();
        }
      },

      fireSuccess: function() {
        this.fire(this.successEventName,
            { 'name': this.name, 'version': this.version });
      },

      fireError: function(response) {
        if (response && response.error) {
          this.fire(this.errorEventName, {
            'name': this.name,
            'version': this.version,
            'error': response.error });
        } else {
          this.fire(this.errorEventName, {
            'name': this.name,
            'version': this.version });
        }
      },

      doneLoadingClient: function() {
        clientLoaded_ = true;
        // Fix for API client load event being fired multiple times by
        // core-shared-lib.
        if (!this.waiting) {
          this.loadApi();
        }
      },

      createSelfRemovingListener: function(eventName) {
        var self = this;
        var handler = function () {
          loaders_[self.name].removeEventListener(eventName, handler);
          self.loadApi();
        };

        return handler;
      },

      loadApi: function() {
        if (clientLoaded_ && this.name && this.version) {
          this.waiting = false;
          // Is this API already loaded?
          if (statuses_[this.name] == 'loaded') {
            this.fireSuccess();
          // Is a different google-api-loader already loading this API?
          } else if (statuses_[this.name] == 'loading') {
            this.waiting = true;
            loaders_[this.name].addEventListener(this.successEventName,
                this.createSelfRemovingListener(this.successEventName));
            loaders_[this.name].addEventListener(this.errorEventName,
                this.createSelfRemovingListener(this.errorEventName));
          // Did we get an error when we tried to load this API before?
          } else if (statuses_[this.name] == 'error') {
            this.fireError();
          // Otherwise, looks like we're loading a new API.
          } else {
            var root;
            if (this.root) {
              root = this.root
            } else if (this.appId) {
              root = 'https://' + this.appId + '.appspot.com/_ah/api';
            }
            statuses_[this.name] = 'loading';
            loaders_[this.name] = this;
            gapi.client.load(this.name, this.version,
                this.handleLoadResponse.bind(this), root);
          }
        }
      }
    });
  })();
