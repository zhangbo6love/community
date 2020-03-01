/**
 * 提交回复
 * */
function post() {
    var qusetionId = $("#question_id").val();
    var content = $("#comment_context").val();
    comment2target(qusetionId, 1, content);

}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }


    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        dataType: "json",
        contentType: "application/json",
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=619f30853dafb1f39a35&redirect_uri=http://localhost:8866/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response);

        }
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);

}


/**
 *
 *
 * 展开二级评论
 * */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    //获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        console.log(subCommentContainer);
        if (subCommentContainer.children().length != 1) {

            //展开二级评论
            comments.addClass("in");
            //标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {

                    // var c = $("<div/>", {
                    //     "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    //     html: comment.content
                    // });
                    subCommentContainer.prepend(
                        "<div class='col-lg-12 col-md-12 col-sm-12 col-xs-12 comments'>"
                        + "<div class='media'>"
                        + "<div class='media-left'>"
                        + "<a href='#'>"
                        + "<img class='media-object img-rounded' src='" + comment.user.avatarUrl + "'>"
                        + "</a>"
                        + "</div>"
                        + "<div class='media-body'>"
                        + "<h6 class='media-heading'>"
                        + "<span>" + comment.user.name + "</span>"
                        + "</h6>"
                        + "<div >" + comment.content + "</div>"
                        + "<div class='menu'>"
                        + "<span class='pull-right'>" + moment(comment.gmtCreate).format('YYYY-MM-DD') + "</span>"
                        + "</div>"
                        + "</div>"
                        + "</div>"
                    );
                    // subCommentContainer.prepend(c);
                });

                //展开二级评论
                comments.addClass("in");
                //标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }

}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }

    }

}

function showSelectTag() {
    $("#select-tag").show();

}