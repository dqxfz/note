

function initContent() {
    changeEditState(false);
    convert();
    paseInit();
}

function changeEditState(edit) {
    if(edit) {
        // 编辑状态
        $(modelView).hide();
        $(modelEdit).show();
    } else {
        // 视图状态
        $(modelView).show();
        $(modelEdit).hide();
    }
}
function setContent(title,text) {
    $(noteTitle).text(title);
    $(noteContent).val(text);
    convert();
}
function displayContent(node) {
    // 如果不是文件夹则执行操作
    if(!isFolder(node)) {
        // 如果node节点是当前正在显示或者正在编辑的节点，则设置为视图状态后返回
        if(currentNode && node.id == last_edit_file_id) {
            changeEditState(false);
            return;
        }
        last_edit_file_id = node.id;
        changeEditState(false);
        if(node.fatherId) {
            $.ajax({
                url: "/content.do",
                data: {"id": node.id, "iconCls": node.iconCls},
                success: function (obj) {
                    setContent(node.text, obj);
                }
            });
        } else {
            $(noteTitle).text(node.text);
            if(principal.id == node.id) {
                return;
            }
            principal.userName = $('#user').text();
            principal.id = node.id;
            principal.type = CommandType.COORDINATION_TYPE_ENTER;
            noteText.id = node.id;
            sendSync(CommandType.COORDINATION_TYPE_PRINCIPAL, principal);
        }
    } else {
        changeEditState(false);
        setContent('日记', '');
        last_edit_file_id = node.id;
        $(portfolio).tree('expand',node.target);
    }
}
function paseInit() {
    document.getElementById('note_content').addEventListener("paste",function(e){
        let clipboardData = window.clipboardData || e.clipboardData, items, item;
        if (clipboardData) {
            items = clipboardData.items;
            if (!items) {
                return;
            }
            item = items[0];
            if (item && item.kind === 'file' && item.type.match(/^image\//i)) {
                let blob = item.getAsFile(), reader = new FileReader();
                reader.readAsDataURL(blob);
                reader.onloadend = function (e) {
                    uploadImage(e.target.result);
                };
            }
        }
    });
};
function insertAtCursor(myField, myValue) {

    //IE 浏览器
    if (document.selection) {
        myField.focus();
        let sel = document.selection.createRange();
        sel.text = myValue;
        sel.select();
    }

    //FireFox、Chrome等
    else if (myField.selectionStart || myField.selectionStart == '0') {
        var startPos = myField.selectionStart;
        var endPos = myField.selectionEnd;

        // 保存滚动条
        var restoreTop = myField.scrollTop;
        myField.value = myField.value.substring(0, startPos) + myValue + myField.value.substring(endPos, myField.value.length);

        if (restoreTop > 0) {
            myField.scrollTop = restoreTop;
        }

        myField.focus();
        myField.selectionStart = startPos + myValue.length;
        myField.selectionEnd = startPos + myValue.length;
        textKeyup();
    } else {
        myField.value += myValue;
        myField.focus();
    }
    convert();
}
function uploadImage(base64) {
    var data = {
        base64 : base64,
        uuidName: generateUUID()
    };
    $.ajax({
        type : "post",
        url : "/portfolio/image.do",
        data : data,
        success : function(obj) {
            insertAtCursor(document.getElementById('note_content'),obj);
        },
        error : function() {
            showError(errorMessage,3000);
        }
    });
}

function convert(){
    let text = $(noteContent).val();
    let converter = new showdown.Converter();
    let html = converter.makeHtml(text);
    $(nodeDisplay).html(html);
    hljs.highlightBlock(document.getElementById("note_display"));
    $(modelView).html(html);
    hljs.highlightBlock(document.getElementById("model_view"));
}
function buttonHandler(btn) {
    let node = currentNode;
    if(node.iconCls == markdown) {
        switch (btn) {
            case "save": {
                $.ajax({
                    url: "/content.do",
                    method: 'put',
                    data: {id: node.id, text: $(noteContent).val()},
                    success: function () {
                    },
                    error: function () {
                        showError(errorMessage,3000);
                    }
                });
                break;
            }
            case "edit": {
                changeEditState(false);
                if(node.iconCls == markdown) {
                    changeEditState(true);
                    if(!node.fatherId) {
                        changeEditState(true);
                    }
                }
                break;
            }
        }
    }
};
