let endPointUrl = 'http://' + window.location.host + "/note"
let stomp;
let snippetSize = 30 * 1024;
let uploadingFile;
let noteFile = {
    fatherId: '',
    snippetNum: 0,
    name: '',
    uuidName: '',
    type: '',
    content: ''
}

function transferSnippet(snippetNum) {
    noteFile.snippetNum = snippetNum;
    let start = noteFile.snippetNum * snippetSize;
    if (start > uploadingFile.size) {
        noteFile.snippetNum = -1;
        stomp.send("/app/file", null, JSON.stringify(noteFile));
    } else {
        let blob = uploadingFile.slice(start, start + snippetSize);
        let reader = new FileReader();
        reader.readAsDataURL(blob);
        reader.onload = function () {
            let result = reader.result;
            noteFile.content = result.substring(result.indexOf(',') + 1);
            stomp.send("/app/file", null, JSON.stringify(noteFile));
        }
    }
}

function isNum(str) {
    var reg = /^[0-9]+.?[0-9]*$/;
    return reg.test(str);
}

function fileCallback(message) {
    let resp = JSON.parse(message.body);
    if (resp.statusCodeValue == 200) {
        let obj = resp.body;
        if(typeof (obj) == 'number' +
            '') {
            transferSnippet(obj);
        } else {
            appendNode(obj.id,obj.state,obj.text, obj.iconCls);
        }
    }
}


function generateUUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
    });
    return uuid;
};

function connectCallback() {
    stomp.subscribe("/topic/file", fileCallback);
}

function errorCallback(error) {
    console.log(error.headers.message);
}

function initStomp() {
    stomp = Stomp.over(new SockJS(endPointUrl));
    let headers = {};
    stomp.connect(headers, connectCallback, errorCallback);
}