# read vinyl file stream

[![Build][1]][2]
[![Test Coverage][3]][4]
[![Code Climate][5]][6]
[![Downloads][7]][8]
[![Version][9]][8]
[![Dependency Status][10]][11]

[1]: https://travis-ci.org/catdad/read-vinyl-file-stream.svg?branch=master
[2]: https://travis-ci.org/catdad/read-vinyl-file-stream

[3]: https://codeclimate.com/github/catdad/read-vinyl-file-stream/badges/coverage.svg
[4]: https://codeclimate.com/github/catdad/read-vinyl-file-stream/coverage

[5]: https://codeclimate.com/github/catdad/read-vinyl-file-stream/badges/gpa.svg
[6]: https://codeclimate.com/github/catdad/read-vinyl-file-stream

[7]: https://img.shields.io/npm/dm/read-vinyl-file-stream.svg
[8]: https://www.npmjs.com/package/read-vinyl-file-stream
[9]: https://img.shields.io/npm/v/read-vinyl-file-stream.svg

[10]: https://david-dm.org/catdad/read-vinyl-file-stream.svg
[11]: https://david-dm.org/catdad/read-vinyl-file-stream

Turns out that reading all the files in a vinyl stream is cumbersome, and supporting all of the options is a little bit annoying. I decided that I don't want to write that code more than once. So here is a library that does that. This is most useful for gulp plugins that need to transform all the files in a stream, though I am sure you can figure out other ways to use it too.

## Install

```bash
npm install read-vinyl-file-stream
```

## API

### read-vinyl-file-stream(iterator {Function} [, flush {Function}] [, encoding {String}])

The module is a function that creates a transform stream. It will read the vinyl file, whether it is a buffer or a stream internally. It takes the following parameters, in order:

- **[iterator]** _{Function}_ Required - the method that will process the files.
- **[flush]** _{Function}_ Optional - the method to call before the stream ends.
- **encoding** _{String}_ Optional - the encoding to use for the content provided to the iterator function. By default, this is a UTF-8 string. The following options are supported:
  - `'utf8'` - provide the content in a UTF-8 string.
  - `'buffer'` - provide the content in a raw buffer. This is useful if you are processing binary files, for example.

[iterator]: #iteratorcontent-file-stream-cb
#### iterator(content, file, stream, cb)

The function that you provide to it has the following parameters, in order:

- **content** - the content of the file.
- **file** - the vinyl file itself.
- **stream** - the transform stream that is being iterated.
- **cb** - a callback to call once you are done processing the file. You must call this in order for the stream to continue.

[flush]: #flushstream-cb
#### flush(stream, cb)

This is a function that will allow you to execute some code after all the files have been read but before the stream ends. It has the following parameters, in order:

- **stream** - the transform stream that is being iterated.
- **cb** - a callback to call once you are done with the flush actions. You must call this in order for the stream to end.

## Examples

Observe all of the files:

```javascript
var readFiles = require('read-vinyl-file-stream');

var input = getVinylStream();

var hashOfFiles = {};

input.pipe(readFiles(function (content, file, stream, cb) {
    hashOfFiles[file.path] = content;

    cb();
}));
```

Transform the content of the file and output it back to the stream:

```javascript
var readFiles = require('read-vinyl-file-stream');

var input = getVinylStream();

input.pipe(readFiles(function (content, file, stream, cb) {
    var newContent = doWorkToTheContent(content);

    cb(null, newContent);
}));
```

Split the file into multiple files and output all of them to the stream:

```javascript
var readFiles = require('read-vinyl-file-stream');
var File = require('vinyl');

var input = getVinylStream();

input.pipe(readFiles(function (content, file, stream, cb) {
    var lines = content.split('\n');

    lines.forEach(function (line, idx) {
        stream.push(new File({
            contents: new Buffer(line),
            path: file.path + 'line' + idx
        }));
    });

    cb();
}));
```

Use inside `gulp` (to create a filter):

```javascript
var gulp = require('gulp');
var readFiles = require('read-vinyl-file-stream');

gulp.task('mytask', function() {
    return gulp.src('*.ext')
        .pipe(readFiles(function (content, file, stream, cb) {
            if (/^n/.test(content)) {
                return cb(null, content);
            }

            cb();
        }))
        .pipe(gulp.dest('filesThatStartWithN'));
});
```
