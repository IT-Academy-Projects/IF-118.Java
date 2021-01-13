function createCourse() {
    const courseRequest = {name: $('#name').val()};
    $.ajax({
        type: "POST",
        url: `/api/v1/courses`,
        data: JSON.stringify(courseRequest),
        contentType: "application/json; charset=utf-8",
        success: function () {
            $( "#course-table-content" ).html(``);
            showTeacherCourses();
            $("#close").click();
        }
    });
}