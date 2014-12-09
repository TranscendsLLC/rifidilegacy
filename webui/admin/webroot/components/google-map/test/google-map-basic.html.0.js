
var map = document.querySelector('#map1');
var map2 = document.querySelector('#map2');
var map3 = document.querySelector('#map3');

suite('google-map', function() {

  // TODO: test setting an invalid latitude/longitude value. See if throws.
  test('invalid lat/lng', function() {
  });
  test.skip('invalid lat/lng');

  // TODO: check toggling showCenterMarker to see if marker is added/removed from map.

  test('showCenterMarker', function() {
  });
  test.skip('showCenterMarker');

  // TODO: test draggable attribute.
  test('draggable', function() {
  });
  test.skip('draggable');

  // TODO: test fitToMarkers actually contains all markers
  test('fitToMarkers', function() {
  });
  test.skip('fitToMarkers');

  // TODO: test panning map and changing it's zoom updates .latitude/longitude/.zoom properties.
  test('panning', function() {
  });
  test.skip('panning');

  test('defaults', function() {
    assert.equal(map.markers.length, 0);
    assert.isFalse(map.fitToMarkers);
    assert.isFalse(map.showCenterMarker);
    assert.isFalse(map.disableDefaultUI);
    assert.equal(map.zoom, 10);
    assert.isNull(map.maxZoom);
    assert.isNull(map.minZoom);
    assert.isTrue(map.zoomable);
    assert.equal(map.latitude, 37.77493);
    assert.equal(map.longitude, -122.41942);
    assert.equal(map.mapType, 'roadmap');
  });

  test('change properties', function(done) {
    map3.addEventListener('google-map-ready', function(e) {
      assert.isTrue(map3.fitToMarkers);
      assert.isTrue(map3.showCenterMarker);
      assert.equal(map3.markers.length, 1);
      assert.equal(map3.zoom, map3.map.getZoom());
      assert.equal(map3.maxZoom, map3.map.maxZoom);
      assert.equal(map3.minZoom, map3.map.minZoom);
      assert.isTrue(map3.zoomable);

      done();
    });
  });


});

