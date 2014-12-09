
  Polymer({
    defaultUrl: 'https://apis.google.com/js/client.js?onload=%%callback%%',
    
    /**
     * Fired when the API library is loaded and available.
     * @event api-load
     */
    notifyEvent: 'api-load',

    /**
     * Wrapper for `gapi.client`.
     * @property api
     * @type gapi.client
     */
    get api() {
      return gapi.client;
    },
    /**
     * Wrapper for `gapi.auth`.
     * @property auth
     * @type gapi.auth
     */
    get auth() {
      return gapi.auth;
    }
  });
