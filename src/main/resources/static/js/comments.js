function loadComments(id) {
    $.get(`api/v1/comments/material/` + id, comments => {
        for (let i = 0; i < comments.length; i++) {
            let comment = comments[i];
            let material_id_comments_body = "#material-" + id + "-comments-body"
            $(material_id_comments_body).append(`
                <div class="comment mt-4 text-justify text-white">
                    <span class="author-${comment.ownerId}-name" class="h3"></span> <span class="created-at">- ${comment.created_at}</span> <br>
                    <p id="message">${comment.message}</p>
                </div>
            `)
            $.get(`api/v1/users/` + comment.ownerId, owner => {
                let author_name = ".author-" + comment.ownerId + "-name"
                $(author_name).text(owner.name);
            })
        }
    })
}

// function loadPrivateComments(materialId) {
//     $.get(`api/v1/comments/material/` + id + `/private`, comments => {
//         for (let i = 0; i < comments.length; i++) {
//             let comment = comments[i];
//             let material_id_private_comments_body = "#material-" + id + "-private-comments-body"
//             $(material_id_private_comments_body).append(`
//                 <div class="comment mt-4 text-justify">
//                     <span class="author-${comment.ownerId}-name" class="h3"></span> <span class="created-at">- ${comment.created_at}</span> <br>
//                     <p id="private-message">${comment.message}</p>
//                 </div>
//             `)
//             $.get(`api/v1/users/` + comment.ownerId, owner => {
//                 let author_name = ".author-" + comment.ownerId + "-name"
//                 $(author_name).text(owner.name);
//             })
//         }
//     })
// }

function toggleComments(id) {
    let material_id_comments = "#material-" + id + "-comments"
    $(material_id_comments).toggle();
}

function addComment(materialId) {
    const commentRequest = {
        message: $('#commentary-message').val(),
        materialId: materialId,
        isPrivate: $('#is-private').is(':checked')
    };
    $.ajax({
        type: "POST",
        url: `api/v1/comments`,
        data: JSON.stringify(commentRequest),
        contentType: "application/json; charset=utf-8",
        success: function () {
            $("#close-comment-modal").click();
            cleanComments(materialId);
            loadComments(materialId);
            let material_id_comments = "#material-" + materialId + "-comments"
            if ($(material_id_comments).css('display') === 'none') {
                toggleComments(materialId);
            }
        }
    });
}

function setMaterialId(id) {
    let attrVal = "addComment(" + id + ");"
    $('#write-comment-button').attr('onclick', attrVal);
}

function cleanComments(materialId) {
    let material_id_comments = "#material-" + materialId + "-comments"
    $(material_id_comments).html(`
        <div id="material-${materialId}-comments-body"></div>
    `)
}