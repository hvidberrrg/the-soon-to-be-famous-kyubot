module.exports = function (config) {
    config.set({
        basePath: '../../..',
        frameworks: ['jasmine'],
        files: [
            'https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js',
            'src/main/webapp/js/*.js',
            'src/test/webapp/js/*.js'
        ],
        exclude: ['src/test/javascript/karma.conf*.js'],
        reporters: ['progress', 'coverage'],
        port: 9876,
        logLevel: config.LOG_INFO,
        browsers: ['PhantomJS'],
        singleRun: false,
        autoWatch: true,
        plugins: [
            'karma-coverage',
            'karma-jasmine',
            'karma-phantomjs-launcher'
        ],
        preprocessors: {
            'src/main/**/*.js': ['coverage']
        },
        coverageReporter: {
            // specify a common output directory
            dir: 'target/jasmine/coverage/',
            reporters: [
                {
                    type: 'lcov',
                    subdir: 'report-lcov'
                },
                {
                    type: 'lcovonly',
                    subdir: '.',
                    file: 'report-lcovonly.txt'
                }
            ]
        }
    });
};