function createGroup() {
    let groupName = {name: $('#name').val()};
    $.ajax({
        type: "POST",
        url: `/api/v1/groups`,
        data: JSON.stringify(groupName),
        contentType: "application/json; charset=utf-8",
        success: function() {
            window.location.replace('/');
        }
    });
}
