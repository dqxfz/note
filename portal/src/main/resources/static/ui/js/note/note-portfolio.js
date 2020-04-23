
$.extend($.fn.filebox.defaults.rules, {
    // filebox验证文件大小的规则函数
    // 如：validType : ['fileSize[1,"MB"]']
    fileSize : {
        validator : function(value, array) {
            var size = array[0];
            var unit = array[1];
            if (!size || isNaN(size) || size == 0) {
                $.error('验证文件大小的值不能为 "' + size + '"');
            } else if (!unit) {
                $.error('请指定验证文件大小的单位');
            }
            var index = -1;
            var unitArr = new Array("bytes", "kb", "mb", "gb", "tb", "pb", "eb", "zb", "yb");
            for (var i = 0; i < unitArr.length; i++) {
                if (unitArr[i] == unit.toLowerCase()) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                $.error('请指定正确的验证文件大小的单位：["bytes", "kb", "mb", "gb", "tb", "pb", "eb", "zb", "yb"]');
            }
            // 转换为bytes公式
            var formula = 1;
            while (index > 0) {
                formula = formula * 1024;
                index--;
            }
            // this为页面上能看到文件名称的文本框，而非真实的file
            // $(this).next()是file元素
            return $(this).next().get(0).files[0].size < parseFloat(size) * formula;
        },
        message : '文件大小必须小于 {0}{1}'
    }
});

function isFolder(node) {
    return node.iconCls == folder || node.iconCls == coordination;
}

function changeMenuState(node) {
    if(node.fatherId == $('#user').text()) {
        $('#rename').hide();
        $('#remove').hide();
    } else {
        $('#rename').show();
        $('#remove').show();
    }
    if(!isFolder(node)) {
        $('#add').hide();
        $('#upload').hide();
        $('#download').show();
    } else {
        $('#add').show();
        $('#upload').show();
        $('#download').hide();
    }
    if(node.iconCls == markdown) {
        node.fatherId ? $('#coordination').show() : $('#coordination').hide();
    } else {
        $('#coordination').hide();
    }

}

function addPortfolio(node) {
    $.ajax({
        url: "/portfolio.do",
        method: 'post',
        data: {"fatherId":node.fatherId,"name":node.text,"iconCls":node.iconCls},
        success: function (obj) {
            node.id = obj;
        },
        error: function () {
            showError(errorMessage, 3000);
            $(portfolio).tree('remove',node.target);
        }
    });
}

function renamePortfolio(node) {
    $.ajax({
        url: "/portfolio.do",
        method: 'put',
        data: {"id":node.id,"name":node.text},
        success: function () {
            if(!isFolder(node)) {
                $(noteTitle).text(node.text)
            }
        },
        error: function () {
            showError(errorMessage,3000);
            $(portfolio).tree('update', {
                target: node.target,
                text: node_text
            });
        }
    });
}

function initPortfolio(url) {
    $(portfolio).tree({
        url: url,
        method: 'get',
        onContextMenu: function(e, node){
            $(portfolio).tree('select', node.target);
            currentNode = node;
            if(principal.id && currentNode.id != principal.id) {
                principal.type = CommandType.COORDINATION_TYPE_EXIT;
                sendSync(CommandType.COORDINATION_TYPE_PRINCIPAL, principal);
                principal.id = null;
            }
            displayContent(node);
            e.preventDefault();
            changeMenuState(node);
            if(node.iconCls != coordination) {
                $('#mm').menu('show', {
                    left: e.pageX,
                    top: e.pageY
                });
            }
        },
        onBeforeEdit: function(node) {
            node_text = node.text;
        },
        // 完成编辑文件名时触发的事件
        onAfterEdit: function(node) {
            // 如果是新增节点（node.id为0，说明是新增节点），则调用添加方法，否则调用重命名方法
            if(node.id == 0) {
                addPortfolio(node);
            } else {
                renamePortfolio(node);
            }
        },
        onClick: function(node){
            currentNode = node;
            if(principal.id && currentNode.id != principal.id) {
                principal.type = CommandType.COORDINATION_TYPE_EXIT;
                sendSync(CommandType.COORDINATION_TYPE_PRINCIPAL, principal);
                principal.id = null;
            }
            displayContent(node);
        },
        onBeforeExpand: function (node) {
            $(portfolio).tree('options').url= (node.iconCls == coordination) ? '/coordination/children.do' : '/portfolio.do';
        }
    });
}

function appendNode(idValue,stateValue, textValue, iconClsVlaue, fatherId) {
    let father = $(portfolio).tree('find', fatherId);
    $(portfolio).tree('append', {
        parent: father.target,
        data: [{
            id: idValue,
            state: stateValue,
            text: textValue,
            fatherId: fatherId,
            iconCls: iconClsVlaue
        }]
    });
    // 如果是新加的就编辑名字
    if(idValue == 0) {
        let node = $(portfolio).tree('find', idValue);
        $(portfolio).tree('beginEdit', node.target);
    }
}

