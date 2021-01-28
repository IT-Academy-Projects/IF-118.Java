function createCourse() {
    const courseRequest = {
        name: $('#courseName').val(),
        description: $("#courseDescription").val()
    };

    let file = $('#course-avatar').prop('files')[0];

    let formData = new FormData();
    formData.append('file', file);
    formData.append('course', JSON.stringify(courseRequest));

    $.ajax({
        type: "POST",
        url: `/api/v1/courses`,
        data: formData,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        success: function () {
            $("#courses-wrapper").html(``);
            showTeacherCourses();
            $("#close-course-modal").click();
        }
    });
}

