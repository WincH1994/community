/**
 * 评论
 */
function reply() {
    var questionId = $('#questionId').val();
    var content = $('#commentContent').val();

    comment2target(questionId,content,1);

}

function comment2target(targetId,content,type){
    if(!content){
        alert("请输入回复内容呀~");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success: function (res) {
            if(res.code === 200){
                alert("回复成功~");
                window.location.reload();
            }else{
                if(res.code === 2003){
                    var isAccepted = confirm(res.message);
                    if(isAccepted){
                        window.open('https://github.com/login/oauth/authorize?client_id=bc4c1e4efac90a3b8692&redirect_uri=http://127.0.0.1:8888/github/callback&scope=user&state=1');
                        window.localStorage.setItem("closable", true);
                    }
                }else{
                    alert(res.message);
                }

            }
        },
        dataType: "json"
    });
}

/**
 * 二级评论
 */
function comment(e) {
    var commentId = $(e).data("id");
    var content = $('#input-'+commentId).val();

    comment2target(commentId,content,2);
}

/**
 *  展开二级评论
 */
function collapseComments(e) {
    var id = $(e).data('id');
    var $comment = $('#comment-'+id);

    if(!$comment.hasClass("in")){
        //展开
        var subCommentContainer = $("#comment-"+id);

        if( subCommentContainer.children().length === 1){
            $.getJSON( "/comment/"+id, function( data ) {
                $.each(data.data.reverse(), function(k,v) {

                    var html = '';
                    html += '<div class="media">'+
                                '<div class="media-left">'+
                                    '<img class="media-object img-rounded" src="'+v.user.avatarUrl+'">'+
                                '</div>'+
                                '<div class="media-body">'+
                                    '<h5 class="media-heading">'+
                                        '<span class="comment-user-name">'+v.user.name+'</span>'+
                                    '</h5>'+
                                    '<div class="comment-content">'+v.content+'</div>'+
                                    '<div class="menu">'+
                                        '<span class="pull-right">'+moment(v.gmtCreate).format('YYYY-MM-DD')+'</span>'+
                                    '</div>'+
                                '</div>'+
                            '</div>';
                    subCommentContainer.prepend(html);
                });
            });
        }

        //添加评论按钮选中CSS
        $(e).addClass("active");
        //切换展示二级评论
        $comment.toggleClass("in");

    }else{
        //关闭
        $(e).removeClass("active");
        //切换展示二级评论
        $comment.toggleClass("in");
    }


    console.log(id);
}

/**
 * 发布选择标签
 */
function selectTag(e) {
    var previous = $('#tag').val();
    var value = $(e).data('tag');

    if(previous.indexOf(value) === -1){
        if(previous){
            $('#tag').val(previous +','+value);
        }else {
            $('#tag').val(value);
        }
    }

}


function showSelectTag() {
    $('#select-tag').show();
}