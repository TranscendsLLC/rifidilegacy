
  Polymer({
    defaultUrl: 'https://www.youtube.com/iframe_api',
    /**
     * Fired when the API library is loaded and available.
     * @event api-load
     */
    notifyEvent: 'api-load',

    callbackName: 'onYouTubeIframeAPIReady',
		
    /**
     * Wrapper for `YT` API object.
     * @property api
     * @type YT
     */
    get api() {
      return YT;
    }
  });
