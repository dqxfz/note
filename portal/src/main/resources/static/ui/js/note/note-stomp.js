
function transferSnippet(snippetNum) {
    isUploading = true;
    noteFile.snippetNum = snippetNum;
    if(uploadState == 'start') {
        let start = noteFile.snippetNum * snippetSizeThreshold;
        let end = start + snippetSizeThreshold;
        //如果end大于等于文件爱你大小说明本次上传是最后一次上传
        noteFile.snippetNum = (end >= uploadingFile.size ? -1 : snippetNum);
        let blob = uploadingFile.slice(start, end);
        let reader = new FileReader();
        reader.readAsDataURL(blob);
        reader.onload = function () {
            let result = reader.result;
            noteFile.content = result.substring(result.indexOf(',') + 1);
            stomp.send("/app/file", null, JSON.stringify(noteFile));
        }
    } else {
        isUploading = false;
    }
}

function closeFileUpload(closeReason) {
    uploadState = 'stop';
    uploadingFile = null;
    isUploading = false;
    $('#progress_bar').progressbar('setValue', 0);
    // 上传成功
    if(closeReason == 'success') {
        $('#process_dlg').dialog('close');
        $('#upload_dlg').dialog('close');
        $.messager.show({
            msg: '上传成功',
            showType: 'show',
            style: {
                top: document.body.scrollTop + document.documentElement.scrollTop,
            }
        });
        //取消文件上传
    } else if(closeReason == 'cancel') {
        $('#upload_dlg').dialog('close');
        $.messager.show({
            msg: '您已取消上传文件',
            showType: 'show',
            style: {
                top: document.body.scrollTop + document.documentElement.scrollTop,
            }
        });
        //上传出错
    } else if(closeReason == 'error') {

    }
}
function fileCallback(message) {
    let resp = JSON.parse(message.body);
    if (resp.statusCodeValue == 200) {
        let obj = resp.body;
        if(typeof (obj) == 'number') {
            // 上传文件中
            $('#progress_bar').progressbar('setValue', Math.floor(obj * snippetSizeThreshold / uploadingFile.size * 100));
            transferSnippet(obj);
        } else {
            // 上传文件完成
            closeFileUpload('success');
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