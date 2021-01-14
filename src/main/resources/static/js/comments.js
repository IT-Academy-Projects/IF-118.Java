$.get(`api/v1/users/me`).then(me => {
    me.comments.forEach(comment =>{
        $.get(`api/v1/comments/` + comment.id).then(comment => {
            $('#comments').append(`
                <div class="comment mt-4 text-justify float-left"> <img src="https://i.imgur.com/yTFUilP.jpg" alt="" class="rounded-circle" width="40" height="40">
                    <span id="author-name" class="h3">${me.name}</span> <span id="created-at">- ${comment.created_at}</span> <br>
                    <p id="message">${comment.message}</p>
                </div>
            `)
        });
    })
});