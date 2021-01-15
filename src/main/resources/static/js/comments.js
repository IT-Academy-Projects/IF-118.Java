function loadComments(id) {
    $.get(`api/v1/comments/material/` + id, comments => {
        for(let i=0; i<comments.length; i++) {
            let comment = comments[i];
                let material_id_comments_body = "#material-" + id + "-comments-body"
                $(material_id_comments_body).append(`
                <div class="comment mt-4 text-justify">
                    <span class="author-${comment.ownerId}-name" class="h3"></span> <span id="created-at">- ${comment.created_at}</span> <br>
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

function toggleComments(id) {
    let material_id_comments = "#material-" + id + "-comments"
    $($(material_id_comments)).toggle();
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
            toggleComments(materialId);
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
        <section>
            <div class="container">
                <div class="row">
                    <div class="col-sm-5 col-md-6 col-12 pb-4" id="material-${materialId}-comments-body">
                    </div>
                </div>
            </div>
        </section>
    `)
}