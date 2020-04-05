

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
    let node02 = tree.tree('find',0);
    tree.tree('beginEdit',node02.target);
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
                url: "/catalog/removeFolder",
                data: {"fatherId": node.fatherId,"catalog_id": node.id},
                success: function (obj) {
                    if(obj.success){
                        $(portfolio).tree('remove',node.target);
                    } else {
                        alert(obj.data);
                    }
                },
                complete: function () {
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

            break;
        }
    }
};

function uploadFile() {
    let file = $('#note_file').filebox('files')[0];
    if(file) {
        uploadingFile = file;
        noteFile.name = uploadingFile.name;
        noteFile.type = uploadingFile.type;
        noteFile.uuidName = generateUUID();
        transferSnippet(0);
    }
}