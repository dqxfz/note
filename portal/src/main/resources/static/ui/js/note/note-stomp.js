let endPointUrl = 'http://' + window.location.host  + "/note"
let stomp;

function contentCallback(message) {
    console.log(message);
}

function nameCallback(message) {
    console.log(message);
}

function sendFile(file) {
    stomp.send("/app/file/name",{},file.name);
}

function connectCallback() {
    console.log('connect success');
    stomp.subscribe("/file/name", nameCallback);
    stomp.subscribe("/file/content", contentCallback);
}
function errorCallback(error) {
    console.log(error.headers.message);
}

function initStomp() {
    stomp = Stomp.over(new SockJS(endPointUrl));
    let headers = {};
    stomp.connect(headers, connectCallback, errorCallback);
}