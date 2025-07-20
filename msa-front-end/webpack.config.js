const path = require('path');
const fs = require('fs');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { VueLoaderPlugin } = require('vue-loader');

const key = fs.readFileSync(path.resolve(__dirname, 'src/assets/key.pem'));
const cert = fs.readFileSync(path.resolve(__dirname, 'src/assets/cert.pem'));

module.exports = {
  mode: 'development', // 또는 'production'
  entry: './src/main.js',
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: 'static/js/[name].[contenthash].js',
    publicPath: '/',
    clean: true,
  },
  devServer: {
    https: {
      key,
      cert,
    },
    static: {
      directory: path.join(__dirname, 'public'),
    },
    compress: true,
    port: 8443,
    hot: true,
    proxy: {
      '/infra': {
        target: 'https://192.168.1.102:8761',
        changeOrigin: true,
        secure: false,
        pathRewrite: { '^/infra': '' },
      },
      '/monitoring/stomp': {
        target: 'wss://192.168.1.102:8761',
        ws: true,
        changeOrigin: true,
        secure: false,
        pathRewrite: { '^/monitoring/stomp': '/stomp' },
      },
    },
    historyApiFallback: true, // Vue Router history 모드 지원
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: 'vue-loader',
      },
      {
        test: /\.js$/,
        loader: 'babel-loader',
        exclude: /node_modules/,
      },
      {
        test: /\.css$/,
        use: ['vue-style-loader', 'css-loader'],
      },
    ],
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    },
    extensions: ['.js', '.vue'],
  },
  plugins: [
    new VueLoaderPlugin(),
    new webpack.ProvidePlugin({
        $: 'jquery',
        jQuery: 'jquery',
    }),
    new HtmlWebpackPlugin({
        template: 'public/index.html',
        filename: 'index.html',
        title: '메인 페이지',
        templateParameters: {
            BASE_URL: './'
        }
    }),
  ],
};
