const { defineConfig } = require('@vue/cli-service');
const webpack = require('webpack');
const path = require('path');

const fs = require('fs');
//defineConfig 호출 전에 실행해서 Node.js 런타임에서만 실행
const key = fs.readFileSync(path.resolve(__dirname, 'src/assets/key.pem'));
const cert = fs.readFileSync(path.resolve(__dirname, 'src/assets/cert.pem'));

module.exports = defineConfig({
  // 빌드 결과물이 저장될 위치 (Spring Boot 등 백엔드 연동 시 경로 지정 가능)
  // outputDir: path.resolve(__dirname, '../src/main/resources/static'),
  publicPath: '/', // 앱 기준 URL, Vue Router history 모드에서 중요
  assetsDir: 'static',
  indexPath: 'index.html',
  // 개발 서버 설정
  devServer: {
    https: {
      key,
      cert,
    },
    proxy: {
      '/infra': {
        target: 'https://192.168.1.102:8761',
        changeOrigin: true,
        pathRewrite: { '^/infra': '' },
      },
      '/monitoring/stomp': {
        target: 'wss://192.168.1.102:8761',
        ws: true,
        changeOrigin: true,
        secure: false, // HTTPS 인증서가 자체 서명일 경우
        pathRewrite: { '^/monitoring/stomp': '/stomp' },
      },
    },
    static: {
      directory: path.join(__dirname, '/'),
    },
    compress: true, // gzip 압축
    port: 8080,
    hot: true,      //코드 변경 시 새로고침 없이 적용
  },
  configureWebpack: {
    plugins: [
      new webpack.ProvidePlugin({
        $: 'jquery',
        jQuery: 'jquery'
      })
    ]
  },
 /*
 pages: {
    index: {
      entry: 'src/main.js',
      template: 'public/index.html',
      filename: 'index.html',
      title: '메인 페이지',
      chunks: ['chunk-vendors', 'chunk-common', 'index']
    },
    about: {
      entry: 'src/about/main.js',
      template: 'public/about.html',
      filename: 'about.html',
      title: '소개 페이지',
      chunks: ['chunk-vendors', 'chunk-common', 'about']
    }
  }, 
  */
  //configureWebpack: { /* Webpack 설정 */ },
  //chainWebpack: config => { /* Webpack 체이닝 설정 */ },
  //pluginOptions: { /* 사용자 플러그인 설정 */ }
});