showCourseCreateForm();

function createCourse() {
    const courseRequest = {name: $('#name').val()};
    $.ajax({
        type: "POST",
        url: `/api/v1/courses`,
        data: JSON.stringify(courseRequest),
        contentType: "application/json; charset=utf-8",
        success: function() {
            showCourses();
        }
    });
}

function showCourseCreateForm() {
    $.ajax(`/api/v1/users/current`).then(data => {
        if (data){
            $('#create-course').show();
        }
    });
}