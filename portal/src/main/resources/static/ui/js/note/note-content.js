
function cursorInit() {
    $.fn.extend({
        insertAtCaret: function(myValue){
            let $t=$(this)[0];
            if (document.selection) {
                this.focus();
                sel = document.selection.createRange();
                sel.text = myValue;
                this.focus();
            }
            else
            if ($t.selectionStart || $t.selectionStart == '0') {
                let startPos = $t.selectionStart;
                let endPos = $t.selectionEnd;
                let scrollTop = $t.scrollTop;
                $t.value = $t.value.substring(0, startPos) + myValue + $t.value.substring(endPos, $t.value.length);
                this.focus();
                $t.selectionStart = startPos + myValue.length;
                $t.selectionEnd = startPos + myValue.length;
                $t.scrollTop = scrollTop;
            }
            else {
                this.value += myValue;
                this.focus();
            }
        }
    });
}

function changeDisplayState(edit) {
    if(edit) {
        $(modelView).hide();
        $(modelEdit).show();
    } else {
        $(modelView).show();
        $(modelEdit).hide();
    }
}

function displayContent(node) {
    // 如果不是文件夹，并且node节点不是当前正在显示的节点，则执行操作
    if(node.iconCls != folder && node.id != current_file) {
        current_file = node.id;
        changeDisplayState(false);
        $.ajax({
            url: "/content",
            data: {"id": node.id, "iconCls": node.iconCls},
            success: function (obj) {
                $(noteTitle).text(node.text);
                $(noteContent).val(obj.data);
                convert();
            }
        });
    }
}
function paseImg()
{
    document.getElementById('note_content').addEventListener("paste",function(e){
        let clipboardData = window.clipboardData || e.clipboardData, items, item;
        if (clipboardData) {
            items = clipboardData.items;
            if (!items) {
                return;
            }
            item = items[0];
            if (item && item.kind === 'file' && item.type.match(/^image\//i)) {
                let blob = item.getAsFile(),
                    reader = new FileReader();
                reader.onloadend = function (e) {
                    uploadImage(e.target.result);
                };
                reader.readAsDataURL(blob);
            }
        }
    });
};

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
    let node = $(portfolio).tree('getSelected');
    if(node.iconCls == markdown) {
        switch (btn) {
            case "save": {
                $.ajax({
                    url: "/content",
                    method: 'put',
                    data: {id: node.id, content: $(noteContent).val()},
                    success: function () {
                        alert(sucessMessage);
                    },
                    error: function () {
                        alert(errorMessage);
                    }
                });
                break;
            }
            case "edit": {
                node.iconCls == markdown ? changeDisplayState(true) : changeDisplayState(false);
                break;
            }
            case "more": {
                alert("more");
                break;
            }
        }
    }
};
