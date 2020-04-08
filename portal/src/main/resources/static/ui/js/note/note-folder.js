
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
function changeMenuState(node) {
    if(node.fatherId == 'wy') {
        $('#rename').hide();
        $('#remove').hide();
    } else {
        $('#rename').show();
        $('#remove').show();
    }
    if(node.iconCls != folder) {
        $('#add').hide();
        $('#upload').hide();
        $('#download').show();
    } else {
        $('#add').show();
        $('#upload').show();
        $('#download').hide();
    }
}

function addPortfolio(node) {
    $.ajax({
        url: "/portfolio",
        method: 'post',
        data: {"fatherId":node.fatherId,"name":node.text,"iconCls":node.iconCls},
        success: function (obj) {
            node.id = obj;
        },
        error: function () {
            alert(errorMessage);
            $(portfolio).tree('remove',node.target);
        }
    });
}

function renamePortfolio(node) {
    $.ajax({
        url: "/portfolio",
        method: 'put',
        data: {"id":node.id,"name":node.text},
        success: function () {
            if(node.iconCls != folder) {
                $(noteTitle).text(node.text)
            }
        },
        error: function () {
            alert(errorMessage);
            $(portfolio).tree('update', {
                target: node.target,
                text: node_text
            });
        }
    });
}

function initPortfolio() {
    $(portfolio).tree({
        url: '/portfolio?id=wy',
        method: 'get',
        onContextMenu: function(e, node){
            displayContent(node);
            e.preventDefault();
            $(portfolio).tree('select', node.target);
            changeMenuState(node);
            $('#mm').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
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
            displayContent(node);
        }
    }).tree('options').url = "/portfolio";
}

function appendNode(idValue,stateValue, textValue, iconClsVlaue) {
    let node = $(portfolio).tree('getSelected');
    $(portfolio).tree('append', {
        parent: node.target,
        data: [{
            id: idValue,
            state: stateValue,
            text: textValue,
            fatherId: node.id,
            iconCls: iconClsVlaue
        }]
    });
    // 如果是新加的就编辑名字
    if(idValue == 0) {
        let node02 = $(portfolio).tree('find', idValue);
        $(portfolio).tree('beginEdit', node02.target);
    }
}

function menuHandler(item){
    let node = $(portfolio).tree('getSelected');

    switch(item.name) {
        case 'folder': {
            appendNode(0,'closed','新文件夹', folder);
            break;
        }
        case 'markdown': {
            appendNode(0,'open','新文件', markdown);
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
            $.ajax({
                url: "/portfolio",
                method: "delete",
                data: {"id": node.id},
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
            noteFile.fatherId = node.id;
            $('#upload_dlg').dialog('open').dialog('setTitle','上传文件');
            break;
        }
        case 'download': {
            let downloadUrl = '/portfolio/note/download';
            if(node.iconCls != markdown) {
                downloadUrl = '/portfolio/file/download'
            }
            $.ajax({
                url: downloadUrl,
                data: {"id": node.id},
                success: function (obj) {
                    download(obj,node.text);
                },
                error: function () {
                    alert('下载失败');
                }
            });
            break;
        }
    }
};

function uploadFile() {
    if($('#note_file').filebox('isValid')) {
        let uuidName = generateUUID();
        $('#process_dlg').dialog('open');
        uploadingFile = $('#note_file').filebox('files')[0];
        uploadingFile.snippetNum=0;

        noteFile.name = uploadingFile.name;
        noteFile.type = uploadingFile.type;
        noteFile.uuidName = generateUUID();
        // let idx = noteFile.name.lastIndexOf('.');
        // if(idx != -1) {
        //     noteFile.uuidName += noteFile.name.substring(idx);
        // }
        // 传输文件元信息
        transferFileMetaData();
        // 开始传输数据
        transferSnippet(uploadingFile.snippetNum);
    }
}
// /**
//  * 获取 blob
//  * @param  {String} url 目标文件地址
//  * @return {cb}
//  */
// function getBlob(url,cb) {
//     var xhr = new XMLHttpRequest();
//     xhr.open('GET', url, true);
//     xhr.responseType = 'blob';
//     xhr.onload = function() {
//         if (xhr.status === 200) {
//             cb(xhr.response);
//         }
//     };
//     xhr.send();
// }
//
// /**
//  * 保存
//  * @param  {Blob} blob
//  * @param  {String} filename 想要保存的文件名称
//  */
// function saveAs(blob, filename) {
//     if (window.navigator.msSaveOrOpenBlob) {
//         navigator.msSaveBlob(blob, filename);
//     } else {
//         var link = document.createElement('a');
//         var body = document.querySelector('body');
//
//         link.href = window.URL.createObjectURL(blob);
//         link.download = filename;
//
//         // fix Firefox
//         link.style.display = 'none';
//         body.appendChild(link);
//
//         link.click();
//         body.removeChild(link);
//
//         window.URL.revokeObjectURL(link.href);
//     };
// }
//
// /**
//  * 下载
//  * @param  {String} url 目标文件地址
//  * @param  {String} filename 想要保存的文件名称
//  */
// function download(url, filename) {
//     getBlob(url, function(blob) {
//         saveAs(blob, filename);
//     });
// };

function getBlob(url) {
    return new Promise(resolve => {
        const xhr = new XMLHttpRequest()
// 避免 200 from disk cache
        url = url + '?r=${Math.random()}'
            xhr.open('get',url, true)
        xhr.responseType = 'blob'
xhr.onload = () => {
    if (xhr.status === 200) {
        debugger
        resolve(xhr.response)
    }
}
        xhr.send()
    })
}
function saveAs(blob,filename) {
    if(window.navigator.msSaveOrOpenBlob){
        navigator.msSaveBlob(blob,filename)
    }else{
        const anchor=document.createElement('a')
        const body= document.querySelector('body')
        anchor.href=window.URL.createObjectURL(blob)
        anchor.download = filename
        anchor.style.display='none'
        body.appendChild(anchor)
        anchor.click()
        body.removeChild(anchor)
        window.URL.revokeObjectURL(anchor.href)
    }
}
async function download(url,newFileName) {
    const blob= await getBlob(url)
    saveAs(blob,newFileName)
}