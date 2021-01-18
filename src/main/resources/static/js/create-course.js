function createCourse() {
    let checkedIds = []
    $( "#groups input:checked" ).each(function(){checkedIds.push($(this).val());});

    const courseRequest = {
        name: $('#courseName').val(),
        groupIds: checkedIds,
        description: $("#courseDescription").val()
    };
    $.ajax({
        type: "POST",
        url: `/api/v1/courses`,
        data: JSON.stringify(courseRequest),
        contentType: "application/json; charset=utf-8",
        success: function () {
            $( "#course-table-content" ).html(``);
            showTeacherCourses();
            $("#close-course-modal").click();
        }
    });
}