
  Polymer({
    defaultUrl: 'https://apis.google.com/js/plusone.js?onload=%%callback%%',
    /**
     * Fired when the API library is loaded and available.
     * @event api-load
     */
    notifyEvent: 'api-load',
		
    /**
     * Wrapper for `gapi`.
     * @property api
     * @type gapi
     */
    get api() {
      return gapi;
    }
  });
