function loadComments(id) {
    $.get(`api/v1/comments/material/`+id).then(comments => {
        comments.forEach(comment =>{
            $.get(`api/v1/comments/` + comment.id).then(comment => {
                let material_id_comments_body = "#material-"+id+"-comments-body"
                $($(material_id_comments_body)).append(`
                <div class="comment mt-4 text-justify">
                    <span class="author-${comment.ownerId}-name" class="h3"></span> <span id="created-at">- ${comment.created_at}</span> <br>
                    <p id="message">${comment.message}</p>
                </div>
            `)
                $.get(`api/v1/users/` + comment.ownerId).then(owner => {
                    let author_name = ".author-"+comment.ownerId+"-name"
                    $($(author_name)).text(owner.name);
                });
            });
        })
    })
}

function toggleComments(id) {
    let material_id_comments = "#material-"+id+"-comments"
    $($(material_id_comments)).toggle();
}