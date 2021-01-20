function createCourse() {
    const courseRequest = {
        name: $('#courseName').val(),
        description: $("#courseDescription").val()
    };

    $.ajax({
        type: "POST",
        url: `/api/v1/courses`,
        data: JSON.stringify(courseRequest),
        contentType: "application/json; charset=utf-8",
        success: function () {
            $("#course-table-content").html(``);
            $("#courses").html(``);
            showTeacherCourses();
            $("#close-course-modal").click();
        }
    });
}
