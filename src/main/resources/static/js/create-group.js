function createGroup() {
    let checkedIds = []
    $( "#courses input:checked" ).each(function(){checkedIds.push($(this).val());});
    let groupRequest = {
        name: $('#groupName').val(),
        courseIds: checkedIds
    };
    let file = $('#group-avatar').prop('files')[0];

    let formData = new FormData();
    formData.append('file', file);
    formData.append('group', JSON.stringify(groupRequest));

    $.ajax({
        type: "POST",
        url: `/api/v1/groups`,
        data: formData,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        success: function() {
            $( "#groups-wrapper" ).html(``);
            showTeacherGroups();
            $("#close-group-modal").click();
        }
    });
}
