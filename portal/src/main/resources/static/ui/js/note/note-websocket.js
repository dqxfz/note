
function transferSnippet(snippetNum) {
    isUploading = true;
    if(uploadState == 'start') {
        let start = snippetNum * snippetSizeThreshold;
        let end = start + snippetSizeThreshold;
        //如果start大于等于文件size大小,说明上传完成
        if(start >= uploadingFile.size) {
            console.log('上传完成');
            return ;
        }
        let blob = uploadingFile.slice(start, end);
        let reader = new FileReader();
        reader.readAsArrayBuffer(blob);
        reader.onload = function (e) {
            let result = e.target.result;
            ws.send(result);
        }
    } else {
        isUploading = false;
    }
}
function transferFileMetaData() {
    fileDTO.command = "upload_start";
    fileDTO.data = JSON.stringify(noteFile);
    ws.send(JSON.stringify(fileDTO));
}
function transferFileComplete() {
    fileDTO.command = "upload_complete";
    fileDTO.data = null;
    ws.send(JSON.stringify(fileDTO));
}
function closeFileUpload(closeReason) {
    uploadState = 'stop';
    uploadingFile = null;
    isUploading = false;
    $('#progress_bar').progressbar('setValue', 0);

    if(closeReason == 'success' || closeReason == 'cancel') {
        let msgValue = closeReason == 'success' ? '上传成功' : '您已取消文件上传';
        $('#process_dlg').dialog('close');
        $('#upload_dlg').dialog('close');
        $.messager.show({
            msg: msgValue,
            showType: 'show',
            style: {
                top: document.body.scrollTop + document.documentElement.scrollTop,
            }
        });
    } else if(closeReason == 'error') {

    }
}

function messageCallback(e) {
    let resp = JSON.parse(e.data);
    switch(resp.command) {
        // 继续上传
        case "response_continue": {
            uploadingFile.snippetNum++;
            let processNum = Math.floor(uploadingFile.snippetNum * snippetSizeThreshold / uploadingFile.size * 100);
            // 发送文件传输完成信息给服务器
            if(processNum >= 100){
                transferFileComplete();
                return;
            }
            $('#progress_bar').progressbar('setValue', processNum);
            transferSnippet(uploadingFile.snippetNum);
            break;
        };
        // 整个上传文件以及保存文件元信息过程完成
        case "response_complete": {
            closeFileUpload('success');
            let obj = resp.data;
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

function closeCallback(e) {
    console.log(e);
}
function openCallback(e) {
    console.log(e);
}
function initWebsocket() {
    ws = new WebSocket(socketUrl);
    ws.onopen = openCallback;
    ws.onmessage = messageCallback;
    ws.onclose = closeCallback;
    stomp = Stomp.over(ws);
}