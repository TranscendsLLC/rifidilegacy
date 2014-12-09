
  Polymer({
    defaultUrl: 'https://www.google.com/jsapi?callback=%%callback%%',
		
		/**
		 * Fired when the API library is loaded and available.
		 * @event api-load
		 */
    notifyEvent: 'api-load',
		
		/**
		 * Wrapper for `google` API namespace.
		 * @property api
		 */
    get api() {
      return google;
    }
  });
