function createGroup() {
    let groupName = {name: $('#groupName').val()};
    $.ajax({
        type: "POST",
        url: `/api/v1/groups`,
        data: JSON.stringify(groupName),
        contentType: "application/json; charset=utf-8",
        success: function() {
            $( "#group-table-content" ).html(``);
            showTeacherGroups();
            $("#close-group-modal").click();
        }
    });
}
