var vertx = require('vertx');

vertx.createHttpServer().requestHandler(function(req) {
      var file = req.path() === '/' ? 'index.html' : req.path();
      req.response.sendFile('startbootstrap-sb-admin-2-1.0.1/' + file);
  }).listen(8282)
