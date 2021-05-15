const WebSocket = require('ws');
const http = require('http');
const querystring = require('querystring');
const cookie = require('cookie');
const chalk = require('chalk');

const PRETTY = false;
const VERBOSE = true;

let JESSIONID = null;

// If we need to authenticate...

// // form data
// var data = querystring.stringify({
//     email: "",
//     password: ""
// });

// const options = {
//     hostname: 'localhost',
//     port: 8081,
//     path: '/api/v1/updates',
//     method: 'POST',
//     headers: {
//         'Content-Type': 'application/x-www-form-urlencoded',
//         'Content-Length': data.length
//     }
// }

// const req = http.request(options, res => {
//     console.log(`statusCode: ${res.statusCode}`)

//     res.on('data', d => {

//         console.log("COOKIES: " + JSON.stringify(res.headers), chalk.yellowBright('DEBUG'));
//         console.log("JESSIONID: " + JSON.stringify(res.headers['set-cookie'][0]), chalk.yellowBright('DEBUG'));

//         JSESSIONID = res.headers['set-cookie'][0];

//         connectWebSocket(JSESSIONID);
//     });
// });
  
// req.on('error', error => {
//     console.error('%s ' + error, chalk.red('ERROR'));
// });
  
// req.write(data);
// req.end();

// Simple websocket client example

connectWebSocket = (() => {
    const ws = new WebSocket(
        'ws://localhost:8081/api/v1/updates?settingsId=caas_dev_c779f6be_e58b_4b23_b085_037c8c47f5d2',
        [],
        {}
    );
    
    ws.on('open', function open() {
        //ws.send('');
    });
    
    ws.on('message', function incoming(data) {
        let jsonData = JSON.parse(data);
        let lvl = (PRETTY) ? 3 : 0;
        //let logData = (VERBOSE) ? JSON.stringify(jsonData, null, lvl) : Object.keys( jsonData )[0];
        let logData = ((Object.keys(jsonData)[0]) == "devices") ? jsonData['devices'][0]['id'] : JSON.stringify(jsonData, null, lvl);
        console.log('%s - %s - ' + logData, chalk.green('INFO'), chalk.blue(new Date()));

    });

    ws.on('close', () => {
        console.log('%s - %s - Socket Closed', chalk.green('INFO'), chalk.blue(new Date()));
    });
})

connectWebSocket();