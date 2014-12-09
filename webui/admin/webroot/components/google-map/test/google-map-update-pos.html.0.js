
var map = document.querySelector('#map');

suite('markers', function() {

  test('update lat/lng', function(done) {
    map.addEventListener('google-map-ready', function(e) {
      assert.equal(map.latitude, 37.555);
      assert.equal(map.longitude, -122.555);
      
      asyncPlatformFlush(function() {
        assert.equal(map.latitude, map.map.getCenter().lat(),
                     'map lat was not updated');
        assert.equal(map.longitude, map.map.getCenter().lng(),
                     'map lng was not updated');

        done();
      });

    });
  });

});

