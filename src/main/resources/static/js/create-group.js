function createGroup() {
    let checkedIds = []
    $( "#courses input:checked" ).each(function(){checkedIds.push($(this).val());});
    let groupRequest = {
        name: $('#groupName').val(),
        courseIds: checkedIds
    };
    $.ajax({
        type: "POST",
        url: `/api/v1/groups`,
        data: JSON.stringify(groupRequest),
        contentType: "application/json; charset=utf-8",
        success: function() {
            $( "#group-table-content" ).html(``);
            showTeacherGroups();
            $("#close-group-modal").click();
        }
    });
}