function menuHandler(item){
    let node = currentNode;
    switch(item.name) {
        case 'folder': {
            appendNode(0,'closed','新文件夹', folder, node.id);
            break;
        }
        case 'markdown': {
            appendNode(0,'open','新文件', markdown, node.id);
            break;
        }
        case 'rename': {
            $(portfolio).tree('beginEdit',node.target);
            break;
        }
        case 'remove': {
            $.messager.progress({
                title: '提示',
                msg: '正在删除文件，请稍候……',
                text: ''
            });
            let url = '/portfolio.do';
            let data = {"id": node.id};
            // if(!node.fatherId) {
            //     let parent = $(portfolio).tree('getParent', node.target);
            //     url = '/coordination.do'
            //     data = {'fatherId': parent.id, 'id': node.id};
            // }
            $.ajax({
                url: url,
                method: "delete",
                data: data,
                success: function () {
                    $(portfolio).tree('remove',node.target);
                    $.messager.progress('close');
                },
                error: function () {
                    $.messager.progress('close');
                }
            });
            break;
        }
        case 'upload': {
            $('#upload_dlg').dialog('open').dialog('setTitle','上传文件');
            break;
        }
        case 'download': {
            if(node.iconCls == markdown) {
                downloadFile('/portfolio/note/download.do?id=' + node.id,node);
                return;
            }
            $.ajax({
                url: '/portfolio/file/download.do',
                data: {"id": node.id},
                success: function (obj) {
                    downloadFile(obj,node);
                },
                error: function () {
                    showError(errorMessage,3000);
                }
            });
            break;
        }
        case 'coordination': {
            $('#coordinate_div').dialog('open').dialog('setTitle','填写协同账户');
            break;
        }
    }
};
function generateUUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
    });
    return uuid;
};
function createProcessBar(session) {
    let process_bar = $('#process_template').clone();
    process_bar.attr('id',session.noteFile.uuidName);
    process_bar.attr('eventType',CommandType.UPLOAD);
    $(process_bar.children('button')[0]).attr('class','note-icon-upload');
    $(process_bar.children('span')[0]).text(session.file.name);
    let process = $(process_bar.children('div')[0]);
    process.progressbar({value:0})
    $('#process_div').append(process_bar);
    process_bar.show();

    session.process = process;
}
function uploadFile() {
    if($('#note_file').filebox('isValid')) {
        $('#upload_dlg').dialog('close');
        let uuidName = generateUUID();
        let file = $('#note_file').filebox('files')[0];
        let selectedNode = currentNode;
        file.snippetNum = 0;
        let ws = createWebsocket();
        wsArray[uuidName] = ws;
        ws.session = {
            file: file,
            noteFile: {
                fatherId: selectedNode.id,
                name: file.name,
                uuidName: uuidName,
                type: file.type,
                size: file.size
            },
        }
        // 创建进度条
        createProcessBar(ws.session);
    }
}
function updateProgress(e) {
    let metadata = e.target.metadata;
    if (e.lengthComputable) {
        let processNum =Math.floor(e.loaded / e.total * 100 * 100) / 100;
        metadata.process.progressbar('setValue', processNum);
    }
}
function transferComplete(e) {
    let xhr = e.target;
    if (xhr.status === 200) {
        let node = xhr.metadata.node;
        let fileName = node.text;
        if(node.iconCls == markdown) {
            fileName += '.md';
        }
        saveAs(xhr.response, fileName);
    }
    xhr.metadata.process.parent().remove();
}
function transferFailed(e) {
    e.target.metadata.process.parent().remove();
    showError(errorMessage,3000);
}
function transferCanceled(e) {
    e.target.metadata.process.parent().remove();
}
/**
 * 获取 blob
 * @param  {String} url 目标文件地址
 * @return {cb}
 */
function getBlob(metadata) {
    let xhr = new XMLHttpRequest();
    xhrArray[metadata.node.id] = xhr;
    xhr.metadata = metadata;
    xhr.open('GET', metadata.url, true);
    xhr.responseType = 'blob';
    xhr.onload = transferComplete;
    xhr.onprogress = updateProgress;
    xhr.onerror = transferFailed;
    xhr.onabort = transferCanceled;
    xhr.send();
}

/**
 * 保存
 * @param  {Blob} blob
 * @param  {String} filename 想要保存的文件名称
 */
function saveAs(blob, filename) {
    if (window.navigator.msSaveOrOpenBlob) {
        navigator.msSaveBlob(blob, filename);
    } else {
        let link = document.createElement('a');
        let body = document.querySelector('body');

        link.href = window.URL.createObjectURL(blob);
        link.download = filename;

        // fix Firefox
        link.style.display = 'none';
        body.appendChild(link);

        link.click();
        body.removeChild(link);

        window.URL.revokeObjectURL(link.href);
    };
}

function downloadFile(url,node) {
    let process_bar = $('#process_template').clone();
    process_bar.attr('id',node.id);
    process_bar.attr('eventType',CommandType.DOWNLOAD);
    $(process_bar.children('button')[0]).attr('class','note-icon-download');
    $(process_bar.children('span')[0]).text(node.text);
    let process = $(process_bar.children('div')[0]);
    process.progressbar({value:0})
    $('#process_div').append(process_bar);
    process_bar.show();

    let downloadMetadata = {
        url: url,
        node: node,
        process: process
    }
    getBlob(downloadMetadata);
}
function initUserInfo() {
    $.ajax({
        url: '/portfolio/rootId.do',
        method: 'get',
        success: function (obj) {
            $('#user').text(obj);
            let logoutUrl = 'http://sso.dqxfz.site:9000/user/logout.do?userName=' + obj;
            $('#logout_btn').attr('href',logoutUrl);
            let url = '/portfolio.do?id=' + obj;
            initPortfolio(url);
        },
        error: function () {
            showError('获取文件目录失败', 3000);
        }
    })
}
function issueCoordinationPeople(){
    let node = currentNode;
    let data = {
        id: node.id,
        userNameStr: $('#coordination_people').val() + "," + $('#user').text()
    }
    $.ajax({
        url: '/coordination/set.do',
        method: 'get',
        data: data,
        success: function (obj) {
            if(obj) {
                showError(obj, 3000);
            } else {
                showPrompt(successMessage, 3000);
            }
        },
        error: function () {
            showError(errorMessage, 3000);
        }
    })
}
