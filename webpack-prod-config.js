const path = require('path');
const webpack = require('webpack');
const CopywebpackPlugin = require('copy-webpack-plugin');

const cesiumModule = 'node_modules/cesium/Build/Cesium/Cesium';
const cesiumSource = 'node_modules/cesium/Source';
const cesiumWorkers = '../Build/Cesium/Workers';

module.exports = {
    mode: "development",
    resolve: {
      alias: {
        // CesiumJS module name
        cesium: path.resolve(__dirname, cesiumModule)
      },
      fallback: {
        "path": require.resolve("path-browserify"),
        "http": false,
        "https": false,
        "zlib": false,
      },
    },

    entry: ['./resources/public/js/compiled/requires.js'],
    output: {
        path: path.resolve(__dirname, './resources/public/js/compiled'),
        filename: 'bundle.js',
        sourcePrefix: '',
        //clean: true,
    },
    module: {
        rules: [
          {
            test: /node_modules\/vfile\/core\.js/,
            use: [{
              loader: 'imports-loader',
              options: {
                type: 'commonjs',
                imports: ['single process/browser process'],
              },
            }],
          },
        ],
    },
    plugins: [
        // Copy Cesium Assets, Widgets, and Workers to a static directory
        new CopywebpackPlugin({
            patterns: [
                { from: path.join(cesiumSource, cesiumWorkers), to: 'Workers' },
                { from: path.join(cesiumSource, 'Assets'), to: 'Assets' },
                { from: path.join(cesiumSource, 'Widgets'), to: 'Widgets' }
            ]
        }),
        new webpack.DefinePlugin({
                    // Define relative base path in cesium for loading assets
                    CESIUM_BASE_URL: JSON.stringify('/js/compiled/')
                }),
    ],
};
