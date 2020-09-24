/* jshint node: true */

var path = require('path');

var through = require('through2');
var ns = require('node-stream');

var encodings = ['utf8', 'utf-8', 'buffer'];
var defaultEnc = 'utf8';

function noopFlush(stream, cb) {
    cb();
}

function castData(data, enc) {
    var isBuffer = Buffer.isBuffer(data);

    if (enc === 'buffer') {
        return isBuffer ? data : (new Buffer(data));
    } else {
        return isBuffer ? data.toString(enc) : data;
    }
}

function writeContent(file, content) {
    if (file.isStream()) {
        var stream = through();

        file.contents = stream;

        stream.write(content);
        stream.end();
    } else {
        file.contents = new Buffer(content);
    }
}

module.exports = function iterateStream(iterator, flush, enc) {
    if (typeof flush === 'string') {
        enc = flush;
        flush = noopFlush;
    }

    if (typeof flush !== 'function') {
        flush = noopFlush;
    }

    var stream = through.obj(function (file, fileEnc, cb) {

        // continue if the file is null
        if (file.isNull()) {
            return cb();
        }

        var content;

        function iteratorCallback(err, content) {
            if (err) {
                return cb(err);
            }

            if (typeof content === 'string' || Buffer.isBuffer(content)) {
                writeContent(file, content);

                stream.push(file);
            }

            cb();
        }

        if (file.isStream()) {
            ns.wait(file.contents, function(err, data) {
                if (err) {
                    return cb(err);
                }

                data = castData(data, enc);
                iterator(data, file, stream, iteratorCallback);
            });
        } else if (file.isBuffer()) {
            content = castData(file.contents, enc);
            iterator(content, file, stream, iteratorCallback);
        } else {
            // not sure what else it could be, but just deal with it
            cb();
        }
    }, function (cb) {
        flush(stream, cb);
    });

    return stream;
};
