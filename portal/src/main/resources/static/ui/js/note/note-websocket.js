
function transferSnippet(ws) {
    let file = ws.session.file;
    let start = file.snippetNum * snippetSizeThreshold;
    let blob = file.slice(start, start + snippetSizeThreshold);
    let reader = new FileReader();
    reader.readAsArrayBuffer(blob);
    reader.onload = function (e) {
        let blob = e.target.result;
        // ws.session.spark.append(blob);
        ws.send(blob);
    }
}
function transferFileMetaData(ws) {
    let fileDTO = {
        command: CommandType.UPLOAD_START,
        data: JSON.stringify(ws.session.noteFile)
    }
    ws.send(JSON.stringify(fileDTO));
}
function transferFileComplete(ws) {
    let fileDTO = {
        command: CommandType.UPLOAD_COMPLETE,
        data: ""
    }
    ws.send(JSON.stringify(fileDTO));
}
function closeFileUpload(ws,commandType) {
    ws.close(1000,"正常关闭");
    ws.session.process.parent().remove();
    switch (commandType) {
        case CommandType.RESPONSE_ERROR: {
            showError('上传失败',3000);
            break;
        }
        case CommandType.CONNECT_ERROR: {
            showError('连接中断',3000);
            break;
        }
    }
}

function messageCallback(e) {
    let ws = e.target;
    let resp = JSON.parse(e.data);
    switch(resp.command) {
        // 继续上传
        case CommandType.RESPONSE_CONTINUE: {
            let file = ws.session.file;
            let start = file.snippetNum * snippetSizeThreshold;
            // 发送文件传输完成信息给服务器
            if(start > file.size) {
                transferFileComplete(ws);
                return;
            }
            let processNum = Math.floor(start / file.size * 100);
            ws.session.process.progressbar('setValue', processNum);
            //开始传送第snippetNum个文件片段
            transferSnippet(ws);
            file.snippetNum++;
            break;
        }
        // 整个上传文件以及保存文件元信息过程完成
        case CommandType.RESPONSE_COMPLETE: {
            closeFileUpload(ws,CommandType.RESPONSE_COMPLETE);
            let obj = resp.data;
            appendNode(obj.id,obj.state,obj.text, obj.iconCls, obj.fatherId);
            break;
        }
        // 上传失败
        case CommandType.RESPONSE_ERROR: {
            closeFileUpload(ws,CommandType.RESPONSE_ERROR);
            break;
        }
    }

}


function closeCallback(e) {
    console.log(e);
}
function errorCallback(e) {
    closeFileUpload(e.target, CommandType.CONNECT_ERROR);
}
function openCallback(e) {
    // 传输文件元信息
    transferFileMetaData(e.target);
}

function initWebsocket() {
    let ws = new WebSocket(socketUrl);
    ws.onopen = openCallback;
    ws.onmessage = messageCallback;
    ws.onclose = closeCallback;
    ws.onerror = errorCallback;
    return ws;
}

function cancel(btn) {
    let processDiv = $(btn).parent();
    let uuid = processDiv.attr('id');
    switch (processDiv.attr('eventType')) {
        case CommandType.UPLOAD:{
            let ws = wsArray[uuid];
            closeFileUpload(ws);
            break;
        }
        case CommandType.DOWNLOAD: {
            xhrArray[uuid].abort();
            break;
        }
    }

}