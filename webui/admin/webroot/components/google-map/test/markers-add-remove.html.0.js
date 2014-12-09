
var map = document.querySelector('#map1');

suite('markers', function() {

  test('markers are defined, added, removed', function(done) {
    map.addEventListener('google-map-ready', function(e) {

      // Check if marker children were setup and can be added/removed.
      assert.equal(map.markers.length, 2);

      asyncPlatformFlush(function() {

        var marker = map.markers[0];
        marker.remove();

        async.nextTick(function() {
          assert.equal(map.markers.length, 1);
          assert.isNull(
              marker.marker.map, 'removed marker is still visible on map');

          map.appendChild(marker);
          async.nextTick(function() {
            assert.isNotNull(
                marker.marker.map, 're-added marker is not visible.');

            done();
          });

       });

      });  
    });
  });

});

